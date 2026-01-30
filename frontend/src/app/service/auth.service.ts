import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../environment';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  usuario: UserJwtDto;  // ✅ Ahora sí viene el usuario
}

export interface UserJwtDto {
  idUsuario: number;
  idTrabajador: number;
  username: string;
  empresa: string;
  cargo: string;
  area: string;
  nombreCompleto: string;
  activo: boolean;
  nivel: string[];  // ✅ Cambiado de 'roles' a 'nivel'
}

export interface AuthState {
  token: string | null;
  user: UserJwtDto | null;
  isAuthenticated: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);

  private API_URL = `${environment.baseUrl}/api/auth`;
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'auth_user';

  private authState = new BehaviorSubject<AuthState>({
    token: null,
    user: null,
    isAuthenticated: false
  });

  public authState$ = this.authState.asObservable();

  constructor() {
    this.loadInitialState();
  }

  /**
   * Carga el estado inicial desde localStorage
   */
  private loadInitialState(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    const token = localStorage.getItem(this.TOKEN_KEY);
    const userStr = localStorage.getItem(this.USER_KEY);

    // Si no hay token, limpiar
    if (!token) {
      this.clearAuthState();
      return;
    }

    // Verificar si el token está expirado
    if (this.isTokenExpired(token)) {
      this.logout();
      return;
    }

    // Cargar usuario desde localStorage
    let user: UserJwtDto | null = null;
    if (userStr) {
      try {
        user = JSON.parse(userStr);
      } catch (error) {
        console.error('Error parseando usuario:', error);
        localStorage.removeItem(this.USER_KEY);
      }
    }

    // Si no pudimos cargar el usuario, decodificar del token
    if (!user) {
      user = this.decodeToken(token);
    }

    if (user) {
      this.authState.next({
        token,
        user,
        isAuthenticated: true
      });
    } else {
      this.logout();
    }
  }

  /**
   * Inicia sesión
   */
  login(credentials: LoginRequest): Observable<AuthResponse> {
    console.log('🔐 login: Solicitando login para', credentials.username);

    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials).pipe(
      tap(response => {
        console.log('✅ login: Respuesta exitosa', {
          hasToken: !!response.token,
          hasUsuario: !!response.usuario,
          usuario: response.usuario.username
        });

        if (!response.token) {
          throw new Error('No se recibió token del servidor');
        }

        if (!response.usuario) {
          console.warn('⚠️ login: El backend no envió usuario, decodificando del token...');
          const usuario = this.decodeToken(response.token);
          if (usuario) {
            this.setToken(response.token, usuario);
          } else {
            throw new Error('No se pudo obtener información del usuario');
          }
        } else {
          this.setToken(response.token, response.usuario);
        }
      }),
      catchError(this.handleError)
    );
  }

  /**
   * Guarda el token y usuario
   */
  private setToken(token: string, usuario: UserJwtDto): void {
    if (!isPlatformBrowser(this.platformId)) return;

    // Validaciones
    if (!token) {
      console.error('❌ setToken: token es null/undefined');
      return;
    }

    if (!usuario) {
      console.error('❌ setToken: usuario es null/undefined');
      return;
    }

    try {
      // Guardar en localStorage
      localStorage.setItem(this.TOKEN_KEY, token);
      localStorage.setItem(this.USER_KEY, JSON.stringify(usuario));

      // Actualizar estado
      this.authState.next({
        token,
        user: usuario,
        isAuthenticated: true
      });

      console.log('✅ setToken: Datos guardados correctamente', {
        usuario: usuario.username,
        nivel: usuario.nivel
      });

    } catch (error) {
      console.error('❌ setToken: Error al guardar datos:', error);
    }
  }

  /**
   * Decodifica el usuario del token JWT
   */
  private decodeToken(token: string): UserJwtDto | null {
    try {
      const payload = token.split('.')[1];
      const decoded = JSON.parse(atob(payload));

      // El JWT tiene: { data: {...}, nivel: [...], ... }
      const userData = decoded.data;

      if (!userData) {
        console.error('❌ decodeToken: No se encontró "data" en el token');
        return null;
      }

      // Mapear a UserJwtDto
      const user: UserJwtDto = {
        idUsuario: userData.idUsuario,
        idTrabajador: userData.idTrabajador,
        username: userData.username,
        empresa: userData.empresa,
        cargo: userData.cargo,
        area: userData.area,
        nombreCompleto: userData.nombreCompleto,
        activo: userData.activo,
        nivel: decoded.nivel || userData.nivel || []  // Tomar nivel de decoded o userData
      };

      console.log('✅ decodeToken: Usuario extraído del token', {
        username: user.username,
        nivel: user.nivel
      });

      return user;

    } catch (error) {
      console.error('❌ decodeToken: Error al decodificar token:', error);
      return null;
    }
  }

  /**
   * Verifica si el token ha expirado
   */
  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const exp = payload.exp;
      if (!exp) return true;
      return Date.now() >= exp * 1000;
    } catch {
      return true;
    }
  }

  /**
   * Cierra sesión
   */
  logout(): void {
    this.clearAuthState();
  }

  private clearAuthState(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.TOKEN_KEY);
      localStorage.removeItem(this.USER_KEY);
    }
    this.authState.next({
      token: null,
      user: null,
      isAuthenticated: false
    });
  }

  // ── Métodos de utilidad ────────────────────────────────────────

  /**
   * Chequeo síncrono de autenticación
   */
  get isAuthenticatedSync(): boolean {
    if (!isPlatformBrowser(this.platformId)) return false;

    const token = localStorage.getItem(this.TOKEN_KEY);
    if (!token) return false;

    if (this.isTokenExpired(token)) {
      this.logout();
      return false;
    }

    return true;
  }

  /**
   * Obtiene el usuario actual (sin logs excesivos)
   */
  get currentUser(): UserJwtDto | null {
    return this.authState.value.user;
  }

  /**
   * Obtiene el token actual
   */
  get token(): string | null {
    const t = this.authState.value.token;
    if (t && this.isTokenExpired(t)) {
      this.logout();
      return null;
    }
    return t;
  }

  /**
   * Obtiene el ID del trabajador
   */
  get currentTrabajadorId(): number | null {
    return this.currentUser?.idTrabajador ?? null;
  }

  // ── Métodos para verificar permisos basados en nivel ───────────

  /**
   * Verifica si el usuario tiene un nivel específico
   */
  isNivel(nivel: string): boolean {
    return this.currentUser?.nivel?.includes(nivel) ?? false;
  }

  /**
   * Verifica si el usuario tiene un nivel mínimo requerido
   * Orden: L1 > L2 > L3 > L4 > L5
   */
  isNivelMinimo(nivelRequerido: string): boolean {
    const orden = ['L1', 'L2', 'L3', 'L4', 'L5'];
    const userNivel = this.currentUser?.nivel?.[0];
    if (!userNivel) return false;

    return orden.indexOf(userNivel) <= orden.indexOf(nivelRequerido);
  }

  /**
   * Verifica si el usuario pertenece a un área específica
   */
  isArea(area: string): boolean {
    return this.currentUser?.area?.toUpperCase() === area.toUpperCase();
  }

  /**
   * Verifica si el usuario tiene un cargo específico
   */
  isCargo(texto: string): boolean {
    return this.currentUser?.cargo
      ?.toUpperCase()
      .includes(texto.toUpperCase()) ?? false;
  }

  /**
   * Fuerza recarga del estado
   */
  public refreshAuthState(): void {
    this.loadInitialState();
  }

  /**
   * Manejo de errores HTTP
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let message = 'Ocurrió un error inesperado';

    if (error.status === 401) {
      message = 'Usuario o contraseña incorrectos';
    } else if (error.status === 0) {
      message = 'No se pudo conectar al servidor. Verifica tu conexión.';
    } else if (error.error?.message) {
      message = error.error.message;
    } else if (error.error?.error) {
      message = error.error.error;
    }

    console.error('❌ AuthService error:', error);
    return throwError(() => new Error(message));
  }

  /**
   * Método para debug (solo usar cuando sea necesario)
   */
  public debugAuthState(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    console.log('🔍 DEBUG Auth State:', {
      localStorage: {
        token: localStorage.getItem(this.TOKEN_KEY)?.substring(0, 20) + '...',
        user: localStorage.getItem(this.USER_KEY)
      },
      currentState: this.authState.value,
      isAuthenticatedSync: this.isAuthenticatedSync
    });
  }
}

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
  usuario: UserJwtDto;
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
  roles: string[];
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

private loadInitialState(): void {
  console.log('🔄 loadInitialState: Cargando estado inicial');

  if (!isPlatformBrowser(this.platformId)) return;

  const token = localStorage.getItem(this.TOKEN_KEY);
  const userStr = localStorage.getItem(this.USER_KEY);

  console.log('📦 loadInitialState: Datos en localStorage', {
    hasToken: !!token,
    hasUser: !!userStr,
    userStr: userStr
  });

  // Si no hay token, limpiar todo
  if (!token) {
    console.log('🔄 loadInitialState: No hay token, limpiando estado');
    this.clearAuthState();
    return;
  }

  // Verificar si el token está expirado
  if (this.isTokenExpired(token)) {
    console.log('🔄 loadInitialState: Token expirado, limpiando estado');
    this.logout();
    return;
  }

  // Si hay token pero no usuario en localStorage, decodificar del token
  let user: UserJwtDto | null = null;

  if (userStr) {
    try {
      // Verificar que no sea el string "undefined"
      if (userStr === 'undefined' || userStr === 'null') {
        console.warn('⚠️ loadInitialState: Datos corruptos en localStorage');
        localStorage.removeItem(this.USER_KEY);
      } else {
        user = JSON.parse(userStr);
        console.log('✅ loadInitialState: Usuario cargado de localStorage', user?.username);
      }
    } catch (error) {
      console.error('❌ loadInitialState: Error parseando usuario:', error);
      localStorage.removeItem(this.USER_KEY);
    }
  }

  // Si no pudimos cargar el usuario de localStorage, decodificar del token
  if (!user) {
    console.log('🔄 loadInitialState: Decodificando usuario del token');
    user = this.decodeToken(token);
  }

  if (user) {
    this.setAuthState(token, user);
    console.log('✅ loadInitialState: Estado cargado exitosamente');
  } else {
    console.error('❌ loadInitialState: No se pudo cargar usuario');
    this.logout();
  }
}

  /**
   * Inicia sesión y guarda el token
   */
login(credentials: LoginRequest): Observable<AuthResponse> {
  return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials).pipe(
    tap(response => {
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
   * Cierra sesión y limpia todo
   */
  logout(): void {
    this.clearAuthState();
  }

private setToken(token: string, usuario: UserJwtDto): void {
  console.log('🔐 setToken: Iniciando', { token: token?.substring(0, 20), usuario });

  if (!isPlatformBrowser(this.platformId)) return;

  // Validar que el usuario no sea undefined/null
  if (!usuario) {
    console.error('❌ setToken: usuario es undefined/null');
    return;
  }

  // Validar que el token no sea undefined/null
  if (!token) {
    console.error('❌ setToken: token es undefined/null');
    return;
  }

  try {
    // Convertir el usuario a JSON de manera segura
    const usuarioJSON = JSON.stringify(usuario);

    // Guardar en localStorage
    localStorage.setItem(this.TOKEN_KEY, token);
    localStorage.setItem(this.USER_KEY, usuarioJSON);

    console.log('✅ setToken: Datos guardados en localStorage', {
      tokenLength: token.length,
      usuario: usuario.username,
      localStorageUser: localStorage.getItem(this.USER_KEY)
    });

    // Actualizar el estado
    this.setAuthState(token, usuario);

  } catch (error) {
    console.error('❌ setToken: Error al guardar datos:', error);
  }
}

private setAuthState(token: string, user: UserJwtDto): void {
  console.log('🔄 setAuthState: Actualizando estado', {
    token: token?.substring(0, 20),
    user: user?.username
  });

  this.authState.next({
    token,
    user,
    isAuthenticated: true
  });

  console.log('✅ setAuthState: Estado actualizado', this.authState.value);
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

  /**
   * Decodifica el payload del JWT (sin verificar firma, solo lectura)
   */
  private decodeToken(token: string): UserJwtDto | null {
    try {
      const payload = token.split('.')[1];
      const decoded = JSON.parse(atob(payload));
      // Ajusta según la estructura real de tu JWT
      return decoded.data as UserJwtDto ?? decoded as UserJwtDto ?? null;
    } catch (e) {
      console.error('Error al decodificar JWT:', e);
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

  // NIVEL
  isNivel(nivel: string): boolean {
    return this.currentUser?.roles?.includes(nivel) ?? false;
  }

  isNivelMinimo(nivelRequerido: string): boolean {
    const orden = ['L1', 'L2', 'L3', 'L4', 'L5'];
    const userNivel = this.currentUser?.roles?.[0];
    if (!userNivel) return false;

    return orden.indexOf(userNivel) <= orden.indexOf(nivelRequerido);
  }

  // ÁREA
  isArea(area: string): boolean {
    return this.currentUser?.area?.toUpperCase() === area.toUpperCase();
  }

  // CARGO
  isCargo(texto: string): boolean {
    return this.currentUser?.cargo
      ?.toUpperCase()
      .includes(texto.toUpperCase()) ?? false;
  }

  // ── Métodos públicos útiles ────────────────────────────────────────

  /**
   * Chequeo SINCRÓNICO (ideal para guards y evitar race conditions en refresh)
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

get currentUser(): UserJwtDto | null {
  const state = this.authState.value;

  // Sin logs para evitar loops
  if (!state.token || this.isTokenExpired(state.token)) {
    this.logout();
    return null;
  }

  // Si ya tenemos usuario en el estado, retornarlo
  if (state.user) {
    return state.user;
  }

  // Si no hay usuario en el estado pero hay token
  if (state.token && !state.user) {
    const userFromToken = this.decodeToken(state.token);
    if (userFromToken) {
      // Actualizar el estado con el usuario decodificado
      this.authState.next({
        ...state,
        user: userFromToken
      });
      return userFromToken;
    }
  }

  return null;
}
  get currentTrabajadorId(): number | null {
    return this.currentUser?.idTrabajador ?? null;
  }

  get token(): string | null {
    const t = this.authState.value.token;
    if (t && this.isTokenExpired(t)) {
      this.logout();
      return null;
    }
    return t;
  }

  /**
   * Fuerza recarga del estado (útil después de refresh token o cambios)
   */
  public refreshAuthState(): void {
    this.loadInitialState();
  }

  // ── Manejo de errores ──────────────────────────────────────────────

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

    return throwError(() => new Error(message));
  }
}

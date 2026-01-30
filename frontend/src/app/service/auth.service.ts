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

  /**
   * Carga el estado inicial desde localStorage (solo en browser)
   */
  private loadInitialState(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    const token = localStorage.getItem(this.TOKEN_KEY);
    if (!token) return;

    const userData = this.decodeToken(token);
    if (!userData || this.isTokenExpired(token)) {
      this.logout();
      return;
    }

    this.setAuthState(token, userData);
  }

  /**
   * Inicia sesión y guarda el token
   */
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials).pipe(
      tap(response => this.setToken(response.token, response.usuario)),
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
  if (!isPlatformBrowser(this.platformId)) return;

  // Guardar en localStorage
  localStorage.setItem(this.TOKEN_KEY, token);
  localStorage.setItem(this.USER_KEY, JSON.stringify(usuario));

  // Actualizar el estado inmediatamente
  this.authState.next({
    token,
    user: usuario,
    isAuthenticated: true
  });

  console.log('🔐 setToken: Token y usuario guardados', {
    tokenExists: !!token,
    user: usuario.username,
    localStorageUser: localStorage.getItem(this.USER_KEY)
  });
}

  // En auth.service.ts
private setAuthState(token: string, user: UserJwtDto): void {
  this.authState.next({
    token,
    user,
    isAuthenticated: true
  });

  // ✅ Asegúrate de que el localStorage se actualice también
  if (isPlatformBrowser(this.platformId)) {
    localStorage.setItem(this.TOKEN_KEY, token);
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }
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

  console.log('👤 currentUser - Estado actual:', {
    hasToken: !!state.token,
    hasUser: !!state.user,
    isAuthenticated: state.isAuthenticated
  });

  // Si hay usuario en el estado, retornarlo
  if (state.user) {
    console.log('👤 currentUser: Retornando usuario del estado:', state.user.username);
    return state.user;
  }

  // Si no hay usuario en el estado pero hay token
  if (state.token && !state.user) {
    console.log('👤 currentUser: No hay user en estado, decodificando del token...');
    const userFromToken = this.decodeToken(state.token);
    if (userFromToken) {
      console.log('👤 currentUser: Usuario decodificado del token:', userFromToken.username);
      // Actualizar el estado con el usuario decodificado
      this.authState.next({
        ...state,
        user: userFromToken
      });
      return userFromToken;
    }
  }

  console.log('👤 currentUser: No se pudo obtener usuario');
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

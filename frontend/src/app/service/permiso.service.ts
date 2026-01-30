import { Injectable, inject, Injector } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { environment } from '../../environment';
import { PermisoResponseDTO, VerificarPermisoDTO } from '../model/permiso.interface';
import { PageResponseDTO } from './cliente.service';

@Injectable({
  providedIn: 'root'
})
export class PermisoService {
  private http = inject(HttpClient);
  private injector = inject(Injector); // Usar Injector para resolver dependencias lazy

  private API_URL = `${environment.baseUrl}/api/permisos`;
  private readonly PERMISOS_KEY = 'user_permisos';

  private permisosSubject = new BehaviorSubject<string[]>([]);
  permisos$ = this.permisosSubject.asObservable();

  constructor() {
    this.cargarPermisosDesdeStorage();
  }

  // ========== CRUD BÁSICO ==========

  crearPermiso(permisoDTO: any): Observable<PermisoResponseDTO> {
    return this.http.post<PermisoResponseDTO>(this.API_URL, permisoDTO);
  }

  actualizarPermiso(id: number, permisoDTO: any): Observable<PermisoResponseDTO> {
    return this.http.put<PermisoResponseDTO>(`${this.API_URL}/${id}`, permisoDTO);
  }

  eliminarPermiso(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  obtenerPermisoPorId(id: number): Observable<PermisoResponseDTO> {
    return this.http.get<PermisoResponseDTO>(`${this.API_URL}/${id}`);
  }
// ✅ Nuevo método para obtener permisos paginados
  listarPermisosPaginados(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'codigo',
    sortDirection: string = 'asc'
  ): Observable<PageResponseDTO<any>> {

    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    return this.http.get<PageResponseDTO<any>>(`${this.API_URL}/paginados`, { params });
  }
  obtenerPermisoPorCodigo(codigo: string): Observable<PermisoResponseDTO> {
    return this.http.get<PermisoResponseDTO>(`${this.API_URL}/codigo/${codigo}`);
  }

  listarTodosPermisos(): Observable<PermisoResponseDTO[]> {
    return this.http.get<PermisoResponseDTO[]>(this.API_URL);
  }

  // ========== VERIFICACIÓN DE PERMISOS ==========

  verificarPermisoUsuario(codigoPermiso: string, idUsuario: number): Observable<boolean> {
    const dto: VerificarPermisoDTO = { codigoPermiso, idUsuario };
    return this.http.post<boolean>(`${this.API_URL}/verificar`, dto);
  }

  obtenerPermisosPorNivel(idNivel: number): Observable<PermisoResponseDTO[]> {
    return this.http.get<PermisoResponseDTO[]>(`${this.API_URL}/nivel/${idNivel}`);
  }

  obtenerPermisosPorArea(idArea: number): Observable<PermisoResponseDTO[]> {
    return this.http.get<PermisoResponseDTO[]>(`${this.API_URL}/area/${idArea}`);
  }

  obtenerPermisosPorCargo(idCargo: number): Observable<PermisoResponseDTO[]> {
    return this.http.get<PermisoResponseDTO[]>(`${this.API_URL}/cargo/${idCargo}`);
  }

  // ========== MÉTODOS PARA EL USUARIO ACTUAL ==========

  obtenerPermisosUsuario(idUsuario: number): Observable<string[]> {
    return this.http.get<string[]>(`${this.API_URL}/usuario/${idUsuario}`).pipe(
      tap(permisos => this.guardarPermisosEnStorage(permisos))
    );
  }

  cargarPermisosUsuarioActual(idUsuario: number): void {
    this.obtenerPermisosUsuario(idUsuario).subscribe({
      error: (error) => {
        console.error('Error cargando permisos:', error);
        this.permisosSubject.next([]);
      }
    });
  }

  // ========== MÉTODOS PARA VERIFICACIÓN EN FRONTEND ==========

  tienePermiso(codigoPermiso: string): boolean {
    const permisos = this.permisosSubject.value;
    return permisos.includes(codigoPermiso);
  }

  tieneAlgunPermiso(codigosPermisos: string[]): boolean {
    const permisos = this.permisosSubject.value;
    return codigosPermisos.some(codigo => permisos.includes(codigo));
  }

  tieneTodosPermisos(codigosPermisos: string[]): boolean {
    const permisos = this.permisosSubject.value;
    return codigosPermisos.every(codigo => permisos.includes(codigo));
  }

  // Método para obtener AuthService solo cuando sea necesario (lazy)
  private getAuthService(): any {
    // Usamos el injector para obtener AuthService solo cuando sea necesario
    // Esto rompe la dependencia circular
    const { AuthService } = require('./auth.service');
    return this.injector.get(AuthService);
  }

  verificarPermisoRemoto(codigoPermiso: string, idUsuario: number): Observable<boolean> {
    return this.verificarPermisoUsuario(codigoPermiso, idUsuario).pipe(
      catchError(() => of(false))
    );
  }

  // ========== MÉTODOS DE STORAGE ==========

  private cargarPermisosDesdeStorage(): void {
    const permisosStr = localStorage.getItem(this.PERMISOS_KEY);
    if (permisosStr) {
      try {
        const permisos = JSON.parse(permisosStr);
        this.permisosSubject.next(permisos);
      } catch {
        this.permisosSubject.next([]);
      }
    }
  }

  private guardarPermisosEnStorage(permisos: string[]): void {
    localStorage.setItem(this.PERMISOS_KEY, JSON.stringify(permisos));
    this.permisosSubject.next(permisos);
  }

  limpiarPermisos(): void {
    localStorage.removeItem(this.PERMISOS_KEY);
    this.permisosSubject.next([]);
  }

  getPermisosActuales(): string[] {
    return this.permisosSubject.value;
  }
}

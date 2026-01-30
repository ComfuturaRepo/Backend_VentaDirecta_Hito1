import { Component, inject, OnInit, HostListener, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import Swal from 'sweetalert2';

// Servicios
import { AuthService } from '../../service/auth.service';
import { PermisoDirective } from '../../directive/permiso.directive';
import { PermisoService } from '../../service/permiso.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    PermisoDirective
  ],
  templateUrl: './layaout-component.html',
  styleUrl: './layaout-component.css',
})
export class LayoutComponent implements OnInit, OnDestroy {
  constructor(
    private authService: AuthService,
    private permisoService: PermisoService,
    private router: Router,
    private sanitizer: DomSanitizer
  ) {}

  // Estado del layout
  isCollapsed = false;
  isMobileOpen = false;
  showUserMenu = false;
  showTopUserMenu = false;
  showNotifications = false;
  mostrarModalPermisos = false;

  // Datos
  logoUrl: SafeUrl | null = null;
  isMobile = false;

  // Permisos
  permisosUsuario: string[] = [];
  permisosCount = 0;
  cargandoPermisos = false;

  private resizeObserver?: ResizeObserver;

  ngOnInit() {
    this.loadLogo();
    this.checkScreenSize();
    this.setupResizeObserver();

    // Suscribirse a cambios en el estado de autenticación
    this.authService.authState$.subscribe(state => {
      if (state.isAuthenticated && state.user) {
        this.cargarPermisosUsuario(state.user.idUsuario);
      } else {
        this.permisosUsuario = [];
        this.permisosCount = 0;
      }
    });
  }

  ngOnDestroy() {
    this.resizeObserver?.disconnect();
    document.body.classList.remove('no-scroll');
  }

  // ========== MÉTODOS DE PERMISOS ==========

  cargarPermisosUsuario(idUsuario: number): void {
    this.cargandoPermisos = true;
    this.permisoService.obtenerPermisosUsuario(idUsuario).subscribe({
      next: (permisos) => {
        this.permisosUsuario = permisos;
        this.permisosCount = permisos.length;
        this.cargandoPermisos = false;
      },
      error: (error) => {
        console.error('Error cargando permisos:', error);
        this.permisosUsuario = [];
        this.permisosCount = 0;
        this.cargandoPermisos = false;
      }
    });
  }

  tienePermiso(codigoPermiso: string): boolean {
    return this.permisoService.tienePermiso(codigoPermiso);
  }

  tieneAlgunPermiso(codigosPermisos: string[]): boolean {
    return this.permisoService.tieneAlgunPermiso(codigosPermisos);
  }

  mostrarPermisosModal(): void {
    this.mostrarModalPermisos = true;
  }

  cerrarModalPermisos(): void {
    this.mostrarModalPermisos = false;
  }

  irAPerfil(): void {
    this.router.navigate(['/perfil']);
  }

  getDescripcionPermiso(codigo: string): string {
    const descripciones: { [key: string]: string } = {
      'DASHBOARD_VIEW': 'Ver el dashboard principal',
      'OT_VIEW': 'Ver órdenes de trabajo',
      'OT_CREATE': 'Crear nuevas OTs',
      'OT_EDIT': 'Editar OTs existentes',
      'SITE_VIEW': 'Ver sitios/locations',
      'SITE_MANAGE': 'Gestionar sitios',
      'OC_VIEW': 'Ver órdenes de compra',
      'OC_CREATE': 'Crear órdenes de compra',
      'OC_EDIT': 'Editar órdenes de compra',
      'USUARIO_VIEW': 'Ver usuarios del sistema',
      'USUARIO_CREATE': 'Crear nuevos usuarios',
      'USUARIO_EDIT': 'Editar usuarios',
      'USUARIO_DELETE': 'Eliminar usuarios',
      'PERMISO_ADMIN': 'Administrar permisos del sistema',
      'TRABAJADOR_VIEW': 'Ver trabajadores',
      'TRABAJADOR_MANAGE': 'Gestionar trabajadores',
      'CLIENTE_VIEW': 'Ver clientes',
      'CLIENTE_MANAGE': 'Gestionar clientes',
      'CONFIGURACION_VIEW': 'Ver configuración',
      'CONFIGURACION_MANAGE': 'Gestionar configuración',
      'REPORTES_VIEW': 'Ver reportes',
      'REPORTES_GENERAR': 'Generar reportes',
      'PERFIL_VIEW': 'Ver perfil de usuario',
      'PERFIL_EDIT': 'Editar perfil de usuario'
    };

    return descripciones[codigo] || 'Permiso del sistema';
  }

  // ========== MÉTODOS DE SEGURIDAD (compatibilidad) ==========

  isNivel(nivel: string): boolean {
    return this.authService.isNivel(nivel);
  }

  isNivelMinimo(nivelRequerido: string): boolean {
    return this.authService.isNivelMinimo(nivelRequerido);
  }

  isArea(area: string): boolean {
    return this.authService.isArea(area);
  }

  isCargo(texto: string): boolean {
    return this.authService.isCargo(texto);
  }

  isAnyArea(areas: string[]): boolean {
    if (!this.authService.currentUser?.area) return false;
    return areas.some(area =>
      this.authService.currentUser?.area?.toUpperCase() === area.toUpperCase()
    );
  }

  isAnyNivel(niveles: string[]): boolean {
    if (!this.authService.currentUser?.roles) return false;
    return niveles.some(nivel =>
      this.authService.currentUser?.roles?.includes(nivel)
    );
  }

  getNivel(): string {
    const roles = this.authService.currentUser?.roles || [];
    const nivel = roles.find(r => r.startsWith('L'));
    return nivel || 'Sin nivel';
  }

  getArea(): string {
    return this.authService.currentUser?.area || 'Sin área';
  }

  getCargo(): string {
    return this.authService.currentUser?.cargo || 'Sin cargo';
  }

  // ========== MÉTODOS EXISTENTES ==========

  private loadLogo() {
    try {
      const cloudinaryUrl = 'https://res.cloudinary.com/dqznlmig0/image/upload/v1769301735/COMFUTURA_LOGOTIPO-04_kwulpt.png';
      this.logoUrl = this.sanitizer.bypassSecurityTrustUrl(cloudinaryUrl);
    } catch (error) {
      console.warn('Logo no encontrado, usando ícono por defecto');
    }
  }

  private checkScreenSize() {
    this.isMobile = window.innerWidth < 1200;
    if (this.isMobile && this.isCollapsed) {
      this.isCollapsed = false;
    }
  }

  private setupResizeObserver() {
    if (typeof ResizeObserver !== 'undefined') {
      this.resizeObserver = new ResizeObserver(() => {
        this.checkScreenSize();
      });

      const mainContent = document.querySelector('.main-content');
      if (mainContent) {
        this.resizeObserver.observe(mainContent);
      }
    }
  }

  @HostListener('window:resize')
  onResize() {
    this.checkScreenSize();
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;

    // Cerrar menús al hacer click fuera
    if (this.showUserMenu && !target.closest('.user-profile') && !target.closest('.user-menu-dropdown')) {
      this.closeUserMenu();
    }

    if (this.showTopUserMenu && !target.closest('.user-dropdown')) {
      this.closeTopUserMenu();
    }

    if (this.showNotifications && !target.closest('.notification-btn') && !target.closest('.notifications-panel')) {
      this.closeNotifications();
    }

    if (this.mostrarModalPermisos && !target.closest('#permisosModal') && !target.closest('.permisos-btn')) {
      this.cerrarModalPermisos();
    }
  }

  get username(): string {
    return this.authService.currentUser?.username ?? 'Usuario';
  }

  get nombreCompleto(): string {
    return this.authService.currentUser?.nombreCompleto ?? 'Usuario';
  }

  get userInitial(): string {
    const name = this.authService.currentUser?.nombreCompleto || this.authService.currentUser?.username;
    return name ? name.charAt(0).toUpperCase() : 'U';
  }

  get currentUser() {
    return this.authService.currentUser;
  }

  get mainRole(): string {
    const roles = this.currentUser?.roles;
    if (!roles || roles.length === 0) return 'Usuario';

    // Prioridad: ADMIN > L niveles > otros
    const adminRole = roles.find(r => r.includes('ADMIN'));
    if (adminRole) return adminRole;

    const nivelRole = roles.find(r => r.startsWith('L'));
    if (nivelRole) return nivelRole;

    return roles[0];
  }

  get userBadgeClass(): string {
    const role = this.mainRole.toLowerCase();
    if (role.includes('admin')) return 'admin-badge';
    if (role.includes('gerente') || role.includes('jefe') || role.includes('l4') || role.includes('l5')) {
      return 'manager-badge';
    }
    if (role.includes('permiso') || this.tienePermiso('PERMISO_ADMIN')) {
      return 'permiso-badge';
    }
    return 'user-badge';
  }

  get hasNotifications(): boolean {
    return true;
  }

  toggleCollapse() {
    if (this.isMobile) {
      this.isMobileOpen = !this.isMobileOpen;
      this.updateBodyScroll();
    } else {
      this.isCollapsed = !this.isCollapsed;
    }
  }

  toggleMobile() {
    this.isMobileOpen = !this.isMobileOpen;
    this.updateBodyScroll();
  }

  closeMobile() {
    this.isMobileOpen = false;
    this.updateBodyScroll();
  }

  private updateBodyScroll() {
    if (this.isMobileOpen) {
      document.body.classList.add('no-scroll');
    } else {
      document.body.classList.remove('no-scroll');
    }
  }

  toggleUserMenu() {
    this.showUserMenu = !this.showUserMenu;
  }

  closeUserMenu() {
    this.showUserMenu = false;
  }

  toggleTopUserMenu() {
    this.showTopUserMenu = !this.showTopUserMenu;
  }

  closeTopUserMenu() {
    this.showTopUserMenu = false;
  }

  toggleNotifications() {
    this.showNotifications = !this.showNotifications;
  }

  closeNotifications() {
    this.showNotifications = false;
  }

  logout() {
    Swal.fire({
      title: '¿Cerrar sesión?',
      text: 'Estás a punto de salir del sistema',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        this.authService.logout();
        this.permisoService.limpiarPermisos();
        this.router.navigate(['/login']);
        Swal.fire({
          icon: 'success',
          title: 'Sesión cerrada',
          text: 'Has salido del sistema correctamente',
          timer: 1500,
          showConfirmButton: false
        });
      }
    });
  }
}

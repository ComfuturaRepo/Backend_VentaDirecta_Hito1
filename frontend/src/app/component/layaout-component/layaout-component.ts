import { Component, inject, OnInit, HostListener, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './layaout-component.html',
  styleUrl: './layaout-component.css',
})
export class LayoutComponent implements OnInit, OnDestroy {
  private authService = inject(AuthService);
  private router = inject(Router);
  private sanitizer = inject(DomSanitizer);

  isCollapsed = false;
  isMobileOpen = false;
  logoUrl: SafeUrl | null = null;
  isMobile = false;
  private resizeObserver?: ResizeObserver;

  ngOnInit() {
    this.loadLogo();
    this.checkScreenSize();
    this.setupResizeObserver();
    this.onResize();
  }

  ngOnDestroy() {
    this.resizeObserver?.disconnect();
    document.body.classList.remove('no-scroll');
  }

  private loadLogo() {
    try {
      // Usando la URL de Cloudinary que ya tienes en el HTML
      const cloudinaryUrl = 'https://res.cloudinary.com/dqznlmig0/image/upload/v1769211282/COMFUTURA_LOGOTIPO-02_ubswoz.png';
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

  get username(): string {
    return this.authService.currentUser?.username ?? 'Usuario';
  }

  get userInitial(): string {
    const name = this.authService.currentUser?.username;
    return name ? name.charAt(0).toUpperCase() : 'U';
  }

  get currentUser() {
    return this.authService.currentUser;
  }

  get mainRole(): string {
    const roles = this.currentUser?.roles;
    if (!roles || roles.length === 0) return '';
    // Priorizar ADMIN si existe
    const adminRole = roles.find(r => r.includes('ADMIN'));
    return adminRole || roles[0];
  }

  get userBadgeClass(): string {
    const role = this.mainRole.toLowerCase();
    if (role.includes('admin')) return 'admin-badge';
    if (role.includes('gerente') || role.includes('jefe')) return 'manager-badge';
    return 'user-badge';
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
      reverseButtons: true,
      customClass: {
        container: 'swal-container'
      }
    }).then((result) => {
      if (result.isConfirmed) {
        this.authService.logout();
        this.router.navigate(['/login']);

        Swal.fire({
          icon: 'success',
          title: 'Sesión cerrada',
          text: 'Has salido del sistema correctamente',
          timer: 1500,
          showConfirmButton: false,
          customClass: {
            container: 'swal-container'
          }
        });
      }
    });
  }

  // Método para verificar si hay notificaciones
  get hasNotifications(): boolean {
    // Implementa lógica real si tienes notificaciones
    // Por ahora, devuelve false para ocultar el badge
    return false;
  }
}
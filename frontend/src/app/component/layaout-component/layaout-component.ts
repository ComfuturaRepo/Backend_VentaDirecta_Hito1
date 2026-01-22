import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../service/auth.service';

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
export class LayoutComponent {
  private authService = inject(AuthService);

  isCollapsed  = false;
  isMobileOpen = false;

  // ── Datos del usuario desde JWT ───────────────────────────────
  get username(): string {
    return this.authService.currentUser?.username ?? 'Usuario';
  }

  get userInitial(): string {
    const name = this.authService.currentUser?.username;
    return name ? name.charAt(0).toUpperCase() : 'U';
  }

  // Opcional: si tienes roles en el JWT
  get isAdmin(): boolean {
    return this.authService.currentUser?.roles?.includes('ADMIN') ?? false;
  }

  // ── Métodos del sidebar ───────────────────────────────────────
  toggleCollapse() {
    this.isCollapsed = !this.isCollapsed;
  }

  toggleMobile() {
    this.isMobileOpen = !this.isMobileOpen;
  }

  closeMobile() {
    this.isMobileOpen = false;
  }

  logout() {
    this.authService.logout();
  }
}

import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';

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
private router = inject(Router);

  isCollapsed  = false;
  isMobileOpen = false;

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

  // Opcional: helper para mostrar roles
  get mainRole(): string {
    const roles = this.currentUser?.roles;
    if (!roles || roles.length === 0) return '';
    return roles[0]; // o roles.find(r => r === 'ADMIN') ?? roles[0]
  }

  get isAdmin(): boolean {
    return this.currentUser?.roles?.includes('ADMIN') ?? false;
  }

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
  this.authService.logout();      // limpia el token / sesión
  this.router.navigate(['/login']); // redirige a la ruta de login
}

}

  import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive,CommonModule],
  templateUrl: './layaout-component.html',
  styleUrl: './layaout-component.css',
})
export class LayoutComponent {
  private router = inject(Router);
  private authService = inject(AuthService);

  username = 'Usuario';

  isCollapsed = false;
  isMobileOpen = false;

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
    this.router.navigate(['/login']);
  }
}

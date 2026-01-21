// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login-componente/login-componente';
import { DashboardComponent } from './pages/dashboard-componente/dashboard-componente';
import { authGuard } from './auth/auth.guard';
import { LayoutComponent } from './component/layaout-component/layaout-component';
import { OtsComponent } from './pages/ots-component/ots-component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: '',
    component: LayoutComponent,
    canActivateChild: [authGuard],
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
      },
      {
        path: 'ot',
        component: OtsComponent,
      },
      // NO DEBES TENER NADA DE ESTO aquí:
      // { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
      // { path: '**', redirectTo: 'dashboard' }
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];

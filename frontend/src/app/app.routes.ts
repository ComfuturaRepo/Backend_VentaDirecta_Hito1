// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login-componente/login-componente';
import { DashboardComponent } from './pages/dashboard-componente/dashboard-componente';
import { authGuard } from './auth/auth.guard';
import { LayoutComponent } from './component/layaout-component/layaout-component';
import { OtsComponent } from './pages/ots-component/ots-component';
import { FormOtsComponent } from './pages/ots-component/form-ots-component/form-ots-component';

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
        children: [                          // ← convierte 'ot' en contenedor de hijos
          {
            path: '',                        // ruta por defecto → lista
            component: OtsComponent,
          },
          {
            path: 'nuevo',                   // crear nueva
            component: FormOtsComponent,
          },
        ]
      },
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];

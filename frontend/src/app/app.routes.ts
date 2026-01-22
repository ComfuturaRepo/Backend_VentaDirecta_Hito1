// app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login-componente/login-componente';
import { DashboardComponent } from './pages/dashboard-componente/dashboard-componente';
import { authGuard } from './auth/auth.guard';
import { OtsComponent } from './pages/ots-component/ots-component';
import { FormOtsComponent } from './pages/ots-component/form-ots-component/form-ots-component';
import { OtDetailComponent } from './pages/ots-component/ot-detail-component/ot-detail-component';
import { LayoutComponent } from './component/layaout-component/layaout-component';

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
        children: [
          { path: '',            component: OtsComponent           }, // lista
          { path: 'nuevo',        component: FormOtsComponent      }, // crear
          { path: 'editar/:id',   component: FormOtsComponent      }, // editar
          { path: ':id',          component: OtDetailComponent     }  // ver detalle
        ]
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];

// app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login-componente/login-componente';
import { DashboardComponent } from './pages/dashboard-componente/dashboard-componente';
import { authGuard } from './auth/auth.guard';
import { OtsComponent } from './pages/ots-component/ots-component';
import { FormOtsComponent } from './pages/ots-component/form-ots-component/form-ots-component';
import { OtDetailComponent } from './pages/ots-component/ot-detail-component/ot-detail-component';
import { LayoutComponent } from './component/layaout-component/layaout-component';
import { SiteComponent } from './pages/site-component/site-component';   // ← nuevo import
import { AnalistaClienteSolicitanteComponent } from './pages/analista-cliente-solicitante-component/analista-cliente-solicitante-component';
import { JefaturaClienteSolicitanteComponent } from './pages/jefatura-cliente-solicitante-component/jefatura-cliente-solicitante-component';

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
          { path: 'nuevo',       component: FormOtsComponent      }, // crear
          { path: 'editar/:id',  component: FormOtsComponent      }, // editar
          { path: ':id',         component: OtDetailComponent     }  // ver detalle
        ]
      },
      {
        path: 'site',
        component: SiteComponent,          // ← gestión de sitios (listado + CRUD)
      },
      {
        path: 'analista-cliente-solicitante',
        component: AnalistaClienteSolicitanteComponent,          // ← gestión de sitios (listado + CRUD)
      },
      {
        path: 'jefatura-cliente-solicitante',
        component: JefaturaClienteSolicitanteComponent,          // ← gestión de sitios (listado + CRUD)
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];

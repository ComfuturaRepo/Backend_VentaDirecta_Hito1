import { Routes } from '@angular/router';

import { LoginComponent } from './pages/login-componente/login-componente';
import { DashboardComponent } from './pages/dashboard-componente/dashboard-componente';
import { OtsComponent } from './pages/ots-component/ots-component';
import { FormOtsComponent } from './pages/ots-component/form-ots-component/form-ots-component';
import { OtDetailComponent } from './pages/ots-component/ot-detail-component/ot-detail-component';
import { LayoutComponent } from './component/layaout-component/layaout-component';
import { SiteComponent } from './pages/site-component/site-component';
import { AnalistaClienteSolicitanteComponent } from './pages/analista-cliente-solicitante-component/analista-cliente-solicitante-component';
import { JefaturaClienteSolicitanteComponent } from './pages/jefatura-cliente-solicitante-component/jefatura-cliente-solicitante-component';
import { OrdenCompraComponent } from './pages/orden-compra-component/orden-compra-component';

import { authGuard } from './auth/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    data: { renderMode: 'server' } // SSR runtime
  },
  {
    path: '',
    component: LayoutComponent,
    canActivateChild: [authGuard],
    data: { renderMode: 'server' },
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
        data: { renderMode: 'server' }
      },
      {
        path: 'ot',
        data: { renderMode: 'server' },
        children: [
          {
            path: '',
            component: OtsComponent,
            data: { renderMode: 'server' }
          },
          {
            path: 'nuevo',
            component: FormOtsComponent,
            data: { renderMode: 'server' }
          },
          {
            path: 'editar/:id',
            component: FormOtsComponent,
            data: { renderMode: 'server' } // 👈 CLAVE
          },
          {
            path: ':id',
            component: OtDetailComponent,
            data: { renderMode: 'server' } // 👈 CLAVE
          }
        ]
      },
      {
        path: 'site',
        component: SiteComponent,
        data: { renderMode: 'server' }
      },
      {
        path: 'analista-cliente-solicitante',
        component: AnalistaClienteSolicitanteComponent,
        data: { renderMode: 'server' }
      },
      {
        path: 'jefatura-cliente-solicitante',
        component: JefaturaClienteSolicitanteComponent,
        data: { renderMode: 'server' }
      },
      {
        path: 'orden-compra',
        component: OrdenCompraComponent,
        data: { renderMode: 'server' }
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];

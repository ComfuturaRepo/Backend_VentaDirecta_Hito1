// src/app/app.routes.ts   ← Rutas para el cliente (browser)
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
  },
  {
    path: '',
    component: LayoutComponent,
    canActivateChild: [authGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      {
        path: 'ot',
        children: [
          { path: '', component: OtsComponent },
          { path: 'nuevo', component: FormOtsComponent },
         // { path: 'editar/:id', component: FormOtsComponent },
         // { path: ':id', component: OtDetailComponent },
        ],
      },
      { path: 'site', component: SiteComponent },
      { path: 'analista-cliente-solicitante', component: AnalistaClienteSolicitanteComponent },
      { path: 'jefatura-cliente-solicitante', component: JefaturaClienteSolicitanteComponent },
      { path: 'orden-compra', component: OrdenCompraComponent },
    ],
  },
  {
    path: '**',
    redirectTo: 'login',
  },
];

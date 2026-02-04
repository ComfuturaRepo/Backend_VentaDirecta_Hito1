// src/app/app.routes.ts
import { Routes } from '@angular/router';

import { LoginComponent } from './pages/login-componente/login-componente';
import { OtsComponent } from './pages/ots-component/ots-component';
import { SiteComponent } from './pages/site-component/site-component';
import { OrdenCompraComponent } from './pages/orden-compra-component/orden-compra-component';
import { GestionCargosSolicitantesComponent } from './pages/gestion-cargos-solicitantes-component/gestion-cargos-solicitantes-component';

import { authGuard } from './auth/auth.guard';
import { UsuariosComponent } from './pages/usuarios-component/usuarios-component';
import { UsuarioPerfilComponent } from './pages/usuario-perfil-component/usuario-perfil-component';
import { TrabajadorComponent } from './pages/trabajador-component/trabajador-component';
import { LayoutComponent } from './component/layaout-component/layaout-component';
import { ClienteComponent } from './pages/cliente-component/cliente-component';
import { DashboardComponente } from './pages/dashboard-componente/dashboard-componente';
import { permisoGuard } from './auth/permiso.guard';
import { GestionPermisosComponent } from './pages/gestion-permisos.component.ts/gestion-permisos.component.ts';


export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard], // ✅ Protege la ACTIVACIÓN del LayoutComponent
    canActivateChild: [authGuard], // ✅ Protege las rutas hijas también
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: DashboardComponente,
        canActivate: [permisoGuard], // Permisos específicos
        data: { permisos: ['DASHBOARD_VIEW'] }
      },
      {
        path: 'ot',
        component: OtsComponent,
        canActivate: [permisoGuard],
        data: { permisos: ['OT_VIEW'] }
      },
      {
        path: 'site',
        component: SiteComponent,
        canActivate: [permisoGuard],
        data: { permisos: ['SITE_VIEW'] }
      },
      {
        path: 'gestion-jefatura-analista',
        component: GestionCargosSolicitantesComponent,
        canActivate: [permisoGuard],
        data: { permisos: ['CONFIGURACION_VIEW'] }
      },
      {
        path: 'orden-compra',
        component: OrdenCompraComponent,
        canActivate: [permisoGuard],
        data: { permisos: ['OC_VIEW'] }
      },
      {
        path: 'usuarios',
        component: UsuariosComponent,
        canActivate: [permisoGuard],
        data: { permisos: ['USUARIO_VIEW'] }
      },
      {
        path: 'perfil',
        component: UsuarioPerfilComponent,
        canActivate: [permisoGuard],
        data: { permisos: ['PERFIL_VIEW'] }
      },
      {
        path: 'trabajador',
        component: TrabajadorComponent,
        canActivate: [permisoGuard],
        data: { permisos: ['TRABAJADOR_VIEW'] }
      },
      {
        path: 'cliente',
        component: ClienteComponent,
        canActivate: [permisoGuard],
        data: { permisos: ['CLIENTE_VIEW'] }
      },
      // Ruta para manejar falta de permisos
      {
        path: 'no-autorizado',
        loadComponent: () => import('./pages/no-autorizado/no-autorizado/no-autorizado')
          .then(m => m.NoAutorizadoComponent)
      }
     , {
  path: 'gestion-permisos',
  component: GestionPermisosComponent,
  canActivate: [authGuard, permisoGuard],
  data: { permisos: ['PERMISO_ADMIN'] }
}
    ]
  },
  {
    path: '**',
    redirectTo: 'login',
    pathMatch: 'full'
  }
];

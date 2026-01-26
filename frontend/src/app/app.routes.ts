// src/app/app.routes.ts
import { Routes } from '@angular/router';

import { LoginComponent } from './pages/login-componente/login-componente';
import { DashboardComponent } from './pages/dashboard-componente/dashboard-componente';
import { OtsComponent } from './pages/ots-component/ots-component';
import { LayoutComponent } from './component/layaout-component/layaout-component';
import { SiteComponent } from './pages/site-component/site-component';
import { OrdenCompraComponent } from './pages/orden-compra-component/orden-compra-component';
import { GestionCargosSolicitantesComponent } from './pages/gestion-cargos-solicitantes-component/gestion-cargos-solicitantes-component';

import { authGuard } from './auth/auth.guard';
import { UsuariosComponent } from './pages/usuarios-component/usuarios-component';
import { UsuarioPerfilComponent } from './pages/usuario-perfil-component/usuario-perfil-component';
import { TrabajadorComponent } from './pages/trabajador-component/trabajador-component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    // Opcional: puedes agregar un título o data si lo necesitas
    // data: { title: 'Iniciar Sesión' }
  },

  // Ruta protegida principal (con layout)
  {
    path: '',
    component: LayoutComponent,
    canActivateChild: [authGuard],
    children: [
      // Redirección por defecto cuando entramos a la raíz protegida
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        // data: { title: 'Dashboard', breadcrumb: 'Inicio' }
      },
      {
        path: 'ot',
        component: OtsComponent,
        // data: { title: 'Órdenes de Trabajo' }
      },
      {
        path: 'site',
        component: SiteComponent,
        // data: { title: 'Sitios' }
      },
      {
        path: 'gestion-jefatura-analista',
        component: GestionCargosSolicitantesComponent,
        // data: { title: 'Gestión Jefatura / Analista' }
      },
      {
        path: 'orden-compra',
        component: OrdenCompraComponent,
        // data: { title: 'Órdenes de Compra' }
      },
      {
        path: 'usuarios',
        component: UsuariosComponent,
        // data: { title: 'Órdenes de Compra' }
      },
      {
        path: 'perfil',
        component: UsuarioPerfilComponent,
        // data: { title: 'Órdenes de Compra' }
      },
          {
        path: 'trabajador',
        component: TrabajadorComponent,
        // data: { title: 'Órdenes de Compra' }
      }
    ]
  },

  // Cualquier otra ruta no reconocida → login
  {
    path: '**',
    redirectTo: 'login',
    pathMatch: 'full'
  }
];

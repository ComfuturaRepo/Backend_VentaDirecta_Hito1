import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { Observable, of, forkJoin } from 'rxjs';
import { map, catchError, switchMap, take } from 'rxjs/operators';
import { AuthService } from '../service/auth.service';
import { PermisoService } from '../service/permiso.service';

export const permisoGuard: CanActivateFn = (route, state): Observable<boolean | UrlTree> => {
  const authService = inject(AuthService);
  const permisoService = inject(PermisoService);
  const router = inject(Router);

  console.log('🔑 permisoGuard: Verificando permisos para:', route.data?.['permisos']);

  // Primero verificar autenticación síncrona
  if (!authService.isAuthenticatedSync) {
    console.log('🔑 permisoGuard: No autenticado (sync)');
    return of(router.createUrlTree(['/login'], {
      queryParams: { returnUrl: state.url }
    }));
  }

  // Obtener el estado de autenticación
  return authService.authState$.pipe(
    take(1), // Solo el primer valor
    map(authState => {
      if (!authState.isAuthenticated || !authState.user) {
        console.log('🔑 permisoGuard: No autenticado o sin usuario');
        return router.createUrlTree(['/login'], {
          queryParams: { returnUrl: state.url }
        });
      }

      return authState.user;
    }),
    switchMap(user => {
      if (user instanceof UrlTree) {
        return of(user);
      }

      const permisosRequeridos = route.data?.['permisos'] as string[] || [];

      // Si no se requieren permisos específicos, permitir acceso
      if (permisosRequeridos.length === 0) {
        console.log('🔑 permisoGuard: No requiere permisos específicos - PERMITIDO');
        return of(true);
      }

      console.log('🔑 permisoGuard: Usuario:', user.username, 'Nivel:', user.nivel);

      // Verificar permisos remotos
      const verificaciones = permisosRequeridos.map(codigo =>
        permisoService.verificarPermisoRemoto(codigo, user.idUsuario)
      );

      return forkJoin(verificaciones).pipe(
        map(resultados => {
          const tieneAlgunPermiso = resultados.some(tiene => tiene);
          console.log('🔑 permisoGuard: Resultados:', resultados, 'Permitido:', tieneAlgunPermiso);

          if (tieneAlgunPermiso) {
            return true;
          }

          console.log('🔑 permisoGuard: Sin permisos suficientes');
          return router.createUrlTree(['/no-autorizado'], {
            queryParams: {
              permisosRequeridos: permisosRequeridos.join(','),
              returnUrl: state.url
            }
          });
        })
      );
    }),
    catchError((error) => {
      console.error('🔑 permisoGuard: Error:', error);
      return of(router.createUrlTree(['/no-autorizado'], {
        queryParams: { returnUrl: state.url }
      }));
    })
  );
};

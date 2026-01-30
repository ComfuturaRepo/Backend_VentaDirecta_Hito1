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

  // Obtener usuario (esperar si es necesario)
  return authService.authState$.pipe(
    take(1), // Solo el primer valor
    map(authState => {
      const user = authService.currentUser; // Usar el getter actualizado

      if (!user) {
        console.log('🔑 permisoGuard: Usuario no disponible');
        return router.createUrlTree(['/login'], {
          queryParams: { returnUrl: state.url }
        });
      }

      console.log('🔑 permisoGuard: Usuario encontrado:', user.username);

      const permisosRequeridos = route.data?.['permisos'] as string[] || [];

      // Si no se requieren permisos específicos
      if (permisosRequeridos.length === 0) {
        return true;
      }

      // Verificar permisos
      return permisoService.verificarPermisoRemoto(permisosRequeridos[0], user.idUsuario).pipe(
        map(tienePermiso => {
          if (tienePermiso) {
            return true;
          }
          return router.createUrlTree(['/no-autorizado'], {
            queryParams: {
              permisosRequeridos: permisosRequeridos.join(','),
              returnUrl: state.url
            }
          });
        })
      );
    }),
    switchMap(result => result instanceof Observable ? result : of(result)),
    catchError((error) => {
      console.error('🔑 permisoGuard: Error:', error);
      return of(router.createUrlTree(['/no-autorizado'], {
        queryParams: { returnUrl: state.url }
      }));
    })
  );
};

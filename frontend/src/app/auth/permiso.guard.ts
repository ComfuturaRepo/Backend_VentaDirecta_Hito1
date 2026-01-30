import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { Observable, of, forkJoin } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthService } from '../service/auth.service';
import { PermisoService } from '../service/permiso.service';

export const permisoGuard: CanActivateFn = (route, state): Observable<boolean | UrlTree> => {
  const authService = inject(AuthService);
  const permisoService = inject(PermisoService);
  const router = inject(Router);

  // Verificar autenticación primero
  if (!authService.isAuthenticatedSync) {
    return of(router.createUrlTree(['/login'], {
      queryParams: { returnUrl: state.url }
    }));
  }

  // Obtener permisos requeridos de la ruta
  const permisosRequeridos = route.data?.['permisos'] as string[] || [];

  // Si no se requieren permisos específicos, permitir acceso
  if (permisosRequeridos.length === 0) {
    return of(true);
  }

  // Verificar si tenemos los permisos en cache (frontend)
  const tienePermisosCache = permisoService.tieneAlgunPermiso(permisosRequeridos);
  if (tienePermisosCache) {
    return of(true);
  }

  // Si no está en cache, verificar en backend (más seguro)
  const usuario = authService.currentUser;
  if (!usuario) {
    return of(router.createUrlTree(['/login'], {
      queryParams: { returnUrl: state.url }
    }));
  }

  // Verificar cada permiso remotamente - PASANDO EL ID_USUARIO
  const verificaciones = permisosRequeridos.map(codigo =>
    permisoService.verificarPermisoRemoto(codigo, usuario.idUsuario)
  );

  // Si solo hay un permiso requerido
  if (verificaciones.length === 1) {
    return verificaciones[0].pipe(
      map(tienePermiso => {
        if (tienePermiso) {
          return true;
        }
        return router.createUrlTree(['/no-autorizado'], {
          queryParams: { faltaPermiso: permisosRequeridos[0], returnUrl: state.url }
        });
      }),
      catchError(() => {
        return of(router.createUrlTree(['/no-autorizado'], {
          queryParams: { returnUrl: state.url }
        }));
      })
    );
  }

  // Si hay múltiples permisos requeridos (condición OR - al menos uno)
  return forkJoin(verificaciones).pipe(
    map(resultados => resultados.some(tiene => tiene)),
    map(tieneAlgunPermiso => {
      if (tieneAlgunPermiso) {
        return true;
      }
      return router.createUrlTree(['/no-autorizado'], {
        queryParams: { permisosRequeridos: permisosRequeridos.join(','), returnUrl: state.url }
      });
    }),
    catchError(() => {
      return of(router.createUrlTree(['/no-autorizado'], {
        queryParams: { returnUrl: state.url }
      }));
    })
  );
};

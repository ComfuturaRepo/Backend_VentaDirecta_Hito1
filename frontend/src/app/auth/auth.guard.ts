import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, catchError, first } from 'rxjs/operators';
import { AuthService } from '../service/auth.service';

export const authGuard: CanActivateFn = (route, state): Observable<boolean | UrlTree> => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.authState$.pipe(
    // Nos quedamos solo con el primer valor emitido
    first(),

    map(auth => {
      // Si ya está autenticado → puede pasar
      if (auth.isAuthenticated) {
        return true;
      }

      // Si no → redirigimos a login guardando la url que quería visitar
      return router.createUrlTree(['/login'], {
        queryParams: { returnUrl: state.url }
      });
    }),

    // En caso de cualquier error → también vamos a login
    catchError(() => {
      return of(
        router.createUrlTree(['/login'], {
          queryParams: { returnUrl: state.url }
        })
      );
    })
  );
};

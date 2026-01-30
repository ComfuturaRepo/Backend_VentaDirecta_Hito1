import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, catchError, first } from 'rxjs/operators';
import { AuthService } from '../service/auth.service';

export const authGuard: CanActivateFn = (route, state): Observable<boolean | UrlTree> | boolean | UrlTree => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Verificación síncrona inmediata
  if (authService.isAuthenticatedSync) {
    console.log('🔒 authGuard: Usuario autenticado (síncrono)');
    return true;
  }

  // Si no está autenticado síncronamente, esperar al observable
  console.log('🔒 authGuard: Esperando autenticación asíncrona...');
  return authService.authState$.pipe(
    first(),
    map(auth => {
      console.log('🔒 authGuard: Estado recibido:', auth.isAuthenticated);
      if (auth.isAuthenticated) {
        return true;
      }
      console.log('🔒 authGuard: Redirigiendo a login');
      return router.createUrlTree(['/login'], {
        queryParams: { returnUrl: state.url }
      });
    }),
    catchError((error) => {
      console.error('🔒 authGuard: Error:', error);
      return of(
        router.createUrlTree(['/login'], {
          queryParams: { returnUrl: state.url }
        })
      );
    })
  );
};

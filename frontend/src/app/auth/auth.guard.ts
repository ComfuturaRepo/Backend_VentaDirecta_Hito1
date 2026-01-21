// src/app/core/guards/auth.guard.ts
import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthService } from '../service/auth.service';

export const authGuard: CanActivateFn = (route, state): Observable<boolean | UrlTree> => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // 1. Chequeo síncrono primero → evita el flicker y el race condition
  if (authService.isAuthenticatedSync) {
    return of(true);
  }

  // 2. Si no, esperamos el observable (por si hay refresh token en el futuro)
  return authService.authState$.pipe(
    map(auth => {
      if (auth.isAuthenticated) {
        return true;
      }

      // Usamos UrlTree → más seguro, evita side-effects dobles
      return router.createUrlTree(['/login'], {
        queryParams: { returnUrl: state.url }
      });
    }),
    catchError(() => of(
      router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } })
    ))
  );
};

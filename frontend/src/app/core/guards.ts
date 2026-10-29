import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
export const authGuard: CanActivateFn = (_route, state) => { const auth = inject(AuthService); return auth.authenticated() ? true : inject(Router).createUrlTree(['/login'], { queryParams: { returnUrl: state.url } }); };
export const guestGuard: CanActivateFn = () => inject(AuthService).authenticated() ? inject(Router).createUrlTree(['/']) : true;
export const permissionGuard: CanActivateFn = route => inject(AuthService).has(route.data['permission']) ? true : inject(Router).createUrlTree(['/acceso-denegado']);

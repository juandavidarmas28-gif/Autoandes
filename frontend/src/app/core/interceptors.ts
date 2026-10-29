import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const auth = inject(AuthService); const token = auth.token();
  if (token && request.url.startsWith(environment.apiUrl)) request = request.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  return next(request);
};
export const expiredSessionInterceptor: HttpInterceptorFn = (request, next) => {
  const auth = inject(AuthService); const router = inject(Router);
  return next(request).pipe(catchError(error => { if (error.status === 401 && !request.url.endsWith('/auth/login')) { auth.logout(); void router.navigate(['/login'], { queryParams: { returnUrl: router.url } }); } return throwError(() => error); }));
};

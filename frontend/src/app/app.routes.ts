import { Routes } from '@angular/router';
import { authGuard, guestGuard, permissionGuard } from './core/guards';
import { LayoutComponent } from './layout/layout.component';

export const routes: Routes = [
  { path: 'login', canActivate: [guestGuard], loadComponent: () => import('./features/login.component').then(m => m.LoginComponent) },
  { path: '', component: LayoutComponent, canActivate: [authGuard], children: [
    { path: '', pathMatch: 'full', redirectTo: 'ordenes' },
    { path: 'servicios/nuevo', canActivate: [permissionGuard], data: { permission: 'SERVICIO_ESCRIBIR' }, loadComponent: () => import('./features/servicios.component').then(m => m.ServiciosComponent) },
    { path: 'servicios/:id/editar', canActivate: [permissionGuard], data: { permission: 'SERVICIO_ESCRIBIR' }, loadComponent: () => import('./features/servicios.component').then(m => m.ServiciosComponent) },
    { path: 'servicios', canActivate: [permissionGuard], data: { permission: 'SERVICIO_LEER' }, loadComponent: () => import('./features/servicios.component').then(m => m.ServiciosComponent) },
    { path: 'vehiculos', canActivate: [permissionGuard], data: { permission: 'VEHICULO_LEER' }, loadComponent: () => import('./features/vehiculos.component').then(m => m.VehiculosComponent) },
    { path: 'ordenes/nueva', canActivate: [permissionGuard], data: { permission: 'ORDEN_REGISTRAR' }, loadComponent: () => import('./features/nueva-orden.component').then(m => m.NuevaOrdenComponent) },
    { path: 'ordenes', canActivate: [permissionGuard], data: { permission: 'ORDEN_LEER' }, loadComponent: () => import('./features/ordenes.component').then(m => m.OrdenesComponent) },
    { path: 'reportes', canActivate: [permissionGuard], data: { permission: 'REPORTE_VER' }, loadComponent: () => import('./features/reportes.component').then(m => m.ReportesComponent) },
    { path: 'acceso-denegado', loadComponent: () => import('./features/status-pages.component').then(m => m.AccesoDenegadoComponent) }
  ]},
  { path: '**', loadComponent: () => import('./features/status-pages.component').then(m => m.NoEncontradoComponent) }
];

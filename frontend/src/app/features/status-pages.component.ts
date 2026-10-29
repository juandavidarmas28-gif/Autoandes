import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
@Component({ standalone: true, imports: [RouterLink], template: '<section class="empty-page"><span class="empty-icon">403</span><p class="eyebrow">ACCESO RESTRINGIDO</p><h1>No tienes permiso para esta sección</h1><p class="muted">Tu sesión sigue activa. Si necesitas acceder, solicita el permiso a un administrador.</p><a class="button primary" routerLink="/">Volver al inicio</a></section>' })
export class AccesoDenegadoComponent {}
@Component({ standalone: true, imports: [RouterLink], template: '<section class="empty-page"><span class="empty-icon">404</span><p class="eyebrow">PÁGINA NO ENCONTRADA</p><h1>No encontramos esta ruta</h1><p class="muted">Verifica la dirección o vuelve al panel.</p><a class="button primary" routerLink="/">Ir al panel</a></section>' })
export class NoEncontradoComponent {}

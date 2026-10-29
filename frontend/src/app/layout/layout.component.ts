import { Component, computed, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from '../core/auth.service';

@Component({ selector: 'aa-layout', standalone: true, imports: [RouterOutlet, RouterLink, RouterLinkActive], template: `
<div class="shell"><aside class="sidebar"><a class="brand" routerLink="/"><span class="brand-mark">A</span><span>AutoAndes<small>TALLER & SERVICIOS</small></span></a><div class="side-label">MENÚ PRINCIPAL</div>
<nav>@for (item of items(); track item.path) { <a [routerLink]="item.path" routerLinkActive="active" [routerLinkActiveOptions]="{exact:true}"><span class="nav-icon">{{item.icon}}</span>{{item.label}}</a> }</nav>
<div class="side-foot"><span class="online-dot"></span>API conectada · sesión segura</div></aside>
<section class="workspace"><header class="topbar"><div><span class="crumb">AutoAndes</span><span class="crumb-sep">/</span><span class="crumb current">{{currentPage()}}</span></div><div class="profile"><div class="avatar">{{initials()}}</div><div><strong>{{auth.session()?.nombreCompleto}}</strong><small>{{auth.session()?.roles?.join(' · ')}}</small></div><button class="logout" (click)="logout()" title="Cerrar sesión">↪</button></div></header><main class="content"><router-outlet /></main></section></div>` })
export class LayoutComponent {
  readonly auth = inject(AuthService); private router = inject(Router);
  private readonly currentUrl = signal(this.router.url);
  readonly items = computed(() => [
    ...(this.auth.has('ORDEN_LEER') ? [{ path: '/ordenes', label: 'Órdenes de servicio', icon: '▤' }] : []),
    ...(this.auth.has('ORDEN_REGISTRAR') ? [{ path: '/ordenes/nueva', label: 'Nueva orden', icon: '+' }] : []),
    ...(this.auth.has('SERVICIO_LEER') ? [{ path: '/servicios', label: 'Servicios', icon: '⚙' }] : []),
    ...(this.auth.has('VEHICULO_LEER') ? [{ path: '/vehiculos', label: 'Vehículos', icon: '▰' }] : []),
    ...(this.auth.has('REPORTE_VER') ? [{ path: '/reportes', label: 'Reportes', icon: '▥' }] : [])
  ]);
  readonly currentPage = computed(() => this.items().find(item => this.currentUrl().startsWith(item.path))?.label ?? 'Panel');
  readonly initials = computed(() => (this.auth.session()?.nombreCompleto ?? 'AA').split(' ').slice(0, 2).map(word => word[0]).join('').toUpperCase());
  constructor() { this.router.events.pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd)).subscribe(event => this.currentUrl.set(event.urlAfterRedirects)); }
  logout(): void { this.auth.logout(); void this.router.navigate(['/login']); }
}

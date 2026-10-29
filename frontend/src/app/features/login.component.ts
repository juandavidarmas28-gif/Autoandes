import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../core/auth.service';
import { ErrorApi } from '../core/models';

@Component({ standalone: true, imports: [ReactiveFormsModule], template: `
<main class="login-page"><div class="login-card"><a class="brand login-brand"><span class="brand-mark">A</span><span>AutoAndes<small>TALLER & SERVICIOS</small></span></a><p class="eyebrow">PORTAL DE OPERACIONES</p><h1>Bienvenido</h1><p class="muted">Ingresa con tus credenciales de AutoAndes.</p>
<form [formGroup]="form" (ngSubmit)="submit()"><label>Usuario<input formControlName="username" autocomplete="username" placeholder="Tu usuario"></label>@if (form.controls.username.touched && form.controls.username.invalid) {<small class="field-error">Ingresa tu usuario.</small>}<label>Contraseña<input type="password" formControlName="password" autocomplete="current-password" placeholder="Tu contraseña"></label>@if(error){<div class="alert error">{{error}}</div>}<button class="button primary full" [disabled]="form.invalid || loading">{{loading ? 'Ingresando…' : 'Iniciar sesión'}} <span>→</span></button></form><small class="login-note">Acceso protegido · AutoAndes API</small></div></main>` })
export class LoginComponent {
  private fb = inject(FormBuilder); private auth = inject(AuthService); private router = inject(Router); private route = inject(ActivatedRoute); private cdr = inject(ChangeDetectorRef);
  form = this.fb.nonNullable.group({ username: ['', Validators.required], password: ['', Validators.required] }); loading = false; error = '';
  submit(): void { if (this.form.invalid) return; this.loading = true; this.error = ''; const { username, password } = this.form.getRawValue(); this.auth.login(username, password).subscribe({ next: () => { this.loading = false; this.cdr.markForCheck(); void this.router.navigateByUrl(this.route.snapshot.queryParamMap.get('returnUrl') || '/'); }, error: (e: ErrorApi) => { this.loading = false; this.error = e?.mensaje || 'No se pudo iniciar sesión.'; this.cdr.markForCheck(); } }); }
}

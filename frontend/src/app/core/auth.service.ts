import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { TokenSession } from './models';

const KEY = 'autoandes.session';
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly state = signal<TokenSession | null>(this.restore());
  readonly session = this.state.asReadonly();
  readonly authenticated = computed(() => !!this.state()?.token && Date.now() < this.expiry());
  readonly permisos = computed(() => this.state()?.permisos ?? []);
  private timer?: ReturnType<typeof setTimeout>;
  constructor(private http: HttpClient, private router: Router) { this.scheduleExpiry(); }
  login(username: string, password: string): Observable<TokenSession> {
    return this.http.post<TokenSession>(`${environment.apiUrl}/auth/login`, { username, password }).pipe(tap(session => this.setSession(session)));
  }
  token(): string | null { return this.state()?.token ?? null; }
  has(permission: string): boolean { return this.permisos().includes(permission); }
  logout(): void { this.state.set(null); sessionStorage.removeItem(KEY); if (this.timer) clearTimeout(this.timer); }
  private setSession(session: TokenSession): void { sessionStorage.setItem(KEY, JSON.stringify({ ...session, _savedAt: Date.now() })); this.state.set(session); this.scheduleExpiry(); }
  private restore(): TokenSession | null {
    try { const raw = sessionStorage.getItem(KEY); if (!raw) return null; const stored = JSON.parse(raw) as TokenSession & { _savedAt: number }; if (Date.now() >= stored._savedAt + stored.expiraEnSegundos * 1000) { sessionStorage.removeItem(KEY); return null; } return stored; }
    catch { sessionStorage.removeItem(KEY); return null; }
  }
  private expiry(): number { try { const raw = sessionStorage.getItem(KEY); return raw ? (JSON.parse(raw)._savedAt + JSON.parse(raw).expiraEnSegundos * 1000) : 0; } catch { return 0; } }
  private scheduleExpiry(): void { if (this.timer) clearTimeout(this.timer); const remaining = this.expiry() - Date.now(); if (remaining > 0) this.timer = setTimeout(() => { const returnUrl = this.router.url; this.logout(); void this.router.navigate(['/login'], { queryParams: { returnUrl } }); }, remaining); }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente, Orden, Servicio, Vehiculo } from './models';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly base = environment.apiUrl;
  constructor(private http: HttpClient) {}
  clientes(): Observable<Cliente[]> { return this.http.get<Cliente[]>(`${this.base}/clientes`); }
  servicios(): Observable<Servicio[]> { return this.http.get<Servicio[]>(`${this.base}/servicios`); }
  crearServicio(body: Partial<Servicio>): Observable<Servicio> { return this.http.post<Servicio>(`${this.base}/servicios`, body); }
  actualizarServicio(id: number, body: Partial<Servicio>): Observable<Servicio> { return this.http.put<Servicio>(`${this.base}/servicios/${id}`, body); }
  estadoServicio(id: number, activo: boolean): Observable<Servicio> { return this.http.patch<Servicio>(`${this.base}/servicios/${id}/estado`, { activo }); }
  vehiculos(clienteId?: number): Observable<Vehiculo[]> { let params = new HttpParams(); if (clienteId) params = params.set('clienteId', clienteId); return this.http.get<Vehiculo[]>(`${this.base}/vehiculos`, { params }); }
  crearVehiculo(body: Partial<Vehiculo>): Observable<Vehiculo> { return this.http.post<Vehiculo>(`${this.base}/vehiculos`, body); }
  actualizarVehiculo(id: number, body: Partial<Vehiculo>): Observable<Vehiculo> { return this.http.put<Vehiculo>(`${this.base}/vehiculos/${id}`, body); }
  borrarVehiculo(id: number): Observable<void> { return this.http.delete<void>(`${this.base}/vehiculos/${id}`); }
  ordenes(filtros: Record<string, string>): Observable<Orden[]> { let params = new HttpParams(); for (const [key, value] of Object.entries(filtros)) if (value) params = params.set(key, value); return this.http.get<Orden[]>(`${this.base}/ordenes`, { params }); }
  crearOrden(body: { vehiculoId: number; kilometraje: number; detalles: { servicioId: number; cantidad: number }[] }): Observable<Orden> { return this.http.post<Orden>(`${this.base}/ordenes`, body); }
  anularOrden(id: number, motivo: string): Observable<Orden> { return this.http.patch<Orden>(`${this.base}/ordenes/${id}/anular`, { motivo }); }
  reporte(desde: string, hasta: string): Observable<{ codigo: string; servicio: string; cantidadTotal: number; importeTotal: number }[]> { const params = new HttpParams().set('desde', desde).set('hasta', hasta); return this.http.get<{ codigo: string; servicio: string; cantidadTotal: number; importeTotal: number }[]>(`${this.base}/reportes/ingresos-por-servicio`, { params }); }
}

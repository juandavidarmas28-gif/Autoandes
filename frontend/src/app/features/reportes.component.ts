import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';
import { ErrorApi } from '../core/models';

@Component({standalone:true,imports:[FormsModule,DecimalPipe],template:`<div class="page-heading"><div><p class="eyebrow">ANÁLISIS DEL NEGOCIO</p><h1>Ingresos por servicio</h1><p class="muted">Consulta los ingresos registrados en un periodo. Las órdenes anuladas se excluyen.</p></div></div>@if(error){<div class="alert error">{{error}}</div>}<section class="panel"><div class="filters report-filters"><label>Desde<input type="date" [(ngModel)]="desde"/></label><label>Hasta<input type="date" [(ngModel)]="hasta"/></label><button class="button primary" [disabled]="loading||!desde||!hasta" (click)="load()">{{loading?'Consultando…':'Generar reporte'}}</button></div><div class="table-wrap"><table><thead><tr><th>CÓDIGO</th><th>SERVICIO</th><th>CANTIDAD TOTAL</th><th>INGRESO TOTAL</th></tr></thead><tbody>@for(r of rows;track r.codigo){<tr><td><span class="code-chip">{{r.codigo}}</span></td><td><strong>{{r.servicio}}</strong></td><td>{{r.cantidadTotal}}</td><td class="price">S/ {{r.importeTotal|number:'1.2-2'}}</td></tr>}@empty{<tr><td colspan="4" class="table-empty">{{loading?'Selecciona un rango para consultar.':'No hay ingresos en ese periodo.'}}</td></tr>}</tbody></table></div></section>`})
export class ReportesComponent{
private api=inject(ApiService);private cdr=inject(ChangeDetectorRef);desde='';hasta='';rows:{codigo:string;servicio:string;cantidadTotal:number;importeTotal:number}[]=[];loading=false;error='';
load():void{if(!this.desde||!this.hasta)return;this.loading=true;this.api.reporte(this.desde,this.hasta).subscribe({next:r=>{this.rows=r;this.loading=false;this.error='';this.cdr.markForCheck();},error:(e:ErrorApi)=>{this.loading=false;this.error=e?.error?.mensaje||e?.mensaje||'No se pudo generar el reporte.';this.cdr.markForCheck();}});}
}

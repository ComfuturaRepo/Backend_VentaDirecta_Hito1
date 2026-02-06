import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrdenCompraService } from '../../../service/orden-compra.service';
import { Aprobacion } from '../../../model/aprobacion.model';
import { OrdenCompraAprobacionService } from '../../../service/aprobacion.service';
import { HttpClientModule } from '@angular/common/http';
import { PermisoDirective } from '../../../directive/permiso.directive';

@Component({
  selector: 'app-orden-compra-aprobaciones',
  standalone: true,

  imports: [CommonModule, FormsModule,    HttpClientModule ,PermisoDirective
],
  templateUrl: './orden-compra-aprobaciones-component.html'
})
export class OrdenCompraAprobacionesComponent implements OnInit {

  @Input() idOc!: number;

  aprobaciones: Aprobacion[] = [];
  loading = true;

  // ✅ empresa elegida en el front
  idEmpresaSeleccionada: number = 1;

  constructor(
    private aprobacionService: OrdenCompraAprobacionService,
    private ordenCompraService: OrdenCompraService   // 👈 ESTE FALTABA

  ) {}

  ngOnInit(): void {
    this.cargarAprobaciones();
  }

  // ============================
  // 📄 HISTORIAL
  // ============================
  cargarAprobaciones(): void {
    this.loading = true;
    this.aprobacionService.getHistorial(this.idOc).subscribe({
      next: data => {
        this.aprobaciones = data;
        this.loading = false;
      },
      error: err => {
        console.error(err);
        this.loading = false;
      }
    });
  }

  // ============================
  // ✅ ACCIONES
  // ============================
  aprobarNivel(aprob: Aprobacion): void {
    if (!confirm(`¿Aprobar nivel ${aprob.nivel}?`)) return;

    this.aprobacionService
      .aprobar(this.idOc, aprob.nivel, 'APROBADO')
      .subscribe(() => this.cargarAprobaciones());
  }

  rechazarNivel(aprob: Aprobacion): void {
    if (!confirm(`¿Rechazar nivel ${aprob.nivel}?`)) return;

    this.aprobacionService
      .aprobar(this.idOc, aprob.nivel, 'RECHAZADO')
      .subscribe(() => this.cargarAprobaciones());
  }

  // ============================
  // 🎨 UI
  // ============================
  getColor(estado: string): string {
    switch (estado) {
      case 'APROBADO': return '#4caf50';
      case 'PENDIENTE': return '#ff9800';
      case 'BLOQUEADO': return '#9e9e9e';
      case 'RECHAZADO': return '#f44336';
      default: return '#2196f3';
    }
  }

  // ============================
  // 🔎 ÚLTIMO NIVEL
  // ============================
  get aprobacionFinalizada(): boolean {
    return this.aprobaciones.some(
      a => a.nivel === 3 && a.estado === 'APROBADO'
    );
  }

  // ============================
  // ⬇ DESCARGAR HTML
  // ============================
  descargarHtml(): void {
  this.ordenCompraService.descargarHtml(
    this.idOc,
    this.idEmpresaSeleccionada
  );
}
getPermisoNivel(nivel: number): string {
  return `APROBADOR_${nivel}`;
}

}

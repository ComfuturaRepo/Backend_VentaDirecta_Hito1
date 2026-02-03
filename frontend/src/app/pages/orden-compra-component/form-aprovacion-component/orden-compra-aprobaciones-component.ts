import { Component, Input, OnInit } from '@angular/core';
import { OrdenCompraAprobacionService } from '../../../service/aprobacion.service';
import { Aprobacion } from '../../../model/aprobacion.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-orden-compra-aprobaciones',
  templateUrl: './orden-compra-aprobaciones-component.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./orden-compra-aprobaciones-component.css']
})
export class OrdenCompraAprobacionesComponent implements OnInit {

  @Input() idOc!: number;

  aprobaciones: Aprobacion[] = [];
  loading: boolean = true;

  // 🔹 Niveles que el usuario puede aprobar (simulación)
  nivelesPermitidos: number[] = [];

  constructor(private aprobacionService: OrdenCompraAprobacionService) { }

  ngOnInit(): void {
    this.loadAprobaciones();
    this.obtenerPermisosUsuario();
  }

  loadAprobaciones(): void {
    this.loading = true;
    this.aprobacionService.getHistorial(this.idOc).subscribe({
      next: (data: Aprobacion[]) => {
        this.aprobaciones = data;
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Error cargando aprobaciones', err);
        this.loading = false;
      }
    });
  }

  getColor(estado: string): string {
    switch (estado) {
      case 'APROBADO': return '#4caf50';
      case 'PENDIENTE': return '#ff9800';
      case 'BLOQUEADO': return '#9e9e9e';
      case 'RECHAZADO': return '#f44336';
      default: return '#2196f3';
    }
  }

  aprobarNivel(aprob: Aprobacion) {
    if (aprob.estado !== 'PENDIENTE') return;

    if (!this.usuarioPuedeAprobar(aprob)) {
      alert('No tienes permiso para aprobar este nivel');
      return;
    }

    const confirmar = confirm(`¿Deseas aprobar el nivel ${aprob.nivel}?`);
    if (!confirmar) return;

    const payload = {
      idOc: this.idOc,
      nivel: aprob.nivel,
      estado: 'APROBADO',
      aprobadoPor: 'Nombre del usuario logueado'
    };

    this.aprobacionService.aprobar(payload).subscribe({
      next: () => {
        alert(`Nivel ${aprob.nivel} aprobado`);
        this.loadAprobaciones();
      },
      error: (err) => {
        console.error(err);
        alert('Error al aprobar');
      }
    });
  }

  rechazarNivel(aprob: Aprobacion) {
    if (aprob.estado !== 'PENDIENTE') return;

    if (!this.usuarioPuedeAprobar(aprob)) {
      alert('No tienes permiso para rechazar este nivel');
      return;
    }

    const confirmar = confirm(`¿Deseas rechazar el nivel ${aprob.nivel}?`);
    if (!confirmar) return;

    const payload = {
      idOc: this.idOc,
      nivel: aprob.nivel,
      estado: 'RECHAZADO',
      aprobadoPor: 'Nombre del usuario logueado'
    };

    this.aprobacionService.aprobar(payload).subscribe({
      next: () => {
        alert(`Nivel ${aprob.nivel} rechazado`);
        this.loadAprobaciones();
      },
      error: (err) => {
        console.error(err);
        alert('Error al rechazar');
      }
    });
  }

  // 🔹 Control de permisos: si el usuario puede aprobar/rechazar este nivel
  usuarioPuedeAprobar(aprob: Aprobacion): boolean {
    return this.nivelesPermitidos.includes(aprob.nivel);
  }

  // 🔹 Cargar los niveles que el usuario puede aprobar
  obtenerPermisosUsuario(): void {
    // Por ahora simulación: usuario puede aprobar niveles 1 y 2
    this.nivelesPermitidos = [1, 2];
    // Aquí luego llamas a tu backend o a tu sesión para traer permisos reales
  }
}

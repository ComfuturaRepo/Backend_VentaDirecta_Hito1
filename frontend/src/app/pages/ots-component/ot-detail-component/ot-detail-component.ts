// src/app/pages/ot-detail/ot-detail.component.ts
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { OtService } from '../../../service/ot.service';
import { OtResponse } from '../../../model/ots';

@Component({
  selector: 'app-ot-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-5">
      <div class="card shadow-lg border-0 rounded-4">
        <div class="card-header bg-gradient-primary text-white py-4">
          <h4 class="mb-0">Detalle de Orden de Trabajo #{{ ot?.ot }}</h4>
        </div>
        <div class="card-body p-5">
          <div *ngIf="loading" class="text-center py-5">
            <div class="spinner-border text-primary" style="width: 4rem; height: 4rem;"></div>
          </div>

          <div *ngIf="!loading && ot" class="row g-4">
            <div class="col-md-6">
              <h5 class="fw-bold text-primary">Información principal</h5>
              <p><strong>Descripción:</strong> {{ ot.descripcion || '—' }}</p>
              <p><strong>Fecha Apertura:</strong> {{ ot.fechaApertura | date:'dd/MM/yyyy' }}</p>
              <p><strong>Fecha Creación:</strong> {{ ot.fechaCreacion | date:'dd/MM/yyyy HH:mm' }}</p>
              <p><strong>Estado:</strong>
                <span class="badge fs-6" [ngClass]="ot.activo ? 'bg-success' : 'bg-danger'">
                  {{ ot.activo ? 'Activa' : 'Inactiva' }}
                </span>
              </p>
            </div>
            <div class="col-md-6">
              <h5 class="fw-bold text-primary">Asignados</h5>
              <!-- Aquí puedes mostrar jefaturas, analistas, etc. si el backend los devuelve -->
              <p><strong>Días Asignados:</strong> {{ ot.diasAsignados ?? '—' }}</p>
            </div>
          </div>

          <div *ngIf="!loading && !ot" class="alert alert-warning text-center">
            No se encontró la orden de trabajo solicitada.
          </div>

          <div class="mt-5 text-center">
            <button class="btn btn-secondary px-5" (click)="volver()">Volver al listado</button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .bg-gradient-primary { background: linear-gradient(135deg, #0d6efd, #0b5ed7); }
  `]
})
export class OtDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private otService = inject(OtService);

  ot: OtResponse | null = null;
  loading = true;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id || isNaN(id)) {
      this.loading = false;
      Swal.fire('Error', 'ID de OT inválido', 'error');
      return;
    }

    this.otService.obtenerPorId(id).subscribe({
      next: (detalle) => {
        this.ot = detalle;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'No se pudo cargar',
          text: err.message || 'La OT no existe o hubo un error en el servidor',
          confirmButtonColor: '#dc3545'
        });
      }
    });
  }

  volver(): void {
    this.router.navigate(['/ot']);
  }
}

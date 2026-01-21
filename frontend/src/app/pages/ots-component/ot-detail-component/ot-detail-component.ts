import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';

import { OtFullDetailResponse } from '../../../model/ots'; // ← Usamos el tipo completo
import { OtService } from '../../../service/ot.service';

@Component({
  selector: 'app-ot-detail',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './ot-detail-component.html',
  styleUrl: './ot-detail-component.css',
})// app-ot-detail.component.ts

export class OtDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private otService = inject(OtService);

  ot: OtFullDetailResponse | null = null;
  loading = true;
  errorMessage = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id || isNaN(id)) {
      this.loading = false;
      this.mostrarError('ID de OT inválido');
      return;
    }

    this.otService.getOtDetalleCompleto(id).subscribe({   // ← cambio aquí
      next: (detalle) => {
        this.ot = detalle;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.message || 'No se pudo cargar el detalle de la OT';
        this.mostrarError(this.errorMessage);
      }
    });
  }

  private mostrarError(mensaje: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: mensaje,
      confirmButtonColor: '#dc3545',
    }).then(() => this.volver());
  }

  volver(): void {
    this.router.navigate(['/ot']);  // o ['/ots'] según tu ruta
  }
}

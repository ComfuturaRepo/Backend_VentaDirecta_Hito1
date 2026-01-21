

  import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { OtFullResponse, OtResponse } from '../../../model/ots';
import { OtService } from '../../../service/ot.service';

@Component({
  selector: 'app-ot-detail',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './ot-detail-component.html',
  styleUrl: './ot-detail-component.css',
})
export class OtDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private otService = inject(OtService);

  ot: OtFullResponse | null = null;
  loading = true;
  errorMessage = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id || isNaN(id)) {
      this.loading = false;
      this.mostrarError('ID inválido');
      return;
    }

    this.otService.obtenerOtParaEdicion(id).subscribe({   // ← cambiar a /full
      next: (detalle) => {
        this.ot = detalle;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.message || 'Error al cargar la OT';
        this.mostrarError(this.errorMessage);
      }
    });
  }

  private mostrarError(mensaje: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Error al cargar',
      text: mensaje,
      confirmButtonColor: '#dc3545',
      confirmButtonText: 'Aceptar'
    });
  }

  volver(): void {
    this.router.navigate(['/ot']); // o '/ots/list' según tu ruta real
  }
}

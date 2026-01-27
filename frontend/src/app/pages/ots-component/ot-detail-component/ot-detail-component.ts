import { Component, Input, OnInit, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';

import { OtService } from '../../../service/ot.service';
import { OtDetailResponse } from '../../../model/ots';

@Component({
  selector: 'app-ot-detail',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './ot-detail-component.html',
  styleUrls: ['./ot-detail-component.css']
})
export class OtDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private otService = inject(OtService);

  @Input() otId?: number;
  @Input() otDetail?: OtDetailResponse;
  @Input() onClose: () => void = () => {
    // Default close behavior
    const modalElement = document.querySelector('.modal.show');
    if (modalElement) {
      (modalElement as HTMLElement).click();
    } else {
      this.router.navigate(['/ot']);
    }
  };

  @Output() closed = new EventEmitter<void>();

  ot: OtDetailResponse | null = null;
  loading = true;
  errorMessage = '';

  estados: string[] = [
    'ASIGNACION',
    'PRESUPUESTO ENVIADO',
    'CREACION DE OC',
    'EN_EJECUCION',
    'EN_LIQUIDACION',
    'EN_FACTURACION',
    'FINALIZADO',
    'CANCELADA'
  ];

  ngOnInit(): void {
    if (this.otDetail) {
      this.ot = this.otDetail;
      this.loading = false;
      return;
    }

    let id: number;
    if (this.otId !== undefined) {
      id = this.otId;
    } else {
      const idFromRoute = Number(this.route.snapshot.paramMap.get('id'));
      if (!idFromRoute || isNaN(idFromRoute)) {
        this.mostrarError('ID de OT inválido');
        return;
      }
      id = idFromRoute;
    }

    this.loadOt(id);
  }

  private loadOt(id: number): void {
    this.loading = true;
    this.otService.getOtById(id).subscribe({
      next: (detalle) => {
        this.ot = detalle;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.message || 'No se pudo cargar el detalle de la OT';
        this.mostrarError(this.errorMessage);
        this.loading = false;
      }
    });
  }

  get currentEstadoIndex(): number {
    if (!this.ot?.estadoOt) return -1;
    const idx = this.estados.indexOf(this.ot.estadoOt);
    return idx >= 0 ? idx : -1;
  }

  getIconoEstado(index: number): string {
    const icons = [
      'bi-person-plus',
      'bi-envelope-paper',
      'bi-cart-check',
      'bi-gear-wide-connected',
      'bi-calculator',
      'bi-receipt-cutoff',
      'bi-check2-circle',
      'bi-x-octagon'
    ];
    return icons[index] || 'bi-question-circle';
  }

  getEstadoIcon(estado?: string | null): string {
    if (!estado) return 'bi-question-circle';

    const estadoUpper = estado.toUpperCase();
    if (estadoUpper.includes('FINALIZADO')) return 'bi-check2-circle';
    if (estadoUpper.includes('CANCELADA')) return 'bi-x-octagon';
    if (estadoUpper.includes('EJECUCION')) return 'bi-play-circle';
    if (estadoUpper.includes('LIQUIDACION')) return 'bi-calculator';
    if (estadoUpper.includes('FACTURACION')) return 'bi-receipt';
    if (estadoUpper.includes('PRESUPUESTO')) return 'bi-envelope-paper';
    if (estadoUpper.includes('ASIGNACION')) return 'bi-person-plus';
    if (estadoUpper.includes('CREACION')) return 'bi-cart-check';

    return 'bi-clock';
  }

  getEstadoBadgeClass(estado?: string | null): string {
    if (!estado) return 'bg-secondary';

    const estadoUpper = estado.toUpperCase();
    if (estadoUpper.includes('FINALIZADO')) return 'bg-success';
    if (estadoUpper.includes('CANCELADA')) return 'bg-danger';
    if (estadoUpper.includes('EJECUCION') || estadoUpper.includes('LIQUIDACION') ||
        estadoUpper.includes('FACTURACION') || estadoUpper.includes('CREACION')) {
      return 'bg-warning';
    }
    if (estadoUpper.includes('PRESUPUESTO') || estadoUpper.includes('ASIGNACION')) {
      return 'bg-info';
    }
    return 'bg-primary';
  }

  get progressPercentage(): number {
    if (this.currentEstadoIndex < 0) return 0;
    return Math.round(((this.currentEstadoIndex + 1) / this.estados.length) * 100);
  }

  formatDate(dateString?: string | null): string {
    if (!dateString) return '—';
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  }

  formatDateTime(dateString?: string | null): string {
    if (!dateString) return '—';
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  private mostrarError(mensaje: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: mensaje,
      confirmButtonColor: '#dc3545',
      customClass: {
        container: 'swal2-container-modal'
      }
    }).then(() => this.onClose());
  }

  closeModal(): void {
    this.onClose();
    this.closed.emit();
  }

  editarOt(): void {
    if (this.ot?.idOts) {
      this.onClose();

      setTimeout(() => {
        if (this.ot?.idOts) {
          this.router.navigate(['/ot/edit', this.ot.idOts]);
        }
      }, 300);
    }
  }

  // Método alias para compatibilidad
  volver(): void {
    this.onClose();
  }
}

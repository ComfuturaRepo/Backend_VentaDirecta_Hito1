// src/app/pages/ots-component/ots-component.ts
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

import { OtService } from '../../service/ot.service';
import { OtResponse, Page } from '../../model/ots';

@Component({
  selector: 'app-ots-component',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './ots-component.html',
  styleUrl: './ots-component.css'
})
export class OtsComponent implements OnInit {
  private otService = inject(OtService);
  private router = inject(Router);

  displayedColumns: string[] = ['ot', 'descripcion', 'fechaCreacion', 'activo', 'acciones'];

  otsPage: Page<OtResponse> | null = null;
  dataSource: OtResponse[] = [];

  pageSize = 10;
  pageIndex = 0;
  totalElements = 0;
  totalPages = 0;

  // null = todas, true = activas, false = inactivas
  activoFilter: boolean | null = true;

  loading = false;
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.loadOts();
  }

  loadOts(page: number = this.pageIndex, size: number = this.pageSize): void {
    this.loading = true;
    this.errorMessage = null;

    const filterValue: boolean = this.activoFilter !== null ? this.activoFilter : true;

    this.otService.listarOts(filterValue, page, size).subscribe({
      next: (pageData) => {
        this.otsPage = pageData;
        this.dataSource = pageData.content || [];
        this.totalElements = pageData.totalElements || 0;
        this.pageIndex = pageData.number || 0;
        this.pageSize = pageData.size || this.pageSize;
        this.totalPages = Math.ceil(this.totalElements / this.pageSize) || 1;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.message || 'No se pudieron cargar las órdenes de trabajo';
        this.loading = false;

      }
    });
  }

  // ────────────────────────────────────────────────
  // Paginación
  // ────────────────────────────────────────────────

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages || page === this.pageIndex) return;
    this.pageIndex = page;
    this.loadOts();
  }

  goToFirst(): void { this.goToPage(0); }
  goToLast(): void { this.goToPage(this.totalPages - 1); }
  goToPrevious(): void { if (this.pageIndex > 0) this.goToPage(this.pageIndex - 1); }
  goToNext(): void { if (this.pageIndex < this.totalPages - 1) this.goToPage(this.pageIndex + 1); }

  changePageSize(newSize: number): void {
    if (newSize === this.pageSize) return;
    this.pageSize = newSize;
    this.pageIndex = 0;
    this.loadOts();
  }

  get visiblePages(): number[] {
    const pages: number[] = [];
    const range = 2;
    let start = Math.max(0, this.pageIndex - range);
    let end = Math.min(this.totalPages - 1, this.pageIndex + range);

    if (end - start < range * 2) {
      if (this.pageIndex <= range) end = Math.min(this.totalPages - 1, range * 2);
      else start = Math.max(0, this.totalPages - 1 - range * 2);
    }

    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  }

  // ────────────────────────────────────────────────
  // Acciones principales
  // ────────────────────────────────────────────────

  onFilterChange(): void {
    this.pageIndex = 0;
    this.loadOts();
  }

  goToCreate(): void {
    this.router.navigate(['/ot/nuevo']);
  }

  // Ver detalle por ID (usa GET /api/ots/{id})
  viewDetail(ot: OtResponse): void {
    this.otService.obtenerPorId(ot.idOts).subscribe({
      next: (detalle) => {
        // Navegamos a la ruta de detalle (puedes cambiar a modal si prefieres)
        this.router.navigate(['/ot', ot.idOts]);
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'No se pudo cargar el detalle',
          text: err.message || 'Error al obtener la OT',
          confirmButtonColor: '#dc3545'
        });
      }
    });
  }

  // Toggle Activar / Desactivar (usa POST /api/ots/{id}/toggle)
  toggleEstado(ot: OtResponse): void {
    const nuevoEstado = ot.activo ? 'Inactiva' : 'Activa';
    const accion = ot.activo ? 'desactivar' : 'activar';

    Swal.fire({
      title: `¿${accion.toUpperCase()} esta OT?`,
      html: `La orden <strong>#${ot.ot}</strong> pasará a estado <strong>${nuevoEstado}</strong>`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: ot.activo ? '#dc3545' : '#28a745',
      cancelButtonColor: '#6c757d',
      confirmButtonText: `Sí, ${accion}`,
      cancelButtonText: 'Cancelar',
      reverseButtons: true
    }).then((result) => {
      if (!result.isConfirmed) return;

      this.otService.toggleEstado(ot.idOts).subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: '¡Éxito!',
            text: `La OT #${ot.ot} ahora está ${nuevoEstado}`,
            timer: 2200,
            showConfirmButton: false,
            position: 'top-end',
            toast: true
          });

          // Recargamos la lista manteniendo página y filtro actual
          this.loadOts();
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Error al cambiar estado',
            text: err.message || 'No se pudo actualizar el estado',
            confirmButtonColor: '#dc3545'
          });
        }
      });
    });
  }
}

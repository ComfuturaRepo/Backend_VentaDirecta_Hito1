// src/app/pages/ots-component/ots-component.ts
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

import { OtResponse, Page } from '../../model/ots';        // Ajusta la ruta según tu estructura
import { OtService } from '../../service/ot.service';

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

  // Columnas de la tabla
  displayedColumns: string[] = ['ot', 'descripcion', 'fechaCreacion', 'activo', 'acciones'];

  // Datos de la tabla y paginación
  otsPage: Page<OtResponse> | null = null;
  dataSource: OtResponse[] = [];

  pageSize = 10;
  pageIndex = 0;
  totalElements = 0;
  totalPages = 0;

  // Filtros
  activoFilter: 'todas' | 'activas' | 'inactivas' = 'activas'; // valor por defecto
  otFilter: number | null = null; // filtro por número OT exacto
  sortField = 'idOts';            // campo por defecto
  sortDirection = 'desc';         // asc o desc

  loading = false;
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.loadOts();
  }

  loadOts(page: number = this.pageIndex): void {
  this.loading = true;
  this.errorMessage = null;

  let activoParam: boolean | null = null;
  if (this.activoFilter === 'activas') activoParam = true;
  else if (this.activoFilter === 'inactivas') activoParam = false;

  const sort = `${this.sortField},${this.sortDirection}`;

  this.otService.listarOts(
    activoParam,          // solo este parámetro de filtro
    page,
    this.pageSize,
    sort
  ).subscribe({
    next: (pageData) => {
      this.otsPage = pageData;
      this.dataSource = pageData.content || [];
      this.totalElements = pageData.totalElements || 0;
      this.pageIndex = pageData.number || 0;
      this.pageSize = pageData.size || this.pageSize;
      this.totalPages = pageData.totalPages || 1;
      this.loading = false;
    },
    error: (err) => {
      this.errorMessage = err.message || 'No se pudieron cargar las órdenes de trabajo';
      this.loading = false;
    }
  });
}

  // ────────────────────────────────────────────────
  // Filtros y búsqueda
  // ────────────────────────────────────────────────

  onFilterChange(): void {
    this.pageIndex = 0; // resetear paginación al cambiar filtro
    this.loadOts();
  }

  onOtFilterChange(value: string): void {
    this.otFilter = value.trim() ? parseInt(value, 10) : null;
    this.onFilterChange();
  }

  clearOtFilter(): void {
    this.otFilter = null;
    this.onFilterChange();
  }

  // ────────────────────────────────────────────────
  // Paginación
  // ────────────────────────────────────────────────

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages || page === this.pageIndex) return;
    this.pageIndex = page;
    this.loadOts(page);
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

    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    return pages;
  }

  // ────────────────────────────────────────────────
  // Acciones principales
  // ────────────────────────────────────────────────

  goToCreate(): void {
    this.router.navigate(['/ot/nuevo']);
  }

 goToEdit(ot: OtResponse): void {
  this.router.navigate(['/ot/editar', ot.idOts]);
}

viewDetail(ot: OtResponse): void {
  this.router.navigate(['/ot', ot.idOts]);           // → /ot/123
}
  toggleEstado(ot: OtResponse): void {
    const nuevoEstado = ot.activo ? 'inactiva' : 'activa';
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
            toast: true,
            position: 'top-end'
          });
          this.loadOts(); // recargar manteniendo filtros y página
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: err.message || 'No se pudo cambiar el estado',
            confirmButtonColor: '#dc3545'
          });
        }
      });
    });
  }
}

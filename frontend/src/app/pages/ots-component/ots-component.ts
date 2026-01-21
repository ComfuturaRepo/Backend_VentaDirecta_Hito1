// src/app/ots/ots-component.ts
import { Component, OnInit, inject } from '@angular/core';
import { MatChipsModule } from '@angular/material/chips';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';

import { Router } from '@angular/router'; // si usas rutas para el create
import { OtService } from '../../service/ot.service';
import { OtResponse, Page } from '../../model/ots';
// O si usas un diálogo/modal, puedes importar MatDialog después

@Component({
  selector: 'app-ots-component',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    FormsModule,
    MatChipsModule
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

  activoFilter: boolean = true; // por defecto muestra activas
  loading = false;
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.loadOts();
  }

  loadOts(page: number = this.pageIndex, size: number = this.pageSize): void {
    this.loading = true;
    this.errorMessage = null;

    this.otService.listarOts(this.activoFilter, page, size).subscribe({
      next: (pageData) => {
        this.otsPage = pageData;
        this.dataSource = pageData.content;
        this.totalElements = pageData.totalElements;
        this.pageIndex = pageData.number;
        this.pageSize = pageData.size;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.message || 'No se pudieron cargar las órdenes de trabajo';
        this.loading = false;
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadOts(this.pageIndex, this.pageSize);
  }

  onFilterChange(): void {
    this.pageIndex = 0; // resetear paginación al cambiar filtro
    this.loadOts(0, this.pageSize);
  }

  goToCreate(): void {
    this.router.navigate(['/ots/nuevo']);
  }

  // Opcional: ver detalle (si tienes un endpoint GET por id)
  viewDetail(ot: OtResponse): void {
    this.router.navigate(['/ots', ot.idOts]);
    // o abrir modal con detalle
  }
}

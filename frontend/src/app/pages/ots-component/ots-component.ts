// ots.component.ts
import { Component, OnInit, TemplateRef, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbModal, NgbModalRef, NgbDropdownModule, NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';
import { FormOtsComponent } from './form-ots-component/form-ots-component';
import { OtDetailComponent } from './ot-detail-component/ot-detail-component';
import { FileSizePipe } from '../../pipe/file-size.pipe';
import { ExcelService } from '../../service/excel.service';
import { OtService } from '../../service/ot.service';
import { OtListDto, Page } from '../../model/ots';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-ots',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NgbDropdownModule,
    NgbDatepickerModule,
    FileSizePipe
  ],
  templateUrl: './ots-component.html',
  styleUrls: ['./ots-component.css']
})
export class OtsComponent implements OnInit {
  private otService = inject(OtService);
  private excelService = inject(ExcelService);
  private modalService = inject(NgbModal);

  @ViewChild('importModal') importModal!: TemplateRef<any>;
  @ViewChild('exportModal') exportModal!: TemplateRef<any>;

  // Datos principales
  ots: OtListDto[] = [];
  page: Page<OtListDto> | null = null;
  loading = false;
  errorMessage: string | null = null;

  // Paginación
  pageSize = 10;
  currentPage = 0;
  totalElements = 0;
  totalPages = 0;

  // Filtros
  searchText = '';
  estadoFilter = '';
  dateRange = {
    desde: '',
    hasta: ''
  };

  // Selección múltiple
  selectedOts = new Set<number>();
  selectedCount = 0;
  selectAllChecked = false;

  // Importación
  importFile: File | null = null;
  importStep = 1;
  importMode: 'normal' | 'masivo' = 'normal';
  importing = false;
  importResult: any = null;

  // Exportación avanzada
  exportFiltroActivo = false;
  exportFiltroText = '';
  exportFechaDesde: Date | null = null;
  exportFechaHasta: Date | null = null;

  // Modal references
  private modalRefs: NgbModalRef[] = [];

  ngOnInit(): void {
    this.loadOts();
  }
// En el OtsComponent, agrega estos métodos:

// ==================== EXPORTACIÓN SIMPLE ====================

/**
 * Exporta las OTs seleccionadas (para el dropdown)
 */
exportSelectedOts(): void {
  if (this.selectedCount === 0) {
    Swal.fire({
      icon: 'warning',
      title: 'Sin selección',
      text: 'Por favor selecciona al menos una OT para exportar',
      confirmButtonColor: '#0d6efd'
    });
    return;
  }

  const otIds = Array.from(this.selectedOts);

  Swal.fire({
    title: 'Exportar seleccionadas',
    html: `¿Exportar <strong>${this.selectedCount} OTs</strong> a Excel?`,
    icon: 'question',
    showCancelButton: true,
    confirmButtonColor: '#198754',
    cancelButtonColor: '#6c757d',
    confirmButtonText: 'Exportar',
    cancelButtonText: 'Cancelar'
  }).then((result) => {
    if (result.isConfirmed) {
      this.excelService.exportOts(otIds).subscribe({
        next: (blob: Blob) => {
          const filename = `ots_seleccionadas_${new Date().getTime()}.xlsx`;
          this.excelService.downloadExcel(blob, filename);

          Swal.fire({
            icon: 'success',
            title: '¡Exportado!',
            text: 'Archivo Excel generado correctamente',
            timer: 2000,
            showConfirmButton: false
          });
        },
        error: (err) => {
          Swal.fire('Error', 'No se pudo generar el archivo Excel', 'error');
        }
      });
    }
  });
}

/**
 * Exporta todas las OTs (para el dropdown)
 */
exportAllOts(): void {
  Swal.fire({
    title: 'Exportar todas las OTs',
    text: `¿Exportar las ${this.totalElements} órdenes de trabajo a Excel?`,
    icon: 'question',
    showCancelButton: true,
    confirmButtonColor: '#0d6efd',
    cancelButtonColor: '#6c757d',
    confirmButtonText: 'Exportar todo',
    cancelButtonText: 'Cancelar'
  }).then((result) => {
    if (result.isConfirmed) {
      this.excelService.exportAllOts().subscribe({
        next: (blob: Blob) => {
          const filename = `todas_ots_${new Date().getTime()}.xlsx`;
          this.excelService.downloadExcel(blob, filename);

          Swal.fire({
            icon: 'success',
            title: '¡Exportado!',
            text: 'Todas las OTs exportadas correctamente',
            timer: 2000,
            showConfirmButton: false
          });
        },
        error: (err) => {
          Swal.fire('Error', 'No se pudo generar el archivo Excel', 'error');
        }
      });
    }
  });
}

/**
 * Exporta con filtros avanzados
 */
exportFilteredOts(): void {
  if (!this.exportFiltroText && !this.exportFechaDesde && !this.exportFechaHasta) {
    this.exportAllOts();
    return;
  }

  this.excelService.exportFilteredOts(
    this.exportFiltroText || undefined,
    this.exportFechaDesde || undefined,
    this.exportFechaHasta || undefined
  ).subscribe({
    next: (blob: Blob) => {
      const filename = `ots_filtradas_${new Date().getTime()}.xlsx`;
      this.excelService.downloadExcel(blob, filename);

      Swal.fire({
        icon: 'success',
        title: '¡Exportado!',
        text: 'OTs filtradas exportadas correctamente',
        timer: 2000,
        showConfirmButton: false
      });
    },
    error: (err) => {
      Swal.fire('Error', 'No se pudo exportar con los filtros especificados', 'error');
    }
  });
}

// ==================== IMPORTACIÓN ====================

/**
 * Descarga la plantilla de importación
 */
downloadImportTemplate(): void {
  this.excelService.downloadTemplate().subscribe({
    next: (blob: Blob) => {
      this.excelService.downloadExcel(blob, 'plantilla_importacion_ots.xlsx');
    },
    error: (err) => {
      Swal.fire('Error', 'No se pudo descargar la plantilla', 'error');
    }
  });
}

/**
 * Descarga el modelo de datos
 */
downloadDataModel(): void {
  this.excelService.downloadModel().subscribe({
    next: (blob: Blob) => {
      this.excelService.downloadExcel(blob, 'modelo_datos_ots.xlsx');
    },
    error: (err) => {
      Swal.fire('Error', 'No se pudo descargar el modelo', 'error');
    }
  });
}

/**
 * Descarga el modelo de relaciones
 */
downloadRelationsModel(): void {
  this.excelService.downloadRelationsModel().subscribe({
    next: (blob: Blob) => {
      this.excelService.downloadExcel(blob, 'modelo_relaciones_ots.xlsx');
    },
    error: (err) => {
      Swal.fire('Error', 'No se pudo descargar el modelo de relaciones', 'error');
    }
  });
}
  // ==================== CARGA DE DATOS ====================
  loadOts(page: number = this.currentPage): void {
    this.loading = true;
    this.errorMessage = null;

    this.otService.listarOts(
      this.searchText.trim() || undefined,
      page,
      this.pageSize,
      'ot,desc'
    ).subscribe({
      next: (pageData) => {
        this.page = pageData;
        this.ots = pageData.content ?? [];
        this.totalElements = pageData.totalElements ?? 0;
        this.currentPage = pageData.number ?? 0;
        this.pageSize = pageData.size ?? this.pageSize;
        this.totalPages = pageData.totalPages ?? 1;
        this.loading = false;
        this.updateSelectionState();
      },
      error: (err) => {
        this.errorMessage = err?.message || 'Error al cargar las OTs';
        this.loading = false;
        Swal.fire('Errorerror');
      }
    });
  }

  // ==================== FILTROS Y BÚSQUEDA ====================
  onSearch(): void {
    this.currentPage = 0;
    this.loadOts();
  }

  clearFilters(): void {
    this.searchText = '';
    this.estadoFilter = '';
    this.dateRange.desde = '';
    this.dateRange.hasta = '';
    this.currentPage = 0;
    this.loadOts();
  }

  // ==================== PAGINACIÓN ====================
  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.loadOts(page);
  }

  changePageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadOts();
  }

  get visiblePages(): number[] {
    const range = 2;
    const start = Math.max(0, this.currentPage - range);
    const end = Math.min(this.totalPages - 1, this.currentPage + range);
    return Array.from({ length: end - start + 1 }, (_, i) => start + i);
  }

  // ==================== SELECCIÓN MÚLTIPLE ====================
  toggleSelectAll(event: any): void {
    const checked = event.target.checked;
    if (checked) {
      this.ots.forEach(ot => this.selectedOts.add(ot.idOts!));
    } else {
      this.selectedOts.clear();
    }
    this.updateSelectionCount();
    this.selectAllChecked = checked;
  }

  toggleSelectOt(ot: OtListDto): void {
    if (this.selectedOts.has(ot.idOts!)) {
      this.selectedOts.delete(ot.idOts!);
    } else {
      this.selectedOts.add(ot.idOts!);
    }
    this.updateSelectionCount();
    this.selectAllChecked = this.selectedOts.size === this.ots.length;
  }

  updateSelectionCount(): void {
    this.selectedCount = this.selectedOts.size;
  }

  updateSelectionState(): void {
    this.selectAllChecked = this.selectedOts.size > 0 && this.selectedOts.size === this.ots.length;
    this.updateSelectionCount();
  }

  // ==================== EXPORTACIÓN MEJORADA ====================
  openExportModal(): void {
    this.exportFiltroText = this.searchText;
    this.exportFiltroActivo = this.selectedOts.size > 0;

    const modalRef = this.modalService.open(this.exportModal, {
      size: 'lg',
      backdrop: 'static',
      centered: true,
      windowClass: 'export-modal'
    });

    this.modalRefs.push(modalRef);
  }

export(): void {
  let exportObservable: Observable<Blob>;
  let exportType = '';

  if (this.exportFiltroActivo && this.selectedCount > 0) {
    // Exportar seleccionadas
    const otIds = Array.from(this.selectedOts);
    exportObservable = this.excelService.exportOts(otIds);
    exportType = 'seleccionadas';
  } else if (this.exportFiltroText || this.exportFechaDesde || this.exportFechaHasta) {
    // Exportar con filtros
    exportObservable = this.excelService.exportFilteredOts(
      this.exportFiltroText || undefined,
      this.exportFechaDesde || undefined,
      this.exportFechaHasta || undefined
    );
    exportType = 'filtradas';
  } else {
    // Exportar todas
    exportObservable = this.excelService.exportAllOts();
    exportType = 'todas';
  }

  // Mostrar loading usando el método correcto
  Swal.fire({
    title: 'Generando Excel...',
    text: 'Por favor espera',
    allowOutsideClick: false,
    showConfirmButton: false,
    willOpen: () => {
      Swal.showLoading();
    }
  });

  exportObservable.subscribe({
    next: (blob) => {
      // Cerrar el loading
      Swal.close();

      const timestamp = new Date().toISOString().slice(0, 19).replace(/[:]/g, '-');
      const filename = `ots_${exportType}_${timestamp}.xlsx`;
      this.excelService.downloadExcel(blob, filename);

      Swal.fire({
        icon: 'success',
        title: '¡Exportación exitosa!',
        text: 'El archivo Excel se ha descargado',
        timer: 2000,
        showConfirmButton: false
      });

      this.closeAllModals();
    },
    error: (err) => {
      Swal.close();
      Swal.fire('Error', 'No se pudo exportar el archivo', 'error');
    }
  });
}

  getExportFilename(): string {
    let base = 'ots_export_';

    if (this.exportFiltroActivo) {
      base += `seleccionadas_${this.selectedCount}_`;
    } else if (this.exportFiltroText) {
      base += `filtradas_`;
    } else {
      base += 'todas_';
    }

    return base + new Date().getTime() + '.xlsx';
  }

  // ==================== IMPORTACIÓN ====================
  openImportModal(): void {
    this.importStep = 1;
    this.importFile = null;
    this.importResult = null;

    const modalRef = this.modalService.open(this.importModal, {
      size: 'lg',
      backdrop: 'static',
      centered: true,
      windowClass: 'import-modal'
    });

    this.modalRefs.push(modalRef);
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    this.processFile(file);
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  onFileDrop(event: DragEvent): void {
    event.preventDefault();
    const files = event.dataTransfer?.files;
    if (files?.[0]) this.processFile(files[0]);
  }

  private processFile(file: File): void {
    if (!file.name.toLowerCase().endsWith('.xlsx')) {
      Swal.fire('Error', 'Solo se permiten archivos Excel (.xlsx)', 'error');
      return;
    }
    this.importFile = file;
  }

  startImport(): void {
    if (!this.importFile) return;

    this.importing = true;
    const importService = this.importMode === 'normal'
      ? this.excelService.importOts(this.importFile)
      : this.excelService.importMasivo(this.importFile);

    importService.subscribe({
      next: (result) => {
        this.importResult = result;
        this.importStep = 3;
        this.importing = false;

        if (result.exito) {
          setTimeout(() => this.loadOts(), 1000);
        }
      },
      error: (err) => {
        Swal.fire('Error', err.error?.mensaje || 'Error en la importación', 'error');
        this.importing = false;
      }
    });
  }

  // ==================== MODALES DE OT ====================
  openCreateModal(): void {
    const modalRef = this.modalService.open(FormOtsComponent, {
      size: 'xl',
      backdrop: 'static',
      centered: true,
      windowClass: 'form-ots-modal'
    });

    modalRef.componentInstance.mode = 'create';

    modalRef.result.then(
      (result) => {
        if (result === 'saved') {
          this.loadOts();
          Swal.fire('Éxito', 'OT creada correctamente', 'success');
        }
      },
      () => {}
    );

    this.modalRefs.push(modalRef);
  }

  openEditModal(ot: OtListDto): void {
    this.otService.getOtParaEdicion(ot.idOts!).subscribe({
      next: (otData) => {
        const modalRef = this.modalService.open(FormOtsComponent, {
          size: 'xl',
          backdrop: 'static',
          centered: true,
          windowClass: 'form-ots-modal'
        });

        modalRef.componentInstance.mode = 'edit';
        modalRef.componentInstance.otId = ot.idOts;
        modalRef.componentInstance.otData = otData;

        modalRef.result.then(
          (result) => {
            if (result === 'saved') {
              this.loadOts();
              Swal.fire('Éxito', 'OT actualizada correctamente', 'success');
            }
          },
          () => {}
        );

        this.modalRefs.push(modalRef);
      },
      error: (err) => Swal.fire('Error', 'No se pudo cargar la OT para editar', 'error')
    });
  }

  openViewModal(ot: OtListDto): void {
    this.otService.getOtById(ot.idOts!).subscribe({
      next: (otDetail) => {
        const modalRef = this.modalService.open(OtDetailComponent, {
          size: 'xl',
          backdrop: 'static',
          centered: true,
          windowClass: 'ot-detail-modal'
        });

        modalRef.componentInstance.otDetail = otDetail;
        this.modalRefs.push(modalRef);
      },
      error: (err) => Swal.fire('Error', 'No se pudo cargar el detalle de la OT', 'error')
    });
  }

  // ==================== UTILITARIOS ====================
  getEstadoClass(estado: string | undefined | null): string {
    switch (estado?.toUpperCase()) {
      case 'FINALIZADA': return 'badge-success';
      case 'EN PROCESO': return 'badge-warning';
      case 'ASIGNACION': return 'badge-info';
      case 'CANCELADA': return 'badge-danger';
      default: return 'badge-secondary';
    }
  }

  toggleEstado(ot: OtListDto): void {
    Swal.fire({
      title: ot.activo ? '¿Desactivar OT?' : '¿Activar OT?',
      text: `La OT #${ot.ot} pasará a estado ${ot.activo ? 'inactiva' : 'activa'}`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí',
      cancelButtonText: 'No'
    }).then((result) => {
      if (result.isConfirmed) {
        this.otService.toggleActivo(ot.idOts!).subscribe({
          next: () => {
            this.loadOts();
            Swal.fire('Éxito', `OT ${ot.activo ? 'desactivada' : 'activada'}`, 'success');
          },
          error: (err) => Swal.fire('Error', 'No se pudo cambiar el estado', 'error')
        });
      }
    });
  }

  downloadTemplate(): void {
    this.excelService.downloadTemplate().subscribe({
      next: (blob) => {
        this.excelService.downloadExcel(blob, 'plantilla_importacion_ots.xlsx');
      },
      error: (err) => Swal.fire('Error', 'No se pudo descargar la plantilla', 'error')
    });
  }

  closeAllModals(): void {
    this.modalRefs.forEach(modal => modal.dismiss());
    this.modalRefs = [];
  }

  // Helper para Math en template
  Math = Math;
}

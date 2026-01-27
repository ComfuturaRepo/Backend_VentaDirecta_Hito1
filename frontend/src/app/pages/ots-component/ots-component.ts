import { Component, OnInit, TemplateRef, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbModal, NgbModalRef, NgbDropdownModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
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
    NgbPaginationModule,
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
  Math=Math;
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

  // Exportación
  exportFiltroActivo = false;
  exportFiltroText = '';
  exportFechaDesde: Date | null = null;
  exportFechaHasta: Date | null = null;

  // Modal references
  private modalRefs: NgbModalRef[] = [];

  ngOnInit(): void {
    this.loadOts();
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
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar las OTs',
          confirmButtonColor: '#dc3545'
        });
      }
    });
  }

  // ==================== FILTROS Y BÚSQUEDA ====================
  onSearch(): void {
    this.currentPage = 0;
    this.selectedOts.clear();
    this.updateSelectionCount();
    this.loadOts();
  }

  clearFilters(): void {
    this.searchText = '';
    this.estadoFilter = '';
    this.dateRange.desde = '';
    this.dateRange.hasta = '';
    this.currentPage = 0;
    this.selectedOts.clear();
    this.updateSelectionCount();
    this.loadOts();
  }

  // ==================== PAGINACIÓN ====================
  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.selectedOts.clear();
    this.updateSelectionCount();
    this.loadOts(page);
  }

  changePageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.selectedOts.clear();
    this.updateSelectionCount();
    this.loadOts();
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

  // ==================== EXPORTACIÓN ====================
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
        this.exportToExcel(() => this.excelService.exportOts(otIds), 'seleccionadas');
      }
    });
  }

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
        this.exportToExcel(() => this.excelService.exportAllOts(), 'todas');
      }
    });
  }



  private exportToExcel(exportFn: () => Observable<Blob>, type: string): void {
    Swal.fire({
      title: 'Generando Excel...',
      text: 'Por favor espera',
      allowOutsideClick: false,
      showConfirmButton: false,
      willOpen: () => {
        Swal.showLoading();
      }
    });

    exportFn().subscribe({
      next: (blob) => {
        Swal.close();
        const timestamp = new Date().toISOString().slice(0, 19).replace(/[:]/g, '-');
        const filename = `ots_${type}_${timestamp}.xlsx`;
        this.excelService.downloadExcel(blob, filename);

        Swal.fire({
          icon: 'success',
          title: '¡Exportación exitosa!',
          text: 'El archivo Excel se ha descargado',
          timer: 2000,
          showConfirmButton: false
        });
      },
      error: (err) => {
        Swal.close();
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo exportar el archivo',
          confirmButtonColor: '#dc3545'
        });
      }
    });
  }

  // ==================== MODALES DE EXPORTACIÓN ====================
  openExportModal(): void {
    this.exportFiltroText = this.searchText;
    this.exportFiltroActivo = this.selectedOts.size > 0;

    const modalRef = this.modalService.open(this.exportModal, {
      size: 'lg',
      backdrop: 'static',
      centered: true,
      scrollable: true,
      windowClass: 'export-modal modal-scrollable'
    });

    this.modalRefs.push(modalRef);
  }

  export(): void {
    if (this.exportFiltroActivo && this.selectedCount > 0) {
      this.exportSelectedOts();
    }  else {
      this.exportAllOts();
    }
    this.closeAllModals();
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
      scrollable: true,
      windowClass: 'import-modal modal-scrollable'
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
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'Solo se permiten archivos Excel (.xlsx)',
        confirmButtonColor: '#dc3545'
      });
      return;
    }

    if (file.size > 10 * 1024 * 1024) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'El archivo no debe superar los 10MB',
        confirmButtonColor: '#dc3545'
      });
      return;
    }

    this.importFile = file;
  }

  startImport(): void {
    if (!this.importFile) return;

    this.importing = true;
    const importService = this.excelService.importMasivo(this.importFile);

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
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: err.error?.mensaje || 'Error en la importación',
          confirmButtonColor: '#dc3545'
        });
        this.importing = false;
      }
    });
  }

  // ==================== DESCARGAS ====================
  downloadImportTemplate(): void {
    this.excelService.downloadTemplate().subscribe({
      next: (blob) => {
        this.excelService.downloadExcel(blob, 'plantilla_importacion_ots.xlsx');
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo descargar la plantilla',
          confirmButtonColor: '#dc3545'
        });
      }
    });
  }





  // ==================== MODALES DE OT ====================
  openCreateModal(): void {
    const modalRef = this.modalService.open(FormOtsComponent, {
      size: 'xl',
      backdrop: 'static',
      centered: true,
      scrollable: true,
      windowClass: 'form-ots-modal modal-scrollable'
    });

    modalRef.componentInstance.mode = 'create';
    modalRef.componentInstance.onClose = () => {
      modalRef.dismiss();
    };

    modalRef.result.then(
      (result) => {
        if (result === 'saved') {
          this.loadOts();
          Swal.fire({
            icon: 'success',
            title: 'Éxito',
            text: 'OT creada correctamente',
            timer: 2000,
            showConfirmButton: false
          });
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
          scrollable: true,
          windowClass: 'form-ots-modal modal-scrollable'
        });

        modalRef.componentInstance.mode = 'edit';
        modalRef.componentInstance.otId = ot.idOts;
        modalRef.componentInstance.otData = otData;
        modalRef.componentInstance.onClose = () => {
          modalRef.dismiss();
        };

        modalRef.result.then(
          (result) => {
            if (result === 'saved') {
              this.loadOts();
              Swal.fire({
                icon: 'success',
                title: 'Éxito',
                text: 'OT actualizada correctamente',
                timer: 2000,
                showConfirmButton: false
              });
            }
          },
          () => {}
        );

        this.modalRefs.push(modalRef);
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo cargar la OT para editar',
          confirmButtonColor: '#dc3545'
        });
      }
    });
  }

  openViewModal(ot: OtListDto): void {
    this.otService.getOtById(ot.idOts!).subscribe({
      next: (otDetail) => {
        const modalRef = this.modalService.open(OtDetailComponent, {
          size: 'xl',
          backdrop: 'static',
          centered: true,
          scrollable: true,
          windowClass: 'ot-detail-modal modal-scrollable'
        });

        modalRef.componentInstance.otDetail = otDetail;
        modalRef.componentInstance.onClose = () => {
          modalRef.dismiss();
        };

        this.modalRefs.push(modalRef);
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo cargar el detalle de la OT',
          confirmButtonColor: '#dc3545'
        });
      }
    });
  }

  // ==================== UTILITARIOS ====================
  getEstadoClass(estado: string | undefined | null): string {
    if (!estado) return 'badge-secondary';

    const estadoUpper = estado.toUpperCase();
    switch (estadoUpper) {
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
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, cambiar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.otService.toggleActivo(ot.idOts!).subscribe({
          next: () => {
            this.loadOts();
            Swal.fire({
              icon: 'success',
              title: 'Éxito',
              text: `OT ${ot.activo ? 'desactivada' : 'activada'} correctamente`,
              timer: 2000,
              showConfirmButton: false
            });
          },
          error: (err) => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo cambiar el estado',
              confirmButtonColor: '#dc3545'
            });
          }
        });
      }
    });
  }

  showImportHelp(): void {
    Swal.fire({
      title: 'Ayuda de importación',
      html: `<div class="text-start">
              <h6>Encabezados requeridos:</h6>
              <ul class="list-group">
                <li class="list-group-item"><code>descripcion</code> - Descripción de la OT</li>
                <li class="list-group-item"><code>fechaapertura</code> - Fecha (dd/mm/aaaa)</li>
                <li class="list-group-item"><code>cliente</code> - Nombre del cliente</li>
                <li class="list-group-item"><code>area</code> - Área del cliente</li>
                <li class="list-group-item"><code>proyecto</code> - Nombre del proyecto</li>
                <li class="list-group-item"><code>fase</code> - Fase del proyecto</li>
                <li class="list-group-item"><code>site</code> - Código del sitio</li>
                <li class="list-group-item"><code>region</code> - Región</li>
                <li class="list-group-item"><code>diasasignados</code> - Número de días</li>
                <li class="list-group-item"><code>estado</code> - Estado de la OT</li>
              </ul>
              <p class="mt-3 text-muted small">Importante: Los encabezados deben ser exactos, en minúsculas y sin espacios</p>
            </div>`,
      width: 600,
      confirmButtonText: 'Entendido'
    });
  }

  closeAllModals(): void {
    this.modalRefs.forEach(modal => modal.dismiss());
    this.modalRefs = [];
  }

  // Helper para truncar texto largo
  truncateText(text: string | undefined | null, maxLength: number = 50): string {
    if (!text) return '—';
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
  }
}

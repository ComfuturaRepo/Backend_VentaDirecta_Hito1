import { Component, OnInit, TemplateRef, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbModal, NgbModalRef, NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';
import { FormOtsComponent } from './form-ots-component/form-ots-component';
import { OtDetailComponent } from './ot-detail-component/ot-detail-component';
import { FileSizePipe } from '../../pipe/file-size.pipe';
import { ExcelService } from '../../service/excel.service';
import { OtService } from '../../service/ot.service';
import { OtListDto } from '../../model/ots';
import { Observable, finalize } from 'rxjs';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import { PageResponse } from '../../model/page.interface';

@Component({
  selector: 'app-ots',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NgbDropdownModule,
    PaginationComponent,
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
  page: PageResponse<OtListDto> | null = null;
  loading = false;
  errorMessage: string | null = null;
  Math = Math;

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

  // Opciones de estado con iconos
  estados = [
    { value: '', label: 'Todos los estados', icon: 'bi-grid' },
    { value: 'ASIGNACION', label: 'Asignación', icon: 'bi-person-badge' },
    { value: 'PRESUPUESTO_ENVIADO', label: 'Presupuesto Enviado', icon: 'bi-send-check' },
    { value: 'CREACION_DE_OC', label: 'Creación de OC', icon: 'bi-file-earmark-text' },
    { value: 'EN_EJECUCION', label: 'En Ejecución', icon: 'bi-gear' },
    { value: 'EN_LIQUIDACION', label: 'En Liquidación', icon: 'bi-cash-stack' },
    { value: 'EN_FACTURACION', label: 'En Facturación', icon: 'bi-receipt' },
    { value: 'FINALIZADO', label: 'Finalizado', icon: 'bi-check-circle' },
    { value: 'CANCELADA', label: 'Cancelada', icon: 'bi-x-circle' }
  ];

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
    ).pipe(
      finalize(() => {
        this.loading = false;
      })
    ).subscribe({
      next: (pageData) => {
        this.page = pageData;
        this.ots = pageData.content ?? [];
        this.totalElements = pageData.totalItems ?? 0;
        this.currentPage = pageData.currentPage ?? 0;
        this.pageSize = pageData.pageSize ?? this.pageSize;
        this.totalPages = pageData.totalPages ?? 1;
        this.updateSelectionState();
      },
      error: (err) => {
        this.errorMessage = err?.message || 'Error al cargar las OTs';
        this.showErrorAlert('Error', 'No se pudieron cargar las OTs');
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

  // ==================== REFRESCAR DATOS ====================
  refreshTable(): void {
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

  // ==================== EXPORTACIÓN MEJORADA ====================
  exportSelectedOts(): void {
    if (this.selectedCount === 0) {
      this.showWarningAlert('Sin selección', 'Por favor selecciona al menos una OT para exportar');
      return;
    }

    const otIds = Array.from(this.selectedOts);

    Swal.fire({
      title: '<strong>Exportar seleccionadas</strong>',
      html: `
        <div class="text-start">
          <p>¿Exportar <span class="fw-bold">${this.selectedCount} OTs</span> a Excel?</p>
          <div class="alert alert-info mt-3">
            <i class="bi bi-info-circle me-2"></i>
            Se generará un archivo con todas las OTs seleccionadas
          </div>
        </div>
      `,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#10b981',
      cancelButtonColor: '#6b7280',
      confirmButtonText: '<i class="bi bi-download me-2"></i>Exportar',
      cancelButtonText: '<i class="bi bi-x me-2"></i>Cancelar',
      customClass: {
        popup: 'sweet-alert-popup',
        confirmButton: 'btn btn-success',
        cancelButton: 'btn btn-secondary'
      },
      buttonsStyling: false
    }).then((result) => {
      if (result.isConfirmed) {
        this.exportToExcel(() => this.excelService.exportOts(otIds), 'seleccionadas');
      }
    });
  }

  exportAllOts(): void {
    Swal.fire({
      title: '<strong>Exportar todas las OTs</strong>',
      html: `
        <div class="text-start">
          <p>¿Exportar las <span class="fw-bold">${this.totalElements} órdenes de trabajo</span> a Excel?</p>
          <div class="alert alert-warning mt-3">
            <i class="bi bi-exclamation-triangle me-2"></i>
            Esta operación puede tomar varios segundos dependiendo de la cantidad de datos
          </div>
        </div>
      `,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#4f46e5',
      cancelButtonColor: '#6b7280',
      confirmButtonText: '<i class="bi bi-database me-2"></i>Exportar todo',
      cancelButtonText: '<i class="bi bi-x me-2"></i>Cancelar',
      customClass: {
        popup: 'sweet-alert-popup',
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-secondary'
      },
      buttonsStyling: false
    }).then((result) => {
      if (result.isConfirmed) {
        this.exportToExcel(() => this.excelService.exportAllOts(), 'todas');
      }
    });
  }

  private exportToExcel(exportFn: () => Observable<Blob>, type: string): void {
    Swal.fire({
      title: '<div class="text-primary"><i class="bi bi-file-earmark-excel fs-1"></i></div>',
      html: '<h5 class="mt-3">Generando archivo Excel...</h5><p class="text-muted">Por favor espera</p>',
      allowOutsideClick: false,
      showConfirmButton: false,
      showCloseButton: false,
      didOpen: () => {
        Swal.showLoading();
      },
      customClass: {
        popup: 'sweet-alert-popup'
      }
    });

    exportFn().subscribe({
      next: (blob) => {
        Swal.close();
        const timestamp = new Date().toISOString().slice(0, 19).replace(/[:]/g, '-');
        const filename = `ots_${type}_${timestamp}.xlsx`;
        this.excelService.downloadExcel(blob, filename);

        this.showSuccessAlert(
          '¡Exportación exitosa!',
          'El archivo Excel se ha descargado correctamente',
          'success'
        );
      },
      error: (err) => {
        Swal.close();
        this.showErrorAlert('Error', 'No se pudo exportar el archivo Excel');
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
    } else {
      this.exportAllOts();
    }
    this.closeAllModals();
  }

  // ==================== IMPORTACIÓN MEJORADA ====================
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
    const dropZone = event.currentTarget as HTMLElement;
    dropZone.classList.add('drag-over');
  }

  onDragLeave(event: DragEvent): void {
    const dropZone = event.currentTarget as HTMLElement;
    dropZone.classList.remove('drag-over');
  }

  onFileDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();

    const dropZone = event.currentTarget as HTMLElement;
    dropZone.classList.remove('drag-over');

    const files = event.dataTransfer?.files;
    if (files?.[0]) this.processFile(files[0]);
  }

  private processFile(file: File): void {
    if (!file.name.toLowerCase().endsWith('.xlsx')) {
      this.showErrorAlert('Formato inválido', 'Solo se permiten archivos Excel (.xlsx)');
      return;
    }

    if (file.size > 10 * 1024 * 1024) {
      this.showErrorAlert('Archivo muy grande', 'El archivo no debe superar los 10MB');
      return;
    }

    this.importFile = file;

    // Mostrar mensaje de éxito
    this.showSuccessAlert(
      'Archivo cargado',
      `${file.name} listo para importar`,
      'success'
    );
  }

  startImport(): void {
    if (!this.importFile) return;

    this.importing = true;

    Swal.fire({
      title: '<div class="text-primary"><i class="bi bi-upload fs-1"></i></div>',
      html: '<h5 class="mt-3">Importando datos...</h5><p class="text-muted">Por favor no cierres esta ventana</p>',
      allowOutsideClick: false,
      showConfirmButton: false,
      showCloseButton: false,
      didOpen: () => {
        Swal.showLoading();
      },
      customClass: {
        popup: 'sweet-alert-popup'
      }
    });

    const importService = this.excelService.importMasivo(this.importFile);

    importService.subscribe({
      next: (result) => {
        this.importResult = result;
        this.importStep = 3;
        this.importing = false;
        Swal.close();

        if (result.exito) {
          setTimeout(() => this.loadOts(), 1000);
        }

        this.showImportResult(result);
      },
      error: (err) => {
        this.importing = false;
        Swal.close();
        this.showErrorAlert('Error en importación', err.error?.mensaje || 'Error al importar el archivo');
      }
    });
  }

  private showImportResult(result: any): void {
    Swal.fire({
      title: '<strong>Resultado de importación</strong>',
      html: `
        <div class="text-start">
          <div class="row g-3 mb-4">
            <div class="col-6">
              <div class="card border-success border-2">
                <div class="card-body text-center py-3">
                  <h2 class="text-success mb-1">${result.exitosos || 0}</h2>
                  <p class="text-success fw-bold mb-0">Registros exitosos</p>
                </div>
              </div>
            </div>
            <div class="col-6">
              <div class="card border-danger border-2">
                <div class="card-body text-center py-3">
                  <h2 class="text-danger mb-1">${result.fallidos || 0}</h2>
                  <p class="text-danger fw-bold mb-0">Registros fallidos</p>
                </div>
              </div>
            </div>
          </div>
          ${result.fallidos > 0 ? `
            <div class="alert alert-warning">
              <i class="bi bi-exclamation-triangle me-2"></i>
              <strong>Errores encontrados:</strong>
              <div class="mt-2 small">
                ${result.registrosConError?.slice(0, 3).map((error: any) =>
                  `<div class="mb-1">• ${error.mensajeError}</div>`
                ).join('')}
                ${result.fallidos > 3 ? `<div class="mt-2 text-muted">... y ${result.fallidos - 3} errores más</div>` : ''}
              </div>
            </div>
          ` : ''}
        </div>
      `,
      icon: result.fallidos > 0 ? 'warning' : 'success',
      confirmButtonText: '<i class="bi bi-check me-2"></i>Entendido',
      customClass: {
        popup: 'sweet-alert-popup',
        confirmButton: 'btn btn-primary'
      },
      buttonsStyling: false
    });
  }

  // ==================== DESCARGAS ====================
  downloadImportTemplate(): void {
    this.excelService.downloadTemplate().subscribe({
      next: (blob) => {
        this.excelService.downloadExcel(blob, 'plantilla_importacion_ots.xlsx');
        this.showSuccessAlert(
          'Plantilla descargada',
          'La plantilla se ha descargado correctamente',
          'success'
        );
      },
      error: (err) => {
        this.showErrorAlert('Error', 'No se pudo descargar la plantilla');
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
          this.refreshTable();
          this.showSuccessAlert(
            '¡OT creada!',
            'La orden de trabajo se ha creado correctamente',
            'success'
          );
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
              this.refreshTable();
              this.showSuccessAlert(
                '¡OT actualizada!',
                'La orden de trabajo se ha actualizado correctamente',
                'success'
              );
            }
          },
          () => {}
        );

        this.modalRefs.push(modalRef);
      },
      error: (err) => {
        this.showErrorAlert('Error', 'No se pudo cargar la OT para editar');
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
        this.showErrorAlert('Error', 'No se pudo cargar el detalle de la OT');
      }
    });
  }

  // ==================== UTILITARIOS ====================
  getEstadoClass(estado: string | undefined | null): string {
    if (!estado) return 'badge-secondary';

    const estadoUpper = estado.toUpperCase();
    switch (estadoUpper) {
      case 'FINALIZADO':
      case 'FINALIZADA':
        return 'badge-success';
      case 'EN_EJECUCION':
      case 'EN_LIQUIDACION':
      case 'EN_FACTURACION':
        return 'badge-warning';
      case 'ASIGNACION':
      case 'PRESUPUESTO_ENVIADO':
      case 'CREACION_DE_OC':
        return 'badge-info';
      case 'CANCELADA':
        return 'badge-danger';
      default:
        return 'badge-secondary';
    }
  }

  getEstadoIcon(estado: string | undefined | null): string {
    if (!estado) return 'bi-question-circle';

    const estadoUpper = estado.toUpperCase();
    switch (estadoUpper) {
      case 'FINALIZADO':
      case 'FINALIZADA':
        return 'bi-check-circle';
      case 'EN_EJECUCION':
      case 'EN_LIQUIDACION':
      case 'EN_FACTURACION':
        return 'bi-clock';
      case 'ASIGNACION':
        return 'bi-person-badge';
      case 'PRESUPUESTO_ENVIADO':
        return 'bi-send-check';
      case 'CREACION_DE_OC':
        return 'bi-file-earmark-text';
      case 'CANCELADA':
        return 'bi-x-circle';
      default:
        return 'bi-question-circle';
    }
  }

  toggleEstado(ot: OtListDto): void {
    const action = ot.activo ? 'desactivar' : 'activar';
    const actionText = ot.activo ? 'Desactivar' : 'Activar';
    const icon = ot.activo ? 'bi-toggle-off' : 'bi-toggle-on';
    const color = ot.activo ? '#f59e0b' : '#10b981';

    Swal.fire({
      title: `<strong>${actionText} OT</strong>`,
      html: `
        <div class="text-start">
          <p>¿Estás seguro de ${action} la OT <span class="fw-bold">#${ot.ot}</span>?</p>
          <div class="alert alert-${ot.activo ? 'warning' : 'info'} mt-3">
            <i class="bi ${icon} me-2"></i>
            La OT pasará a estado <strong>${ot.activo ? 'inactiva' : 'activa'}</strong>
          </div>
        </div>
      `,
      icon: ot.activo ? 'warning' : 'question',
      showCancelButton: true,
      confirmButtonColor: color,
      cancelButtonColor: '#6b7280',
      confirmButtonText: `<i class="bi ${icon} me-2"></i>${actionText}`,
      cancelButtonText: '<i class="bi bi-x me-2"></i>Cancelar',
      customClass: {
        popup: 'sweet-alert-popup',
        confirmButton: 'btn',
        cancelButton: 'btn btn-secondary'
      },
      buttonsStyling: false
    }).then((result) => {
      if (result.isConfirmed) {
        this.otService.toggleActivo(ot.idOts!).subscribe({
          next: () => {
            this.loadOts();
            this.showSuccessAlert(
              `¡OT ${action}ada!`,
              `La OT #${ot.ot} ha sido ${action}ada correctamente`,
              'success'
            );
          },
          error: (err) => {
            this.showErrorAlert('Error', 'No se pudo cambiar el estado de la OT');
          }
        });
      }
    });
  }

  showImportHelp(): void {
    Swal.fire({
      title: '<strong>Ayuda de importación</strong>',
      html: `
        <div class="text-start">
          <h6 class="mb-3 text-primary">Encabezados requeridos:</h6>
          <div class="table-responsive">
            <table class="table table-sm">
              <thead>
                <tr>
                  <th>Encabezado</th>
                  <th>Descripción</th>
                  <th>Ejemplo</th>
                </tr>
              </thead>
              <tbody>
                <tr><td><code>descripcion</code></td><td>Descripción de la OT</td><td>Mantenimiento sitio A</td></tr>
                <tr><td><code>fechaapertura</code></td><td>Fecha (dd/mm/aaaa)</td><td>15/12/2023</td></tr>
                <tr><td><code>cliente</code></td><td>Nombre del cliente</td><td>Empresa ABC</td></tr>
                <tr><td><code>area</code></td><td>Área del cliente</td><td>Tecnología</td></tr>
                <tr><td><code>proyecto</code></td><td>Nombre del proyecto</td><td>Proyecto X</td></tr>
                <tr><td><code>fase</code></td><td>Fase del proyecto</td><td>Fase 1</td></tr>
                <tr><td><code>site</code></td><td>Código del sitio</td><td>SIT001</td></tr>
                <tr><td><code>region</code></td><td>Región</td><td>Norte</td></tr>
                <tr><td><code>diasasignados</code></td><td>Número de días</td><td>30</td></tr>
                <tr><td><code>estado</code></td><td>Estado de la OT</td><td>EN_EJECUCION</td></tr>
              </tbody>
            </table>
          </div>
          <div class="alert alert-info mt-3">
            <i class="bi bi-info-circle me-2"></i>
            <strong>Importante:</strong> Los encabezados deben ser exactos, en minúsculas y sin espacios
          </div>
        </div>
      `,
      width: 700,
      confirmButtonText: '<i class="bi bi-check me-2"></i>Entendido',
      customClass: {
        popup: 'sweet-alert-popup',
        confirmButton: 'btn btn-primary'
      },
      buttonsStyling: false
    });
  }

  // ==================== HELPERS DE ALERTAS ====================
  private showSuccessAlert(title: string, text: string, icon: any = 'success'): void {
    Swal.fire({
      title: `<strong>${title}</strong>`,
      text: text,
      icon: icon,
      timer: 3000,
      showConfirmButton: false,
      customClass: {
        popup: 'sweet-alert-popup'
      }
    });
  }

  private showErrorAlert(title: string, text: string): void {
    Swal.fire({
      title: `<strong>${title}</strong>`,
      text: text,
      icon: 'error',
      confirmButtonText: '<i class="bi bi-check me-2"></i>Entendido',
      customClass: {
        popup: 'sweet-alert-popup',
        confirmButton: 'btn btn-danger'
      },
      buttonsStyling: false
    });
  }

  private showWarningAlert(title: string, text: string): void {
    Swal.fire({
      title: `<strong>${title}</strong>`,
      text: text,
      icon: 'warning',
      confirmButtonText: '<i class="bi bi-check me-2"></i>Entendido',
      customClass: {
        popup: 'sweet-alert-popup',
        confirmButton: 'btn btn-warning'
      },
      buttonsStyling: false
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

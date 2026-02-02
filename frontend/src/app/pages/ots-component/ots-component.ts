import { Component, OnInit, TemplateRef, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbModal, NgbModalRef, NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';
import { FormOtsComponent } from './form-ots-component/form-ots-component';
import { OtDetailComponent } from './ot-detail-component/ot-detail-component';
import { FileSizePipe } from '../../pipe/file-size.pipe';
import { ExcelService, ImportResultDTO } from '../../service/excel.service';
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
    FileSizePipe,
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
    { value: '', label: 'Todos los estados', icon: 'bi-grid', color: '#6c757d' },
    { value: 'ASIGNACION', label: 'Asignación', icon: 'bi-person-badge', color: '#0d6efd' },
    { value: 'PRESUPUESTO_ENVIADO', label: 'Presupuesto Enviado', icon: 'bi-send-check', color: '#20c997' },
    { value: 'CREACION_DE_OC', label: 'Creación de OC', icon: 'bi-file-earmark-text', color: '#6f42c1' },
    { value: 'EN_EJECUCION', label: 'En Ejecución', icon: 'bi-gear', color: '#fd7e14' },
    { value: 'EN_LIQUIDACION', label: 'En Liquidación', icon: 'bi-cash-stack', color: '#e83e8c' },
    { value: 'EN_FACTURACION', label: 'En Facturación', icon: 'bi-receipt', color: '#17a2b8' },
    { value: 'FINALIZADO', label: 'Finalizado', icon: 'bi-check-circle', color: '#198754' },
    { value: 'CANCELADA', label: 'Cancelada', icon: 'bi-x-circle', color: '#dc3545' }
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
  importResult: ImportResultDTO | null = null;

  // Exportación
  exportFiltroActivo = false;
  exportFiltroText = '';
  exportFechaDesde: string = '';
  exportFechaHasta: string = '';
get activosCount(): number {
  return this.ots.filter(o => o.activo).length;
}
  // Modal references
  private modalRefs: NgbModalRef[] = [];

  // Constantes
  private readonly MAX_FILE_SIZE_NORMAL = 20 * 1024 * 1024; // 20MB
  private readonly MAX_FILE_SIZE_MASIVO = 50 * 1024 * 1024; // 50MB
  private readonly MAX_ROWS = 1000;

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
          <p>¿Exportar <span class="fw-bold text-primary">${this.selectedCount} OTs</span> a Excel?</p>
          <div class="alert alert-info mt-3 border-0 bg-light-blue">
            <i class="bi bi-info-circle me-2"></i>
            Se generará un archivo con todas las OTs seleccionadas
          </div>
        </div>
      `,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#0d6efd',
      cancelButtonColor: '#6c757d',
      confirmButtonText: '<i class="bi bi-download me-2"></i>Exportar',
      cancelButtonText: '<i class="bi bi-x me-2"></i>Cancelar',
      customClass: {
        popup: 'sweet-alert-popup border-0',
        confirmButton: 'btn btn-primary',
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
          <p>¿Exportar las <span class="fw-bold text-primary">${this.totalElements} órdenes de trabajo</span> a Excel?</p>
          <div class="alert alert-warning mt-3 border-0 bg-light-warning">
            <i class="bi bi-exclamation-triangle me-2"></i>
            Esta operación puede tomar varios segundos dependiendo de la cantidad de datos
          </div>
        </div>
      `,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#0d6efd',
      cancelButtonColor: '#6c757d',
      confirmButtonText: '<i class="bi bi-database me-2"></i>Exportar todo',
      cancelButtonText: '<i class="bi bi-x me-2"></i>Cancelar',
      customClass: {
        popup: 'sweet-alert-popup border-0',
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
      html: '<h5 class="mt-3 text-dark">Generando archivo Excel...</h5><p class="text-muted">Por favor espera</p>',
      allowOutsideClick: false,
      showConfirmButton: false,
      showCloseButton: false,
      customClass: {
        popup: 'sweet-alert-popup border-0'
      },
      didOpen: () => {
        Swal.showLoading();
      }
    });

    exportFn().subscribe({
      next: (blob) => {
        Swal.close();
        const filename = this.excelService.generateFileName(`ots_${type}`, 'xlsx');
        this.excelService.downloadExcel(blob, filename);

        this.showSuccessAlert(
          '¡Exportación exitosa!',
          `El archivo "${filename}" se ha descargado correctamente`,
          'success'
        );
      },
      error: (err) => {
        Swal.close();
        this.showErrorAlert('Error en exportación', 'No se pudo exportar el archivo Excel');
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

  // ==================== IMPORTACIÓN MEJORADA CON VALIDACIONES ====================
  openImportModal(): void {
    this.importStep = 1;
    this.importFile = null;
    this.importResult = null;
    this.importMode = 'normal';

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
    const validation = this.validateFile(file);
    if (!validation.isValid) {
      this.showErrorAlert('Archivo inválido', validation.message);
      return;
    }

    this.importFile = file;
    this.importStep = 2;

    this.showSuccessAlert(
      'Archivo cargado',
      `${file.name} listo para importar (${this.formatFileSize(file.size)})`,
      'success'
    );
  }

  private validateFile(file: File): { isValid: boolean; message: string } {
    // Validar extensión
    const validExtensions = ['.xlsx', '.xls'];
    const fileExtension = file.name.substring(file.name.lastIndexOf('.')).toLowerCase();

    if (!validExtensions.includes(fileExtension)) {
      return {
        isValid: false,
        message: 'Solo se permiten archivos Excel (.xlsx, .xls)'
      };
    }

    // Validar tamaño según modo
    const maxSize = this.importMode === 'masivo' ? this.MAX_FILE_SIZE_MASIVO : this.MAX_FILE_SIZE_NORMAL;
    if (file.size > maxSize) {
      const maxSizeMB = maxSize / (1024 * 1024);
      return {
        isValid: false,
        message: `El archivo excede el tamaño máximo de ${maxSizeMB}MB para importación ${this.importMode}`
      };
    }

    return { isValid: true, message: 'Archivo válido' };
  }

  private formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  startImport(): void {
    if (!this.importFile) {
      this.showErrorAlert('Error', 'No hay archivo seleccionado');
      return;
    }

    // Validar archivo nuevamente
    const validation = this.validateFile(this.importFile);
    if (!validation.isValid) {
      this.showErrorAlert('Archivo inválido', validation.message);
      return;
    }

    this.importing = true;

    Swal.fire({
      title: '<div class="text-primary"><i class="bi bi-upload fs-1"></i></div>',
      html: '<h5 class="mt-3 text-dark">Importando datos...</h5><p class="text-muted">Por favor no cierres esta ventana</p>',
      allowOutsideClick: false,
      showConfirmButton: false,
      showCloseButton: false,
      customClass: {
        popup: 'sweet-alert-popup border-0'
      },
      didOpen: () => {
        Swal.showLoading();
      }
    });

    const importService = this.importMode === 'masivo'
      ? this.excelService.importMasivo(this.importFile)
      : this.excelService.importOts(this.importFile);

    importService.subscribe({
      next: (result: ImportResultDTO) => {
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
        this.showErrorAlert('Error en importación', err.error?.mensaje || err.message || 'Error al importar el archivo');
      }
    });
  }

  private showImportResult(result: ImportResultDTO): void {
    const html = this.excelService.formatImportResult(result);

    Swal.fire({
      title: result.exito ? '<strong class="text-success">✅ Importación Exitosa</strong>' :
                            '<strong class="text-warning">⚠️ Importación con Errores</strong>',
      html: `<div style="text-align: left;">${html}</div>`,
      icon: result.fallidos === 0 ? 'success' : 'warning',
      showCancelButton: result.fallidos > 0,
      confirmButtonText: '<i class="bi bi-check me-2"></i>Aceptar',
      cancelButtonText: result.fallidos > 0 ? '<i class="bi bi-list me-2"></i>Ver detalles' : undefined,
      customClass: {
        popup: 'sweet-alert-popup border-0',
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-outline-primary'
      },
      buttonsStyling: false
    }).then((result) => {
      if (result.dismiss === Swal.DismissReason.cancel) {
        this.showDetailedImportErrors();
      }
    });
  }

  private showDetailedImportErrors(): void {
    if (!this.importResult?.registrosConError?.length) return;

    let errorsHtml = `
      <div class="text-start">
        <h6 class="text-danger mb-3"><i class="bi bi-x-circle-fill me-2"></i>Detalle de errores:</h6>
        <div class="table-responsive">
          <table class="table table-sm table-bordered">
            <thead class="table-light">
              <tr>
                <th width="80">Fila</th>
                <th>Error</th>
              </tr>
            </thead>
            <tbody>
    `;

    this.importResult.registrosConError.forEach((error: any) => {
      errorsHtml += `
        <tr>
          <td class="fw-semibold text-danger">${error.filaExcel || 'N/A'}</td>
          <td class="text-dark">${error.mensajeError || 'Error desconocido'}</td>
        </tr>
      `;
    });

    errorsHtml += `
            </tbody>
          </table>
        </div>
        <div class="alert alert-info mt-3 border-0 bg-light-blue">
          <i class="bi bi-info-circle me-2"></i>
          <strong>Total errores:</strong> ${this.importResult.fallidos} de ${this.importResult.totalRegistros} registros
        </div>
      </div>
    `;

    Swal.fire({
      title: '<strong class="text-danger">📋 Detalle de Errores</strong>',
      html: errorsHtml,
      width: 800,
      showConfirmButton: true,
      confirmButtonText: '<i class="bi bi-download me-2"></i>Descargar reporte',
      showCancelButton: true,
      cancelButtonText: '<i class="bi bi-x me-2"></i>Cerrar',
      customClass: {
        popup: 'sweet-alert-popup border-0',
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-secondary'
      },
      buttonsStyling: false
    }).then((result) => {
      if (result.isConfirmed) {
        this.downloadErrorReport();
      }
    });
  }

  private downloadErrorReport(): void {
    if (!this.importResult?.registrosConError) return;

    const reportContent = this.generateErrorReport();
    const blob = new Blob([reportContent], { type: 'text/plain' });
    const filename = `reporte_errores_importacion_${new Date().toISOString().slice(0, 10)}.txt`;

    this.excelService.downloadText(blob, filename);
  }

  private generateErrorReport(): string {
    let report = `Reporte de Errores de Importación - OTs\n`;
    report += `=========================================\n`;
    report += `Fecha: ${new Date().toLocaleString()}\n`;
    report += `Total registros: ${this.importResult?.totalRegistros || 0}\n`;
    report += `Exitosos: ${this.importResult?.exitosos || 0}\n`;
    report += `Fallidos: ${this.importResult?.fallidos || 0}\n\n`;
    report += `Detalle de errores:\n`;
    report += `===================\n\n`;

    this.importResult?.registrosConError?.forEach((error: any, index: number) => {
      report += `${index + 1}. Fila ${error.filaExcel || 'N/A'}:\n`;
      report += `   Error: ${error.mensajeError || 'Error desconocido'}\n`;
      if (error.cliente) report += `   Cliente: ${error.cliente}\n`;
      if (error.proyecto) report += `   Proyecto: ${error.proyecto}\n`;
      report += `\n`;
    });

    return report;
  }

  // ==================== DESCARGAS DE PLANTILLAS ====================
  downloadImportTemplate(): void {
    this.excelService.downloadTemplate().subscribe({
      next: (blob) => {
        const filename = this.excelService.generateFileName('plantilla_importacion_ots', 'xlsx');
        this.excelService.downloadExcel(blob, filename);
        this.showSuccessAlert(
          'Plantilla descargada',
          `La plantilla "${filename}" se ha descargado correctamente`,
          'success'
        );
      },
      error: (err) => {
        this.showErrorAlert('Error', 'No se pudo descargar la plantilla');
      }
    });
  }

  downloadModeloDatos(): void {
    this.excelService.downloadModeloDatos().subscribe({
      next: (blob) => {
        const filename = this.excelService.generateFileName('modelo_datos_combos', 'xlsx');
        this.excelService.downloadExcel(blob, filename);
        this.showSuccessAlert(
          'Modelo descargado',
          `El modelo de datos "${filename}" se ha descargado correctamente`,
          'success'
        );
      },
      error: (err) => {
        this.showErrorAlert('Error', 'No se pudo descargar el modelo de datos');
      }
    });
  }

  downloadInstrucciones(): void {
    this.excelService.downloadInstrucciones().subscribe({
      next: (blob) => {
        const filename = this.excelService.generateFileName('instrucciones_importacion', 'txt');
        this.excelService.downloadText(blob, filename);
        this.showSuccessAlert(
          'Instrucciones descargadas',
          `Las instrucciones "${filename}" se han descargado correctamente`,
          'success'
        );
      },
      error: (err) => {
        this.showErrorAlert('Error', 'No se pudo descargar las instrucciones');
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
      this.refreshTable();
    };

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
          this.refreshTable();
        };

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

  // ==================== HELPERS ====================
  getEstadoClass(estado: string | undefined | null): string {
    if (!estado) return 'badge-light text-dark';
    const estadoObj = this.estados.find(e => e.value === estado.toUpperCase());
    return estadoObj ? `badge-light text-${estadoObj.color?.replace('#', '')}` : 'badge-light text-dark';
  }

  getEstadoIcon(estado: string | undefined | null): string {
    if (!estado) return 'bi-question-circle';
    const estadoObj = this.estados.find(e => e.value === estado.toUpperCase());
    return estadoObj?.icon || 'bi-question-circle';
  }

  toggleEstado(ot: OtListDto): void {
    const action = ot.activo ? 'desactivar' : 'activar';
    const actionText = ot.activo ? 'Desactivar' : 'Activar';
    const icon = ot.activo ? 'bi-toggle-off' : 'bi-toggle-on';
    const color = ot.activo ? '#fd7e14' : '#198754';

    Swal.fire({
      title: `<strong>${actionText} OT</strong>`,
      html: `
        <div class="text-start">
          <p>¿Estás seguro de ${action} la OT <span class="fw-bold text-primary">#${ot.ot}</span>?</p>
          <div class="alert ${ot.activo ? 'alert-warning' : 'alert-success'} mt-3 border-0">
            <i class="bi ${icon} me-2"></i>
            La OT pasará a estado <strong>${ot.activo ? 'inactiva' : 'activa'}</strong>
          </div>
        </div>
      `,
      icon: ot.activo ? 'warning' : 'question',
      showCancelButton: true,
      confirmButtonColor: color,
      cancelButtonColor: '#6c757d',
      confirmButtonText: `<i class="bi ${icon} me-2"></i>${actionText}`,
      cancelButtonText: '<i class="bi bi-x me-2"></i>Cancelar',
      customClass: {
        popup: 'sweet-alert-popup border-0',
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
      title: '<strong class="text-primary">📋 Ayuda de importación</strong>',
      html: `
        <div class="text-start">
          <h6 class="mb-3 text-primary">Encabezados requeridos:</h6>
          <div class="table-responsive">
            <table class="table table-sm table-bordered">
              <thead class="table-light">
                <tr>
                  <th width="150">Encabezado</th>
                  <th>Descripción</th>
                  <th width="150">Ejemplo</th>
                </tr>
              </thead>
              <tbody>
                <tr><td><code>fechaapertura</code></td><td>Fecha (dd/mm/aaaa)</td><td>15/12/2023</td></tr>
                <tr><td><code>cliente</code></td><td>Nombre del cliente</td><td>Empresa ABC</td></tr>
                <tr><td><code>area</code></td><td>Área del cliente</td><td>Tecnología</td></tr>
                <tr><td><code>proyecto</code></td><td>Nombre del proyecto</td><td>Proyecto X</td></tr>
                <tr><td><code>fase</code></td><td>Fase del proyecto</td><td>Fase 1</td></tr>
                <tr><td><code>site</code></td><td>Código del sitio</td><td>SIT001</td></tr>
                <tr><td><code>region</code></td><td>Región</td><td>Norte</td></tr>
                <tr><td><code>tipoOt</code></td><td>Tipo OT (nuevo)</td><td>Tipo 1</td></tr>
                <tr><td><code>estado</code></td><td>Estado OT</td><td>ASIGNACION</td></tr>
                <tr><td><code>otAnterior</code></td><td>OT anterior (condicional)</td><td>12345</td></tr>
              </tbody>
            </table>
          </div>
          <div class="alert alert-info mt-3 border-0 bg-light-blue">
            <i class="bi bi-info-circle me-2"></i>
            <strong>Importante:</strong> Los encabezados deben ser exactos, en minúsculas y sin espacios
          </div>
        </div>
      `,
      width: 700,
      confirmButtonText: '<i class="bi bi-check me-2"></i>Entendido',
      customClass: {
        popup: 'sweet-alert-popup border-0',
        confirmButton: 'btn btn-primary'
      },
      buttonsStyling: false
    });
  }

  // ==================== HELPERS DE ALERTAS ====================
  private showSuccessAlert(title: string, text: string, icon: any = 'success'): void {
    Swal.fire({
      title: `<strong class="text-success">${title}</strong>`,
      text: text,
      icon: icon,
      timer: 3000,
      showConfirmButton: false,
      customClass: {
        popup: 'sweet-alert-popup border-0'
      }
    });
  }

  private showErrorAlert(title: string, text: string): void {
    Swal.fire({
      title: `<strong class="text-danger">${title}</strong>`,
      text: text,
      icon: 'error',
      confirmButtonText: '<i class="bi bi-check me-2"></i>Entendido',
      customClass: {
        popup: 'sweet-alert-popup border-0',
        confirmButton: 'btn btn-danger'
      },
      buttonsStyling: false
    });
  }

  private showWarningAlert(title: string, text: string): void {
    Swal.fire({
      title: `<strong class="text-warning">${title}</strong>`,
      text: text,
      icon: 'warning',
      confirmButtonText: '<i class="bi bi-check me-2"></i>Entendido',
      customClass: {
        popup: 'sweet-alert-popup border-0',
        confirmButton: 'btn btn-warning'
      },
      buttonsStyling: false
    });
  }

  closeAllModals(): void {
    this.modalRefs.forEach(modal => modal.dismiss());
    this.modalRefs = [];
  }

  truncateText(text: string | undefined | null, maxLength: number = 50): string {
    if (!text) return '—';
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
  }
}

import { Component, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import Swal from 'sweetalert2';
import {
  ATSListDTO,
  ATSRequestDTO,
  ATSResponseDTO,
  EPPDTO,
  ParticipanteDTO,
  SSTFormRequestDTO,
  SSTFormResponseDTO,
  TipoRiesgoDTO,
  TrabajoDTO
} from '../../model/ssoma.model';
import { ATSListComponent } from '../ats-list.component/ats-list.component';
import { SearchBarComponent } from '../../component/search-bar.component/search-bar.component';
import { ATSService } from '../../service/ssoma.service';
import { PageResponse } from '../../model/page.interface';
import { SSTCompleteFormComponent } from '../ats-list.component/sst-complete-form.component/sst-complete-form.component';


@Component({
  selector: 'app-sst-ats',
  templateUrl: './sst-ats.component.html',
  styleUrls: ['./sst-ats.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ATSListComponent,
    SearchBarComponent,
    SSTCompleteFormComponent
  ]
})
export class SSTATSComponent implements OnInit {
  // Lista paginada
  atsList: ATSListDTO[] = [];
  currentPage = 0;
  pageSize = 10;
  totalItems = 0;
  totalPages = 0;
  searchTerm = '';

  // Estados de UI
  showDetailModal = false;
  showCreateForm = false;
  showCompleteForm = false;
  isLoading = false;
  isCreating = false;
  isCreatingComplete = false;

  // Datos seleccionados
  selectedATS: ATSResponseDTO | null = null;
  newATS: ATSRequestDTO = this.getDefaultATS();
  sstForm: SSTFormRequestDTO = this.getDefaultSSTForm();

  // Datos de prueba
  trabajos: TrabajoDTO[] = [
    { idTrabajo: 1, nombre: 'Instalación de Antenas en Altura' },
    { idTrabajo: 2, nombre: 'Mantenimiento de Equipos Eléctricos' },
    { idTrabajo: 3, nombre: 'Trabajos en Espacios Confinados' },
    { idTrabajo: 4, nombre: 'Excavación para Cimentación' },
    { idTrabajo: 5, nombre: 'Izaje de Equipos Pesados' }
  ];

  trabajadores: ParticipanteDTO[] = [
    { idTrabajador: 1, nombres: 'JUAN RAMON', apellidos: 'AGUIRRE RONDINEL', cargo: 'ANALISTA FINANCIERO', rol: 'Supervisor' },
    { idTrabajador: 2, nombres: 'JOSE MICHAEL', apellidos: 'BENAVIDES ROMERO', cargo: 'ANALISTA DE ENERGIA', rol: 'SOMA' },
    { idTrabajador: 3, nombres: 'LEEANN ALEJANDRA', apellidos: 'BENITES PALOMINO', cargo: 'SUPERVISORA DE OBRAS CIVILES', rol: 'Responsable' },
    { idTrabajador: 4, nombres: 'CROSBY', apellidos: 'BRICEÑO MARAVI', cargo: 'GERENTE DE CUENTA COMERCIAL', rol: 'Trabajador' }
  ];

  epps: EPPDTO[] = [
    { idEPP: 1, nombre: 'Casco de seguridad' },
    { idEPP: 2, nombre: 'Chaleco reflectante' },
    { idEPP: 3, nombre: 'Zapato de seguridad' },
    { idEPP: 4, nombre: 'Guantes de cuero' },
    { idEPP: 5, nombre: 'Lentes de seguridad' },
    { idEPP: 6, nombre: 'Protectores auditivos' },
    { idEPP: 7, nombre: 'Arnés de cuerpo completo' }
  ];

  tiposRiesgo: TipoRiesgoDTO[] = [
    { id: 1, nombre: 'Trabajo en altura' },
    { id: 2, nombre: 'Trabajo eléctrico' },
    { id: 3, nombre: 'Trabajo en espacios confinados' },
    { id: 4, nombre: 'Trabajo en caliente' },
    { id: 5, nombre: 'Excavación y zanjas' },
    { id: 6, nombre: 'Trabajos de izaje' }
  ];

  constructor(private atsService: ATSService) {}

  ngOnInit(): void {
    this.loadATS();
  }

 @HostListener('document:keydown', ['$event'])
handleEscapeKey(event: KeyboardEvent): void {
  if (event.key === 'Escape') {
    if (this.showDetailModal || this.showCreateForm || this.showCompleteForm) {
      event.preventDefault();
      this.onCloseAllModals();
    }
  }
}


  private getDefaultATS(): ATSRequestDTO {
    return {
      fecha: new Date().toISOString().split('T')[0],
      hora: '08:00:00',
      empresa: '',
      lugarTrabajo: '',
      idTrabajo: 0,
      participantes: [],
      riesgos: [],
      eppIds: [],
      tipoRiesgoIds: []
    };
  }
get totalParticipantes(): number {
  if (!this.atsList || this.atsList.length === 0) {
    return 0;
  }
  return this.atsList.reduce((sum, ats) => sum + (ats.cantidadParticipantes || 0), 0);
}
  private getDefaultSSTForm(): SSTFormRequestDTO {
    return {
      empresa: '',
      lugarTrabajo: '',
      fecha: new Date().toISOString().split('T')[0],
      hora: '08:00:00',
      ats: {
        idTrabajo: 0,
        participantes: [],
        riesgos: [],
        eppIds: [],
        tipoRiesgoIds: []
      },
      capacitacion: {
        tema: '',
        fecha: new Date().toISOString().split('T')[0],
        hora: '08:00:00',
        idCapacitador: 0,
        asistentes: []
      },
      inspeccionEPP: {
        idInspector: 0,
        detalles: []
      },
      inspeccionHerramienta: {
        idSupervisor: 0,
        detalles: []
      },
      petar: {
        respuestas: [],
        trabajadoresAutorizadosIds: []
      }
    };
  }

  getATSHoy(): number {
    const hoy = new Date().toISOString().split('T')[0];
    return this.atsList.filter(ats => ats.fecha === hoy).length;
  }

  loadATS(): void {
    this.isLoading = true;
    this.atsService.getAllATS(this.currentPage, this.pageSize, 'fecha,desc', this.searchTerm)
      .subscribe({
        next: (response: PageResponse<ATSListDTO>) => {
          this.atsList = response.content;
          this.totalItems = response.totalItems;
          this.totalPages = response.totalPages;
          this.currentPage = response.currentPage;
          this.pageSize = response.pageSize;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error al cargar ATS:', error);
          this.showError('Error al cargar la lista de ATS');
          this.isLoading = false;
        }
      });
  }

  onPageChange(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadATS();
    }
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadATS();
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.loadATS();
  }

  onViewDetails(id: number): void {
    this.atsService.getATSById(id)
      .subscribe({
        next: (ats: ATSResponseDTO) => {
          this.selectedATS = ats;
          this.showDetailModal = true;
        },
        error: (error) => {
          console.error('Error al obtener detalles:', error);
          this.showError('Error al cargar los detalles del ATS');
        }
      });
  }

  onCloseDetail(): void {
    this.showDetailModal = false;
    this.selectedATS = null;
  }

  onShowCreateForm(): void {
    this.newATS = this.getDefaultATS();
    this.showCreateForm = true;
  }

  onCloseCreateForm(): void {
    this.showCreateForm = false;
  }

  onCloseAllModals(): void {
    this.showDetailModal = false;
    this.showCreateForm = false;
    this.showCompleteForm = false;
    this.selectedATS = null;
  }

  onCreateATS(atsData: ATSRequestDTO): void {
    if (!this.validateATS(atsData)) return;

    this.isCreating = true;
    this.atsService.createATS(atsData)
      .subscribe({
        next: (response) => {
          this.showSuccess('ATS creado exitosamente');
          this.showCreateForm = false;
          this.loadATS();
          this.isCreating = false;
        },
        error: (error) => {
          console.error('Error al crear ATS:', error);
          this.showError('Error al crear el ATS');
          this.isCreating = false;
        }
      });
  }

  onShowSSTCompleteForm(): void {
    this.sstForm = this.getDefaultSSTForm();
    this.showCompleteForm = true;
  }

  startSSTForm(): void {
    // Implementa la lógica para iniciar el formulario SST completo
    this.showCompleteForm = false;
    this.showCreateForm = true; // O inicia un formulario especial
  }

  onCloseCompleteForm(): void {
    this.showCompleteForm = false;
  }

  onCreateSSTCompleteForm(): void {
    if (!this.validateSSTForm()) return;

    this.isCreatingComplete = true;
    this.atsService.crearFormularioCompleto(this.sstForm)
      .subscribe({
        next: (response: SSTFormResponseDTO) => {
          this.showSuccess(`
            <div class="text-center">
              <i class="bi bi-check-circle display-4 text-success mb-3"></i>
              <h4 class="mb-3">¡Formulario SST Completo Creado!</h4>
              <div class="text-start">
                <p class="mb-2"><strong>ATS:</strong> ${response.numeroRegistroATS}</p>
                <p class="mb-2"><strong>Capacitación:</strong> ${response.numeroRegistroCapacitacion}</p>
                <p class="mb-2"><strong>Inspección EPP:</strong> ${response.numeroRegistroInspeccionEPP}</p>
                <p class="mb-2"><strong>Inspección Herramientas:</strong> ${response.numeroRegistroInspeccionHerramienta}</p>
                <p class="mb-0"><strong>PETAR:</strong> ${response.numeroRegistroPETAR}</p>
              </div>
            </div>
          `);
          this.showCompleteForm = false;
          this.loadATS();
          this.isCreatingComplete = false;
        },
        error: (error) => {
          console.error('Error al crear formulario completo:', error);
          this.showError('Error al crear el formulario SST completo');
          this.isCreatingComplete = false;
        }
      });
  }

  onRefreshList(): void {
    this.loadATS();
  }

  printATS(): void {
    if (this.selectedATS) {
      // Aquí va la lógica para imprimir
      console.log('Imprimiendo ATS:', this.selectedATS);
      // Por ejemplo, podrías abrir una ventana de impresión
      window.print();
    }
  }

  validateATS(ats: ATSRequestDTO): boolean {
    if (!ats.empresa.trim()) {
      this.showWarning('Ingrese el nombre de la empresa');
      return false;
    }
    if (!ats.lugarTrabajo.trim()) {
      this.showWarning('Ingrese el lugar de trabajo');
      return false;
    }
    if (!ats.idTrabajo) {
      this.showWarning('Seleccione un trabajo');
      return false;
    }
    if (ats.participantes.length === 0) {
      this.showWarning('Agregue al menos un participante');
      return false;
    }
    return true;
  }

  validateSSTForm(): boolean {
    if (!this.sstForm.empresa.trim()) {
      this.showWarning('Ingrese el nombre de la empresa');
      return false;
    }
    if (!this.sstForm.lugarTrabajo.trim()) {
      this.showWarning('Ingrese el lugar de trabajo');
      return false;
    }
    return true;
  }

  showSuccess(message: string): void {
    Swal.fire({
      icon: 'success',
      title: '¡Éxito!',
      html: message,
      timer: 5000,
      showConfirmButton: true,
      confirmButtonColor: '#0d6efd',
      confirmButtonText: 'Aceptar',
      customClass: {
        popup: 'animated fadeIn',
        title: 'text-primary',
        confirmButton: 'btn btn-primary'
      }
    });
  }

  showError(message: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: message,
      timer: 3000,
      showConfirmButton: true,
      confirmButtonColor: '#dc3545',
      confirmButtonText: 'Entendido',
      customClass: {
        popup: 'animated fadeIn',
        title: 'text-danger'
      }
    });
  }

  showWarning(message: string): void {
    Swal.fire({
      icon: 'warning',
      title: 'Advertencia',
      text: message,
      timer: 3000,
      showConfirmButton: true,
      confirmButtonColor: '#ffc107',
      confirmButtonText: 'Entendido',
      customClass: {
        popup: 'animated fadeIn',
        title: 'text-warning'
      }
    });
  }
}

import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, takeUntil, switchMap, of } from 'rxjs';
import { NgbModal, NgbModalRef, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import { Cliente, ClienteCreateUpdateDTO, ClienteService, PageResponseDTO } from '../../service/cliente.service';
import { DropdownItem, DropdownService } from '../../service/dropdown.service';
import { AreaService, AreaCreateUpdateDTO } from '../../service/area.service';

interface Notification {
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  title?: string;
}

@Component({
  selector: 'app-cliente',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    PaginationComponent,
    NgbAlertModule
  ],
  templateUrl: './cliente-component.html',
  styleUrl: './cliente-component.css',
})
export class ClienteComponent implements OnInit, OnDestroy {
  // Estados
  isLoading = false;
  isSubmitting = false;
  isCreatingArea = false;

  // Datos
  clientes: Cliente[] = [];
  areasDropdown: DropdownItem[] = [];
pageResponse: PageResponseDTO<Cliente> = {
  content: [],
  currentPage: 0,
  totalItems: 0,
  totalPages: 0,
  first: true,
  last: true,
  pageSize: 10
};
  // Estadísticas
  activeClientsCount = 0;
  inactiveClientsCount = 0;

  // Paginación
  currentPage = 0;
  pageSize = 10;
  searchTerm = '';

  // Formularios
  clienteForm!: FormGroup;
  areaForm!: FormGroup;
  editMode = false;
  selectedClienteId: number | null = null;

  // Modal
  @ViewChild('clienteModal') clienteModal!: TemplateRef<any>;
  @ViewChild('areaModal') areaModal!: TemplateRef<any>;
  modalRef!: NgbModalRef;

  // Notificaciones
  notifications: Notification[] = [];

  // Búsqueda reactiva
  private searchSubject = new Subject<string>();
  private destroy$ = new Subject<void>();

  // Filtros
  filterActivo: boolean | null = null;

  // Creación rápida de área
  newAreaName = '';
  showAreaInput = false;

  constructor(
    private clienteService: ClienteService,
    private areaService: AreaService,
    private dropdownService: DropdownService,
    private modalService: NgbModal,
    private fb: FormBuilder
  ) {
    this.initForms();
  }

  ngOnInit(): void {
    this.loadDropdowns();
    this.loadClientes();

    // Configurar búsqueda reactiva
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(term => {
      this.searchTerm = term;
      this.currentPage = 0;
      this.loadClientes();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // =============================
  // INICIALIZACIÓN
  // =============================

  private initForms(): void {
    // Formulario de cliente
    this.clienteForm = this.fb.group({
      razonSocial: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(150)]],
      ruc: ['', [
        Validators.required,
        Validators.pattern(/^\d{11}$/),
        Validators.maxLength(11)
      ]],
      areaIds: [[], Validators.required],
      activo: [true]
    });

    // Formulario de área
    this.areaForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      activo: [true]
    });
  }

  // =============================
  // NOTIFICACIONES
  // =============================

  private showNotification(type: 'success' | 'error' | 'warning' | 'info', message: string, title?: string): void {
    const notification: Notification = {
      type,
      message,
      title
    };

    this.notifications.push(notification);

    // Auto-remover después de 5 segundos
    setTimeout(() => {
      this.removeNotification(notification);
    }, 5000);
  }

  removeNotification(notification: Notification): void {
    const index = this.notifications.indexOf(notification);
    if (index > -1) {
      this.notifications.splice(index, 1);
    }
  }

  clearNotifications(): void {
    this.notifications = [];
  }

  // =============================
  // CARGA DE DATOS
  // =============================

  private loadDropdowns(): void {
  this.isLoading = true;
  this.dropdownService.getAreas().subscribe({
    next: (areas) => {
      console.log('Áreas recibidas:', areas); // Para debug

      // Filtra áreas activas usando la propiedad 'estado'
      this.areasDropdown = areas.filter(area => area.estado === true);

      console.log('Áreas activas después de filtrar:', this.areasDropdown.length); // Para debug
      this.isLoading = false;
    },
    error: (error) => {
      console.error('Error al cargar áreas:', error);
      this.showNotification('error', 'Error al cargar las áreas');
      this.isLoading = false;
    }
  });
}

  loadClientes(): void {
    this.isLoading = true;

    let request$;

    if (this.searchTerm.trim()) {
      request$ = this.clienteService.search(this.searchTerm, this.currentPage, this.pageSize);
    } else if (this.filterActivo !== null) {
      if (this.filterActivo) {
        request$ = this.clienteService.getActivos(this.currentPage, this.pageSize);
      } else {
        // Para inactivos, usamos búsqueda o filtramos del total
        request$ = this.clienteService.getAll(this.currentPage, this.pageSize).pipe(
          switchMap(response => {
            const filtered = {
              ...response,
              content: response.content.filter(c => !c.activo)
            };
            return of(filtered);
          })
        );
      }
    } else {
      request$ = this.clienteService.getAll(this.currentPage, this.pageSize);
    }

    request$.subscribe({
      next: (response) => {
        this.pageResponse = response;
        this.clientes = response.content;
        this.calculateStatistics();
        this.isLoading = false;
      },
      error: (error) => {
        this.showNotification('error', 'Error al cargar los clientes');
        this.isLoading = false;
      }
    });
  }

  private calculateStatistics(): void {
    this.activeClientsCount = this.clientes.filter(c => c.activo).length;
    this.inactiveClientsCount = this.clientes.filter(c => !c.activo).length;
  }

  // =============================
  // BÚSQUEDA Y FILTROS
  // =============================

  onSearch(term: string): void {
    this.searchSubject.next(term);
  }

  onFilterChange(filter: string): void {
    switch (filter) {
      case 'activos':
        this.filterActivo = true;
        break;
      case 'inactivos':
        this.filterActivo = false;
        break;
      default:
        this.filterActivo = null;
        break;
    }
    this.currentPage = 0;
    this.loadClientes();
  }

  clearFilters(): void {
    this.filterActivo = null;
    this.searchTerm = '';
    this.currentPage = 0;
    this.loadClientes();
  }

  onRefresh(): void {
    this.loadClientes();
  }

  // =============================
  // PAGINACIÓN
  // =============================

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadClientes();
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadClientes();
  }

  // =============================
  // CRUD DE CLIENTES
  // =============================

  openCreateModal(): void {
    this.editMode = false;
    this.selectedClienteId = null;
    this.clienteForm.reset({
      razonSocial: '',
      ruc: '',
      areaIds: [],
      activo: true
    });

    this.modalRef = this.modalService.open(this.clienteModal, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
  }

  openEditModal(cliente: Cliente): void {
    this.editMode = true;
    this.selectedClienteId = cliente.idCliente;

    this.clienteService.getById(cliente.idCliente).subscribe({
      next: (clienteDetail) => {
        const areaIds = clienteDetail.areas?.map(area => area.idArea) || [];

        this.clienteForm.patchValue({
          razonSocial: clienteDetail.razonSocial,
          ruc: clienteDetail.ruc,
          areaIds: areaIds,
          activo: clienteDetail.activo
        });

        this.modalRef = this.modalService.open(this.clienteModal, {
          size: 'lg',
          backdrop: 'static',
          keyboard: false
        });
      },
      error: (error) => {
        this.showNotification('error', 'Error al cargar el cliente');
      }
    });
  }

  saveCliente(): void {
    if (this.clienteForm.invalid) {
      this.markFormGroupTouched(this.clienteForm);
      return;
    }

    const formData = this.clienteForm.value;

    // Validar RUC antes de enviar
    if (!this.clienteService.validateRucFormat(formData.ruc)) {
      this.showNotification('error', 'El RUC debe tener 11 dígitos', 'Error de validación');
      return;
    }

    this.isSubmitting = true;

    const clienteData: ClienteCreateUpdateDTO = {
      razonSocial: formData.razonSocial,
      ruc: formData.ruc,
      areaIds: formData.areaIds
    };

    const validation = this.clienteService.validateClienteData(clienteData);
    if (!validation.valid) {
      validation.errors.forEach(error => this.showNotification('error', error, 'Error de validación'));
      this.isSubmitting = false;
      return;
    }

    // Verificar RUC único
    this.clienteService.checkRucExists(formData.ruc).pipe(
      switchMap(exists => {
        if (this.editMode && this.selectedClienteId) {
          // En edición, verificar si el RUC existe en otro cliente
          return this.clienteService.checkRucExistsForEdit(formData.ruc, this.selectedClienteId!).pipe(
            switchMap(existsForEdit => {
              if (existsForEdit) {
                throw new Error('El RUC ya está registrado en otro cliente');
              }
              return this.clienteService.update(this.selectedClienteId!, clienteData);
            })
          );
        } else {
          // En creación, verificar si el RUC ya existe
          if (exists) {
            throw new Error('El RUC ya está registrado');
          }
          return this.clienteService.create(clienteData);
        }
      })
    ).subscribe({
      next: (response) => {
        const message = this.editMode
          ? 'Cliente actualizado correctamente'
          : 'Cliente creado correctamente';

        this.showNotification('success', message);
        this.modalRef.close();
        this.loadClientes();
      },
      error: (error) => {
        this.showNotification('error', error.message || 'Error al guardar el cliente');
      },
      complete: () => {
        this.isSubmitting = false;
      }
    });
  }

  toggleStatus(cliente: Cliente): void {
    const action = cliente.activo ? 'desactivar' : 'activar';
    if (!confirm(`¿Está seguro de ${action} el cliente "${cliente.razonSocial}"?`)) {
      return;
    }

    this.isLoading = true;
    this.clienteService.toggleStatus(cliente.idCliente).subscribe({
      next: (updatedCliente) => {
        this.showNotification(
          'success',
          `Cliente ${updatedCliente.activo ? 'activado' : 'desactivado'} correctamente`
        );
        this.loadClientes();
      },
      error: (error) => {
        this.showNotification('error', 'Error al cambiar el estado del cliente');
        this.isLoading = false;
      }
    });
  }

  deleteCliente(cliente: Cliente): void {
    if (!confirm(`¿Está seguro de eliminar el cliente "${cliente.razonSocial}"? Esta acción no se puede deshacer.`)) {
      return;
    }

    this.isLoading = true;
    this.clienteService.delete(cliente.idCliente).subscribe({
      next: () => {
        this.showNotification('success', 'Cliente eliminado correctamente');
        this.loadClientes();
      },
      error: (error) => {
        this.showNotification('error', 'Error al eliminar el cliente');
        this.isLoading = false;
      }
    });
  }

  // =============================
  // CREACIÓN RÁPIDA DE ÁREAS
  // =============================

  toggleAreaInput(): void {
    this.showAreaInput = !this.showAreaInput;
    if (this.showAreaInput) {
      this.newAreaName = '';
      setTimeout(() => {
        const input = document.getElementById('newAreaInput');
        if (input) input.focus();
      }, 0);
    }
  }

 // En el método que crea áreas rápidas, ajusta:
createAreaQuick(): void {
  if (!this.newAreaName || this.newAreaName.trim().length < 2) {
    this.showNotification('error', 'El nombre del área debe tener al menos 2 caracteres');
    return;
  }

  const areaData: AreaCreateUpdateDTO = {
    nombre: this.newAreaName.trim()
  };

  this.isCreatingArea = true;

  this.areaService.checkNombreExists(this.newAreaName).pipe(
    switchMap(exists => {
      if (exists) {
        throw new Error('El área ya existe');
      }
      return this.areaService.create(areaData);
    })
  ).subscribe({
    next: (newArea) => {
      // Agregar al dropdown actual - IMPORTANTE: usar 'estado' en lugar de 'adicional'
      const dropdownItem: DropdownItem = {
        id: newArea.idArea,
        label: newArea.nombre,
        adicional: undefined,
        estado: true  // ← Añade esta propiedad
      };
      this.areasDropdown.push(dropdownItem);

      // Agregar al formulario
      const currentIds = this.clienteForm.get('areaIds')?.value || [];
      this.clienteForm.get('areaIds')?.setValue([...currentIds, newArea.idArea]);

      this.showNotification('success', 'Área creada correctamente');
      this.newAreaName = '';
      this.showAreaInput = false;
    },
    error: (error) => {
      this.showNotification('error', error.message || 'Error al crear el área');
    },
    complete: () => {
      this.isCreatingArea = false;
    }
  });
}

// En saveArea también:


  openAreaModal(): void {
    this.areaForm.reset({
      nombre: '',
      activo: true
    });

    this.modalService.open(this.areaModal, {
      size: 'md',
      backdrop: 'static',
      keyboard: false
    });
  }

saveArea(): void {
  if (this.areaForm.invalid) {
    this.markFormGroupTouched(this.areaForm);
    return;
  }

  const formData = this.areaForm.value;
  const areaData: AreaCreateUpdateDTO = {
    nombre: formData.nombre
  };

  this.isSubmitting = true;

  this.areaService.create(areaData).subscribe({
    next: (newArea) => {
      // Agregar al dropdown usando el formato correcto
      const dropdownItem: DropdownItem = {
        id: newArea.idArea,
        label: newArea.nombre,
        adicional: undefined,
        estado: true
      };
      this.areasDropdown.push(dropdownItem);

      // Agregar al formulario
      const currentIds = this.clienteForm.get('areaIds')?.value || [];
      this.clienteForm.get('areaIds')?.setValue([...currentIds, newArea.idArea]);

      this.showNotification('success', 'Área creada correctamente');
      this.modalService.dismissAll();
    },
    error: (error) => {
      this.showNotification('error', error.message || 'Error al crear el área');
    },
    complete: () => {
      this.isSubmitting = false;
    }
  });
}

  // =============================
  // UTILIDADES
  // =============================

  onAreaToggle(areaId: number): void {
    const currentIds = this.clienteForm.get('areaIds')?.value || [];
    const index = currentIds.indexOf(areaId);

    if (index === -1) {
      // Agregar
      this.clienteForm.get('areaIds')?.setValue([...currentIds, areaId]);
    } else {
      // Remover
      const newIds = [...currentIds];
      newIds.splice(index, 1);
      this.clienteForm.get('areaIds')?.setValue(newIds);
    }

    this.clienteForm.get('areaIds')?.markAsTouched();
  }

  getStatusText(activo: boolean): string {
    return activo ? 'Activo' : 'Inactivo';
  }

  getStatusClass(activo: boolean): string {
    return activo ? 'badge bg-success' : 'badge bg-danger';
  }

  getAreasNames(areas: any[]): string {
    return this.clienteService.getAreasNames(areas);
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  // =============================
  // GETTERS PARA TEMPLATE
  // =============================

  get razonSocial() { return this.clienteForm.get('razonSocial'); }
  get ruc() { return this.clienteForm.get('ruc'); }
  get areaIds() { return this.clienteForm.get('areaIds'); }
  get areaNombre() { return this.areaForm.get('nombre'); }

  // Método para clases de alertas
  getAlertClass(type: string): string {
    switch (type) {
      case 'success': return 'alert-success';
      case 'error': return 'alert-danger';
      case 'warning': return 'alert-warning';
      case 'info': return 'alert-info';
      default: return 'alert-info';
    }
  }

  getAlertIcon(type: string): string {
    switch (type) {
      case 'success': return 'bi-check-circle-fill';
      case 'error': return 'bi-exclamation-circle-fill';
      case 'warning': return 'bi-exclamation-triangle-fill';
      case 'info': return 'bi-info-circle-fill';
      default: return 'bi-info-circle-fill';
    }
  }
}

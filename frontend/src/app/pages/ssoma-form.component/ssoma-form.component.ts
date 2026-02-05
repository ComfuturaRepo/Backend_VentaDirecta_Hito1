// src/app/ssoma/components/ssoma-form/ssoma-form.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators, FormControl } from '@angular/forms';
import { RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { NgselectDropdownComponent } from '../../component/ngselect-dropdown-component/ngselect-dropdown-component';
import { DropdownItem, DropdownService } from '../../service/dropdown.service';
import { SsomaService } from '../../service/ssoma.service';

@Component({
  selector: 'app-ssoma-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    NgselectDropdownComponent
  ],
  templateUrl: './ssoma-form.component.html',
  styleUrls: ['./ssoma-form.component.css']
})
export class SsomaFormComponent implements OnInit {
  // Formulario principal
  ssomaForm: FormGroup;

  // Estados
  loading = true;
  isSubmitting = false;
  activeTab: string = 'ats';

  // Dropdown data
  trabajos: DropdownItem[] = [];
  tareas: DropdownItem[] = [];
  peligros: DropdownItem[] = [];
  riesgos: DropdownItem[] = [];
  medidasControl: DropdownItem[] = [];
  epps: DropdownItem[] = [];
  tiposRiesgo: DropdownItem[] = [];
  trabajadores: DropdownItem[] = [];
  roles: DropdownItem[] = [];
  herramientas: DropdownItem[] = [];
  preguntasPetar: DropdownItem[] = [];
  supervisoresSST: DropdownItem[] = [];
  capacitadores: DropdownItem[] = [];
  inspectores: DropdownItem[] = [];
  supervisoresOperativos: DropdownItem[] = [];

  // Fechas
  today = new Date().toISOString().split('T')[0];
  currentTime = new Date().toTimeString().split(' ')[0].substring(0, 5);

  constructor(
    private fb: FormBuilder,
    private dropdownService: DropdownService,
    private ssomaService: SsomaService
  ) {
    this.ssomaForm = this.createForm();
  }

  ngOnInit(): void {
    this.loadDropdowns();
    this.generateRegistrationNumbers();
  }

  // ========== CREACIÓN DE FORMULARIOS ==========
// En SsomaFormComponent
toggleEpp(eppId: number, event: any): void {
  const eppArray = this.atsEppIds;
  if (event.target.checked) {
    eppArray.push(this.fb.control(eppId));
  } else {
    const index = eppArray.controls.findIndex(x => x.value === eppId);
    if (index >= 0) {
      eppArray.removeAt(index);
    }
  }
}
// src/app/pages/ssoma-form.component/ssoma-form.component.ts
// Reemplaza estos métodos:

// Método para obtener controles de forma segura
getFormControl(formArray: FormArray, index: number, controlName: string): FormControl {
  const control = formArray.at(index).get(controlName);
  return control as FormControl;
}


  // Método específico para participantes ATS
  getParticipanteControl(index: number, controlName: string): FormControl {
    return this.getFormControl(this.atsParticipantes, index, controlName);
  }

  // Método específico para riesgos ATS
  getRiesgoControl(index: number, controlName: string): FormControl {
    return this.getFormControl(this.atsRiesgos, index, controlName);
  }

  // Método específico para asistentes capacitación
  getAsistenteControl(index: number, controlName: string): FormControl {
    return this.getFormControl(this.capacitacionAsistentes, index, controlName);
  }

  // Método específico para detalles EPP
  getEppDetalleControl(index: number, controlName: string): FormControl {
    return this.getFormControl(this.inspeccionEppDetalles, index, controlName);
  }

  // Método específico para detalles herramientas
  getHerramientaDetalleControl(index: number, controlName: string): FormControl {
    return this.getFormControl(this.inspeccionHerramientaDetalles, index, controlName);
  }

  // Método específico para respuestas PETAR
  getRespuestaControl(index: number, controlName: string): FormControl {
    return this.getFormControl(this.petarRespuestas, index, controlName);
  }

  // Método para obtener controles de trabajadores autorizados
  getTrabajadorControl(index: number): FormControl {
    return this.petarTrabajadoresIds.at(index) as FormControl;
  }

toggleTipoRiesgo(tipoId: number, event: any): void {
  const tipoArray = this.atsTipoRiesgoIds;
  if (event.target.checked) {
    tipoArray.push(this.fb.control(tipoId));
  } else {
    const index = tipoArray.controls.findIndex(x => x.value === tipoId);
    if (index >= 0) {
      tipoArray.removeAt(index);
    }
  }
}
  createForm(): FormGroup {
    return this.fb.group({
      // ATS
      ats: this.fb.group({
        fecha: [this.today, Validators.required],
        hora: [this.currentTime, Validators.required],
        empresa: ['COMFUTURA SAC', Validators.required],
        lugarTrabajo: ['', Validators.required],
        idTrabajo: [null, Validators.required],
        participantes: this.fb.array([]),
        riesgos: this.fb.array([]),
        eppIds: this.fb.array([]),
        tipoRiesgoIds: this.fb.array([])
      }),

      // Capacitación
      capacitacion: this.fb.group({
        numeroRegistro: ['', Validators.required],
        tema: ['Charla de 5 Minutos', Validators.required],
        fecha: [this.today, Validators.required],
        hora: [this.currentTime, Validators.required],
        idCapacitador: [null, Validators.required],
        asistentes: this.fb.array([])
      }),

      // Inspección EPP
      inspeccionEpp: this.fb.group({
        numeroRegistro: ['', Validators.required],
        fecha: [this.today, Validators.required],
        idInspector: [null, Validators.required],
        detalles: this.fb.array([])
      }),

      // Inspección Herramientas
      inspeccionHerramienta: this.fb.group({
        numeroRegistro: ['', Validators.required],
        fecha: [this.today, Validators.required],
        idSupervisor: [null, Validators.required],
        detalles: this.fb.array([])
      }),

      // PETAR
      petar: this.fb.group({
        numeroRegistro: ['', Validators.required],
        fecha: [this.today, Validators.required],
        respuestas: this.fb.array([]),
        trabajadoresAutorizadosIds: this.fb.array([])
      })
    });
  }

  // ========== GETTERS PARA FORM ARRAYS ==========

  get atsParticipantes(): FormArray {
    return this.ssomaForm.get('ats.participantes') as FormArray;
  }

  get atsRiesgos(): FormArray {
    return this.ssomaForm.get('ats.riesgos') as FormArray;
  }

  get atsEppIds(): FormArray {
    return this.ssomaForm.get('ats.eppIds') as FormArray;
  }

  get atsTipoRiesgoIds(): FormArray {
    return this.ssomaForm.get('ats.tipoRiesgoIds') as FormArray;
  }

  get capacitacionAsistentes(): FormArray {
    return this.ssomaForm.get('capacitacion.asistentes') as FormArray;
  }

  get inspeccionEppDetalles(): FormArray {
    return this.ssomaForm.get('inspeccionEpp.detalles') as FormArray;
  }

  get inspeccionHerramientaDetalles(): FormArray {
    return this.ssomaForm.get('inspeccionHerramienta.detalles') as FormArray;
  }

  get petarRespuestas(): FormArray {
    return this.ssomaForm.get('petar.respuestas') as FormArray;
  }

  get petarTrabajadoresIds(): FormArray {
    return this.ssomaForm.get('petar.trabajadoresAutorizadosIds') as FormArray;
  }

  // ========== MÉTODOS PARA AGREGAR ELEMENTOS ==========

  addAtsParticipante(): void {
    this.atsParticipantes.push(this.fb.group({
      idTrabajador: [null, Validators.required],
      idRol: [null, Validators.required]
    }));
  }

  addAtsRiesgo(): void {
    this.atsRiesgos.push(this.fb.group({
      idTarea: [null, Validators.required],
      idPeligro: [null, Validators.required],
      idRiesgo: [null, Validators.required],
      idMedida: [null, Validators.required]
    }));
  }

  addCapacitacionAsistente(): void {
    this.capacitacionAsistentes.push(this.fb.group({
      idTrabajador: [null, Validators.required],
      observaciones: ['']
    }));
  }

  addInspeccionEppDetalle(): void {
    this.inspeccionEppDetalles.push(this.fb.group({
      idTrabajador: [null, Validators.required],
      idEpp: [null, Validators.required],
      cumple: [true],
      observacion: ['']
    }));
  }

  addInspeccionHerramientaDetalle(): void {
    this.inspeccionHerramientaDetalles.push(this.fb.group({
      idHerramienta: [null, Validators.required],
      cumple: [true],
      fotoUrl: [''],
      observacion: ['']
    }));
  }

  addPetarRespuesta(): void {
    this.petarRespuestas.push(this.fb.group({
      idPregunta: [null, Validators.required],
      respuesta: [true]
    }));
  }

  addPetarTrabajador(): void {
    this.petarTrabajadoresIds.push(this.fb.control(null, Validators.required));
  }

  // ========== MÉTODOS PARA REMOVER ELEMENTOS ==========

  removeAtsParticipante(index: number): void {
    this.atsParticipantes.removeAt(index);
  }

  removeAtsRiesgo(index: number): void {
    this.atsRiesgos.removeAt(index);
  }

  removeCapacitacionAsistente(index: number): void {
    this.capacitacionAsistentes.removeAt(index);
  }

  removeInspeccionEppDetalle(index: number): void {
    this.inspeccionEppDetalles.removeAt(index);
  }

  removeInspeccionHerramientaDetalle(index: number): void {
    this.inspeccionHerramientaDetalles.removeAt(index);
  }

  removePetarRespuesta(index: number): void {
    this.petarRespuestas.removeAt(index);
  }

  removePetarTrabajador(index: number): void {
    this.petarTrabajadoresIds.removeAt(index);
  }

  // ========== CARGA DE DATOS ==========

// En el método loadDropdowns():
loadDropdowns(): void {
  this.loading = true;
  this.dropdownService.loadSsomaFormDropdowns().subscribe({
    next: (data: any) => { // <-- Agrega tipo explícito
      this.trabajos = data.trabajos;
      this.peligros = data.peligros;
      this.epps = data.epps;
      this.tiposRiesgo = data.tiposRiesgo;
      this.herramientas = data.herramientas;
      this.preguntasPetar = data.preguntasPetar;
      this.trabajadores = data.trabajadores;
      this.roles = data.rolesTrabajo;
      this.supervisoresSST = data.supervisoresSST;
      this.capacitadores = data.capacitadores;
      this.inspectores = data.inspectores;
      this.supervisoresOperativos = data.supervisoresOperativos;

      // Agregar elementos iniciales
      this.addAtsParticipante();
      this.addAtsRiesgo();
      this.addCapacitacionAsistente();
      this.addInspeccionEppDetalle();
      this.addInspeccionHerramientaDetalle();
      this.addPetarRespuesta();
      this.addPetarTrabajador();

      this.loading = false;
    },
    error: (error: any) => { // <-- Agrega tipo explícito
      console.error('Error loading dropdowns:', error);
      this.loading = false;
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'No se pudieron cargar los datos. Por favor, intente nuevamente.'
      });
    }
  });
}

  generateRegistrationNumbers(): void {
    const today = new Date();
    const year = today.getFullYear();
    const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0');

    this.ssomaForm.patchValue({
      capacitacion: {
        numeroRegistro: `CAP-${year}-${random}`
      },
      inspeccionEpp: {
        numeroRegistro: `INSP-EPP-${year}-${random}`
      },
      inspeccionHerramienta: {
        numeroRegistro: `INSP-HERR-${year}-${random}`
      },
      petar: {
        numeroRegistro: `PETAR-${year}-${random}`
      }
    });
  }

  // ========== MANEJO DE TAREAS POR TRABAJO ==========

  onTrabajoChange(idTrabajo: number): void {
    if (idTrabajo) {
      this.dropdownService.getTareasByTrabajo(idTrabajo).subscribe({
        next: (tareas) => {
          this.tareas = tareas;
        },
        error: (error) => {
          console.error('Error loading tareas:', error);
        }
      });
    } else {
      this.tareas = [];
    }
  }

  // ========== MANEJO DE RIESGOS POR PELIGRO ==========

  onPeligroChange(index: number, idPeligro: number): void {
    if (idPeligro) {
      this.dropdownService.getRiesgosByPeligro(idPeligro).subscribe({
        next: (riesgos) => {
          const riesgoControl = this.atsRiesgos.at(index).get('idRiesgo');
          const medidaControl = this.atsRiesgos.at(index).get('idMedida');

          riesgoControl?.setValue(null);
          medidaControl?.setValue(null);
        }
      });
    }
  }

  onRiesgoChange(index: number, idRiesgo: number): void {
    if (idRiesgo) {
      this.dropdownService.getMedidasByRiesgo(idRiesgo).subscribe({
        next: (medidas) => {
          this.atsRiesgos.at(index).get('idMedida')?.setValue(null);
        }
      });
    }
  }

  // ========== MANEJO DE TABS ==========

  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  isTabActive(tab: string): boolean {
    return this.activeTab === tab;
  }

  nextTab(): void {
    const tabs = ['ats', 'capacitacion', 'inspeccionEpp', 'inspeccionHerramienta', 'petar'];
    const currentIndex = tabs.indexOf(this.activeTab);
    if (currentIndex < tabs.length - 1) {
      this.activeTab = tabs[currentIndex + 1];
      window.scrollTo(0, 0);
    }
  }

  prevTab(): void {
    const tabs = ['ats', 'capacitacion', 'inspeccionEpp', 'inspeccionHerramienta', 'petar'];
    const currentIndex = tabs.indexOf(this.activeTab);
    if (currentIndex > 0) {
      this.activeTab = tabs[currentIndex - 1];
      window.scrollTo(0, 0);
    }
  }

  getProgressWidth(): string {
    const tabs = ['ats', 'capacitacion', 'inspeccionEpp', 'inspeccionHerramienta', 'petar'];
    const currentIndex = tabs.indexOf(this.activeTab);
    return `${((currentIndex + 1) / tabs.length) * 100}%`;
  }

  getProgressPercentage(): number {
    const tabs = ['ats', 'capacitacion', 'inspeccionEpp', 'inspeccionHerramienta', 'petar'];
    const currentIndex = tabs.indexOf(this.activeTab);
    return Math.round(((currentIndex + 1) / tabs.length) * 100);
  }

  getActiveTabName(): string {
    const names: {[key: string]: string} = {
      'ats': 'ATS',
      'capacitacion': 'Capacitación',
      'inspeccionEpp': 'Inspección EPP',
      'inspeccionHerramienta': 'Inspección Herramientas',
      'petar': 'PETAR'
    };
    return names[this.activeTab] || '';
  }

  // ========== ENVÍO DEL FORMULARIO ==========

  onSubmit(): void {
    // Validar formulario
    if (this.ssomaForm.invalid) {
      Swal.fire({
        icon: 'warning',
        title: 'Formulario incompleto',
        text: 'Por favor, complete todos los campos requeridos',
        confirmButtonColor: '#3b82f6'
      });
      this.markFormGroupTouched(this.ssomaForm);
      return;
    }

    this.isSubmitting = true;

    Swal.fire({
      title: '¿Crear SSOMA completo?',
      text: 'Se crearán las 5 hojas del SSOMA en una sola transacción',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3b82f6',
      cancelButtonColor: '#6b7280',
      confirmButtonText: 'Sí, crear',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        const formValue = this.prepareFormData();

        this.ssomaService.crearSsomaCompleto(formValue).subscribe({
          next: (response) => {
            this.isSubmitting = false;

            Swal.fire({
              icon: 'success',
              title: '¡SSOMA creado exitosamente!',
              html: `
                <div class="text-left">
                  <p><strong>Transacción ID:</strong> ${response.transaccionId || 'N/A'}</p>
                  <p><strong>ATS:</strong> ${response.ats?.idAts || 'N/A'}</p>
                  <p><strong>Capacitación:</strong> ${response.capacitacion?.idCapacitacion || 'N/A'}</p>
                  <p><strong>Inspección EPP:</strong> ${response.inspeccionEpp?.id || 'N/A'}</p>
                  <p><strong>Fecha:</strong> ${new Date().toLocaleDateString()}</p>
                </div>
              `,
              confirmButtonColor: '#10b981',
              confirmButtonText: 'Aceptar'
            });

            this.resetForm();
          },
          error: (error) => {
            this.isSubmitting = false;

            Swal.fire({
              icon: 'error',
              title: 'Error al crear SSOMA',
              text: error.error?.message || 'Error en el servidor',
              confirmButtonColor: '#ef4444'
            });
          }
        });
      } else {
        this.isSubmitting = false;
      }
    });
  }

  prepareFormData(): any {
    return {
      ats: this.ssomaForm.value.ats,
      capacitacion: this.ssomaForm.value.capacitacion,
      inspeccionEpp: this.ssomaForm.value.inspeccionEpp,
      inspeccionHerramienta: this.ssomaForm.value.inspeccionHerramienta,
      petar: this.ssomaForm.value.petar
    };
  }

  markFormGroupTouched(formGroup: FormGroup | FormArray): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup || control instanceof FormArray) {
        this.markFormGroupTouched(control);
      }
    });
  }

  resetForm(): void {
    // Guardar valores por defecto
    const empresa = this.ssomaForm.get('ats.empresa')?.value;
    const lugarTrabajo = this.ssomaForm.get('ats.lugarTrabajo')?.value;

    // Resetear formulario
    this.ssomaForm.reset();

    // Restaurar valores por defecto
    this.ssomaForm.patchValue({
      ats: {
        fecha: this.today,
        hora: this.currentTime,
        empresa: empresa || 'COMFUTURA SAC',
        lugarTrabajo: lugarTrabajo || ''
      },
      capacitacion: {
        fecha: this.today,
        hora: this.currentTime,
        tema: 'Charla de 5 Minutos'
      },
      inspeccionEpp: {
        fecha: this.today
      },
      inspeccionHerramienta: {
        fecha: this.today
      },
      petar: {
        fecha: this.today
      }
    });

    // Generar nuevos números de registro
    this.generateRegistrationNumbers();

    // Limpiar arrays
    this.clearAllArrays();

    // Agregar elementos iniciales
    this.addAtsParticipante();
    this.addAtsRiesgo();
    this.addCapacitacionAsistente();
    this.addInspeccionEppDetalle();
    this.addInspeccionHerramientaDetalle();
    this.addPetarRespuesta();
    this.addPetarTrabajador();

    // Volver al primer tab
    this.activeTab = 'ats';
  }

  clearAllArrays(): void {
    // ATS
    while (this.atsParticipantes.length !== 0) this.atsParticipantes.removeAt(0);
    while (this.atsRiesgos.length !== 0) this.atsRiesgos.removeAt(0);
    while (this.atsEppIds.length !== 0) this.atsEppIds.removeAt(0);
    while (this.atsTipoRiesgoIds.length !== 0) this.atsTipoRiesgoIds.removeAt(0);

    // Capacitación
    while (this.capacitacionAsistentes.length !== 0) this.capacitacionAsistentes.removeAt(0);

    // Inspección EPP
    while (this.inspeccionEppDetalles.length !== 0) this.inspeccionEppDetalles.removeAt(0);

    // Inspección Herramientas
    while (this.inspeccionHerramientaDetalles.length !== 0) this.inspeccionHerramientaDetalles.removeAt(0);

    // PETAR
    while (this.petarRespuestas.length !== 0) this.petarRespuestas.removeAt(0);
    while (this.petarTrabajadoresIds.length !== 0) this.petarTrabajadoresIds.removeAt(0);
  }
}

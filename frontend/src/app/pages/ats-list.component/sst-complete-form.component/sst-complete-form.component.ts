import { NgselectDropdownComponent } from './../../../component/ngselect-dropdown-component/ngselect-dropdown-component';
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {
  SSTFormRequestDTO,
  AsistenteDTO,
  InspeccionEPPDetalleDTO,
  InspeccionHerramientaDetalleDTO,
  PETARRespuestaDTO,
  TrabajoDTO,
  EPPDTO,
  TipoRiesgoDTO,
  RiesgoDTO,
  TareaDTO,
  PeligroDTO,
  MedidaControlDTO,
  PreguntaPETARDTO,
  HerramientaDTO,
} from '../../../model/ssoma.model';
import { DropdownItem } from '../../../service/dropdown.service';
import { Trabajador } from '../../../service/trabajador.service';

@Component({
  selector: 'app-sst-complete-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, NgselectDropdownComponent],
  templateUrl: './sst-complete-form.component.html',
  styleUrls: ['./sst-complete-form.component.css']
})
export class SSTCompleteFormComponent implements OnInit {
  @Input() formData!: SSTFormRequestDTO;
  @Input() trabajos: TrabajoDTO[] = [];
  @Input() trabajadores: Trabajador[] = [];
  @Input() epps: EPPDTO[] = [];
  @Input() tiposRiesgo: TipoRiesgoDTO[] = [];
  @Input() tareas: TareaDTO[] = [];
  @Input() peligros: PeligroDTO[] = [];
  @Input() riesgos: RiesgoDTO[] = [];
  @Input() medidasControl: MedidaControlDTO[] = [];
  @Input() preguntasPETAR: PreguntaPETARDTO[] = [];
  @Input() herramientas: HerramientaDTO[] = [];
  @Input() isLoading: boolean = false;

  @Output() save = new EventEmitter<SSTFormRequestDTO>();
  @Output() cancel = new EventEmitter<void>();

  // Variables para dropdowns
  trabajosDropdown: DropdownItem[] = [];
  trabajadoresDropdown: DropdownItem[] = [];
  eppsDropdown: DropdownItem[] = [];
  tiposRiesgoDropdown: DropdownItem[] = [];
  tareasDropdown: DropdownItem[] = [];
  peligrosDropdown: DropdownItem[] = [];
  riesgosDropdown: DropdownItem[] = [];
  medidasControlDropdown: DropdownItem[] = [];
  herramientasDropdown: DropdownItem[] = [];

  // Variables para nuevas filas
  newParticipante: { idTrabajador: number, idRol: number } = { idTrabajador: 0, idRol: 0 };
  newRiesgo: { idTarea: number, idPeligro: number, idRiesgo: number, idMedida: number } =
    { idTarea: 0, idPeligro: 0, idRiesgo: 0, idMedida: 0 };
  newAsistente: AsistenteDTO = { idTrabajador: 0, observaciones: '' };
  newEPPDetalle: InspeccionEPPDetalleDTO = {
    idTrabajador: 0,
    idEPP: 0,
    cumple: false,
    observacion: ''
  };
  newHerramientaDetalle: InspeccionHerramientaDetalleDTO = {
    idHerramienta: 0,
    cumple: false,
    fotoUrl: '',
    observacion: ''
  };

  // Roles para participantes
  roles = [
    { id: 1, nombre: 'Supervisor de Trabajo' },
    { id: 2, nombre: 'SOMA (Supervisor SSOMA)' },
    { id: 3, nombre: 'Responsable del Lugar' },
    { id: 4, nombre: 'Trabajador' },
    { id: 5, nombre: 'Capacitador' },
    { id: 6, nombre: 'Inspector EPP' },
    { id: 7, nombre: 'Supervisor Operativo' },
    { id: 8, nombre: 'Brigadista' }
  ];

  rolesDropdown: DropdownItem[] = [];

  // Tipos de inspección EPP
  tiposInspeccion = [
    { id: 1, nombre: 'Planificada' },
    { id: 2, nombre: 'No Planificada' }
  ];

  // Preguntas PETAR fijas
  preguntasPETARFijas = [
    { id: 1, texto: '¿Requiere evaluación de ambiente de trabajo?' },
    { id: 2, texto: '¿El ambiente es natural?' },
    { id: 3, texto: '¿El ambiente es forzado?' },
    { id: 4, texto: '¿Velocidad del aire aceptable?' },
    { id: 5, texto: '¿Contenido de oxígeno adecuado?' },
    { id: 6, texto: '¿Límite inferior de explosividad aceptable?' },
    { id: 7, texto: '¿Se contrastó con documentación respectiva?' },
    { id: 8, texto: '¿Todas las respuestas son afirmativas?' }
  ];

  // Preguntas herramientas manuales
  preguntasHerramientas = [
    { id: 1, texto: '¿La herramienta cuenta con cinta de color de inspección del mes?' },
    { id: 2, texto: '¿La herramienta a utilizar se encuentra en buen estado?' },
    { id: 3, texto: '¿Los mangos están en buen estado?' },
    { id: 4, texto: '¿No presenta grietas ni fracturas?' },
    { id: 5, texto: '¿Los bordes están libres de rebabas?' },
    { id: 6, texto: '¿Los mecanismos funcionan correctamente?' },
    { id: 7, texto: '¿Está identificada correctamente?' },
    { id: 8, texto: '¿Se encuentra limpia y ordenada?' }
  ];

  // Estados para PETAR
  estadosPETAR = [
    { id: 1, nombre: 'Sí' },
    { id: 2, nombre: 'No' },
    { id: 3, nombre: 'Ninguna de las anteriores' }
  ];

  // Step control para navegación
  currentStep = 1;
  totalSteps = 5;

  ngOnInit(): void {
    this.initializeDropdowns();
  }

  initializeDropdowns(): void {
    // Trabajos
    this.trabajosDropdown = this.trabajos.map(t => ({
      id: t.idTrabajo,
      label: t.nombre
    }));

    // Trabajadores
    this.trabajadoresDropdown = this.trabajadores.map(t => ({
      id: t.idTrabajador,
      label: `${t.nombres} ${t.apellidos}`,
      adicional: t.cargoNombre
    }));

    // EPPs
    this.eppsDropdown = this.epps.map(e => ({
      id: e.idEPP,
      label: e.nombre
    }));

    // Tipos de riesgo
    this.tiposRiesgoDropdown = this.tiposRiesgo.map(t => ({
      id: t.id,
      label: t.nombre
    }));

    // Tareas
    this.tareasDropdown = this.tareas.map(t => ({
      id: t.idTarea,
      label: t.nombre
    }));

    // Peligros
    this.peligrosDropdown = this.peligros.map(p => ({
      id: p.idPeligro,
      label: p.descripcion
    }));

    // Riesgos
    this.riesgosDropdown = this.riesgos.map(r => ({
      id: r.idRiesgo,
      label: r.descripcion
    }));

    // Medidas de control
    this.medidasControlDropdown = this.medidasControl.map(m => ({
      id: m.idMedida,
      label: m.descripcion
    }));

    // Herramientas
    this.herramientasDropdown = this.herramientas.map(h => ({
      id: h.idHerramienta,
      label: h.nombre,
      adicional: h.tipo
    }));

    // Roles
    this.rolesDropdown = this.roles.map(r => ({
      id: r.id,
      label: r.nombre
    }));
  }

  // Navegación entre steps
  nextStep(): void {
    if (this.currentStep < this.totalSteps) {
      this.currentStep++;
    }
  }

  prevStep(): void {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }

  goToStep(step: number): void {
    this.currentStep = step;
  }

  // Métodos para agregar elementos
  addParticipante(): void {
    if (this.newParticipante.idTrabajador && this.newParticipante.idRol) {
      this.formData.ats.participantes.push({...this.newParticipante});
      this.newParticipante = { idTrabajador: 0, idRol: 0 };
    }
  }

  removeParticipante(index: number): void {
    this.formData.ats.participantes.splice(index, 1);
  }

  addRiesgo(): void {
    if (this.newRiesgo.idTarea && this.newRiesgo.idPeligro && this.newRiesgo.idRiesgo && this.newRiesgo.idMedida) {
      this.formData.ats.riesgos.push({...this.newRiesgo});
      this.newRiesgo = { idTarea: 0, idPeligro: 0, idRiesgo: 0, idMedida: 0 };
    }
  }

  removeRiesgo(index: number): void {
    this.formData.ats.riesgos.splice(index, 1);
  }

  addAsistente(): void {
    if (this.newAsistente.idTrabajador) {
      this.formData.capacitacion.asistentes.push({...this.newAsistente});
      this.newAsistente = { idTrabajador: 0, observaciones: '' };
    }
  }

  removeAsistente(index: number): void {
    this.formData.capacitacion.asistentes.splice(index, 1);
  }

  addEPPDetalle(): void {
    if (this.newEPPDetalle.idTrabajador && this.newEPPDetalle.idEPP) {
      this.formData.inspeccionEPP.detalles.push({...this.newEPPDetalle});
      this.newEPPDetalle = { idTrabajador: 0, idEPP: 0, cumple: false, observacion: '' };
    }
  }

  removeEPPDetalle(index: number): void {
    this.formData.inspeccionEPP.detalles.splice(index, 1);
  }

  addHerramientaDetalle(): void {
    if (this.newHerramientaDetalle.idHerramienta) {
      this.formData.inspeccionHerramienta.detalles.push({...this.newHerramientaDetalle});
      this.newHerramientaDetalle = { idHerramienta: 0, cumple: false, fotoUrl: '', observacion: '' };
    }
  }

  removeHerramientaDetalle(index: number): void {
    this.formData.inspeccionHerramienta.detalles.splice(index, 1);
  }

  // Métodos para manejar selecciones
  onTrabajoSelected(event: any): void {
    if (event?.id) {
      this.formData.ats.idTrabajo = event.id;
    }
  }

  onEPPSelectionChange(event: any): void {
    if (event?.ids) {
      this.formData.ats.eppIds = event.ids;
    }
  }

  onTipoRiesgoSelectionChange(event: any): void {
    if (event?.ids) {
      this.formData.ats.tipoRiesgoIds = event.ids;
    }
  }

  onTrabajadorSelected(event: any, field: string): void {
    if (event?.id) {
      switch(field) {
        case 'participante':
          this.newParticipante.idTrabajador = event.id;
          break;
        case 'capacitador':
          this.formData.capacitacion.idCapacitador = event.id;
          break;
        case 'asistente':
          this.newAsistente.idTrabajador = event.id;
          break;
        case 'inspector':
          this.formData.inspeccionEPP.idInspector = event.id;
          break;
        case 'eppTrabajador':
          this.newEPPDetalle.idTrabajador = event.id;
          break;
        case 'supervisor':
          this.formData.inspeccionHerramienta.idSupervisor = event.id;
          break;
      }
    }
  }

  onRolSelected(event: any): void {
    if (event?.id) {
      this.newParticipante.idRol = event.id;
    }
  }

  onTareaSelected(event: any): void {
    if (event?.id) {
      this.newRiesgo.idTarea = event.id;
    }
  }

  onPeligroSelected(event: any): void {
    if (event?.id) {
      this.newRiesgo.idPeligro = event.id;
    }
  }

  onRiesgoSelected(event: any): void {
    if (event?.id) {
      this.newRiesgo.idRiesgo = event.id;
    }
  }

  onMedidaSelected(event: any): void {
    if (event?.id) {
      this.newRiesgo.idMedida = event.id;
    }
  }

  onEPPItemSelected(event: any): void {
    if (event?.id) {
      this.newEPPDetalle.idEPP = event.id;
    }
  }

  onHerramientaSelected(event: any): void {
    if (event?.id) {
      this.newHerramientaDetalle.idHerramienta = event.id;
    }
  }

  // Métodos helper
  getTrabajadorNombre(id: number): string {
    const trabajador = this.trabajadores.find(t => t.idTrabajador === id);
    return trabajador ? `${trabajador.nombres} ${trabajador.apellidos}` : 'No especificado';
  }

  getTrabajoNombre(id: number): string {
    const trabajo = this.trabajos.find(t => t.idTrabajo === id);
    return trabajo ? trabajo.nombre : 'No especificado';
  }

  getEPPNombre(id: number): string {
    const epp = this.epps.find(e => e.idEPP === id);
    return epp ? epp.nombre : 'No especificado';
  }

  getRolNombre(id: number): string {
    const rol = this.roles.find(r => r.id === id);
    return rol ? rol.nombre : 'No especificado';
  }

  getHerramientaNombre(id: number): string {
    const herramienta = this.herramientas.find(h => h.idHerramienta === id);
    return herramienta ? herramienta.nombre : 'No especificado';
  }

  // Manejo de fotos
  onFotoSeleccionada(event: any, index: number): void {
    const file = event.target.files[0];
    if (file) {
      // Aquí iría la lógica para subir la foto y obtener la URL
      // Por ahora, simulamos una URL
      this.formData.inspeccionHerramienta.detalles[index].fotoUrl = `assets/herramientas/${file.name}`;
    }
  }

  // Métodos para PETAR
  addPETARRespuesta(idPregunta: number, respuesta: boolean): void {
    const existingIndex = this.formData.petar.respuestas.findIndex(r => r.idPregunta === idPregunta);

    if (existingIndex >= 0) {
      this.formData.petar.respuestas[existingIndex].respuesta = respuesta;
    } else {
      this.formData.petar.respuestas.push({ idPregunta, respuesta });
    }
  }

  getPETARRespuesta(idPregunta: number): boolean | null {
    const respuesta = this.formData.petar.respuestas.find(r => r.idPregunta === idPregunta);
    return respuesta ? respuesta.respuesta : null;
  }

  onTrabajadorAutorizadoSelected(event: any): void {
    if (event?.ids) {
      this.formData.petar.trabajadoresAutorizadosIds = event.ids;
    }
  }

  // Validación y guardado
  validateForm(): boolean {
    // Validaciones básicas
    if (!this.formData.empresa.trim()) return false;
    if (!this.formData.lugarTrabajo.trim()) return false;
    if (!this.formData.ats.idTrabajo) return false;
    if (this.formData.ats.participantes.length === 0) return false;

    return true;
  }

  onSubmit(): void {
    if (this.validateForm()) {
      this.save.emit(this.formData);
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}

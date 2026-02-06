import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-ssoma-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ssoma-form.component.html',
  styleUrls: ['./ssoma-form.component.css']
})
export class SsomaFormComponent implements OnInit, OnDestroy {
  ssomaForm!: FormGroup;
  activeTab: string = 'ats';
  submitting = false;

  // Datos de prueba
  ots = [
    { id: 1, nombre: 'OT-2025-001 - Planta A' },
    { id: 2, nombre: 'OT-2025-002 - Línea 3' },
    { id: 3, nombre: 'OT-2025-003 - Mantenimiento' }
  ];

  trabajadores = [
    { id: 1, nombre: 'Juan Pérez' },
    { id: 2, nombre: 'María Gómez' },
    { id: 3, nombre: 'Carlos Ruiz' },
    { id: 4, nombre: 'Ana Torres' },
    { id: 5, nombre: 'Luis Mendoza' }
  ];

  epps = [
    { id: 1, nombre: 'Casco' },
    { id: 2, nombre: 'Guantes dieléctricos' },
    { id: 3, nombre: 'Botas de seguridad' },
    { id: 4, nombre: 'Lentes de protección' },
    { id: 5, nombre: 'Arnés' }
  ];

  // Control del modal de cámara
  showCameraModal = false;
  currentSection: 'participantes' | 'asistentes' | 'detallesEpp' | null = null;
  currentIndex: number = -1;
  fotoPreview: string | null = null;
  private videoStream: MediaStream | null = null;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.initForm();
  }

  ngOnDestroy(): void {
    this.stopCamera();
  }

  initForm(): void {
    this.ssomaForm = this.fb.group({
      idOts: [null, Validators.required],

      ats: this.fb.group({
        empresa: ['', Validators.required],
        lugarTrabajo: ['', Validators.required],
        coordenadas: ['', Validators.required],
        participantes: this.fb.array([]),
        eppIds: this.fb.array<number>([])
      }),

      capacitacion: this.fb.group({
        tema: ['', Validators.required],
        idCapacitador: [null, Validators.required],
        asistentes: this.fb.array([])
      }),

      inspeccionEpp: this.fb.group({
        idInspector: [null, Validators.required],
        detalles: this.fb.array([])
      }),

      inspeccionHerramienta: this.fb.group({
        detalles: this.fb.array([])
      }),

      petar: this.fb.group({
        requiereEvaluacionAmbiente: [false],
        aperturaLineaEquipos: [false],
        horaInicio: ['08:00'],
        idBrigadista: [null],
        idResponsableTrabajo: [null],
        respuestas: this.fb.array([]),
        trabajadoresAutorizadosIds: this.fb.array([])
      })
    });

    this.addParticipante();
    this.addAsistente();
    this.addDetalleEpp();
    this.addDetalleHerramienta();
    this.addTrabajadorAutorizadoPetar();
    this.inicializarRespuestasPetar();
  }

  // Getters tipados
  get atsForm() { return this.ssomaForm.get('ats') as FormGroup; }
  get capacitacionForm() { return this.ssomaForm.get('capacitacion') as FormGroup; }
  get inspeccionEppForm() { return this.ssomaForm.get('inspeccionEpp') as FormGroup; }
  get inspeccionHerramientaForm() { return this.ssomaForm.get('inspeccionHerramienta') as FormGroup; }
  get petarForm() { return this.ssomaForm.get('petar') as FormGroup; }

  get participantesArray() { return this.atsForm.get('participantes') as FormArray; }
  get eppIdsArray() { return this.atsForm.get('eppIds') as FormArray; }
  get asistentesArray() { return this.capacitacionForm.get('asistentes') as FormArray; }
  get detallesEppArray() { return this.inspeccionEppForm.get('detalles') as FormArray; }
  get detallesHerramientaArray() { return this.inspeccionHerramientaForm.get('detalles') as FormArray; }
  get respuestasPetarArray() { return this.petarForm.get('respuestas') as FormArray; }
  get autorizadosPetarArray() { return this.petarForm.get('trabajadoresAutorizadosIds') as FormArray; }

  // ─────────────────────────────────────────────
  // Métodos agregar / quitar
  // ─────────────────────────────────────────────

  addParticipante() {
    this.participantesArray.push(this.fb.group({
      idTrabajador: [null, Validators.required],
      fotoBase64: [null as string | null]
    }));
  }

  removeParticipante(index: number) {
    if (this.participantesArray.length > 1) this.participantesArray.removeAt(index);
  }

  addAsistente() {
    this.asistentesArray.push(this.fb.group({
      idTrabajador: [null, Validators.required],
      fotoBase64: [null as string | null]
    }));
  }

  removeAsistente(index: number) {
    if (this.asistentesArray.length > 1) this.asistentesArray.removeAt(index);
  }

  addDetalleEpp() {
    this.detallesEppArray.push(this.fb.group({
      idTrabajador: [null, Validators.required],
      idEpp: [null, Validators.required],
      cumple: [true],
      fotoBase64: [null as string | null]
    }));
  }

  removeDetalleEpp(index: number) {
    if (this.detallesEppArray.length > 1) this.detallesEppArray.removeAt(index);
  }

  addDetalleHerramienta() {
    this.detallesHerramientaArray.push(this.fb.group({
      nombreHerramienta: ['', Validators.required],
      observacion: ['']
    }));
  }

  removeDetalleHerramienta(index: number) {
    if (this.detallesHerramientaArray.length > 1) this.detallesHerramientaArray.removeAt(index);
  }

  private inicializarRespuestasPetar() {
    const preguntas = [1, 2, 3, 4];
    preguntas.forEach(id => {
      this.respuestasPetarArray.push(this.fb.group({
        idPregunta: id,
        respuesta: true,
        observacion: ''
      }));
    });
  }

  addTrabajadorAutorizadoPetar() {
    this.autorizadosPetarArray.push(this.fb.control(null, Validators.required));
  }

  removeTrabajadorAutorizadoPetar(index: number) {
    if (this.autorizadosPetarArray.length > 1) this.autorizadosPetarArray.removeAt(index);
  }

  // ─────────────────────────────────────────────
  // Cámara
  // ─────────────────────────────────────────────

  openCamera(section: 'participantes' | 'asistentes' | 'detallesEpp', index: number) {
    this.currentSection = section;
    this.currentIndex = index;
    this.fotoPreview = null;
    this.showCameraModal = true;

    setTimeout(() => {
      const video = document.getElementById('cameraVideo') as HTMLVideoElement;
      if (video) {
        navigator.mediaDevices.getUserMedia({ video: { facingMode: 'user' } })
          .then(stream => {
            this.videoStream = stream;
            video.srcObject = stream;
            video.play();
          })
          .catch(err => {
            console.error('Error al acceder a la cámara', err);
            Swal.fire('Error', 'No se pudo abrir la cámara. Verifica permisos.', 'error');
            this.closeCameraModal();
          });
      }
    }, 200);
  }

  capturePhoto() {
    const video = document.getElementById('cameraVideo') as HTMLVideoElement;
    const canvas = document.getElementById('photoCanvas') as HTMLCanvasElement;

    if (!video || !canvas) return;

    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;

    const ctx = canvas.getContext('2d');
    if (ctx) {
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
      this.fotoPreview = canvas.toDataURL('image/jpeg', 0.85);
    }
  }

  savePhoto() {
    if (!this.fotoPreview || this.currentIndex < 0 || !this.currentSection) return;

    let targetArray: FormArray;

    switch (this.currentSection) {
      case 'participantes':
        targetArray = this.participantesArray;
        break;
      case 'asistentes':
        targetArray = this.asistentesArray;
        break;
      case 'detallesEpp':
        targetArray = this.detallesEppArray;
        break;
      default:
        return;
    }

    const group = targetArray.at(this.currentIndex) as FormGroup;
    group.patchValue({ fotoBase64: this.fotoPreview });

    Swal.fire('Foto guardada', 'La foto se ha asignado correctamente', 'success');
    this.closeCameraModal();
  }

  closeCameraModal() {
    this.stopCamera();
    this.showCameraModal = false;
    this.currentSection = null;
    this.currentIndex = -1;
    this.fotoPreview = null;
  }

  private stopCamera() {
    if (this.videoStream) {
      this.videoStream.getTracks().forEach(track => track.stop());
      this.videoStream = null;
    }
  }

  getFoto(section: string, index: number): string | null {
    let array: FormArray | null = null;

    if (section === 'participantes') array = this.participantesArray;
    if (section === 'asistentes') array = this.asistentesArray;
    if (section === 'detallesEpp') array = this.detallesEppArray;

    if (!array) return null;

    const group = array.at(index) as FormGroup;
    return group?.get('fotoBase64')?.value || null;
  }

  // ─────────────────────────────────────────────
  // Toggle EPP (corregido tipado)
  // ─────────────────────────────────────────────

  toggleEpp(id: number) {
    const array = this.eppIdsArray;
    const index = array.controls.findIndex((c: any) => c.value === id);
    if (index > -1) {
      array.removeAt(index);
    } else {
      array.push(this.fb.control(id));
    }
  }

  isEppSelected(id: number): boolean {
    return this.eppIdsArray.controls.some((c: any) => c.value === id);
  }

  // ─────────────────────────────────────────────
  // Navegación y submit (de prueba)
  // ─────────────────────────────────────────────

  setTab(tab: string) {
    this.activeTab = tab;
  }

  onSubmit() {
    if (this.ssomaForm.invalid) {
      this.ssomaForm.markAllAsTouched();
      Swal.fire('Faltan campos', 'Complete los obligatorios', 'warning');
      return;
    }

    console.log('Formulario completo (prueba):', this.ssomaForm.value);
    Swal.fire('Formulario válido', 'Revisa la consola para ver los datos (incluye fotos en base64)', 'info');
  }
}

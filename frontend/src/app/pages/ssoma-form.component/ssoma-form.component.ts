import { Component, OnInit, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { 
  SsomaService, 
  SsomaRequestDTO,
  ParticipanteRequestDTO,
  SecuenciaTareaRequestDTO,
  ChecklistSeguridadRequestDTO,
  EppCheckRequestDTO,
  CharlaRequestDTO,
  InspeccionTrabajadorRequestDTO,
  PetarRequestDTO
} from '../../service/ssoma.service';
import { DropdownService } from '../../service/dropdown.service';

@Component({
  selector: 'app-ssoma-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ssoma-form.component.html'
})
export class SsomaFormularioComponent implements OnInit, OnDestroy {
  @ViewChild('videoElement') videoElement!: ElementRef;
  @ViewChild('canvasElement') canvasElement!: ElementRef;
  @ViewChild('videoRecorder') videoRecorder!: ElementRef<HTMLVideoElement>;
  
  // Formulario principal
  formulario: SsomaRequestDTO;
  
  // Estado de cámara
  isCameraActive = false;
  isTakingPhoto = false;
  stream: MediaStream | null = null;
  currentPhotoType: 'participante' | 'epp' | 'herramienta' | 'charla' = 'participante';
  currentPhotoIndex = 0;
  
  // Estado de video
  mediaRecorder: MediaRecorder | null = null;
  recordedChunks: Blob[] = [];
  isRecording = false;
  recordingTimer: any;
  recordingSeconds = 0;
  recordingInterval: any;
  recordingTimerText = '00:00';
  showRecordingIndicator = false;
  
  // Dropdowns
  empresas: any[] = [];
  trabajadores: any[] = [];
  temasCharla: any[] = [];
  herramientasMaestras: any[] = [];
  
  // Arrays para controles
  checklistItems = [
    'Casco de seguridad',
    'Zapatos de seguridad',
    'Chaleco reflectivo',
    'Guantes de protección',
    'Lentes de seguridad',
    'Arnés de seguridad',
    'Mascarilla',
    'Otros'
  ];
  
  eppItems = [
    'Casco',
    'Lentes',
    'Orejeras',
    'Guantes',
    'Botas',
    'Arnés',
    'Chaleco',
    'Mascarilla'
  ];
  
  // Estado de pestañas
  activeTab = 'hoja1';
  
  // Métodos auxiliares
  formatRecordingTime(seconds: number): string {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  }
  
  constructor(
    private ssomaService: SsomaService,
    private dropdownService: DropdownService
  ) {
    this.formulario = this.crearFormularioVacio();
  }
  
  ngOnDestroy(): void {
    this.detenerCamara();
    this.detenerGrabacionVideo();
    
    // Limpiar todos los timers
    if (this.recordingTimer) {
      clearInterval(this.recordingTimer);
    }
    if (this.recordingInterval) {
      clearInterval(this.recordingInterval);
    }
  }
  
  ngOnInit(): void {
    this.inicializarFormulario();
    this.cargarDropdowns();
  }
  
  // Inicializar formulario con datos automáticos
  inicializarFormulario(): void {
    const ahora = new Date();
    
    // Fecha y hora automáticas (no editables)
    this.formulario.fecha = ahora;
    this.formulario.horaInicio = this.formatTime(ahora);
    this.formulario.horaFin = this.formatTime(new Date(ahora.getTime() + 8 * 60 * 60 * 1000));
    this.formulario.horaInicioTrabajo = this.formatTime(new Date(ahora.getTime() + 30 * 60 * 1000));
    this.formulario.horaFinTrabajo = this.formatTime(new Date(ahora.getTime() + 8 * 60 * 60 * 1000 - 30 * 60 * 1000));
    
    // Inicializar charla con datos automáticos
    this.formulario.charla = {
      temaId: undefined,
      fechaCharla: ahora,
      duracionHoras: 0.5,
      capacitadorId: undefined
    };
  }
  
  formatTime(date: Date): string {
    return date.toTimeString().split(' ')[0].substring(0, 5);
  }
  
  crearFormularioVacio(): SsomaRequestDTO {
    return {
      idOts: 0,
      participantes: [this.crearParticipanteVacio()],
      secuenciasTarea: [this.crearSecuenciaVacia()],
      checklistSeguridad: this.checklistItems.map(item => ({
        itemNombre: item,
        usado: false,
        observaciones: ''
      })),
      eppChecks: this.eppItems.map(item => ({
        eppNombre: item,
        usado: false
      })),
      inspeccionesTrabajador: [this.crearInspeccionVacia()],
      herramientasInspeccion: [{
        herramientaMaestraId: undefined,
        herramientaNombre: '',
        p1: false, p2: false, p3: false, p4: false,
        p5: false, p6: false, p7: false, p8: false,
        observaciones: ''
      }]
    };
  }
  
  crearParticipanteVacio(): ParticipanteRequestDTO {
    return {
      trabajadorId: undefined,
      nombre: '',
      cargo: '',
      firma: undefined
    };
  }
  
  crearSecuenciaVacia(): SecuenciaTareaRequestDTO {
    return {
      secuenciaTarea: '',
      peligro: '',
      riesgo: '',
      consecuencias: '',
      medidasControl: '',
      orden: 1
    };
  }
  
  crearInspeccionVacia(): InspeccionTrabajadorRequestDTO {
    return {
      tipoInspeccion: 'PLANIFICADA',
      trabajadorId: undefined,
      trabajadorNombre: '',
      casco: false, lentes: false, orejeras: false, tapones: false,
      guantes: false, botas: false, arnes: false, chaleco: false,
      mascarilla: false, gafas: false,
      otros: '',
      accionCorrectiva: '',
      seguimiento: '',
      responsableId: undefined
    };
  }
  
  cargarDropdowns(): void {
    this.dropdownService.getEmpresas().subscribe({
      next: (data) => this.empresas = data,
      error: (err) => console.error('Error cargando empresas:', err)
    });
    
    this.dropdownService.getTrabajadores().subscribe({
      next: (data) => this.trabajadores = data,
      error: (err) => console.error('Error cargando trabajadores:', err)
    });
    
    // Datos ficticios
    this.temasCharla = [
      { id: 1, label: 'Prevención de Riesgos' },
      { id: 2, label: 'Uso de EPP' },
      { id: 3, label: 'Procedimientos de Trabajo Seguro' },
      { id: 4, label: 'Emergencias y Evacuación' }
    ];
    
    this.herramientasMaestras = [
      { id: 1, label: 'Martillo' },
      { id: 2, label: 'Destornillador' },
      { id: 3, label: 'Llave Inglesa' },
      { id: 4, label: 'Alicate' },
      { id: 5, label: 'Sierra' }
    ];
  }
  
  // Métodos para controles dinámicos
  agregarParticipante(): void {
    this.formulario.participantes.push(this.crearParticipanteVacio());
  }
  
  eliminarParticipante(index: number): void {
    if (this.formulario.participantes.length > 1) {
      this.formulario.participantes.splice(index, 1);
    }
  }
  
  agregarSecuencia(): void {
    const nuevaSecuencia = this.crearSecuenciaVacia();
    nuevaSecuencia.orden = this.formulario.secuenciasTarea.length + 1;
    this.formulario.secuenciasTarea.push(nuevaSecuencia);
  }
  
  eliminarSecuencia(index: number): void {
    if (this.formulario.secuenciasTarea.length > 1) {
      this.formulario.secuenciasTarea.splice(index, 1);
      this.formulario.secuenciasTarea.forEach((sec, i) => sec.orden = i + 1);
    }
  }
  
  agregarInspeccion(): void {
    this.formulario.inspeccionesTrabajador.push(this.crearInspeccionVacia());
  }
  
  eliminarInspeccion(index: number): void {
    if (this.formulario.inspeccionesTrabajador.length > 1) {
      this.formulario.inspeccionesTrabajador.splice(index, 1);
    }
  }
  
  agregarHerramienta(): void {
    this.formulario.herramientasInspeccion.push({
      herramientaMaestraId: undefined,
      herramientaNombre: '',
      p1: false, p2: false, p3: false, p4: false,
      p5: false, p6: false, p7: false, p8: false,
      observaciones: ''
    });
  }
  
  eliminarHerramienta(index: number): void {
    if (this.formulario.herramientasInspeccion.length > 1) {
      this.formulario.herramientasInspeccion.splice(index, 1);
    }
  }
  
  async iniciarCamara(tipo: 'participante' | 'epp' | 'herramienta' | 'charla', index: number = 0): Promise<void> {
    if (this.isRecording) {
      this.detenerGrabacionVideo();
    }
    
    this.currentPhotoType = tipo;
    this.currentPhotoIndex = index;
    this.isTakingPhoto = true;
    
    try {
      if (this.stream) {
        this.detenerCamara();
      }
      
      this.stream = await navigator.mediaDevices.getUserMedia({ 
        video: { 
          width: { ideal: 640 },
          height: { ideal: 480 },
          facingMode: 'environment'
        },
        audio: false 
      });
      
      if (this.videoElement) {
        this.videoElement.nativeElement.srcObject = this.stream;
        this.videoElement.nativeElement.play();
        this.isCameraActive = true;
      }
      
      Swal.fire({
        title: '📸 CÁMARA ACTIVA',
        html: `
          <div class="text-center">
            <div class="mb-3" style="position: relative;">
              <video id="liveCamera" autoplay playsinline 
                     style="width: 100%; max-height: 400px; border-radius: 10px; border: 3px solid #dc3545;"></video>
              <div style="position: absolute; top: 10px; left: 10px; background: rgba(220, 53, 69, 0.8); color: white; padding: 5px 10px; border-radius: 5px;">
                <i class="fas fa-circle fa-beat" style="color: #fff;"></i> CÁMARA ACTIVA
              </div>
            </div>
            <p class="text-danger fw-bold">Posicione para tomar la foto</p>
            <div class="d-grid gap-2">
              <button id="btnTakePhoto" class="btn btn-success btn-lg">
                <i class="fas fa-camera"></i> TOMAR FOTO AHORA
              </button>
              <button id="btnCancel" class="btn btn-outline-secondary">
                <i class="fas fa-times"></i> Cancelar
              </button>
            </div>
          </div>
        `,
        showConfirmButton: false,
        allowOutsideClick: false,
        didOpen: () => {
          const videoEl = document.getElementById('liveCamera') as HTMLVideoElement;
          if (videoEl && this.stream) {
            videoEl.srcObject = this.stream;
            videoEl.play();
          }
          
          const btnTakePhoto = document.getElementById('btnTakePhoto');
          const btnCancel = document.getElementById('btnCancel');
          
          if (btnTakePhoto) {
            btnTakePhoto.onclick = () => {
              this.tomarFoto();
              Swal.close();
            };
          }
          
          if (btnCancel) {
            btnCancel.onclick = () => {
              this.detenerCamara();
              Swal.close();
            };
          }
        }
      });
      
    } catch (error: any) {
      Swal.fire({
        icon: 'error',
        title: 'Error de cámara',
        text: 'No se pudo acceder a la cámara: ' + error.message,
        confirmButtonText: 'Aceptar'
      });
      this.isTakingPhoto = false;
    }
  }
  
  tomarFoto(): void {
    if (!this.stream || !this.videoElement || !this.canvasElement) {
      Swal.fire('Error', 'Cámara no disponible', 'error');
      return;
    }
    
    // Efecto de flash
    document.body.style.backgroundColor = '#ffffff';
    setTimeout(() => {
      document.body.style.backgroundColor = '';
    }, 100);
    
    const video = this.videoElement.nativeElement;
    const canvas = this.canvasElement.nativeElement;
    const context = canvas.getContext('2d');
    
    if (!context) {
      Swal.fire('Error', 'No se pudo acceder al contexto del canvas', 'error');
      return;
    }
    
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    context.drawImage(video, 0, 0, canvas.width, canvas.height);
    
    canvas.toBlob((blob: Blob | null) => {
      if (blob) {
        const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
        const fileName = `foto-${this.currentPhotoType}-${timestamp}.jpg`;
        const file = new File([blob], fileName, { type: 'image/jpeg', lastModified: Date.now() });
        
        this.procesarFotoTomada(file);
        
        const imageUrl = URL.createObjectURL(blob);
        Swal.fire({
          title: '📸 ¡FOTO TOMADA!',
          html: `
            <div class="text-center">
              <img src="${imageUrl}" style="max-width: 100%; max-height: 300px; border-radius: 10px; border: 3px solid #28a745;">
              <div class="mt-3">
                <p class="text-success fw-bold">
                  <i class="fas fa-check-circle"></i> Foto guardada correctamente
                </p>
                <p>Tipo: ${this.currentPhotoType.toUpperCase()}</p>
              </div>
            </div>
          `,
          showConfirmButton: true,
          confirmButtonText: 'Aceptar',
          timer: 3000
        }).then(() => {
          URL.revokeObjectURL(imageUrl);
          this.detenerCamara();
        });
      }
    }, 'image/jpeg', 0.95);
  }
  
  procesarFotoTomada(file: File): void {
    switch (this.currentPhotoType) {
      case 'participante':
        if (!this.formulario.fotosParticipantes) {
          this.formulario.fotosParticipantes = [];
        }
        this.formulario.fotosParticipantes.push({
          participanteIndex: this.currentPhotoIndex,
          foto: file,
          tipoFoto: 'FRONTAL'
        });
        break;
        
      case 'epp':
        if (!this.formulario.fotosEpp) {
          this.formulario.fotosEpp = [];
        }
        this.formulario.fotosEpp.push({
          eppIndex: this.currentPhotoIndex,
          foto: file
        });
        break;
        
      case 'herramienta':
        if (!this.formulario.fotosHerramientas) {
          this.formulario.fotosHerramientas = [];
        }
        this.formulario.fotosHerramientas.push({
          herramientaIndex: this.currentPhotoIndex,
          foto: file
        });
        break;
        
      case 'charla':
        if (this.currentPhotoIndex < this.formulario.checklistSeguridad.length) {
          this.formulario.checklistSeguridad[this.currentPhotoIndex].foto = file;
        }
        break;
    }
    
    this.isTakingPhoto = false;
    this.detenerCamara();
  }
  
  detenerCamara(): void {
    if (this.stream) {
      this.stream.getTracks().forEach(track => track.stop());
      this.stream = null;
    }
    this.isCameraActive = false;
    this.isTakingPhoto = false;
  }
  
  async grabarVideoCharla(): Promise<void> {
    try {
      this.detenerCamara();
      
      Swal.fire({
        title: '🎥 PREPARANDO GRABACIÓN',
        text: 'Iniciando cámara y micrófono...',
        icon: 'info',
        showConfirmButton: false,
        timer: 1500
      });
      
      const stream = await navigator.mediaDevices.getUserMedia({ 
        video: { 
          width: { ideal: 1280 },
          height: { ideal: 720 },
          facingMode: 'environment'
        }, 
        audio: true 
      });
      
      this.mediaRecorder = new MediaRecorder(stream, {
        mimeType: 'video/webm; codecs=vp9'
      });
      
      this.recordedChunks = [];
      this.isRecording = true;
      this.recordingSeconds = 0;
      this.showRecordingIndicator = true;
      
      this.mediaRecorder.ondataavailable = (event: BlobEvent) => {
        if (event.data && event.data.size > 0) {
          this.recordedChunks.push(event.data);
        }
      };
      
      this.mediaRecorder.onstop = () => {
        this.finalizarGrabacionVideo(stream);
      };
      
      this.mediaRecorder.start(1000);
      
      this.recordingInterval = setInterval(() => {
        this.recordingSeconds++;
        this.recordingTimerText = this.formatRecordingTime(this.recordingSeconds);
        
        // Detener automáticamente después de 30 segundos
        if (this.recordingSeconds >= 30) {
          this.detenerGrabacionVideo();
        }
      }, 1000);
      
      Swal.fire({
        title: '🎬 GRABANDO VIDEO...',
        html: `
          <div class="text-center">
            <div class="mb-3">
              <div class="recording-indicator">
                <i class="fas fa-circle fa-beat text-danger fa-3x"></i>
              </div>
              <h1 class="text-danger mt-3">${this.recordingTimerText}</h1>
              <p class="text-muted">Habla frente a la cámara (máximo 30 segundos)</p>
              <div class="progress" style="height: 10px;">
                <div class="progress-bar bg-danger progress-bar-striped progress-bar-animated" 
                     role="progressbar" style="width: ${(this.recordingSeconds / 30) * 100}%">
                </div>
              </div>
            </div>
            <button id="btnStopRecording" class="btn btn-danger btn-lg mt-3">
              <i class="fas fa-stop"></i> DETENER GRABACIÓN
            </button>
          </div>
        `,
        showConfirmButton: false,
        allowOutsideClick: false,
        didOpen: () => {
          const btnStop = document.getElementById('btnStopRecording');
          if (btnStop) {
            btnStop.onclick = () => this.detenerGrabacionVideo();
          }
        }
      });
      
    } catch (error: any) {
      Swal.fire({
        icon: 'error',
        title: 'Error de cámara',
        text: 'No se pudo acceder a la cámara/micrófono: ' + error.message,
        confirmButtonText: 'Aceptar'
      });
    }
  }
  
  detenerGrabacionVideo(): void {
    if (this.mediaRecorder && this.isRecording) {
      this.mediaRecorder.stop();
      this.isRecording = false;
      this.showRecordingIndicator = false;
      clearInterval(this.recordingInterval);
      clearInterval(this.recordingTimer);
      Swal.close();
    }
  }
  
  private finalizarGrabacionVideo(stream: MediaStream): void {
    const blob = new Blob(this.recordedChunks, { type: 'video/webm' });
    const file = new File([blob], `video-charla-${Date.now()}.webm`, { 
      type: 'video/webm' 
    });
    
    this.formulario.videoCharla = {
      video: file,
      duracionSegundos: this.recordingSeconds
    };
    
    Swal.fire({
      icon: 'success',
      title: '✅ VIDEO GRABADO',
      html: `
        <div class="text-center">
          <i class="fas fa-check-circle fa-3x text-success mb-3"></i>
          <p>Video grabado correctamente</p>
          <p><strong>Duración:</strong> ${this.recordingSeconds} segundos</p>
        </div>
      `,
      timer: 3000,
      showConfirmButton: false
    });
    
    stream.getTracks().forEach(track => track.stop());
    this.isRecording = false;
    this.showRecordingIndicator = false;
  }
  
  async tomarFirma(index: number): Promise<void> {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ video: true });
      
      Swal.fire({
        title: '✍️ TOMAR FIRMA',
        text: 'Coloque la firma frente a la cámara',
        showCancelButton: true,
        confirmButtonText: 'Tomar Foto',
        cancelButtonText: 'Cancelar'
      }).then(async (result) => {
        if (result.isConfirmed) {
          const video = document.createElement('video');
          video.srcObject = stream;
          video.play();
          
          await new Promise(resolve => setTimeout(resolve, 1000));
          
          const canvas = document.createElement('canvas');
          canvas.width = video.videoWidth;
          canvas.height = video.videoHeight;
          const ctx = canvas.getContext('2d');
          
          if (ctx) {
            ctx.drawImage(video, 0, 0);
            canvas.toBlob(blob => {
              if (blob) {
                const file = new File([blob], `firma-${index}.jpg`, { type: 'image/jpeg' });
                this.formulario.participantes[index].firma = file;
                Swal.fire('Éxito', 'Firma digital capturada', 'success');
              }
            }, 'image/jpeg');
          }
        }
        
        stream.getTracks().forEach(track => track.stop());
      });
      
    } catch (error) {
      Swal.fire('Error', 'No se pudo acceder a la cámara', 'error');
    }
  }
  
  inicializarPetar(): void {
    if (!this.formulario.petar) {
      this.formulario.petar = {
        energiaPeligrosa: false,
        trabajoAltura: false,
        izaje: false,
        excavacion: false,
        espaciosConfinados: false,
        trabajoCaliente: false,
        otros: false,
        otrosDescripcion: '',
        velocidadAire: '',
        contenidoOxigeno: '',
        horaInicioPetar: this.formulario.horaInicioTrabajo,
        horaFinPetar: this.formulario.horaFinTrabajo,
        respuestas: [
          { preguntaId: 1, respuesta: false, observaciones: '' },
          { preguntaId: 2, respuesta: false, observaciones: '' },
          { preguntaId: 3, respuesta: false, observaciones: '' },
          { preguntaId: 4, respuesta: false, observaciones: '' }
        ],
        equiposProteccion: [
          { equipoNombre: 'Casco', usado: false },
          { equipoNombre: 'Lentes', usado: false },
          { equipoNombre: 'Guantes', usado: false },
          { equipoNombre: 'Botas', usado: false },
          { equipoNombre: 'Arnés', usado: false }
        ]
      };
    }
  }
  
  agregarEquipoProteccion(): void {
    this.inicializarPetar();
    if (this.formulario.petar) {
      this.formulario.petar.equiposProteccion.push({
        equipoNombre: '',
        usado: false
      });
    }
  }
  
  eliminarEquipoProteccion(index: number): void {
    if (this.formulario.petar?.equiposProteccion && this.formulario.petar.equiposProteccion.length > 1) {
      this.formulario.petar.equiposProteccion.splice(index, 1);
    }
  }
  
  validarFormulario(): boolean {
    if (!this.formulario.idOts || this.formulario.idOts <= 0) {
      Swal.fire('Error', 'Debe ingresar un número de OT válido', 'error');
      return false;
    }
    
    if (!this.formulario.empresaId) {
      Swal.fire('Error', 'Debe seleccionar una empresa', 'error');
      return false;
    }
    
    return true;
  }
  
  guardarFormulario(): void {
    if (!this.validarFormulario()) return;
    
    Swal.fire({
      title: '¿Guardar formulario SSOMA?',
      text: 'Se guardarán todas las 5 hojas del formulario',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, guardar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.enviarFormulario();
      }
    });
  }
  
  enviarFormulario(): void {
    Swal.fire({
      title: 'Guardando...',
      allowOutsideClick: false,
      didOpen: () => Swal.showLoading()
    });
    
    this.ssomaService.crearFormularioCompleto(this.formulario).subscribe({
      next: (response) => {
        Swal.fire({
          icon: 'success',
          title: '¡Éxito!',
          text: `Formulario SSOMA creado con ID: ${response.idSsoma}`,
          confirmButtonText: 'Aceptar'
        });
        
        this.formulario = this.crearFormularioVacio();
        this.inicializarFormulario();
        this.detenerCamara();
      },
      error: (error) => {
        console.error('Error:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo guardar el formulario',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }
  
  get charlaTemaId(): number | undefined {
    return this.formulario.charla?.temaId;
  }
  
  set charlaTemaId(value: number | undefined) {
    if (!this.formulario.charla) {
      this.formulario.charla = {
        temaId: undefined,
        fechaCharla: new Date(),
        duracionHoras: 0.5,
        capacitadorId: undefined
      };
    }
    this.formulario.charla.temaId = value;
  }
  
  get charlaCapacitadorId(): number | undefined {
    return this.formulario.charla?.capacitadorId;
  }
  
  set charlaCapacitadorId(value: number | undefined) {
    if (!this.formulario.charla) {
      this.formulario.charla = {
        temaId: undefined,
        fechaCharla: new Date(),
        duracionHoras: 0.5,
        capacitadorId: undefined
      };
    }
    this.formulario.charla.capacitadorId = value;
  }
  
  cambiarTab(tab: string): void {
    this.activeTab = tab;
    this.detenerCamara();
    
    if (tab === 'hoja5' && !this.formulario.petar) {
      this.inicializarPetar();
    }
  }
  // Métodos para manejar la fecha de la charla
getCharlaFecha(): string {
  if (!this.formulario.charla?.fechaCharla) {
    return '';
  }
  const date = new Date(this.formulario.charla.fechaCharla);
  return date.toISOString().split('T')[0];
}

setCharlaFecha(event: Event): void {
  const input = event.target as HTMLInputElement;
  const fechaValue = input.value;
  
  if (!this.formulario.charla) {
    this.formulario.charla = {
      temaId: undefined,
      fechaCharla: new Date(),
      duracionHoras: 0.5,
      capacitadorId: undefined
    };
  }
  
  if (fechaValue) {
    this.formulario.charla.fechaCharla = new Date(fechaValue);
  }
}

// Métodos para manejar la duración de la charla
getCharlaDuracion(): number {
  return this.formulario.charla?.duracionHoras || 0.5;
}

setCharlaDuracion(value: number | null): void {
  if (!this.formulario.charla) {
    this.formulario.charla = {
      temaId: undefined,
      fechaCharla: new Date(),
      duracionHoras: 0.5,
      capacitadorId: undefined
    };
  }
  
  this.formulario.charla.duracionHoras = value || 0.5;
}
  mostrarPreviewFoto(imageUrl: string, tipo: string): void {
    Swal.fire({
      title: `Foto de ${tipo} tomada`,
      imageUrl: imageUrl,
      imageAlt: `Foto de ${tipo}`,
      showConfirmButton: true,
      confirmButtonText: 'Aceptar',
      imageWidth: 400,
      imageHeight: 300
    }).then(() => {
      URL.revokeObjectURL(imageUrl);
    });
  }
  
  async verificarPermisosCamara(): Promise<boolean> {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ video: true });
      stream.getTracks().forEach(track => track.stop());
      return true;
    } catch {
      Swal.fire({
        icon: 'warning',
        title: 'Permisos de cámara',
        text: 'Por favor habilite los permisos de cámara en su navegador',
        confirmButtonText: 'Entendido'
      });
      return false;
    }
  }
}
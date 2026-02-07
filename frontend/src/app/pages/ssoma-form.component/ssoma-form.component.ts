import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
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
export class SsomaFormularioComponent implements OnInit {
  @ViewChild('videoElement') videoElement!: ElementRef;
  @ViewChild('canvasElement') canvasElement!: ElementRef;
  
  // Formulario principal
  formulario: SsomaRequestDTO;
  
  // Estado de cámara
  isCameraActive = false;
  stream: MediaStream | null = null;
  currentPhotoType: 'participante' | 'epp' | 'herramienta' | 'charla' = 'participante';
  currentPhotoIndex = 0;
  
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
  
  constructor(
    private ssomaService: SsomaService,
    private dropdownService: DropdownService
  ) {
    this.formulario = this.crearFormularioVacio();
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
    this.formulario.horaFin = this.formatTime(new Date(ahora.getTime() + 8 * 60 * 60 * 1000)); // +8 horas
    this.formulario.horaInicioTrabajo = this.formatTime(new Date(ahora.getTime() + 30 * 60 * 1000)); // +30 min
    this.formulario.horaFinTrabajo = this.formatTime(new Date(ahora.getTime() + 8 * 60 * 60 * 1000 - 30 * 60 * 1000)); // -30 min
    
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
  
  // MÉTODOS PARA CÁMARA EN TIEMPO REAL
  async iniciarCamara(tipo: 'participante' | 'epp' | 'herramienta' | 'charla', index: number = 0): Promise<void> {
    this.currentPhotoType = tipo;
    this.currentPhotoIndex = index;
    
    try {
      this.stream = await navigator.mediaDevices.getUserMedia({ 
        video: { facingMode: 'environment' },
        audio: false 
      });
      
      if (this.videoElement) {
        this.videoElement.nativeElement.srcObject = this.stream;
        this.isCameraActive = true;
      }
    } catch (error) {
      Swal.fire('Error', 'No se pudo acceder a la cámara', 'error');
    }
  }
  
  tomarFoto(): void {
    if (!this.stream || !this.videoElement || !this.canvasElement) return;
    
    const video = this.videoElement.nativeElement;
    const canvas = this.canvasElement.nativeElement;
    const context = canvas.getContext('2d');
    
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    context.drawImage(video, 0, 0, canvas.width, canvas.height);
    
    canvas.toBlob((blob: Blob | null) => {
      if (blob) {
        const file = new File([blob], `foto-${Date.now()}.jpg`, { type: 'image/jpeg' });
        this.procesarFotoTomada(file);
      }
    }, 'image/jpeg', 0.9);
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
        Swal.fire('Éxito', 'Foto de participante tomada', 'success');
        break;
        
      case 'epp':
        if (!this.formulario.fotosEpp) {
          this.formulario.fotosEpp = [];
        }
        this.formulario.fotosEpp.push({
          eppIndex: this.currentPhotoIndex,
          foto: file
        });
        Swal.fire('Éxito', 'Foto de EPP tomada', 'success');
        break;
        
      case 'herramienta':
        if (!this.formulario.fotosHerramientas) {
          this.formulario.fotosHerramientas = [];
        }
        this.formulario.fotosHerramientas.push({
          herramientaIndex: this.currentPhotoIndex,
          foto: file
        });
        Swal.fire('Éxito', 'Foto de herramienta tomada', 'success');
        break;
        
      case 'charla':
        // Para foto de charla, guardamos en checklist
        if (this.currentPhotoIndex < this.formulario.checklistSeguridad.length) {
          this.formulario.checklistSeguridad[this.currentPhotoIndex].foto = file;
          Swal.fire('Éxito', 'Foto de checklist tomada', 'success');
        }
        break;
    }
    
    this.detenerCamara();
  }
  
  detenerCamara(): void {
    if (this.stream) {
      this.stream.getTracks().forEach(track => track.stop());
      this.stream = null;
      this.isCameraActive = false;
    }
  }
  
  // MÉTODO PARA GRABAR VIDEO
  async grabarVideoCharla(): Promise<void> {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ 
        video: true, 
        audio: true 
      });
      
      const mediaRecorder = new MediaRecorder(stream);
      const chunks: Blob[] = [];
      
      mediaRecorder.ondataavailable = (event) => {
        if (event.data.size > 0) {
          chunks.push(event.data);
        }
      };
      
      mediaRecorder.onstop = () => {
        const blob = new Blob(chunks, { type: 'video/webm' });
        const file = new File([blob], `video-charla-${Date.now()}.webm`, { type: 'video/webm' });
        
        this.formulario.videoCharla = {
          video: file,
          duracionSegundos: 30
        };
        
        Swal.fire('Éxito', 'Video de charla grabado (30 segundos)', 'success');
        
        // Detener stream
        stream.getTracks().forEach(track => track.stop());
      };
      
      // Iniciar grabación
      mediaRecorder.start();
      
      // Mostrar mensaje de grabación
      Swal.fire({
        title: 'Grabando...',
        text: 'Grabando video de 30 segundos',
        timer: 30000,
        timerProgressBar: true,
        showConfirmButton: false
      }).then(() => {
        // Detener grabación automáticamente después de 30 segundos
        mediaRecorder.stop();
      });
      
    } catch (error) {
      Swal.fire('Error', 'No se pudo acceder a la cámara/micrófono', 'error');
    }
  }
  
  // MÉTODO PARA TOMAR FIRMA (simulación con cámara)
  async tomarFirma(index: number): Promise<void> {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ video: true });
      
      Swal.fire({
        title: 'Tome una foto de la firma',
        text: 'Coloque la firma frente a la cámara y haga clic en Tomar Foto',
        showCancelButton: true,
        confirmButtonText: 'Tomar Foto',
        cancelButtonText: 'Cancelar'
      }).then(async (result) => {
        if (result.isConfirmed) {
          // Tomar foto de la firma
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
        
        // Detener stream
        stream.getTracks().forEach(track => track.stop());
      });
      
    } catch (error) {
      Swal.fire('Error', 'No se pudo acceder a la cámara', 'error');
    }
  }
  
  // Métodos para PETAR
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
  
  // Validación
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
  
  // Envío del formulario
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
        
        // Resetear formulario
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
  // En el componente TypeScript
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
  // Navegación entre pestañas
  cambiarTab(tab: string): void {
    this.activeTab = tab;
    this.detenerCamara();
    
    if (tab === 'hoja5' && !this.formulario.petar) {
      this.inicializarPetar();
    }
  }
}
import { Component, OnInit } from '@angular/core';

import Swal from 'sweetalert2';
import { HerramientaConIndice, HerramientaInspeccionRequestDTO, InspeccionTrabajadorRequestDTO, ParticipanteRequestDTO, SecuenciaTareaRequestDTO, SsomaRequestDTO, SsomaService } from '../../service/ssoma.service';
import { DropdownService } from '../../service/dropdown.service';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-ssoma-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,FormsModule],
  templateUrl: './ssoma-form.component.html',
  styleUrls: ['./ssoma-form.component.css']
})
export class SsomaFormularioComponent implements OnInit {
  
  // Formulario principal
  formulario: SsomaRequestDTO;
  
  // Archivos
  fotosParticipantes: {index: number, file: File, tipo: string}[] = [];
  videoCharlaFile?: File;
  fotosEpp: {index: number, file: File}[] = [];
  fotosHerramientas: {index: number, file: File}[] = [];
  
  // Dropdowns
  empresas: any[] = [];
  trabajos: any[] = [];
  trabajadores: any[] = [];
  temasCharla: any[] = [];
  herramientasMaestras: any[] = [];
  preguntasPetar: any[] = [];
  
  // Arrays para controles dinámicos
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
  
  herramientasPreguntas = [
    '¿Está en buen estado?',
    '¿Tiene filo adecuado?',
    '¿Mango en buen estado?',
    '¿Sin grietas?',
    '¿Limpia?',
    '¿Sin óxido?',
    '¿Guardada correctamente?',
    '¿Identificada?'
  ];
  
  // Estado de pestañas
  activeTab = 'hoja1';
  
  constructor(
    private ssomaService: SsomaService,
    private dropdownService: DropdownService
  ) {
    // Inicializar formulario vacío
    this.formulario = this.crearFormularioVacio();
  }
  
// Asegúrate de inicializar charla en el constructor o ngOnInit
ngOnInit(): void {
  this.cargarDropdowns();
  this.inicializarFormulario();
  
  // Asegurar que charla esté inicializada
  if (!this.formulario.charla) {
    this.formulario.charla = {
      temaId: undefined,
      fechaCharla: new Date(),
      duracionHoras: 0.5,
      capacitadorId: undefined
    };
  }
}

// O crea un getter para acceso seguro
get charlaForm() {
  if (!this.formulario.charla) {
    this.formulario.charla = {
      temaId: undefined,
      fechaCharla: new Date(),
      duracionHoras: 0.5,
      capacitadorId: undefined
    };
  }
  return this.formulario.charla;
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
      herramientasInspeccion: [this.crearHerramientaVacia()]
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
      casco: false,
      lentes: false,
      orejeras: false,
      tapones: false,
      guantes: false,
      botas: false,
      arnes: false,
      chaleco: false,
      mascarilla: false,
      gafas: false,
      otros: '',
      accionCorrectiva: '',
      seguimiento: '',
      responsableId: undefined
    };
  }
  
// O cambia el método crearHerramientaVacia:
crearHerramientaVacia(): HerramientaConIndice {
  return {
    herramientaMaestraId: undefined,
    herramientaNombre: '',
    p1: false,
    p2: false,
    p3: false,
    p4: false,
    p5: false,
    p6: false,
    p7: false,
    p8: false,
    observaciones: ''
  };
}
  
  cargarDropdowns(): void {
    // Cargar empresas
    this.dropdownService.getEmpresas().subscribe({
      next: (data) => this.empresas = data,
      error: (err) => console.error('Error cargando empresas:', err)
    });
    
    // Cargar trabajadores
    this.dropdownService.getTrabajadores().subscribe({
      next: (data) => this.trabajadores = data,
      error: (err) => console.error('Error cargando trabajadores:', err)
    });
    
    // Datos ficticios para otros dropdowns
    this.trabajos = [
      { id: 1, label: 'Instalación Eléctrica' },
      { id: 2, label: 'Mantenimiento Mecánico' },
      { id: 3, label: 'Trabajo en Altura' },
      { id: 4, label: 'Excavación' },
      { id: 5, label: 'Espacios Confinados' }
    ];
    
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
    
    this.preguntasPetar = [
      { id: 1, pregunta: '¿Se identificaron todas las energías peligrosas?' },
      { id: 2, pregunta: '¿Se bloqueó/etiquetó correctamente?' },
      { id: 3, pregunta: '¿Se verificó la ausencia de tensión?' },
      { id: 4, pregunta: '¿Se estableció zona de trabajo delimitada?' }
    ];
  }
  
  inicializarFormulario(): void {
    // Fecha actual
    this.formulario.fecha = new Date();
    
    // Hora actual
    const ahora = new Date();
    this.formulario.horaInicio = ahora.toTimeString().split(' ')[0];
    this.formulario.horaInicioTrabajo = ahora.toTimeString().split(' ')[0];
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
      // Reordenar
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
    this.formulario.herramientasInspeccion.push(this.crearHerramientaVacia());
  }
  
  eliminarHerramienta(index: number): void {
    if (this.formulario.herramientasInspeccion.length > 1) {
      this.formulario.herramientasInspeccion.splice(index, 1);
    }
  }
  
  // Métodos para archivos
  onFotoParticipanteSeleccionada(event: any, index: number, tipo: string): void {
    const file = event.target.files[0];
    if (file) {
      this.fotosParticipantes.push({ index, file, tipo });
      // Mostrar preview
      const reader = new FileReader();
      reader.onload = (e: any) => {
        // Aquí podrías mostrar la imagen en un preview
      };
      reader.readAsDataURL(file);
    }
  }
  
  onFirmaParticipanteSeleccionada(event: any, index: number): void {
    const file = event.target.files[0];
    if (file && this.formulario.participantes[index]) {
      this.formulario.participantes[index].firma = file;
    }
  }
  
  onVideoCharlaSeleccionado(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.videoCharlaFile = file;
      // Crear objeto video charla
      this.formulario.videoCharla = {
        video: file,
        duracionSegundos: 30 // Puedes calcular esto o pedirlo al usuario
      };
    }
  }
  
  onFotoChecklistSeleccionada(event: any, index: number): void {
    const file = event.target.files[0];
    if (file && this.formulario.checklistSeguridad[index]) {
      this.formulario.checklistSeguridad[index].foto = file;
    }
  }
  
  onFotoEppSeleccionada(event: any, index: number): void {
    const file = event.target.files[0];
    if (file && this.formulario.eppChecks[index]) {
      this.formulario.eppChecks[index].foto = file;
    }
  }
  
  onFotoHerramientaSeleccionada(event: any, index: number): void {
    const file = event.target.files[0];
    if (file && this.formulario.herramientasInspeccion[index]) {
      this.formulario.herramientasInspeccion[index].foto = file;
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
        horaInicioPetar: this.formulario.horaInicio,
        horaFinPetar: this.formulario.horaFin,
        respuestas: this.preguntasPetar.map(p => ({
          preguntaId: p.id,
          respuesta: false,
          observaciones: ''
        })),
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
  
// CON ESTE:
agregarEquipoProteccion(): void {
  this.inicializarPetar(); // Asegurar que petar existe
  if (this.formulario.petar) {
    this.formulario.petar.equiposProteccion.push({
      equipoNombre: '',
      usado: false
    });
  }
}
  
// CON ESTE:
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
    
    if (!this.formulario.trabajoId) {
      Swal.fire('Error', 'Debe seleccionar un trabajo', 'error');
      return false;
    }
    
    // Validar participantes
    for (const participante of this.formulario.participantes) {
      if (!participante.nombre.trim() || !participante.cargo.trim()) {
        Swal.fire('Error', 'Todos los participantes deben tener nombre y cargo', 'error');
        return false;
      }
    }
    
    return true;
  }
  
  // Envío del formulario
  guardarFormulario(): void {
    if (!this.validarFormulario()) {
      return;
    }
    
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
    // Mostrar loading
    Swal.fire({
      title: 'Guardando...',
      text: 'Por favor espere',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });
    
    // Preparar archivos
    this.prepararArchivosParaEnvio();
    
    // Enviar al backend
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
        this.fotosParticipantes = [];
        this.videoCharlaFile = undefined;
        this.fotosEpp = [];
        this.fotosHerramientas = [];
      },
      error: (error) => {
        console.error('Error:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo guardar el formulario. Por favor intente nuevamente.',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }
  
  prepararArchivosParaEnvio(): void {
    // Convertir fotos participantes al formato esperado
    this.formulario.fotosParticipantes = this.fotosParticipantes.map(foto => ({
      participanteIndex: foto.index,
      foto: foto.file,
      tipoFoto: foto.tipo as 'FRONTAL' | 'CREDENCIAL'
    }));
    
    // Fotos EPP
    this.formulario.fotosEpp = this.fotosEpp.map(foto => ({
      eppIndex: foto.index,
      foto: foto.file
    }));
    
    // Fotos herramientas
    this.formulario.fotosHerramientas = this.fotosHerramientas.map(foto => ({
      herramientaIndex: foto.index,
      foto: foto.file
    }));
  }
  
  // Navegación entre pestañas
  cambiarTab(tab: string): void {
    this.activeTab = tab;
    
    // Si vamos a la hoja 5 (PETAR), inicializarlo
    if (tab === 'hoja5' && !this.formulario.petar) {
      this.inicializarPetar();
    }
  }
  
  // Helper para formatos de hora
  getHoraActual(): string {
    const ahora = new Date();
    return ahora.toTimeString().split(' ')[0];
  }
  
  // Método para capturar video (usando cámara)
  iniciarGrabacionVideo(): void {
    if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
      navigator.mediaDevices.getUserMedia({ video: true, audio: true })
        .then(stream => {
          // Aquí implementarías la grabación real
          // Por simplicidad, mostramos un mensaje
          Swal.fire('Info', 'Funcionalidad de grabación en desarrollo', 'info');
          
          // Detener stream
          stream.getTracks().forEach(track => track.stop());
        })
        .catch(err => {
          Swal.fire('Error', 'No se pudo acceder a la cámara: ' + err.message, 'error');
        });
    } else {
      Swal.fire('Error', 'Tu navegador no soporta grabación de video', 'error');
    }
  }
  
  // Método para tomar foto (usando cámara)
  tomarFoto(): void {
    if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
      navigator.mediaDevices.getUserMedia({ video: true })
        .then(stream => {
          // Crear elemento de video temporal
          const video = document.createElement('video');
          video.srcObject = stream;
          video.play();
          
          // Tomar foto después de un momento
          setTimeout(() => {
            const canvas = document.createElement('canvas');
            canvas.width = video.videoWidth;
            canvas.height = video.videoHeight;
            const ctx = canvas.getContext('2d');
            
            if (ctx) {
              ctx.drawImage(video, 0, 0);
              
              // Convertir a File
              canvas.toBlob(blob => {
                if (blob) {
                  const file = new File([blob], 'foto-camara.jpg', { type: 'image/jpeg' });
                  
                  Swal.fire({
                    title: 'Foto tomada',
                    text: '¿En qué sección deseas usar esta foto?',
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonText: 'Participantes',
                    cancelButtonText: 'EPP',
                    showDenyButton: true,
                    denyButtonText: 'Herramientas'
                  }).then(result => {
                    if (result.isConfirmed) {
                      // Agregar a fotos participantes
                      this.fotosParticipantes.push({
                        index: 0,
                        file,
                        tipo: 'FRONTAL'
                      });
                      Swal.fire('Éxito', 'Foto agregada a participantes', 'success');
                    } else if (result.isDenied) {
                      // Agregar a fotos herramientas
                      this.fotosHerramientas.push({
                        index: 0,
                        file
                      });
                      Swal.fire('Éxito', 'Foto agregada a herramientas', 'success');
                    } else {
                      // Agregar a fotos EPP
                      this.fotosEpp.push({
                        index: 0,
                        file
                      });
                      Swal.fire('Éxito', 'Foto agregada a EPP', 'success');
                    }
                  });
                }
              }, 'image/jpeg');
            }
            
            // Detener stream
            stream.getTracks().forEach(track => track.stop());
          }, 1000);
        })
        .catch(err => {
          Swal.fire('Error', 'No se pudo acceder a la cámara: ' + err.message, 'error');
        });
    } else {
      Swal.fire('Error', 'Tu navegador no soporta acceso a cámara', 'error');
    }
  }
}
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

import { DropdownItem, DropdownService } from '../../service/dropdown.service';
import {
  OtCreateRequest,
  OtTrabajadorRequest,
  CrearOtCompletaRequest,
  OtResponse,
  OtService
} from '../../service/ot.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-create-ot',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
 templateUrl: './ot-component.html',
  styleUrl: './ot-component.css'
})
export class CreateOtComponent implements OnInit {
  form!: FormGroup;
  submitted = false;
  loading = false;

  usernameLogueado: string = '—';
  trabajadorIdLogueado: number | null = null;

  // Dropdowns
  clientes: DropdownItem[] = [];
  areas: DropdownItem[] = [];           // ← se carga dinámicamente
  proyectos: DropdownItem[] = [];
  fases: DropdownItem[] = [];
  sites: DropdownItem[] = [];
  regiones: DropdownItem[] = [];

  // Trabajadores disponibles (puedes cargar desde otro endpoint)
  trabajadoresDisponibles: DropdownItem[] = [];

  constructor(
    private fb: FormBuilder,
    private otService: OtService,
    private dropdownService: DropdownService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Info del usuario logueado
    const user = this.authService.currentUser;
    this.usernameLogueado = user?.username || '—';
    this.trabajadorIdLogueado = user?.idTrabajador ?? null;

    // Inicializar formulario
    this.form = this.fb.group({
      idCliente: ['', Validators.required],
      idArea: ['', Validators.required],
      idProyecto: ['', Validators.required],
      idFase: ['', Validators.required],
      idSite: ['', Validators.required],
      idRegion: ['', Validators.required],
      descripcion: [''],
      fechaApertura: ['', Validators.required],
      // Responsables (todos opcionales)
      jefaturaClienteSolicitante: [''],
      analistaClienteSolicitante: [''],
      coordinadoresTiCwPextEnergia: [''],
      jefaturaResponsable: [''],
      liquidador: [''],
      ejecutante: [''],
      analistaContable: [''],
      // Trabajadores
      trabajadores: this.fb.array([])
    });

    // Cascada: al cambiar cliente → cargar áreas
    this.form.get('idCliente')?.valueChanges.subscribe(clienteId => {
      if (clienteId) {
        this.cargarAreasPorCliente(clienteId);
        this.form.get('idArea')?.reset(); // reset área cuando cambia cliente
      } else {
        this.areas = [];
        this.form.get('idArea')?.reset();
      }
    });

    // Cargar dropdowns iniciales
    this.cargarDropdownsIniciales();
  }

  // Getters útiles
  get f() { return this.form.controls; }
  get trabajadoresArray() { return this.form.get('trabajadores') as FormArray; }

  private cargarDropdownsIniciales(): void {
    this.dropdownService.getClientes().subscribe(d => this.clientes = d);
    this.dropdownService.getProyectos().subscribe(d => this.proyectos = d);
    this.dropdownService.getFases().subscribe(d => this.fases = d);
    this.dropdownService.getSites().subscribe(d => this.sites = d);
    this.dropdownService.getRegiones().subscribe(d => this.regiones = d);

    // Si tienes endpoint para trabajadores disponibles:
    // this.dropdownService.getTrabajadores().subscribe(d => this.trabajadoresDisponibles = d);
  }

  private cargarAreasPorCliente(idCliente: number): void {
    this.dropdownService.getAreasByCliente(idCliente).subscribe({
      next: (areas) => this.areas = areas,
      error: () => {
        this.areas = [];
        Swal.fire('Error', 'No se pudieron cargar las áreas del cliente', 'error');
      }
    });
  }

  agregarTrabajador() {
    this.trabajadoresArray.push(
      this.fb.group({
        idTrabajador: ['', Validators.required],
        rolEnOt: ['', [Validators.required, Validators.maxLength(50)]]
      })
    );
  }

  eliminarTrabajador(index: number) {
    this.trabajadoresArray.removeAt(index);
  }

  onSubmit(): void {
    this.submitted = true;
    this.form.markAllAsTouched();

    if (this.form.invalid) {
      Swal.fire({
        icon: 'warning',
        title: 'Formulario incompleto',
        text: 'Por favor completa los campos obligatorios',
        confirmButtonColor: '#3085d6'
      });
      return;
    }

    this.loading = true;

    const values = this.form.value;

    const otPayload: OtCreateRequest = {
      idCliente: Number(values.idCliente),
      idArea: Number(values.idArea),
      idProyecto: Number(values.idProyecto),
      idFase: Number(values.idFase),
      idSite: Number(values.idSite),
      idRegion: Number(values.idRegion),
      descripcion: values.descripcion?.trim() || undefined,
      fechaApertura: values.fechaApertura,
      diasAsignados: values.diasAsignados ?? 0,
      idOtsAnterior: null,

      jefaturaClienteSolicitante: values.jefaturaClienteSolicitante?.trim() || undefined,
      analistaClienteSolicitante: values.analistaClienteSolicitante?.trim() || undefined,
      coordinadoresTiCwPextEnergia: values.coordinadoresTiCwPextEnergia?.trim() || undefined,
      jefaturaResponsable: values.jefaturaResponsable?.trim() || undefined,
      liquidador: values.liquidador?.trim() || undefined,
      ejecutante: values.ejecutante?.trim() || undefined,
      analistaContable: values.analistaContable?.trim() || undefined
    };

    const trabajadores: OtTrabajadorRequest[] = (values.trabajadores || []).map((t: any) => ({
      idTrabajador: Number(t.idTrabajador),
      rolEnOt: t.rolEnOt.trim()
    }));

    const payload: CrearOtCompletaRequest = {
      ot: otPayload,
      trabajadores,
      detalles: [] // ← si implementas detalles después, mapea aquí
    };

    this.otService.crearOtCompleta(payload).subscribe({
      next: (res: OtResponse) => {
        Swal.fire({
          icon: 'success',
          title: '¡Orden creada!',
          html: `OT <strong>#${res.ot}</strong> registrada exitosamente<br>
                 <small>Fecha: ${res.fechaApertura}</small>`,
          timer: 3500,
          showConfirmButton: false
        });
        this.router.navigate(['/ots/list']);
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error al crear OT',
          text: err.message || 'Ocurrió un problema inesperado. Intenta nuevamente.'
        });
      },
      complete: () => this.loading = false
    });
  }

  resetForm(): void {
    this.form.reset();
    this.trabajadoresArray.clear();
    this.submitted = false;
    this.areas = [];
  }
}

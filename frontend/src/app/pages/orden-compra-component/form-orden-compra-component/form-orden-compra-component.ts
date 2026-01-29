// src/app/pages/orden-compra-component/form-orden-compra-component/form-orden-compra-component.ts

import { Component, EventEmitter, Input, Output, OnInit, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import Swal from 'sweetalert2';

import { OrdenCompraRequest, OrdenCompraResponse } from '../../../model/orden-compra.model';
import { OrdenCompraService } from '../../../service/orden-compra.service';
import { DropdownItem, DropdownService } from '../../../service/dropdown.service';

@Component({
  selector: 'app-form-orden-compra-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './form-orden-compra-component.html',
  styleUrls: ['./form-orden-compra-component.css']
})

export class FormOrdenCompraComponent implements OnInit, OnChanges {
@Input() tipoSeleccionado: 'MATERIAL' | 'SERVICIO' | null = null;
get modalTitle(): string {
  return this.tipoSeleccionado === 'SERVICIO' ? 'Nueva Orden de Servicio' : 'Nueva Orden de Material';
}
  @Input() ocToEdit: OrdenCompraResponse | null = null;
  @Input() isEdit: boolean = false;

  @ViewChild('formOrden') formOrden!: NgForm;

  @Output() onClose = new EventEmitter<void>();
  @Output() onSubmitted = new EventEmitter<boolean>();

  // Dropdowns
  ots: DropdownItem[] = [];
maestros: { id: number; label: string; codigo: string }[] = [];

  proveedores: DropdownItem[] = [];

  // Estados (estático por ahora – puedes crear endpoint si lo prefieres dinámico)
  estados: DropdownItem[] = [
    { id: 1, label: 'PENDIENTE' },
    { id: 2, label: 'APROBADA' },
    { id: 3, label: 'EN PROCESO' },
    { id: 4, label: 'ATENDIDA' },
    { id: 5, label: 'CERRADA' },
    { id: 6, label: 'RECHAZADA' },
    { id: 7, label: 'ANULADA' }
  ];

  // FormOrdenCompraComponent
formasPago: { id: string; label: string }[] = [
  { id: 'CONTADO', label: 'Contado' },
  { id: 'CREDITO', label: 'Crédito' },
  { id: 'TRANSFERENCIA', label: 'Transferencia' },
  { id: 'TARJETA', label: 'Tarjeta' }
];


  // Form model
  form: OrdenCompraRequest = this.getDefaultForm();

  isLoading = true;
  isSubmitting = false;

  constructor(
    private ordenService: OrdenCompraService,
    private dropdownService: DropdownService
  ) {}



private cargarDropdowns(): void {
  this.isLoading = true;

  this.dropdownService.loadOrdenCompraDropdowns().subscribe({
    next: (data) => {
      this.ots = data.ots;

      // 🔹 Mapear maestros para incluir 'codigo'
     this.maestros = data.maestros.map(m => ({
          id: m.id,
          label: m.label,
          codigo: (m as any).codigo ?? 'C' // por defecto 'C' si no viene
        }));


      this.proveedores = data.proveedores;
      this.isLoading = false;

      this.aplicarValoresEdicion();
    },
    error: (err) => {
      console.error('Error cargando dropdowns', err);
      Swal.fire('Error', 'No se pudieron cargar los catálogos necesarios', 'error');
      this.isLoading = false;
    }
  });
}



  ngOnChanges(changes: SimpleChanges): void {
    // Re-aplicar valores cuando cambie el input de edición (después de cargar dropdowns)
    if ((changes['ocToEdit'] || changes['isEdit']) && !this.isLoading) {
      this.aplicarValoresEdicion();
    }
  }

private aplicarValoresEdicion(): void {
  if (this.isEdit && this.ocToEdit) {
    this.form = {
      idEstadoOc: this.ocToEdit.idEstadoOc ?? 1,
      idOts: this.ocToEdit.idOts ?? 0,
      idProveedor: this.ocToEdit.idProveedor ?? 0,
      formaPago: this.ocToEdit.formaPago ?? '',
      subtotal: this.ocToEdit.subtotal ?? 0,
      igvPorcentaje: this.ocToEdit.igvPorcentaje ?? 0,
      igvTotal: this.ocToEdit.igvTotal ?? 0,
      total: this.ocToEdit.total ?? 0,
      fechaOc: this.ocToEdit.fechaOc ?? new Date().toISOString(),
      observacion: this.ocToEdit.observacion || '',

      // 🔹 Mapear correctamente a OcDetalleRequest
      detalles: this.ocToEdit.detalles?.map(d => {
        const cantidad = d.cantidad ?? 0;
        const precio = d.precioUnitario ?? 0;
        const subtotal = cantidad * precio;
        const igv = subtotal * 0.18; // si siempre es 18%
        const total = subtotal + igv;
        const tipo = d.codigo?.startsWith('S') ? 'SERVICIO' : 'MATERIAL'; // <-- aquí

        return {
          idMaestro: d.idProducto ?? 0,
          cantidad,
          precioUnitario: precio,
          subtotal,
          igv,
          total
        };
      }) ?? [],

      aplicarIgv: true
    };
  } else {

    this.form = this.getDefaultForm();
  }
}


  private getDefaultForm(): OrdenCompraRequest {
  return {
    idEstadoOc: 1,
    idOts: 0,
    idProveedor: 0,
    formaPago: '',
    subtotal: 0,
    igvPorcentaje: 0,
    igvTotal: 0,
    total: 0,
    fechaOc: new Date().toISOString(),
    observacion: '',
    detalles: [],
    aplicarIgv: true
  };
  }

  guardar(): void {


    this.isSubmitting = true;

    const observable = this.isEdit && this.ocToEdit?.idOc != null
      ? this.ordenService.actualizar(this.ocToEdit.idOc, this.form)
      : this.ordenService.crear(this.form);

    observable.subscribe({
      next: () => {
        this.mostrarExito(this.isEdit ? 'Orden actualizada correctamente' : 'Orden creada correctamente');
        this.onSubmitted.emit(true);
        this.cerrar();
      },
      error: (err) => {
        console.error('Error al guardar orden', err);
        Swal.fire('Error', err.error?.message || 'No se pudo guardar la orden de compra', 'error');
        this.isSubmitting = false;
      }
    });
  }

  cerrar(): void {
    this.onClose.emit();
  }

  private mostrarExito(mensaje: string): void {
    Swal.fire({
      icon: 'success',
      title: 'Éxito',
      text: mensaje,
      timer: 1800,
      showConfirmButton: false
    });
  }

  // Lista de materiales para el select
materiales: DropdownItem[] = [];

ngOnInit(): void {
  this.cargarDropdowns();
  this.cargarMateriales(); // carga los materiales
}

cargarMateriales(): void {
  // simulación de servicio real
  const todosMateriales = [
    { id: 1, label: 'Material A', tipo: 'MATERIAL' },
    { id: 2, label: 'Material B', tipo: 'MATERIAL' },
    { id: 3, label: 'Servicio X', tipo: 'SERVICIO' },
    { id: 4, label: 'Servicio Y', tipo: 'SERVICIO' }
  ];

  // filtrar según lo que se seleccionó en el modal
  if (this.tipoSeleccionado) {
    this.materiales = todosMateriales.filter(m => m.tipo === this.tipoSeleccionado);
  } else {
    this.materiales = todosMateriales; // fallback
  }

  // si es nueva orden, agregar un detalle inicial
  if (!this.isEdit) {
    this.agregarDetalle();
  }
}

// Agregar nuevo detalle
agregarDetalle(): void {
 this.form.detalles.push({
  idMaestro: 0,
  cantidad: 1,
  precioUnitario: 0,
  subtotal: 0,
  igv: 0,
  total: 0,
  tipo: '' // valor por defecto
});
}
// Agregar dentro de la clase FormOrdenCompraComponent
onMaestroChange(d: any) {
  // Busca el maestro seleccionado
  const maestro = this.maestros.find(m => m.id === d.idMaestro);
  if (maestro) {
    // Si el código empieza con 'S' → SERVICIO, sino → MATERIAL
    d.tipo = maestro.codigo?.startsWith('S') ? 'SERVICIO' : 'MATERIAL';
  } else {
    // fallback
    d.tipo = 'MATERIAL';
  }
}


// Eliminar detalle
eliminarDetalle(index: number): void {
  this.form.detalles.splice(index, 1);
  this.calcularTotales();
}

// Calcular total de un detalle
calcularTotalDetalle(d: any): void {
  d.total = (d.cantidad || 0) * (d.precioUnitario || 0);
  this.calcularTotales();
}

// Calcular totales de la OC
calcularTotales(): void {
  const subtotal = this.form.detalles.reduce((sum, d) => sum + (d.total || 0), 0);
  this.form.subtotal = subtotal;
  this.form.igvTotal = this.form.aplicarIgv ? subtotal * 0.18 : 0;
  this.form.total = subtotal + this.form.igvTotal;
}

}
// 🔹 Nuevo tipo para maestros con código
interface MaestroItem {
  id: number;
  label: string;
  codigo: string; // 'C' para Material, 'S' para Servicio
}

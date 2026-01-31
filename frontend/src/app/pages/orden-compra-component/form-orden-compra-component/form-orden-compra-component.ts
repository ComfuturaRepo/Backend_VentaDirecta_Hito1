import { Component, EventEmitter, Input, Output, OnInit, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import Swal from 'sweetalert2';
import { OrdenCompraRequest, OrdenCompraResponse, OcDetalleRequest } from '../../../model/orden-compra.model';
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
  @Input() ocToEdit: OrdenCompraResponse | null = null;
  @Input() isEdit = false;

  @ViewChild('formOrden') formOrden!: NgForm;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSubmitted = new EventEmitter<boolean>();

  form: OrdenCompraRequest = this.getDefaultForm();
  isLoading = true;
  isSubmitting = false;

  maestros: { id: number; label: string; codigo: string }[] = [];
  proveedores: DropdownItem[] = [];
  ots: DropdownItem[] = [];

  materiales: DropdownItem[] = [];

  constructor(private ordenService: OrdenCompraService, private dropdownService: DropdownService) {}

  ngOnInit(): void {
    this.cargarDropdowns();
    this.cargarMateriales();
  }
  // Método para cargar materiales
cargarMateriales(): void {
  const todosMateriales = [
    { id: 1, label: 'Material A', tipo: 'MATERIAL' },
    { id: 2, label: 'Material B', tipo: 'MATERIAL' },
    { id: 3, label: 'Servicio X', tipo: 'SERVICIO' },
    { id: 4, label: 'Servicio Y', tipo: 'SERVICIO' }
  ];

 
}

  ngOnChanges(changes: SimpleChanges): void {
    if ((changes['ocToEdit'] || changes['isEdit']) && !this.isLoading) this.aplicarValoresEdicion();
  }

  private cargarDropdowns(): void {
    this.isLoading = true;
    this.dropdownService.loadOrdenCompraDropdowns().subscribe({
      next: (data) => {
        this.ots = data.ots;
        this.maestros = data.maestros.map(m => ({ id: m.id, label: m.label, codigo: (m as any).codigo ?? 'C' }));
        this.proveedores = data.proveedores;
        this.isLoading = false;
        this.aplicarValoresEdicion();
      },
      error: () => {
        Swal.fire('Error', 'No se pudieron cargar los catálogos', 'error');
        this.isLoading = false;
      }
    });
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
        detalles: this.ocToEdit.detalles?.map(d => this.mapDetalle(d)) ?? [],
        aplicarIgv: true
      };
    } else {
      this.form = this.getDefaultForm();
    }
  }

  private mapDetalle(d: OcDetalleRequest | any): OcDetalleRequest {
    const cantidad = d.cantidad ?? 0;
    const precio = d.precioUnitario ?? 0;
    const subtotal = cantidad * precio;
    const igv = subtotal * 0.18;
    const total = subtotal + igv;
    return {
      idMaestro: d.idProducto ?? 0,
      cantidad,
      precioUnitario: precio,
      subtotal,
      igv,
      total,
      tipo: d.codigo?.startsWith('S') ? 'SERVICIO' : 'MATERIAL'
    };
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

  agregarDetalle(): void {
    this.form.detalles.push({ idMaestro: 0, cantidad: 1, precioUnitario: 0, subtotal: 0, igv: 0, total: 0, tipo: '' });
  }

  eliminarDetalle(index: number): void {
    this.form.detalles.splice(index, 1);
    this.calcularTotales();
  }

  calcularTotalDetalle(d: OcDetalleRequest): void {
    d.total = (d.cantidad || 0) * (d.precioUnitario || 0);
    this.calcularTotales();
  }

  
calcularTotales(): void {
  const subtotal = this.form.detalles.reduce(
    (sum, d) => sum + ((d.cantidad || 0) * (d.precioUnitario || 0)),
    0
  );

  this.form.subtotal = +subtotal.toFixed(2);

  if (this.form.aplicarIgv === true) {
    this.form.igvPorcentaje = 18; // 🔴 ESTO ES LO QUE FALTABA
    this.form.igvTotal = +(this.form.subtotal * 0.18).toFixed(2);
  } else {
    this.form.igvPorcentaje = 0;
    this.form.igvTotal = 0;
  }

  this.form.total = +(this.form.subtotal + this.form.igvTotal).toFixed(2);
}

formasPago: { id: string; label: string }[] = [
  { id: 'CONTADO', label: 'Contado' },
  { id: 'CREDITO', label: 'Crédito' },
  { id: 'TRANSFERENCIA', label: 'Transferencia' },
  { id: 'TARJETA', label: 'Tarjeta' }
];
  guardar(): void {
      console.log('FORM OC →', this.form);
    this.isSubmitting = true;
    const observable = this.isEdit && this.ocToEdit?.idOc != null
      ? this.ordenService.actualizar(this.ocToEdit.idOc, this.form)
      : this.ordenService.crear(this.form);

    observable.subscribe({
      next: () => {
        Swal.fire({ icon: 'success', title: 'Éxito', text: this.isEdit ? 'Orden actualizada' : 'Orden creada', timer: 1800, showConfirmButton: false });
        this.onSubmitted.emit(true);
        this.cerrar();
      },
      error: (err) => {
        Swal.fire('Error', err.error?.message || 'No se pudo guardar la OC', 'error');
        this.isSubmitting = false;
      }
    });
  }
estados: { id: number; label: string }[] = [
  { id: 1, label: 'PENDIENTE' },
  { id: 2, label: 'APROBADA' },
  { id: 3, label: 'EN PROCESO' },
  { id: 4, label: 'ATENDIDA' },
  { id: 5, label: 'CERRADA' },
  { id: 6, label: 'RECHAZADA' },
  { id: 7, label: 'ANULADA' }
];

  cerrar(): void {
    this.onClose.emit();
  }

  
}



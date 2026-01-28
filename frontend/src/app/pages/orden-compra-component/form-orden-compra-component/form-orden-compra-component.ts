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

  @Input() ocToEdit: OrdenCompraResponse | null = null;
  @Input() isEdit: boolean = false;

  @ViewChild('formOrden') formOrden!: NgForm;

  @Output() onClose = new EventEmitter<void>();
  @Output() onSubmitted = new EventEmitter<boolean>();

  // Dropdowns
  ots: DropdownItem[] = [];
  maestros: DropdownItem[] = [];
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

  // Form model
  form: OrdenCompraRequest = this.getDefaultForm();

  isLoading = true;
  isSubmitting = false;

  constructor(
    private ordenService: OrdenCompraService,
    private dropdownService: DropdownService
  ) {}

  ngOnInit(): void {
    this.cargarDropdowns();
  }

  private cargarDropdowns(): void {
    this.isLoading = true;

    this.dropdownService.loadOrdenCompraDropdowns().subscribe({
      next: (data) => {
        this.ots = data.ots;
        this.maestros = data.maestros;
        this.proveedores = data.proveedores;
        this.isLoading = false;

        // Aplicar valores de edición después de cargar los datos
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
      detalles: this.ocToEdit.detalles?.map(d => ({
        idProducto: d.idProducto,
        cantidad: d.cantidad,
        precioUnitario: d.precioUnitario,
        total: d.total,
        observacion: d.observacion})) ?? [],
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
}

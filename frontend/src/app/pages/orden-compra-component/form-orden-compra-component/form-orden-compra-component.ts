import { Component, EventEmitter, Input, Output, OnInit, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import Swal from 'sweetalert2';
import { OrdenCompraRequest, OrdenCompraResponse, OcDetalleRequest } from '../../../model/orden-compra.model';
import { OrdenCompraService } from '../../../service/orden-compra.service';
import { DropdownItem, DropdownService } from '../../../service/dropdown.service';
import { MaestroCodigoService } from '../../../service/maestro-codigo.service';
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

 maestros: { id: number; label: string }[] = [];
maestroMap = new Map<number, { codigo: string; descripcion: string }>();

  proveedores: DropdownItem[] = [];
  ots: DropdownItem[] = [];



  constructor(private ordenService: OrdenCompraService, private dropdownService: DropdownService, private maestroCodigoService: MaestroCodigoService){}
ngOnInit(): void {
  this.cargarDropdowns();   // OTs + proveedores
  this.cargarMaestros();    // 👈 SOLO maestro_codigo
  this.agregarDetalle();
}

  // Método para cargar materiales
cargarMaestros(): void {
  this.maestroCodigoService.listarParaCombo().subscribe(data => {

    console.log('BACKEND →', data);

    this.maestros = data.map((m: any) => ({
      id: m.idMaestro || m.id,
      label: (m.codigo || 'SIN-COD') + ' - ' + (m.descripcion || 'SIN-DESC')
    }));

    console.log('MAESTROS →', this.maestros);
  });
}


  ngOnChanges(changes: SimpleChanges): void {
    if ((changes['ocToEdit'] || changes['isEdit']) && !this.isLoading) this.aplicarValoresEdicion();
  }

private cargarDropdowns(): void {
  this.isLoading = true;

  this.dropdownService.loadOrdenCompraDropdowns().subscribe({
    next: (data) => {
      this.ots = data.ots;
      this.proveedores = data.proveedores;

      // limpiar
      this.maestroMap.clear();

      // construir combo + map





      this.isLoading = false;
      this.aplicarValoresEdicion();
    },
    error: () => {
      Swal.fire('Error', 'No se pudieron cargar los catálogos', 'error');
      this.isLoading = false;
    }
  });
}

getMaestroTexto(idMaestro: number): string {
  const m = this.maestroMap.get(idMaestro);
  return m ? `${m.codigo} - ${m.descripcion}` : '-';
}


private aplicarValoresEdicion(): void {
  if (!this.isEdit || !this.ocToEdit || this.maestros.length === 0) {
    return;
  }

  this.form = {
    idEstadoOc: this.ocToEdit.idEstadoOc,
    idOts: this.ocToEdit.idOts,
    idProveedor: this.ocToEdit.idProveedor,
    formaPago: this.ocToEdit.formaPago ?? '',
    igvPorcentaje: this.ocToEdit.igvPorcentaje ?? 18,
    fechaOc: this.ocToEdit.fechaOc,
    observacion: this.ocToEdit.observacion ?? '',
    aplicarIgv: true,
    detalles: this.ocToEdit.detalles.map(d => ({
      idMaestro: d.idMaestro ?? 0,          // 🔑 CLAVE
      cantidad: d.cantidad ?? 0,
      precioUnitario: d.precioUnitario ?? 0
    }))
  };
}




 

 private getDefaultForm(): OrdenCompraRequest {
  return {
    idEstadoOc: 1,
    idOts: 0,
    idProveedor: 0,
    formaPago: '',
    igvPorcentaje: 18,
    fechaOc: new Date().toISOString(),
    observacion: '',
    detalles: [],
    aplicarIgv: true
  };
}


agregarDetalle(): void {
  this.form.detalles.push({
   
    cantidad: 1,
    precioUnitario: 0
  });
}



  eliminarDetalle(index: number): void {
    this.form.detalles.splice(index, 1);

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



// orden-compra-component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { OrdenCompraService } from '../../service/orden-compra.service'; // ajusta ruta
import { OcDetalleResponse, OrdenCompraRequest, OrdenCompraResponse, PageOrdenCompra } from '../../model/orden-compra.model';
import { FormOrdenCompraComponent } from "./form-orden-compra-component/form-orden-compra-component"; // ajusta ruta

@Component({
  selector: 'app-orden-compra-component',
  standalone: true,
  imports: [CommonModule, FormsModule, FormOrdenCompraComponent],
  templateUrl: './orden-compra-component.html',
  styleUrls: ['./orden-compra-component.css']
})
export class OrdenCompraComponent implements OnInit {

  pageData: PageOrdenCompra | null = null;
  ordenes: OrdenCompraResponse[] = [];
  filteredOrdenes: OrdenCompraResponse[] = [];
// Dentro de OrdenCompraComponent
selectedDetalles: OcDetalleResponse[] = []; // detalles cargados
showDetallesModal: boolean = false;        // controla modal
  searchTerm: string = '';
  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 1;

  isLoading: boolean = false;
  errorMessage: string | null = null;

  // Para el formulario modal
  showFormModal: boolean = false;
  isEditMode: boolean = false;
  selectedOc: OrdenCompraResponse | null = null;

  constructor(private ordenService: OrdenCompraService) {}

  ngOnInit(): void {
    this.cargarOrdenes();
  }

  cargarOrdenes(page: number = 0): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.ordenService.listar(page, this.pageSize).subscribe({
      next: (data) => {
        this.pageData = data;
        this.ordenes = data.content;
        this.filteredOrdenes = [...this.ordenes];
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'No se pudieron cargar las órdenes';
        this.mostrarError(this.errorMessage);
        this.isLoading = false;
      }
    });
  }

 aplicarFiltro(): void {
  const term = this.searchTerm.trim().toLowerCase();
  if (!term) {
    this.filteredOrdenes = [...this.ordenes];
    return;
  }

  this.filteredOrdenes = this.ordenes.filter(oc =>
    oc.ot.toString().toLowerCase().includes(term) ||           // número a string
    oc.proveedorNombre?.toLowerCase().includes(term) ||
    oc.estadoNombre?.toLowerCase().includes(term) ||
    (oc.observacion?.toLowerCase().includes(term) ?? false)
  );
}


  onSearchChange(): void {
    this.aplicarFiltro();
  }

  irAPagina(pagina: number): void {
    if (pagina >= 0 && pagina < this.totalPages) {
      this.cargarOrdenes(pagina);
    }
  }

  // ──────────────────────────────────────────────
  //            Acciones de la tabla
  // ──────────────────────────────────────────────

detalles: OcDetalleResponse[] = [];

verDetalle(oc: OrdenCompraResponse): void {
  console.log('CLICK DETALLE', oc.idOc);

  this.ordenService.obtenerDetallesPorOc(oc.idOc).subscribe({
    next: (resp) => {
      console.log('DETALLES RECIBIDOS', resp);

      this.detalles = resp.content; // 🔥 SOLO EL ARRAY
      this.mostrarModalDetalle();
    },
    error: () => {
      Swal.fire('Error', 'No se pudieron cargar los detalles', 'error');
    }
  });
}


mostrarModalDetalle(): void {
  const modal = document.getElementById('modalDetalleOc');
  if (modal) {
    const bsModal = new (window as any).bootstrap.Modal(modal);
    bsModal.show();
  }
}

// Para el modal de detalles
showDetalleModal: boolean = false;
detalleOcSeleccionada: OrdenCompraResponse | null = null;
detallesOc: OcDetalleResponse[] = [];

  editarOrden(oc: OrdenCompraResponse): void {
    this.selectedOc = { ...oc }; // copia para editar
    this.isEditMode = true;
    this.showFormModal = true;
  }

  toggleEstado(oc: OrdenCompraResponse): void {
    const nuevosEstadosPosibles = {
      'PENDIENTE': 'EN PROCESO',
      'EN PROCESO': 'APROBADA',
      'APROBADA': 'ATENDIDA',
      'ATENDIDA': 'CERRADA',
      'RECHAZADA': 'PENDIENTE',
      'ANULADA': 'PENDIENTE'
    };

    const nuevoEstadoNombre = nuevosEstadosPosibles[oc.estadoNombre as keyof typeof nuevosEstadosPosibles] || 'PENDIENTE';

    Swal.fire({
      title: '¿Cambiar estado?',
      text: `Pasar de "${oc.estadoNombre}" → "${nuevoEstadoNombre}"?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, cambiar'
    }).then((result) => {
      if (result.isConfirmed) {
        // Aquí deberías tener un endpoint o lógica para cambiar solo estado
        // Por simplicidad, suponemos que actualizamos todo el objeto (o crea método específico)
                  const request:  OrdenCompraRequest = {
              idEstadoOc: this.mapEstadoNombreToId(nuevoEstadoNombre),
              idOts: oc.idOts,               // correcto, no uses oc.idOc
              idProveedor: oc.idProveedor,   // agrega proveedor real
              formaPago: oc.formaPago ?? '', // si existe
              subtotal: oc.subtotal ?? 0,
              igvPorcentaje: oc.igvPorcentaje ?? 0,
              igvTotal: oc.igvTotal ?? 0,
              total: oc.total ?? 0,
              fechaOc: oc.fechaOc ?? new Date().toISOString(),
              observacion: oc.observacion ?? '',
               detalles: oc.detalles?.map(d => ({
                  idMaestro: d.idProducto ?? 0,  // obligatorio
                  cantidad: d.cantidad ?? 0,
                  precioUnitario: d.precioUnitario ?? 0,
                  subtotal: 0,                   // backend lo calcula
                  igv: 0,                        // backend lo calcula
                  total: d.total ?? 0

              })) ?? [],
              aplicarIgv: true // o según tu lógica
            };


        this.ordenService.actualizar(oc.idOc, request).subscribe({
          next: () => {
            this.mostrarExito(`Estado cambiado a ${nuevoEstadoNombre}`);
            this.cargarOrdenes(this.currentPage);
          },
          error: () => this.mostrarError('No se pudo cambiar el estado')
        });
      }
    });
  }

  // Helper – ajusta según tus IDs reales de estado
  private mapEstadoNombreToId(nombre: string): number {
    const map: Record<string, number> = {
      'PENDIENTE': 1,
      'EN PROCESO': 2,
      'APROBADA': 3,
      'ATENDIDA': 4,
      'CERRADA': 5,
      'RECHAZADA': 6,
      'ANULADA': 7
    };
    return map[nombre] ?? 1;
  }

  // ──────────────────────────────────────────────
  //               Form Modal
  // ──────────────────────────────────────────────

  abrirModalCrear(): void {
    this.selectedOc = null;
    this.isEditMode = false;
    this.showFormModal = true;
  }

  onFormSubmitted(success: boolean): void {
    this.showFormModal = false;
    if (success) {
      this.cargarOrdenes(this.currentPage);
    }
  }

  // SweetAlert helpers
  private mostrarExito(mensaje: string): void {
    Swal.fire({ icon: 'success', title: 'Éxito', text: mensaje, timer: 2200, showConfirmButton: false });
  }

  private mostrarError(mensaje: string): void {
    Swal.fire({ icon: 'error', title: 'Error', text: mensaje });
  }

  getEstadoClass(estado: string): string {
    const map: Record<string, string> = {
      'APROBADA': 'bg-success',
      'PENDIENTE': 'bg-warning text-dark',
      'EN PROCESO': 'bg-primary',
      'ATENDIDA': 'bg-info',
      'CERRADA': 'bg-secondary',
      'RECHAZADA': 'bg-danger',
      'ANULADA': 'bg-dark'
    };
    return map[estado] || 'bg-secondary';
  }

getTotal(oc: OrdenCompraResponse): number {
  // suma de cantidad * precioUnitario de cada detalle
  return oc.detalles?.reduce(
    (sum, d) => sum + ((d.cantidad ?? 0) * (d.precioUnitario ?? 0)),
    0
  ) ?? 0;
}
getCantidadTotal(oc: OrdenCompraResponse): number {
  if (!oc.detalles) return 0;
  return oc.detalles.reduce((sum, d) => sum + (d.cantidad ?? 0), 0);
}

getTotalDetalle(oc: OrdenCompraResponse): number {
  if (!oc.detalles) return 0;
  return oc.detalles.reduce((sum, d) => sum + ((d.cantidad ?? 0) * (d.precioUnitario ?? 0)), 0);
}

}

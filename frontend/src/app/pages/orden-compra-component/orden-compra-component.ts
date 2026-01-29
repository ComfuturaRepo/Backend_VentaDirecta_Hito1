  // orden-compra-component.ts
  import { Component, OnInit } from '@angular/core';
  import { CommonModule } from '@angular/common';
  import { FormsModule } from '@angular/forms';
  import Swal from 'sweetalert2';
  import { OrdenCompraService } from '../../service/orden-compra.service';
  import { OcDetalleService } from '../../service/oc-detalle.service';
  import { FormOrdenCompraComponent } from "./form-orden-compra-component/form-orden-compra-component";
  import { PaginationComponent } from "../../component/pagination.component/pagination.component";
  import { OcDetalleResponse, OrdenCompraResponse, PageOrdenCompra } from '../../model/orden-compra.model';

  @Component({
    selector: 'app-orden-compra-component',
    standalone: true,
    imports: [CommonModule, FormsModule, FormOrdenCompraComponent, PaginationComponent],
    templateUrl: './orden-compra-component.html',
    styleUrls: ['./orden-compra-component.css']
  })
  export class OrdenCompraComponent implements OnInit {

    // Datos de tabla y paginación
    pageData: PageOrdenCompra | null = null;
    ordenes: OrdenCompraResponse[] = [];
    filteredOrdenes: OrdenCompraResponse[] = [];
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
    tipoSeleccionado: 'MATERIAL' | 'SERVICIO' | null = null;

    // Para modal de detalles
    showDetalleModal: boolean = false;
    detalleOcSeleccionada: OrdenCompraResponse | null = null;
    detallesOc: OcDetalleResponse[] = [];

    // Modal tipo
    showTipoModal: boolean = false;

    constructor(
      private ordenCompraService: OrdenCompraService,
      private ocDetalleService: OcDetalleService
    ) {}

    ngOnInit(): void {
      this.cargarOrdenes();
    }

    // ──────────────── CARGAR ORDENES CON PAGINACIÓN ────────────────

   cargarOrdenes(page: number = 0): void {
  this.isLoading = true;
  this.errorMessage = null;

  this.ordenCompraService.listar(page, this.pageSize, this.searchTerm).subscribe({
    next: (data) => {
      this.pageData = data;
      // Ya vienen todos los campos correctamente
      this.ordenes = data.content;
      this.filteredOrdenes = [...this.ordenes];
      this.currentPage = data.page.number;
      this.totalPages = data.page.totalPages;
      this.isLoading = false;
    },
    error: (err) => {
      this.errorMessage = 'No se pudieron cargar las órdenes';
      this.mostrarError(this.errorMessage);
      this.isLoading = false;
    }
  });
}




    onSearchChange(): void {
      this.currentPage = 0;
      this.cargarOrdenes(this.currentPage);
    }

    cambiarPageSize(newSize: number) {
      this.pageSize = newSize;
      this.currentPage = 0;
      this.cargarOrdenes(this.currentPage);
    }

    irAPagina(pagina: number): void {
      if (pagina >= 0 && pagina < this.totalPages) {
        this.currentPage = pagina;
        this.cargarOrdenes(this.currentPage);
      }
    }

    // ──────────────── MODAL DE TIPO ────────────────
    abrirModalTipo(): void {
      this.showTipoModal = true;
    }

    abrirNuevaOrden(tipo: 'MATERIAL' | 'SERVICIO'): void {
      this.tipoSeleccionado = tipo;
      this.showTipoModal = false;
      this.selectedOc = null;
      this.isEditMode = false;
      this.showFormModal = true;
    }

    // ──────────────── DETALLES ────────────────
    verDetalle(oc: OrdenCompraResponse): void {
      this.detalleOcSeleccionada = oc;
      this.showDetalleModal = true;

      this.ocDetalleService.listarPorOrden(oc.idOc).subscribe({
        next: (resp: any) => {
          this.detallesOc = resp.content;
        },
        error: (err) => {
          console.error('Error al cargar detalles', err);
          Swal.fire('Error', 'No se pudieron cargar los detalles', 'error');
        }
      });
    }

    // ──────────────── EDITAR ORDEN ────────────────
    editarOrden(oc: OrdenCompraResponse): void {
      this.selectedOc = { ...oc };
      this.isEditMode = true;
      this.showFormModal = true;
    }

    // ──────────────── CAMBIAR ESTADO ────────────────
    toggleEstado(oc: OrdenCompraResponse): void {
      const estados = {
        'PENDIENTE': 'EN PROCESO',
        'EN PROCESO': 'APROBADA',
        'APROBADA': 'ATENDIDA',
        'ATENDIDA': 'CERRADA',
        'RECHAZADA': 'PENDIENTE',
        'ANULADA': 'PENDIENTE'
      };
      const nuevoEstado = estados[oc.estadoNombre as keyof typeof estados] || 'PENDIENTE';

      Swal.fire({
        title: '¿Cambiar estado?',
        text: `Pasar de "${oc.estadoNombre}" → "${nuevoEstado}"?`,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Sí, cambiar',
        cancelButtonText: 'Cancelar'
      }).then(result => {
        if (result.isConfirmed) {
          // Aquí debes llamar al backend para actualizar
          this.cargarOrdenes(this.currentPage);
          Swal.fire('Éxito', `Estado cambiado a ${nuevoEstado}`, 'success');
        }
      });
    }

    // ──────────────── FORM MODAL ────────────────
    onFormSubmitted(success: boolean): void {
      this.showFormModal = false;
      if (success) {
        this.cargarOrdenes(this.currentPage);
      }
    }

    // ──────────────── SWEET ALERTS ────────────────
    private mostrarExito(mensaje: string): void {
      Swal.fire({ icon: 'success', title: 'Éxito', text: mensaje, timer: 2200, showConfirmButton: false });
    }

    private mostrarError(mensaje: string): void {
      Swal.fire({ icon: 'error', title: 'Error', text: mensaje });
    }

    // ──────────────── UTILS ────────────────
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
  }

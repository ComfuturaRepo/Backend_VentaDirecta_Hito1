import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { OrdenCompraService } from '../../service/orden-compra.service';
import { FormOrdenCompraComponent } from './form-orden-compra-component/form-orden-compra-component';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import { OcDetalleResponse, OrdenCompraResponse, PageOrdenCompra } from '../../model/orden-compra.model';
import { OrdenCompraAprobacionesComponent } from './form-aprovacion-component/orden-compra-aprobaciones.component';

@Component({
  selector: 'app-orden-compra-component',
  standalone: true,
  imports: [CommonModule, FormsModule, FormOrdenCompraComponent, PaginationComponent, OrdenCompraAprobacionesComponent ],

  templateUrl: './orden-compra-component.html',
  styleUrls: ['./orden-compra-component.css']
})
export class OrdenCompraComponent implements OnInit {
  pageData: PageOrdenCompra | null = null;
  ordenes: OrdenCompraResponse[] = [];
  filteredOrdenes: OrdenCompraResponse[] = [];
  searchTerm = '';
  currentPage = 0;
  pageSize = 10;
  totalPages = 1;
  isLoading = false;
  errorMessage: string | null = null;

  showFormModal = false;
  isEditMode = false;
  selectedOc: OrdenCompraResponse | null = null;

  showDetalleModal = false;
  detalleOcSeleccionada: OrdenCompraResponse | null = null;
  detallesOc: OcDetalleResponse[] = [];

  constructor(private ordenCompraService: OrdenCompraService) {}

  ngOnInit(): void {
    this.cargarOrdenes();
  }

  cargarOrdenes(page: number = 0): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.ordenCompraService.listar(page, this.pageSize, this.searchTerm).subscribe({
      next: (data) => {
        this.pageData = data;
        this.ordenes = data.content;
        this.filteredOrdenes = [...this.ordenes];
        this.currentPage = data.page.number;
        this.totalPages = data.page.totalPages;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'No se pudieron cargar las órdenes';
        Swal.fire('Error', this.errorMessage, 'error');
        this.isLoading = false;
      }
    });
  }

  onSearchChange(): void {
    this.currentPage = 0;
    this.cargarOrdenes(this.currentPage);
  }

  irAPagina(pagina: number): void {
    if (pagina >= 0 && pagina < this.totalPages) {
      this.currentPage = pagina;
      this.cargarOrdenes(this.currentPage);
    }
  }

  abrirNuevaOrden(): void {
    this.showFormModal = true;
    this.isEditMode = false;
    this.selectedOc = null;
  }

  verDetalle(oc: OrdenCompraResponse): void {
    this.detalleOcSeleccionada = oc;
    this.detallesOc = oc.detalles ?? [];
    this.showDetalleModal = true;
  }

  editarOrden(oc: OrdenCompraResponse): void {
    this.selectedOc = { ...oc };
    this.isEditMode = true;
    this.showFormModal = true;
  }

  onFormSubmitted(success: boolean): void {
    this.showFormModal = false;
    if (success) this.cargarOrdenes(this.currentPage);
  }

  // 🔹 Estado badge
getEstadoClass(estado: string): string {
  switch (estado?.toLowerCase()) {
    case 'pendiente': return 'bg-warning text-dark';
    case 'aprobado': return 'bg-success';
    case 'anulado': return 'bg-danger';
    default: return 'bg-secondary';
  }
}

// 🔹 Cambiar estado (mock por ahora)
toggleEstado(oc: OrdenCompraResponse): void {
  Swal.fire('Info', 'Cambio de estado pendiente de implementar', 'info');
}

// 🔹 Cambio de page size
cambiarPageSize(size: number): void {
  this.pageSize = size;
  this.currentPage = 0;
  this.cargarOrdenes();
}

// 🔹 Tipo seleccionado (si el form lo pide)
tipoSeleccionado: string | null = null;
}

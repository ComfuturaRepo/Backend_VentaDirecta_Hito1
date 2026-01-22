// site.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Site } from '../../model/site.interface';
import { SiteService } from '../../service/site.service';
import { Page } from '../../model/ots';

@Component({
  selector: 'app-site',
  standalone: true,
  imports: [CommonModule, FormsModule],
 templateUrl: './site-component.html',
  styleUrl: './site-component.css',
})
export class SiteComponent implements OnInit {
  // Datos principales
  sites: Site[] = [];
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  // Filtro
  searchDescripcion: string = '';

  // Formulario (crear/editar)
  showForm = false;
  isEditMode = false;
  currentSite: Site = { codigoSitio: '', descripcion: '', activo: true };

  // Loading y mensajes
  isLoading = false;
  errorMessage: string | null = null;

  constructor(private siteService: SiteService) {}

  ngOnInit(): void {
    this.loadSites();
  }

  loadSites(page: number = this.currentPage): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.siteService.listar(page, this.pageSize).subscribe({
      next: (response: Page<Site>) => {
        this.sites = response.content;
        this.currentPage = response.number;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.isLoading = false;

        // Aplicamos filtro local si hay búsqueda
        if (this.searchDescripcion.trim()) {
          this.applyLocalFilter();
        }
      },
      error: (err) => {
        this.errorMessage = 'Error al cargar los sitios';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  applyLocalFilter(): void {
    if (!this.searchDescripcion.trim()) {
      // Si se limpia el filtro, volvemos a cargar la página actual sin filtro
      this.loadSites(this.currentPage);
      return;
    }

    // Filtro simple por descripción (frontend)
    const term = this.searchDescripcion.toLowerCase().trim();
    this.sites = this.sites.filter(site =>
      site.descripcion?.toLowerCase().includes(term)
    );
  }

  onSearchChange(): void {
    if (this.searchDescripcion.trim() === '') {
      this.loadSites(this.currentPage);
    } else {
      this.applyLocalFilter();
    }
  }

  openCreateForm(): void {
    this.isEditMode = false;
    this.currentSite = { codigoSitio: '', descripcion: '', activo: true };
    this.showForm = true;
  }

  openEditForm(site: Site): void {
    this.isEditMode = true;
    this.currentSite = { ...site }; // copia para no modificar el original directamente
    this.showForm = true;
  }

  saveSite(): void {
    if (!this.currentSite.codigoSitio?.trim()) {
      this.errorMessage = 'El código de sitio es obligatorio';
      return;
    }

    this.isLoading = true;

    this.siteService.guardar(this.currentSite).subscribe({
      next: (savedSite) => {
        this.showForm = false;
        this.loadSites(this.currentPage); // recargamos la página actual
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error al guardar el sitio';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  cancelForm(): void {
    this.showForm = false;
    this.errorMessage = null;
  }

  toggleActivo(site: Site): void {
    if (!site.idSite) return;

    this.isLoading = true;

    this.siteService.toggle(site.idSite).subscribe({
      next: () => {
        // Actualizamos localmente para no recargar toda la página
        const index = this.sites.findIndex(s => s.idSite === site.idSite);
        if (index !== -1) {
          this.sites[index].activo = !this.sites[index].activo;
        }
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error al cambiar estado';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  // Paginación
  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadSites(page);
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.goToPage(this.currentPage - 1);
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.goToPage(this.currentPage + 1);
    }
  }
}

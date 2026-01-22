// analista-cliente-solicitante-component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AnalistaClienteSolicitante } from '../../model/analista-cliente-solicitante.interface';
import { AnalistaClienteSolicitanteService } from '../../service/analista-cliente-solicitante.service';

@Component({
  selector: 'app-analista-cliente-solicitante-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './analista-cliente-solicitante-component.html',
  styleUrl: './analista-cliente-solicitante-component.css',
})
export class AnalistaClienteSolicitanteComponent implements OnInit {

  // Lista principal
analistas: AnalistaClienteSolicitante[] = [];
analistasOriginal: AnalistaClienteSolicitante[] = [];

  // Filtro de búsqueda (local)
  searchDescripcion: string = '';

  // Formulario crear / editar
  showForm = false;
  isEditMode = false;
  currentAnalista: AnalistaClienteSolicitante = { descripcion: '', activo: true };

  // Estados de UI
  isLoading = false;
  errorMessage: string | null = null;

  constructor(
    private analistaService: AnalistaClienteSolicitanteService
  ) {}

  ngOnInit(): void {
    this.loadAnalistas();
  }

 loadAnalistas(): void {
  this.isLoading = true;
  this.errorMessage = null;

  this.analistaService.listar().subscribe({
    next: (data) => {
      this.analistasOriginal = data;
      this.applyLocalFilter();
      this.isLoading = false;
    },
    error: (err) => {
      this.errorMessage = 'Error al cargar los analistas';
      console.error(err);
      this.isLoading = false;
    }
  });
}


 applyLocalFilter(): void {
  const term = this.searchDescripcion.trim().toLowerCase();

  if (!term) {
    this.analistas = [...this.analistasOriginal];
    return;
  }

  this.analistas = this.analistasOriginal.filter(a =>
    a.descripcion.toLowerCase().includes(term)
  );
}

onSearchChange(): void {
  this.applyLocalFilter();
}


  openCreateForm(): void {
    this.isEditMode = false;
    this.currentAnalista = { descripcion: '', activo: true };
    this.showForm = true;
    this.errorMessage = null;
  }

  openEditForm(analista: AnalistaClienteSolicitante): void {
    this.isEditMode = true;
    this.currentAnalista = { ...analista }; // copia superficial
    this.showForm = true;
    this.errorMessage = null;
  }

  saveAnalista(): void {
    if (!this.currentAnalista.descripcion?.trim()) {
      this.errorMessage = 'La descripción es obligatoria';
      return;
    }

    this.isLoading = true;

    this.analistaService.guardar(this.currentAnalista).subscribe({
      next: () => {
        this.showForm = false;
        this.loadAnalistas(); // recargar la lista completa
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error al guardar el analista';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  cancelForm(): void {
    this.showForm = false;
    this.errorMessage = null;
  }

  toggleActivo(analista: AnalistaClienteSolicitante): void {
    if (!analista.id) return;

    this.isLoading = true;

    this.analistaService.toggle(analista.id).subscribe({
      next: () => {
        // Actualización optimista
        const index = this.analistas.findIndex(a => a.id === analista.id);
        if (index !== -1) {
          this.analistas[index].activo = !this.analistas[index].activo;
        }
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'No se pudo cambiar el estado';
        console.error(err);
        this.isLoading = false;
      }
    });
  }
}

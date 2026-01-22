import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { JefaturaClienteSolicitante } from '../../model/jefatura-cliente-solicitante.interface';
import { JefaturaClienteSolicitanteService } from '../../service/jefatura-cliente-solicitante.service';

@Component({
  selector: 'app-jefatura-cliente-solicitante-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './jefatura-cliente-solicitante-component.html',
  styleUrl: './jefatura-cliente-solicitante-component.css',
})
export class JefaturaClienteSolicitanteComponent implements OnInit {

  // Lista original (backend)
  jefaturasOriginal: JefaturaClienteSolicitante[] = [];

  // Lista mostrada (filtrada)
  jefaturas: JefaturaClienteSolicitante[] = [];

  searchDescripcion = '';

  showForm = false;
  isEditMode = false;
  currentJefatura: JefaturaClienteSolicitante = { descripcion: '', activo: true };

  isLoading = false;
  errorMessage: string | null = null;

  constructor(
    private jefaturaService: JefaturaClienteSolicitanteService
  ) {}

  ngOnInit(): void {
    this.loadJefaturas();
  }

  loadJefaturas(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.jefaturaService.listar().subscribe({
      next: (data) => {
        this.jefaturasOriginal = data;
        this.applyLocalFilter();
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'No se pudieron cargar las jefaturas';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  applyLocalFilter(): void {
    const term = this.searchDescripcion.trim().toLowerCase();

    if (!term) {
      this.jefaturas = [...this.jefaturasOriginal];
      return;
    }

    this.jefaturas = this.jefaturasOriginal.filter(j =>
      j.descripcion.toLowerCase().includes(term)
    );
  }

  onSearchChange(): void {
    this.applyLocalFilter();
  }

  openCreateForm(): void {
    this.isEditMode = false;
    this.currentJefatura = { descripcion: '', activo: true };
    this.showForm = true;
    this.errorMessage = null;
  }

  openEditForm(jefatura: JefaturaClienteSolicitante): void {
    this.isEditMode = true;
    this.currentJefatura = { ...jefatura };
    this.showForm = true;
    this.errorMessage = null;
  }

  saveJefatura(): void {
    if (!this.currentJefatura.descripcion?.trim()) {
      this.errorMessage = 'La descripción es obligatoria';
      return;
    }

    this.isLoading = true;

    this.jefaturaService.guardar(this.currentJefatura).subscribe({
      next: () => {
        this.showForm = false;
        this.loadJefaturas();
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error al guardar la jefatura';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  cancelForm(): void {
    this.showForm = false;
    this.errorMessage = null;
  }

  toggleActivo(jefatura: JefaturaClienteSolicitante): void {
    if (!jefatura.id) return;

    this.isLoading = true;

    this.jefaturaService.toggle(jefatura.id).subscribe({
      next: () => {
        const idx = this.jefaturasOriginal.findIndex(j => j.id === jefatura.id);
        if (idx !== -1) {
          this.jefaturasOriginal[idx].activo = !this.jefaturasOriginal[idx].activo;
          this.applyLocalFilter();
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

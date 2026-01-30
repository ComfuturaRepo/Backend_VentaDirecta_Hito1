import { Component, Input, Output, EventEmitter, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { DropdownItem } from '../../service/dropdown.service';

@Component({
  selector: 'app-ngselect-dropdown',
  standalone: true,
  imports: [CommonModule, FormsModule, NgSelectModule],
  template: `
    <ng-select
      [items]="items"
      [placeholder]="placeholder"
      [searchable]="true"
      [clearable]="true"
      [loading]="loading"
      [virtualScroll]="items.length > 50"
      [multiple]="multiple"
      [closeOnSelect]="!multiple"
      [(ngModel)]="internalValue"
      (change)="onSelectionChange($event)"
      (search)="onSearch($event)"
      (open)="onOpen()"
      (close)="onClose()"
      [class.is-invalid]="isInvalid"
      [disabled]="disabled"
      [compareWith]="compareItems">

      <!-- Template para mostrar seleccionado -->
      <ng-template ng-label-tmp let-item="item">
        <span *ngIf="item">
          {{ getItemDisplay(item) }}
        </span>
      </ng-template>

      <!-- Template para opciones -->
      <ng-template ng-option-tmp let-item="item" let-index="index" let-search="searchTerm">
        <div class="dropdown-option" [class.disabled]="item.estado === false">
          <div class="d-flex align-items-center">
            <!-- Checkbox para múltiple selección -->
            <div *ngIf="multiple" class="me-2">
              <div class="form-check">
                <input class="form-check-input" type="checkbox"
                       [checked]="isItemSelected(item)"
                       [disabled]="item.estado === false"
                       readonly>
              </div>
            </div>

            <!-- Contenido principal -->
            <div class="flex-grow-1">
              <div class="option-label">
                {{ getItemDisplay(item) }}
              </div>
              <small *ngIf="item.adicional" class="text-muted option-additional">
                {{ item.adicional }}
              </small>
            </div>

            <!-- Estado inactivo -->
            <span *ngIf="item.estado === false" class="badge bg-secondary ms-2">Inactivo</span>

            <!-- Check para selección simple -->
            <i *ngIf="!multiple && isItemSelected(item)" class="bi bi-check-lg text-primary ms-2"></i>
          </div>
        </div>
      </ng-template>

      <!-- Sin resultados -->
      <ng-template ng-notfound-tmp let-searchTerm="searchTerm">
        <div class="text-center p-3">
          <i class="bi bi-search text-muted fs-4 mb-2"></i>
          <p class="mb-1 fw-medium">{{ noResultsText }}</p>
          <small *ngIf="searchTerm" class="text-muted">
            Búsqueda: "{{ searchTerm }}"
          </small>
          <div *ngIf="items.length === 0 && !searchTerm" class="mt-2">
            <small class="text-muted">No hay opciones disponibles</small>
          </div>
        </div>
      </ng-template>

      <!-- Loading -->
      <ng-template ng-loadingtext-tmp>
        <div class="text-center p-3">
          <div class="spinner-border spinner-border-sm text-primary me-2"></div>
          <span class="fw-medium">{{ loadingText }}</span>
        </div>
      </ng-template>

    </ng-select>
  `,
  styles: [`
    :host {
      display: block;
      width: 100%;
    }

    .is-invalid ::ng-deep .ng-select-container {
      border-color: #dc3545 !important;
      box-shadow: 0 0 0 0.25rem rgba(220, 53, 69, 0.25) !important;
    }

    ::ng-deep .ng-dropdown-panel {
      z-index: 1050 !important;
      border-radius: 0.5rem !important;
      box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
    }

    ::ng-deep .ng-option {
      padding: 0.75rem 1rem !important;
    }

    ::ng-deep .ng-option.ng-option-disabled {
      opacity: 0.5;
      cursor: not-allowed !important;
    }

    .dropdown-option.disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }

    .option-label {
      font-weight: 500;
      color: #212529;
    }

    .option-additional {
      font-size: 0.85em;
      margin-top: 0.125rem;
      display: block;
    }
  `],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => NgselectDropdownComponent),
      multi: true
    }
  ]
})
export class NgselectDropdownComponent implements ControlValueAccessor {
  @Input() items: DropdownItem[] = [];
  @Input() placeholder: string = 'Seleccione una opción';
  @Input() loading: boolean = false;
  @Input() multiple: boolean = false;
  @Input() isInvalid: boolean = false;
  @Input() disabled: boolean = false;
  @Input() noResultsText: string = 'No se encontraron resultados';
  @Input() loadingText: string = 'Cargando...';

  @Output() selectionChange = new EventEmitter<any>();
  @Output() searchChange = new EventEmitter<string>();
  @Output() opened = new EventEmitter<void>();
  @Output() closed = new EventEmitter<void>();

  // Para ControlValueAccessor
  internalValue: any = null;
  private onChange: (value: any) => void = () => {};
  private onTouched: () => void = () => {};

  /**
   * Muestra el texto del ítem
   * Reglas:
   * 1. Si label existe y no es null/empty → usar label
   * 2. Si label es null/empty pero adicional existe → usar adicional
   * 3. Si ambos son null/empty → mostrar "—"
   */
  getItemDisplay(item: DropdownItem): string {
    if (!item) return '—';

    // Si label existe y no está vacío
    if (item.label && item.label.trim() !== '') {
      return item.label;
    }

    // Si label es null/vacío pero adicional existe
    if (item.adicional && item.adicional.trim() !== '') {
      return item.adicional;
    }

    // Ambos son null/vacíos
    return '—';
  }

  /**
   * Verifica si un ítem está seleccionado
   */
  isItemSelected(item: DropdownItem): boolean {
    if (!item || this.internalValue === null || this.internalValue === undefined) {
      return false;
    }

    if (this.multiple) {
      // Para múltiple selección
      return Array.isArray(this.internalValue)
        ? this.internalValue.some(val => this.compareItems(val, item))
        : false;
    } else {
      // Para selección simple
      return this.compareItems(this.internalValue, item);
    }
  }

  /**
   * Compara dos ítems por ID
   */
  compareItems(item1: any, item2: any): boolean {
    if (!item1 || !item2) return false;

    // Si ambos son objetos DropdownItem, comparar por ID
    if (typeof item1 === 'object' && typeof item2 === 'object') {
      return item1.id === item2.id;
    }

    // Si son valores primitivos (IDs)
    return item1 === item2;
  }

  onSelectionChange(event: any): void {
    this.onChange(event);
    this.onTouched();

    // Emitir evento con formato consistente
    if (this.multiple && Array.isArray(event)) {
      this.selectionChange.emit({
        ids: event.map(item => item.id),
        items: event
      });
    } else if (event) {
      this.selectionChange.emit({
        id: event.id,
        label: this.getItemDisplay(event),
        adicional: event.adicional
      });
    } else {
      this.selectionChange.emit(null);
    }
  }

onSearch(event: { term: string; items: any[] }) {
  console.log(event.term);
}


  onOpen(): void {
    this.opened.emit();
  }

  onClose(): void {
    this.closed.emit();
  }

  // ControlValueAccessor implementation
  writeValue(value: any): void {
    if (this.multiple && Array.isArray(value)) {
      // Para múltiple: encontrar los objetos completos basados en IDs
      this.internalValue = value.map(id =>
        this.items.find(item => item.id === id)
      ).filter(item => item !== undefined);
    } else if (value && typeof value === 'number') {
      // Para simple: encontrar el objeto completo basado en ID
      this.internalValue = this.items.find(item => item.id === value);
    } else {
      this.internalValue = value;
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }
}

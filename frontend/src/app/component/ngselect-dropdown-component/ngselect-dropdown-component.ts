import { Component, Input, Output, EventEmitter, forwardRef, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { Subject, debounceTime, distinctUntilChanged, Subscription } from 'rxjs';

export interface DropdownItem {
  id: number;
  label?: string;
  adicional?: string;
  estado?: boolean | null;  // ← CAMBIA ESTO
}

@Component({
  selector: 'app-ngselect-dropdown',
  standalone: true,
  imports: [CommonModule, FormsModule, NgSelectModule],
  template: `
    <div class="dropdown-wrapper" [class.disabled-wrapper]="disabled">
      <!-- Label mejorado con icono -->
      <label *ngIf="label" class="dropdown-label">
        <div class="label-content">
          <span class="label-text">{{ label }}</span>
          <span *ngIf="required" class="required-indicator">*</span>
          <span *ngIf="helpText" class="help-icon" [title]="helpText">
            <i class="bi bi-info-circle"></i>
          </span>
        </div>
      </label>

      <!-- Contenedor principal del dropdown -->
      <div class="dropdown-container" [class.has-error]="isInvalid" [class.is-focused]="isFocused">
        <!-- Icono izquierdo opcional -->
        <div *ngIf="leftIcon" class="dropdown-left-icon">
          <i class="bi" [class]="leftIcon"></i>
        </div>

        <!-- Dropdown principal -->
        <ng-select
          [items]="items"
          [placeholder]="placeholder"
          [searchable]="true"
          [clearable]="true"
          [loading]="loading"
          [virtualScroll]="items.length > 50"
          [multiple]="multiple"
          [closeOnSelect]="!multiple"
          [ngModel]="selectedItem"
          (ngModelChange)="onModelChange($event)"
          (search)="onSearch($event)"
          (open)="onOpen()"
          (close)="onClose()"
          (focus)="isFocused = true"
          (blur)="isFocused = false"
          [disabled]="disabled"
          [compareWith]="compareById"
          class="custom-ng-select">

          <!-- Template para mostrar seleccionado -->
          <ng-template ng-label-tmp let-item="item">
            <div class="selected-item">
              <span *ngIf="item" class="selected-text">
                {{ getItemDisplay(item) }}
              </span>
              <span *ngIf="!item" class="placeholder-text">
                {{ placeholder }}
              </span>
            </div>
          </ng-template>

          <!-- Template para opciones -->
          <ng-template ng-option-tmp let-item="item" let-index="index" let-search="searchTerm">
            <div class="dropdown-option" [class.is-disabled]="item.estado === false">
              <div class="option-content">
                <!-- Checkbox para selección múltiple -->
                <div *ngIf="multiple" class="option-checkbox">
                  <div class="checkbox-wrapper">
                    <div class="custom-checkbox" [class.checked]="isItemSelected(item)">
                      <i class="bi bi-check"></i>
                    </div>
                  </div>
                </div>

                <!-- Contenido de la opción -->
                <div class="option-main">
                  <div class="option-header">
                    <span class="option-label">{{ getItemDisplay(item) }}</span>
                    <span *ngIf="item.estado === false" class="badge-inactive">
                      <i class="bi bi-slash-circle"></i> Inactivo
                    </span>
                    <i *ngIf="!multiple && isItemSelected(item)" class="bi bi-check-lg option-selected-icon"></i>
                  </div>

                  <div *ngIf="item.adicional" class="option-additional">
                    <i class="bi bi-info-circle"></i>
                    <span>{{ item.adicional }}</span>
                  </div>
                </div>
              </div>
            </div>
          </ng-template>

          <!-- Sin resultados -->
          <ng-template ng-notfound-tmp let-searchTerm="searchTerm">
            <div class="no-results">
              <div class="no-results-icon">
                <i class="bi bi-search"></i>
              </div>
              <h4 class="no-results-title">{{ noResultsText }}</h4>
              <p *ngIf="searchTerm" class="no-results-search">
                No se encontraron resultados para "<strong>{{ searchTerm }}</strong>"
              </p>
              <div *ngIf="items.length === 0 && !searchTerm" class="empty-state">
                <i class="bi bi-inbox"></i>
                <p>No hay opciones disponibles</p>
              </div>
            </div>
          </ng-template>

          <!-- Loading -->
          <ng-template ng-loadingtext-tmp>
            <div class="loading-state">
              <div class="loading-spinner">
                <div class="spinner"></div>
              </div>
              <p class="loading-text">{{ loadingText }}</p>
            </div>
          </ng-template>
        </ng-select>

        <!-- Icono derecho (estado) -->
        <div class="dropdown-right-icon">
          <i *ngIf="isInvalid" class="bi bi-exclamation-circle error-icon"></i>
          <i *ngIf="!isInvalid && selectedItem && !multiple" class="bi bi-check-circle success-icon"></i>
          <i *ngIf="!isInvalid && multiple && selectedItem?.length > 0" class="bi bi-check-circle success-icon"></i>
        </div>
      </div>

      <!-- Contador de selecciones para múltiple -->
      <div *ngIf="multiple && selectedItem?.length > 0" class="selection-counter">
        <span class="counter-badge">
          <i class="bi bi-check2-all"></i>
          {{ selectedItem.length }} seleccionado{{ selectedItem.length !== 1 ? 's' : '' }}
        </span>
      </div>

      <!-- Mensajes de ayuda y error -->
      <div *ngIf="errorText && isInvalid" class="error-message">
        <i class="bi bi-exclamation-triangle"></i>
        <span>{{ errorText }}</span>
      </div>

      <div *ngIf="helpText && !errorText" class="help-message">
        <i class="bi bi-info-circle"></i>
        <span>{{ helpText }}</span>
      </div>
    </div>
  `,


  styles:[`
:host {
  display: block;
  width: 100%;
}

/* Contenedor principal */
.dropdown-wrapper {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  margin-bottom: 1.5rem;
}

.dropdown-wrapper.disabled-wrapper {
  opacity: 0.6;
  pointer-events: none;
}

/* Label mejorado */
.dropdown-label {
  display: block;
  margin-bottom: 0.75rem;
}

.label-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.label-text {
  color: #374151;
  font-weight: 600;
  font-size: 0.875rem;
  letter-spacing: -0.01em;
}

.required-indicator {
  color: #ef4444;
  font-weight: 700;
  margin-left: 2px;
}

.help-icon {
  color: #6b7280;
  font-size: 0.875rem;
  cursor: help;
  opacity: 0.7;
  transition: opacity 0.2s;
}

.help-icon:hover {
  opacity: 1;
}

/* Contenedor del dropdown */
.dropdown-container {
  position: relative;
  display: flex;
  align-items: center;
  border-radius: 12px;
  background: #ffffff;
  border: 2px solid #e5e7eb;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  width: 100%; /* ✅ ASEGURAR ancho completo */
}

.dropdown-container:hover {
  border-color: #9ca3af;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.dropdown-container.is-focused {
  border-color: #3b82f6;
  box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.15);
}

.dropdown-container.has-error {
  border-color: #ef4444 !important;
  background: linear-gradient(0deg, rgba(239, 68, 68, 0.02), rgba(239, 68, 68, 0.02)), #ffffff;
}

.dropdown-container.has-error:hover {
  border-color: #dc2626 !important;
}

/* Iconos laterales */
.dropdown-left-icon,
.dropdown-right-icon {
  display: flex;
  align-items: center;
  padding: 0 1rem;
  color: #6b7280;
  transition: color 0.2s;
  flex-shrink: 0; /* ✅ EVITAR que se encojan */
}

.dropdown-left-icon {
  border-right: 1px solid #f3f4f6;
}

.dropdown-right-icon {
  border-left: 1px solid #f3f4f6;
}

.success-icon {
  color: #10b981;
}

.error-icon {
  color: #ef4444;
}

/* NgSelect personalizado */
::ng-deep .custom-ng-select .ng-select-container {
  min-height: 56px;
  height: auto;
  border: none !important;
  background: transparent !important;
  box-shadow: none !important;
  padding: 0.75rem 1rem;
  width: 100%; /* ✅ ASEGURAR ancho completo */
}

::ng-deep .custom-ng-select .ng-select-container:hover {
  background: transparent !important;
}

::ng-deep .custom-ng-select .ng-placeholder {
  color: #9ca3af;
  font-weight: 400;
  font-size: 0.9375rem;
  opacity: 0.8;
}

::ng-deep .custom-ng-select .ng-value-container {
  padding-left: 0 !important;
  width: 100%; /* ✅ ASEGURAR ancho completo */
}

/* Texto seleccionado - MEJORADO para texto largo */
.selected-item {
  display: flex;
  align-items: center;
  min-height: 1.5rem;
  width: 100%; /* ✅ ASEGURAR ancho completo */
}

.selected-text {
  color: #111827;
  font-weight: 500;
  font-size: 0.9375rem;
  line-height: 1.5;
  word-break: break-word; /* ✅ Permite romper palabras largas */
  overflow-wrap: break-word; /* ✅ Para palabras muy largas */
  white-space: normal; /* ✅ Permite múltiples líneas */
  flex: 1; /* ✅ Ocupa todo el espacio disponible */
  min-width: 0; /* ✅ Importante para que funcione flex con texto largo */
}

.placeholder-text {
  color: #9ca3af;
  font-weight: 400;
  word-break: break-word; /* ✅ Para placeholder largo */
}

/* Panel desplegable - MÁS ANCHO */
::ng-deep .custom-ng-select .ng-dropdown-panel {
  border-radius: 12px;
  border: 2px solid #e5e7eb;
  background: #ffffff;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.12);
  margin-top: 8px;
  animation: slideDown 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  min-width: 350px; /* ✅ ANCHO MÍNIMO aumentado */
  width: auto; /* ✅ Ancho automático basado en contenido */
  max-width: 500px; /* ✅ ANCHO MÁXIMO para evitar que sea enorme */
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-8px) scale(0.98);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* Opciones - MEJORADO para texto largo */
.dropdown-option {
  padding: 0.875rem 1rem;
  border-bottom: 1px solid #f9fafb;
  transition: all 0.2s;
  cursor: pointer;
  min-width: 0; /* ✅ Importante para contenedor flex con texto largo */
}

.dropdown-option:hover {
  background: linear-gradient(90deg, rgba(59, 130, 246, 0.08) 0%, rgba(59, 130, 246, 0.04) 100%) !important;
}

.dropdown-option.is-disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background: #f9fafb !important;
}

.dropdown-option.is-disabled:hover {
  background: #f9fafb !important;
}

.option-content {
  display: flex;
  align-items: flex-start; /* ✅ Cambiado a flex-start para mejor alineación */
  gap: 0.75rem;
  min-width: 0; /* ✅ Importante para texto largo */
}

/* Checkbox personalizado */
.option-checkbox {
  flex-shrink: 0; /* ✅ No se encoje */
  margin-top: 0.125rem; /* ✅ Ajuste fino para alineación */
}

.checkbox-wrapper {
  width: 20px;
  height: 20px;
  position: relative;
}

.custom-checkbox {
  width: 20px;
  height: 20px;
  border: 2px solid #d1d5db;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  background: #ffffff;
  flex-shrink: 0; /* ✅ No se encoje */
}

.custom-checkbox.checked {
  border-color: #3b82f6;
  background: #3b82f6;
}

.custom-checkbox.checked i {
  color: #ffffff;
  font-size: 0.75rem;
}

/* Contenido principal de la opción - MEJORADO para texto largo */
.option-main {
  flex: 1;
  min-width: 0; /* ✅ CRÍTICO: Permite que el texto se ajuste */
  overflow: hidden; /* ✅ Evita desbordamiento */
}

.option-header {
  display: flex;
  align-items: flex-start; /* ✅ Cambiado para mejor alineación con texto largo */
  justify-content: space-between;
  gap: 0.5rem;
  margin-bottom: 0.25rem;
  flex-wrap: wrap; /* ✅ Permite que los elementos se ajusten */
}

.option-label {
  color: #111827;
  font-weight: 500;
  font-size: 0.9375rem;
  flex: 1; /* ✅ Ocupa espacio disponible */
  line-height: 1.4;
  word-break: break-word; /* ✅ Permite romper palabras largas */
  overflow-wrap: break-word; /* ✅ Para palabras muy largas */
  white-space: normal; /* ✅ Permite múltiples líneas */
  min-width: 200px; /* ✅ Ancho mínimo para opciones */
  max-width: 100%; /* ✅ No exceder el contenedor */
}

.badge-inactive {
  background: #f3f4f6;
  color: #6b7280;
  padding: 0.25rem 0.5rem;
  border-radius: 6px;
  font-size: 0.75rem;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  flex-shrink: 0; /* ✅ No se encoje */
  margin-left: auto; /* ✅ Empuja a la derecha */
}

.option-selected-icon {
  color: #3b82f6;
  font-size: 1rem;
  flex-shrink: 0; /* ✅ No se encoje */
  margin-left: 0.5rem;
}

.option-additional {
  display: flex;
  align-items: flex-start; /* ✅ Cambiado para mejor alineación */
  gap: 0.5rem;
  color: #6b7280;
  font-size: 0.8125rem;
  line-height: 1.4;
  word-break: break-word; /* ✅ Para texto adicional largo */
}

.option-additional i {
  font-size: 0.75rem;
  opacity: 0.7;
  flex-shrink: 0; /* ✅ Icono no se encoje */
  margin-top: 0.125rem; /* ✅ Ajuste fino para alineación */
}

/* Estado sin resultados */
.no-results {
  padding: 2.5rem 1.5rem;
  text-align: center;
  min-width: 300px; /* ✅ Ancho mínimo para mensajes */
}

.no-results-icon {
  color: #9ca3af;
  font-size: 3rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.no-results-title {
  color: #374151;
  font-weight: 600;
  font-size: 1rem;
  margin-bottom: 0.5rem;
  word-break: break-word; /* ✅ Para títulos largos */
}

.no-results-search {
  color: #6b7280;
  font-size: 0.875rem;
  line-height: 1.5;
  word-break: break-word; /* ✅ Para texto de búsqueda largo */
}

.empty-state {
  color: #9ca3af;
  margin-top: 1rem;
}

.empty-state i {
  font-size: 2.5rem;
  margin-bottom: 0.75rem;
  display: block;
}

/* Estado de carga */
.loading-state {
  padding: 2.5rem 1.5rem;
  text-align: center;
  min-width: 300px; /* ✅ Ancho mínimo */
}

.loading-spinner {
  margin-bottom: 1rem;
}

.spinner {
  width: 2.5rem;
  height: 2.5rem;
  border: 3px solid #e5e7eb;
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-text {
  color: #6b7280;
  font-weight: 500;
  font-size: 0.9375rem;
}

/* Contador de selecciones */
.selection-counter {
  margin-top: 0.75rem;
}

.counter-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: #f0f9ff;
  color: #0369a1;
  padding: 0.375rem 0.875rem;
  border-radius: 20px;
  font-size: 0.8125rem;
  font-weight: 500;
  border: 1px solid #bae6fd;
}

/* Mensajes de error y ayuda */
.error-message,
.help-message {
  display: flex;
  align-items: flex-start; /* ✅ Cambiado para texto largo */
  gap: 0.5rem;
  margin-top: 0.5rem;
  font-size: 0.8125rem;
  line-height: 1.4;
  padding: 0.5rem 0.75rem;
  border-radius: 8px;
  animation: fadeIn 0.3s ease;
  word-break: break-word; /* ✅ Para mensajes largos */
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-4px); }
  to { opacity: 1; transform: translateY(0); }
}

.error-message {
  background: linear-gradient(0deg, rgba(239, 68, 68, 0.05), rgba(239, 68, 68, 0.05)), #fef2f2;
  color: #b91c1c;
  border-left: 3px solid #ef4444;
}

.help-message {
  background: #f0f9ff;
  color: #0369a1;
  border-left: 3px solid #0ea5e9;
}

/* Para selección múltiple - Tags MEJORADO */
::ng-deep .custom-ng-select .ng-value-container .ng-value {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  border-radius: 20px;
  padding: 0.375rem 0.875rem;
  margin-right: 0.5rem;
  margin-bottom: 0.5rem;
  font-size: 0.8125rem;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  box-shadow: 0 2px 5px rgba(59, 130, 246, 0.25);
  animation: popIn 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  max-width: 100%; /* ✅ Limitar ancho máximo */
  word-break: break-word; /* ✅ Para texto largo en tags */
}

@keyframes popIn {
  from {
    opacity: 0;
    transform: scale(0.8) translateY(4px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

::ng-deep .custom-ng-select .ng-value-container .ng-value-icon {
  color: rgba(255, 255, 255, 0.9);
  transition: all 0.2s;
  margin-left: 0.5rem;
  font-size: 0.75rem;
  flex-shrink: 0; /* ✅ Icono no se encoje */
}

::ng-deep .custom-ng-select .ng-value-container .ng-value-icon:hover {
  color: white;
  transform: scale(1.1);
}

/* Scrollbar personalizada */
::ng-deep .custom-ng-select .ng-dropdown-panel .ng-dropdown-panel-items {
  max-height: 280px;
  overflow-y: auto;
}

::ng-deep .custom-ng-select .ng-dropdown-panel .ng-dropdown-panel-items::-webkit-scrollbar {
  width: 8px;
}

::ng-deep .custom-ng-select .ng-dropdown-panel .ng-dropdown-panel-items::-webkit-scrollbar-track {
  background: #f9fafb;
  border-radius: 4px;
}

::ng-deep .custom-ng-select .ng-dropdown-panel .ng-dropdown-panel-items::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 4px;
}

::ng-deep .custom-ng-select .ng-dropdown-panel .ng-dropdown-panel-items::-webkit-scrollbar-thumb:hover {
  background: #9ca3af;
}

/* Responsive */
@media (max-width: 768px) {
  .dropdown-container {
    border-radius: 10px;
  }

  ::ng-deep .custom-ng-select .ng-select-container {
    min-height: 52px;
    padding: 0.625rem 0.875rem;
  }

  .dropdown-left-icon,
  .dropdown-right-icon {
    padding: 0 0.75rem;
  }

  /* En móviles, el panel ocupa casi toda la pantalla */
  ::ng-deep .custom-ng-select .ng-dropdown-panel {
    min-width: calc(100vw - 40px) !important; /* ✅ Ocupa casi todo el ancho */
    max-width: calc(100vw - 40px) !important;
    left: 50% !important;
    transform: translateX(-50%) !important;
  }

  .option-label {
    min-width: 150px; /* ✅ Ancho mínimo reducido en móvil */
  }
}

/* Animación para cambios de estado */
.dropdown-wrapper ::ng-deep .ng-select.ng-select-opened .ng-arrow {
  transform: rotate(180deg);
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* ✅ NUEVO: Estilo para input de búsqueda */
::ng-deep .custom-ng-select .ng-input input {
  width: 100% !important;
  min-width: 200px !important;
  font-size: 0.9375rem;
  color: #111827;
}

/* ✅ NUEVO: Asegurar que el contenedor no se desborde */
::ng-deep .custom-ng-select {
  width: 100% !important;
  min-width: 0 !important; /* ✅ Importante para flex */
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
export class NgselectDropdownComponent implements ControlValueAccessor, OnInit, OnDestroy {
  @Input() items: DropdownItem[] = [];
  @Input() label: string = '';
  @Input() placeholder: string = 'Seleccione una opción';
  @Input() loading: boolean = false;
  @Input() multiple: boolean = false;
  @Input() required: boolean = false;
  @Input() disabled: boolean = false;
  @Input() isInvalid: boolean = false;
  @Input() errorText: string = '';
  @Input() helpText: string = '';
  @Input() noResultsText: string = 'No se encontraron resultados';
  @Input() loadingText: string = 'Cargando...';
  @Input() leftIcon: string = '';

  @Output() selectionChange = new EventEmitter<any>();
  @Output() searchChange = new EventEmitter<string>();
  @Output() opened = new EventEmitter<void>();
  @Output() closed = new EventEmitter<void>();

  selectedItem: any = null;
  isFocused: boolean = false;
  private searchSubject = new Subject<string>();
  private searchSubscription?: Subscription;

  private onChange: (value: any) => void = () => {};
  private onTouched: () => void = () => {};

  ngOnInit(): void {
    this.searchSubscription = this.searchSubject
      .pipe(
        debounceTime(300),
        distinctUntilChanged()
      )
      .subscribe(term => {
        this.searchChange.emit(term);
      });
  }

  ngOnDestroy(): void {
    this.searchSubscription?.unsubscribe();
  }

  getItemDisplay(item: DropdownItem): string {
    if (!item) return '—';

    if (item.label && item.label.trim() !== '') {
      return item.label;
    }

    if (item.adicional && item.adicional.trim() !== '') {
      return item.adicional;
    }

    return '—';
  }

  isItemSelected(item: DropdownItem): boolean {
    if (!item || !this.selectedItem) {
      return false;
    }

    if (this.multiple) {
      return Array.isArray(this.selectedItem)
        ? this.selectedItem.some((selected: any) => this.compareById(selected, item))
        : false;
    } else {
      return this.compareById(this.selectedItem, item);
    }
  }

  compareById(item1: any, item2: any): boolean {
    if (!item1 || !item2) return false;

    const id1 = typeof item1 === 'object' ? item1.id : item1;
    const id2 = typeof item2 === 'object' ? item2.id : item2;

    return id1 === id2;
  }

  onModelChange(event: any): void {
    console.log('NgSelect - Model changed:', event);

    // Actualizar el valor interno
    this.selectedItem = event;

    // Determinar qué valor enviar al formControl
    let valueToSend: any;

    if (this.multiple && Array.isArray(event)) {
      valueToSend = event.map(item => item?.id || item);
    } else if (event) {
      // Si es un objeto, enviar solo el ID; si ya es un número, enviar el número
      valueToSend = typeof event === 'object' ? event.id : event;
    } else {
      valueToSend = null;
    }

    console.log('NgSelect - Sending to formControl:', valueToSend);

    // Notificar al formulario
    this.onChange(valueToSend);
    this.onTouched();

    // Emitir evento personalizado
    this.selectionChange.emit(event);
  }

  onSearch(event: { term: string; items: any[] }): void {
    this.searchSubject.next(event.term);
  }

  onOpen(): void {
    this.opened.emit();
  }

  onClose(): void {
    this.closed.emit();
  }

  writeValue(value: any): void {
    console.log('NgSelect - writeValue called:', value);

    if (!value) {
      this.selectedItem = null;
      return;
    }

    // Convertir ID numérico a objeto correspondiente
    if (this.multiple && Array.isArray(value)) {
      this.selectedItem = value.map(val => {
        if (typeof val === 'object') {
          return val; // Ya es un objeto
        } else if (typeof val === 'number') {
          // Buscar el objeto por ID
          return this.items.find(item => item.id === val);
        }
        return null;
      }).filter(item => item !== null);
    } else if (value) {
      if (typeof value === 'object') {
        this.selectedItem = value; // Ya es un objeto
      } else if (typeof value === 'number') {
        // Buscar el objeto por ID
        this.selectedItem = this.items.find(item => item.id === value);
      } else {
        this.selectedItem = value;
      }
    } else {
      this.selectedItem = null;
    }

    console.log('NgSelect - selectedItem after writeValue:', this.selectedItem);
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

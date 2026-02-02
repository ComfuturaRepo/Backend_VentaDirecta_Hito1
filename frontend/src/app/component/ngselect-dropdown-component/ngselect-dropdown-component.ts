import { Component, Input, Output, EventEmitter, forwardRef, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { Subject, debounceTime, distinctUntilChanged, Subscription } from 'rxjs';

export interface DropdownItem {
  id: number;
  label?: string;
  adicional?: string;
  estado?: boolean;
}

@Component({
  selector: 'app-ngselect-dropdown',
  standalone: true,
  imports: [CommonModule, FormsModule, NgSelectModule],
  template: `
    <div class="dropdown-wrapper" [class.disabled-wrapper]="disabled">
      <!-- Label opcional -->
      <label *ngIf="label" class="dropdown-label form-label fw-semibold mb-2">
        {{ label }}
        <span *ngIf="required" class="text-danger">*</span>
      </label>

      <!-- Dropdown principal -->
      <ng-select
        [items]="items"
        [placeholder]="placeholder"
        [searchable]="true"
        [clearable]="true"
        [loading]="loading"
        [virtualScroll]="items.length > 100"
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
          <div class="selected-item-wrapper">
            <span *ngIf="item" class="selected-item">
              {{ getItemDisplay(item) }}
            </span>
          </div>
        </ng-template>

        <!-- Template para opciones -->
        <ng-template ng-option-tmp let-item="item" let-index="index" let-search="searchTerm">
          <div class="dropdown-option" [class.disabled]="item.estado === false">
            <div class="d-flex align-items-center">
              <!-- Checkbox para múltiple selección -->
              <div *ngIf="multiple" class="me-3">
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
          <div class="no-results text-center p-4">
            <i class="bi bi-search text-muted fs-4 mb-3"></i>
            <p class="mb-1 fw-medium text-dark">{{ noResultsText }}</p>
            <small *ngIf="searchTerm" class="text-muted">
              Búsqueda: "<span class="fw-medium">{{ searchTerm }}</span>"
            </small>
            <div *ngIf="items.length === 0 && !searchTerm" class="mt-3">
              <i class="bi bi-inbox text-muted fs-3 mb-2"></i>
              <p class="text-muted mb-0">No hay opciones disponibles</p>
            </div>
          </div>
        </ng-template>

        <!-- Loading -->
        <ng-template ng-loadingtext-tmp>
          <div class="loading-wrapper text-center p-4">
            <div class="spinner-border text-primary mb-3" style="width: 2rem; height: 2rem;" role="status">
              <span class="visually-hidden">Cargando...</span>
            </div>
            <p class="fw-medium text-dark mb-0">{{ loadingText }}</p>
          </div>
        </ng-template>
      </ng-select>

      <!-- Mensajes de ayuda y error -->
      <div *ngIf="helpText" class="help-text form-text text-muted mt-2">
        <i class="bi bi-info-circle me-1"></i>
        {{ helpText }}
      </div>
      <div *ngIf="errorText" class="error-text invalid-feedback d-flex align-items-center mt-2">
        <i class="bi bi-exclamation-triangle me-2"></i>
        {{ errorText }}
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: block;
      width: 100%;
    }

    .dropdown-wrapper {
      transition: all 0.3s ease;
      margin-bottom: 1.5rem;
    }

    .disabled-wrapper {
      opacity: 0.6;
      pointer-events: none;
    }

    .dropdown-label {
      color: #2c3e50;
      font-size: 0.95rem;
      margin-bottom: 0.5rem;
      display: block;
    }

    /* Container styles - FONDO BLANCO SÓLIDO */
    ::ng-deep .ng-select-container {
      min-height: 48px;
      border-radius: 10px;
      border: 2px solid #e2e8f0;
      background: #ffffff !important; /* FONDO BLANCO SÓLIDO */
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
      font-size: 0.95rem;
    }

    ::ng-deep .ng-select-container:hover {
      border-color: #94a3b8;
      background: #ffffff;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    }

    ::ng-deep .ng-select-container.ng-select-focused {
      border-color: #3b82f6;
      background: #ffffff;
      box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.15);
    }

    ::ng-deep .ng-select-container.is-invalid {
      border-color: #dc3545 !important;
      background: #fff5f5 !important;
      box-shadow: 0 0 0 4px rgba(220, 53, 69, 0.15) !important;
    }

    ::ng-deep .ng-select-container.ng-select-disabled {
      background: #f8f9fa !important;
      border-color: #e2e8f0 !important;
      color: #6c757d;
    }

    /* Placeholder */
    ::ng-deep .ng-placeholder {
      color: #94a3b8;
      font-size: 0.95rem;
      font-weight: 400;
    }

    /* Selected item */
    .selected-item-wrapper {
      display: flex;
      align-items: center;
      padding: 2px 0;
    }

    .selected-item {
      color: #2c3e50;
      font-weight: 500;
      font-size: 0.95rem;
    }

    /* Dropdown panel - FONDO BLANCO SÓLIDO */
    ::ng-deep .ng-dropdown-panel {
      z-index: 1080;
      border-radius: 10px;
      border: 2px solid #e2e8f0;
      background: #ffffff !important; /* FONDO BLANCO SÓLIDO */
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.12);
      margin-top: 8px;
      animation: slideDown 0.2s ease-out;
    }

    @keyframes slideDown {
      from {
        opacity: 0;
        transform: translateY(-10px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    /* Options */
    ::ng-deep .ng-option {
      padding: 12px 18px !important;
      border-bottom: 1px solid #f1f5f9;
      transition: all 0.2s ease;
      background: #ffffff !important; /* FONDO BLANCO SÓLIDO */
    }

    ::ng-deep .ng-option:hover {
      background: linear-gradient(90deg, rgba(59, 130, 246, 0.08) 0%, rgba(59, 130, 246, 0.04) 100%) !important;
      padding-left: 22px !important;
    }

    ::ng-deep .ng-option.ng-option-selected {
      background: linear-gradient(90deg, rgba(59, 130, 246, 0.12) 0%, rgba(59, 130, 246, 0.06) 100%) !important;
      font-weight: 600;
      color: #1e40af;
    }

    ::ng-deep .ng-option.ng-option-marked {
      background-color: #f8fafc !important;
    }

    ::ng-deep .ng-option.ng-option-disabled {
      opacity: 0.5;
      cursor: not-allowed;
      background: #f8f9fa !important;
    }

    /* Option styling */
    .dropdown-option.disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }

    .option-label {
      font-weight: 500;
      color: #2c3e50;
      font-size: 0.95rem;
      margin-bottom: 2px;
    }

    .option-additional {
      font-size: 0.85em;
      margin-top: 2px;
      display: block;
      color: #64748b;
      line-height: 1.4;
    }

    /* Multiple selection tags */
    ::ng-deep .ng-value-container .ng-value {
      background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
      color: white;
      border-radius: 20px;
      padding: 6px 14px;
      margin-right: 6px;
      margin-bottom: 6px;
      font-size: 0.85rem;
      font-weight: 500;
      display: inline-flex;
      align-items: center;
      box-shadow: 0 2px 5px rgba(59, 130, 246, 0.25);
    }

    ::ng-deep .ng-value-container .ng-value-icon {
      color: white;
      opacity: 0.9;
      transition: all 0.2s ease;
      margin-left: 8px;
      font-size: 12px;
    }

    ::ng-deep .ng-value-container .ng-value-icon:hover {
      opacity: 1;
      transform: scale(1.1);
    }

    /* Clear and dropdown arrows */
    ::ng-deep .ng-arrow-wrapper,
    ::ng-deep .ng-clear-wrapper {
      color: #64748b;
      transition: color 0.2s ease;
    }

    ::ng-deep .ng-clear-wrapper:hover {
      color: #dc3545;
    }

    ::ng-deep .ng-arrow-wrapper:hover {
      color: #3b82f6;
    }

    /* Search input */
    ::ng-deep .ng-input input {
      color: #2c3e50;
      font-size: 0.95rem;
      padding: 8px 12px;
    }

    ::ng-deep .ng-input input:focus {
      outline: none;
      box-shadow: none;
    }

    /* No results styling */
    .no-results {
      background: #ffffff;
    }

    .no-results i {
      color: #94a3b8;
    }

    /* Loading styling */
    .loading-wrapper {
      background: #ffffff;
    }

    /* Help and error text */
    .help-text {
      font-size: 0.85rem;
      color: #64748b;
      display: flex;
      align-items: center;
    }

    .error-text {
      font-size: 0.85rem;
      color: #dc3545;
      display: flex;
      align-items: center;
      padding: 8px 12px;
      background: #fff5f5;
      border-radius: 6px;
      border-left: 4px solid #dc3545;
    }

    /* Responsive */
    @media (max-width: 768px) {
      ::ng-deep .ng-select-container {
        min-height: 46px;
        font-size: 0.9rem;
      }

      ::ng-deep .ng-option {
        padding: 10px 16px !important;
      }

      .dropdown-label {
        font-size: 0.9rem;
      }

      .option-label {
        font-size: 0.9rem;
      }
    }

    /* Scrollbar styling */
    ::ng-deep .ng-dropdown-panel .ng-dropdown-panel-items {
      max-height: 300px;
      overflow-y: auto;
    }

    ::ng-deep .ng-dropdown-panel .ng-dropdown-panel-items::-webkit-scrollbar {
      width: 6px;
    }

    ::ng-deep .ng-dropdown-panel .ng-dropdown-panel-items::-webkit-scrollbar-track {
      background: #f1f5f9;
      border-radius: 3px;
    }

    ::ng-deep .ng-dropdown-panel .ng-dropdown-panel-items::-webkit-scrollbar-thumb {
      background: #cbd5e1;
      border-radius: 3px;
    }

    ::ng-deep .ng-dropdown-panel .ng-dropdown-panel-items::-webkit-scrollbar-thumb:hover {
      background: #94a3b8;
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

  @Output() selectionChange = new EventEmitter<any>();
  @Output() searchChange = new EventEmitter<string>();
  @Output() opened = new EventEmitter<void>();
  @Output() closed = new EventEmitter<void>();

  internalValue: any = null;
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
    if (!item || this.internalValue == null) {
      return false;
    }

    if (this.multiple) {
      return Array.isArray(this.internalValue)
        ? this.internalValue.some(val => this.compareItems(val, item))
        : false;
    } else {
      return this.compareItems(this.internalValue, item);
    }
  }

  compareItems(item1: any, item2: any): boolean {
    if (!item1 || !item2) return false;

    if (typeof item1 === 'object' && typeof item2 === 'object') {
      return item1.id === item2.id;
    }

    return item1 === item2;
  }

  onSelectionChange(event: any): void {
    this.onChange(event);
    this.onTouched();

    if (this.multiple && Array.isArray(event)) {
      this.selectionChange.emit({
        ids: event.map(item => item.id),
        items: event,
        count: event.length
      });
    } else if (event) {
      this.selectionChange.emit({
        id: event.id,
        label: this.getItemDisplay(event),
        adicional: event.adicional,
        item: event
      });
    } else {
      this.selectionChange.emit(null);
    }
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
    if (this.multiple && Array.isArray(value)) {
      this.internalValue = value.map(id =>
        this.items.find(item => item.id === id)
      ).filter(item => item !== undefined);
    } else if (value && typeof value === 'number') {
      this.internalValue = this.items.find(item => item.id === value);
    } else if (value && typeof value === 'object' && value.id) {
      this.internalValue = value;
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

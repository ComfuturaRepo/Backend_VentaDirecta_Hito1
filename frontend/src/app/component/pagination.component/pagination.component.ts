// src/app/shared/components/pagination/pagination.component.ts
import { Component, Input, Output, EventEmitter, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent {
  @Input() currentPage: number = 0;
  @Input() totalItems: number = 0;
  @Input() totalPages: number = 0;
  @Input() pageSize: number = 10;
  @Input() showInfo: boolean = true;
  @Input() showSizeSelector: boolean = true;
  @Input() showNavigation: boolean = true;
  @Input() showPageNumbers: boolean = true;
  @Input() showJumpToPage: boolean = true;
  @Input() pageSizes: number[] = [10, 20, 50, 100];
  @Input() align: 'start' | 'center' | 'end' = 'center';
  @Input() size: 'sm' | 'md' | 'lg' = 'md';
  @Input() isLoading: boolean = false;

  @Output() pageChange = new EventEmitter<number>();
  @Output() pageSizeChange = new EventEmitter<number>();
  @Output() refresh = new EventEmitter<void>();

  showJumpInput: boolean = false;
  jumpToPage: number = 1;
  isMobile: boolean = false;

  ngOnInit(): void {
    this.checkIfMobile();
    this.jumpToPage = this.currentPage + 1;
  }

  @HostListener('window:resize')
  onResize(): void {
    this.checkIfMobile();
  }

  private checkIfMobile(): void {
    this.isMobile = window.innerWidth < 768;
  }

  get showingFrom(): number {
    return this.totalItems > 0 ? (this.currentPage * this.pageSize) + 1 : 0;
  }

  get showingTo(): number {
    return Math.min((this.currentPage + 1) * this.pageSize, this.totalItems);
  }

  get isFirstPage(): boolean {
    return this.currentPage === 0;
  }

  get isLastPage(): boolean {
    return this.currentPage === this.totalPages - 1;
  }

  get hasResults(): boolean {
    return this.totalItems > 0;
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages && page !== this.currentPage && !this.isLoading) {
      this.pageChange.emit(page);
    }
  }

  changePageSize(size: number): void {
    if (this.pageSize !== size && !this.isLoading) {
      this.pageSizeChange.emit(size);
    }
  }

  goToFirstPage(): void {
    this.goToPage(0);
  }

  goToLastPage(): void {
    this.goToPage(this.totalPages - 1);
  }

  goToPrevPage(): void {
    if (this.currentPage > 0) {
      this.goToPage(this.currentPage - 1);
    }
  }

  goToNextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.goToPage(this.currentPage + 1);
    }
  }

  jumpToSpecificPage(): void {
    const page = this.jumpToPage - 1;
    if (page >= 0 && page < this.totalPages && page !== this.currentPage && !this.isLoading) {
      this.goToPage(page);
    }
    this.showJumpInput = false;
  }

  toggleJumpInput(): void {
    if (this.isMobile) {
      this.showJumpInput = !this.showJumpInput;
    } else {
      this.showJumpInput = !this.showJumpInput;
      if (this.showJumpInput) {
        this.jumpToPage = this.currentPage + 1;
        setTimeout(() => {
          const input = document.querySelector('.jump-to-input') as HTMLInputElement;
          if (input) input.focus();
        }, 0);
      }
    }
  }

  onRefresh(): void {
    if (!this.isLoading) {
      this.refresh.emit();
    }
  }

  // Método para cerrar modal en móvil
  closeMobileModal(): void {
    if (this.isMobile) {
      this.showJumpInput = false;
    }
  }
}

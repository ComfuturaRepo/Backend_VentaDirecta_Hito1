// src/app/shared/models/pagination.model.ts
export interface PageResponse<T> {
  content: T[];
  currentPage: number;           // Página actual (0-based en backend)
  totalItems: number;           // Total de elementos
  totalPages: number;           // Total de páginas
  first: boolean;
  last: boolean;
  pageSize: number;            // Elementos por página
}

export interface PaginationConfig {
  showInfo?: boolean;          // OPCIONAL
  showSizeSelector?: boolean;  // OPCIONAL
  showNavigation?: boolean;    // OPCIONAL
  showJumpToPage?: boolean;    // OPCIONAL
  showPageNumbers?: boolean;   // OPCIONAL
  pageSizes?: number[];        // OPCIONAL
  maxPageNumbers?: number;     // OPCIONAL
  align?: 'start' | 'center' | 'end'; // OPCIONAL
  size?: 'sm' | 'md' | 'lg';   // OPCIONAL
}

export const DEFAULT_PAGINATION_CONFIG: PaginationConfig = {
  showInfo: true,
  showSizeSelector: true,
  showNavigation: true,
  showJumpToPage: true,
  showPageNumbers: true,
  pageSizes: [5, 10, 20, 50, 100],
  maxPageNumbers: 5,
  align: 'center',
  size: 'md'
};

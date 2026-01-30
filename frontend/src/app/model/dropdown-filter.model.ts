export interface DropdownConfig {
  placeholder?: string;
  searchPlaceholder?: string;
  noResultsText?: string;
  loadingText?: string;
  showClearButton?: boolean;
  showSearch?: boolean;
  multiple?: boolean;
  maxHeight?: string;
  width?: string;
  size?: 'sm' | 'md' | 'lg';
  theme?: 'light' | 'dark' | 'primary';
  showIcon?: boolean;
  groupBy?: string;
  virtualScroll?: boolean;
  itemHeight?: number;
  showAdditionalInfo?: boolean; // Para mostrar campo "adicional" si viene
}

export const DEFAULT_CONFIG: DropdownConfig = {
  placeholder: 'Seleccione una opción',
  searchPlaceholder: 'Buscar...',
  noResultsText: 'No se encontraron resultados',
  loadingText: 'Cargando...',
  showClearButton: true,
  showSearch: true,
  multiple: false,
  maxHeight: '300px',
  width: '100%',
  size: 'md',
  theme: 'light',
  showIcon: false,
  virtualScroll: false,
  itemHeight: 40,
  showAdditionalInfo: false // Por defecto no mostrar adicional
};

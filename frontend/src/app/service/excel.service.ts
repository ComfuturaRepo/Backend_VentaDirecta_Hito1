import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

export interface ImportResultDTO {
  inicio: number;
  fin: number;
  duracionMs: number;
  totalRegistros: number;
  exitosos: number;
  fallidos: number;
  exito: boolean;
  mensaje: string;
  erroresValidacion: number;
  erroresPersistencia: number;
  warnings: number;
  registrosProcesados: any[];
  registrosConError: any[];
  resumenErrores: string[];
}

@Injectable({
  providedIn: 'root'
})
export class ExcelService {
  private apiUrl = `${environment.baseUrl}/api/excel`;

  constructor(private http: HttpClient) {}

  // ==================== EXPORTACIÓN ====================

  /**
   * Exporta las OTs seleccionadas (por IDs)
   */
  exportOts(otIds: number[]): Observable<Blob> {
    return this.http.post(`${this.apiUrl}/export/ots`, otIds, {
      responseType: 'blob',
      headers: new HttpHeaders({
        'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      })
    });
  }

  /**
   * Exporta todas las OTs del sistema
   */
  exportAllOts(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export/all`, {
      responseType: 'blob',
      headers: new HttpHeaders({
        'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      })
    });
  }

  // ==================== IMPORTACIÓN ====================

  /**
   * Descarga la plantilla para importación con datos de referencia
   */
  downloadTemplate(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/import/template`, {
      responseType: 'blob',
      headers: new HttpHeaders({
        'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      })
    });
  }

  /**
   * Descarga plantilla básica (sin datos de referencia)
   */
  downloadTemplateSimple(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/import/template-simple`, {
      responseType: 'blob',
      headers: new HttpHeaders({
        'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      })
    });
  }

  /**
   * Descarga solo el modelo de datos (combos)
   */
  downloadModeloDatos(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/import/modelo`, {
      responseType: 'blob',
      headers: new HttpHeaders({
        'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      })
    });
  }

  /**
   * Importa OTs desde archivo Excel
   */
  importOts(file: File): Observable<ImportResultDTO> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<ImportResultDTO>(`${this.apiUrl}/import/ots`, formData, {
      headers: new HttpHeaders({
        // No establecer Content-Type, FormData lo maneja automáticamente
      })
    });
  }

  /**
   * Importación masiva (para muchos registros)
   */
  importMasivo(file: File): Observable<ImportResultDTO> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<ImportResultDTO>(`${this.apiUrl}/import/masivo`, formData);
  }

  /**
   * Descarga instrucciones de importación
   */
  downloadInstrucciones(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/import/instrucciones`, {
      responseType: 'blob',
      headers: new HttpHeaders({
        'Accept': 'text/plain'
      })
    });
  }

  /**
   * Obtiene límites de importación
   */
  getLimitesImportacion(): Observable<string> {
    return this.http.get(`${this.apiUrl}/import/limites`, {
      responseType: 'text'
    });
  }

  /**
   * Valida archivo antes de importar
   */
  validateFile(file: File): { isValid: boolean; message: string } {
    const validExtensions = ['.xlsx', '.xls'];
    const fileExtension = file.name.substring(file.name.lastIndexOf('.')).toLowerCase();

    if (!validExtensions.includes(fileExtension)) {
      return {
        isValid: false,
        message: 'Solo se permiten archivos Excel (.xlsx, .xls)'
      };
    }

    // Límite normal: 20MB
    if (file.size > 20 * 1024 * 1024) {
      return {
        isValid: false,
        message: 'El archivo excede el tamaño máximo de 20MB'
      };
    }

    return { isValid: true, message: 'Archivo válido' };
  }

  /**
   * Valida archivo para importación masiva
   */
  validateFileMasivo(file: File): { isValid: boolean; message: string } {
    const validExtensions = ['.xlsx'];
    const fileExtension = file.name.substring(file.name.lastIndexOf('.')).toLowerCase();

    if (!validExtensions.includes(fileExtension)) {
      return {
        isValid: false,
        message: 'Para importación masiva solo se permiten archivos .xlsx'
      };
    }

    // Límite masivo: 50MB
    if (file.size > 50 * 1024 * 1024) {
      return {
        isValid: false,
        message: 'El archivo excede el tamaño máximo de 50MB para importación masiva'
      };
    }

    return { isValid: true, message: 'Archivo válido para importación masiva' };
  }

  // ==================== UTILITARIOS ====================

  /**
   * Descarga un blob como archivo Excel
   */
  downloadExcel(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.style.display = 'none';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }

  /**
   * Descarga un blob como archivo de texto
   */
  downloadText(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.style.display = 'none';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }

  /**
   * Formatea el resultado de importación para mostrar
   */
  formatImportResult(result: ImportResultDTO): string {
    let message = `✅ Importación completada\n\n`;
    message += `📊 Resumen:\n`;
    message += `   • Registros procesados: ${result.totalRegistros}\n`;
    message += `   • Éxitos: ${result.exitosos}\n`;
    message += `   • Fallidos: ${result.fallidos}\n`;
    message += `   • Tiempo: ${result.duracionMs} ms\n\n`;

    if (result.erroresValidacion > 0) {
      message += `⚠️ Errores de validación: ${result.erroresValidacion}\n`;
    }

    if (result.erroresPersistencia > 0) {
      message += `⚠️ Errores de persistencia: ${result.erroresPersistencia}\n`;
    }

    if (result.warnings > 0) {
      message += `⚠️ Advertencias: ${result.warnings}\n`;
    }

    if (result.resumenErrores && result.resumenErrores.length > 0) {
      message += `\n❌ Errores principales:\n`;
      result.resumenErrores.forEach((error, index) => {
        message += `   ${index + 1}. ${error}\n`;
      });
    }

    return message;
  }

  /**
   * Genera nombre de archivo con timestamp
   */
  generateFileName(baseName: string, extension: string = 'xlsx'): string {
    const timestamp = new Date().toISOString()
      .replace(/[:.]/g, '-')
      .replace('T', '_')
      .substring(0, 19);
    return `${baseName}_${timestamp}.${extension}`;
  }

  /**
   * Test de conexión
   */
  testConnection(): Observable<string> {
    return this.http.get(`${this.apiUrl}/test`, {
      responseType: 'text'
    });
  }

  /**
   * Obtiene estado del servicio
   */
  getServiceStatus(): Observable<string> {
    return this.http.get(`${this.apiUrl}/status`, {
      responseType: 'text'
    });
  }

  /**
   * Muestra diálogo de resultado de importación
   */
  showImportResultDialog(result: ImportResultDTO): Promise<any> {
    const importResult = this.formatImportResult(result);

    // Usar SweetAlert2 o similar para mostrar el resultado
    // Esta es una implementación básica
    return Promise.resolve({
      title: result.exito ? '✅ Importación Exitosa' : '⚠️ Importación con Errores',
      html: `<pre style="text-align: left; white-space: pre-wrap;">${importResult}</pre>`,
      icon: result.exito ? 'success' : 'warning',
      confirmButtonText: 'Aceptar',
      showCancelButton: true,
      cancelButtonText: 'Ver Detalles'
    });
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

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
      responseType: 'blob'
    });
  }

  /**
   * Exporta todas las OTs del sistema
   */
  exportAllOts(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export/all`, {
      responseType: 'blob'
    });
  }


  // ==================== IMPORTACIÓN ====================

  /**
   * Descarga la plantilla para importación
   */
  downloadTemplate(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/import/template`, {
      responseType: 'blob'
    });
  }



  /**
   * Importación masiva (para muchos registros)
   */
  importMasivo(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.apiUrl}/import/masivo`, formData);
  }

  /**
   * Descarga el modelo de datos
   */
  downloadModel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/modelo/ots`, {
      responseType: 'blob'
    });
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
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }



  /**
   * Test de conexión
   */
  testConnection(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/test`, {
      responseType: 'blob'
    });
  }
}

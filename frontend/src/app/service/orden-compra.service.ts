import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environment';
import { OrdenCompraRequest, OrdenCompraResponse, PageOrdenCompra } from '../model/orden-compra.model';

@Injectable({
  providedIn: 'root'
})
export class OrdenCompraService {
  private apiUrl = `${environment.baseUrl}/api/ordenes-compra`;

  constructor(private http: HttpClient) {}

  crear(request: OrdenCompraRequest): Observable<OrdenCompraResponse> {
    return this.http.post<OrdenCompraResponse>(this.apiUrl, request).pipe(catchError(this.handleError));
  }

  actualizar(id: number, request: OrdenCompraRequest): Observable<OrdenCompraResponse> {
    return this.http.put<OrdenCompraResponse>(`${this.apiUrl}/${id}`, request).pipe(catchError(this.handleError));
  }

  listar(page = 0, size = 10, searchTerm?: string): Observable<PageOrdenCompra> {
    let params = new HttpParams().set('page', page).set('size', size).set('sortBy', 'idOc').set('direction', 'ASC');
    if (searchTerm) params = params.set('search', searchTerm);
    return this.http.get<PageOrdenCompra>(this.apiUrl, { params }).pipe(catchError(this.handleError));
  }

  private handleError(error: any): Observable<never> {
    let errorMessage = 'Ocurrió un error inesperado';
    if (error.error instanceof ErrorEvent) errorMessage = `Error: ${error.error.message}`;
    else errorMessage = error.error?.message || `Código: ${error.status} - ${error.message}`;
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }

descargarHtml(idOc: number, idEmpresa: number): void {
    const url = `http://localhost:8080/api/ordenes-compra/${idOc}/descargar-html?idEmpresa=${idEmpresa}`;

  this.http.get(url, {
    params: { idEmpresa },
    responseType: 'blob'
  }).subscribe(blob => {

    const file = new Blob([blob], { type: 'text/html' });
    const fileURL = URL.createObjectURL(file);

    const a = document.createElement('a');
    a.href = fileURL;
    a.download = `orden-compra-${idOc}.html`;
    a.click();

    URL.revokeObjectURL(fileURL);
  });
}

getById(idOc: number): Observable<OrdenCompraResponse> {
  return this.http
    .get<OrdenCompraResponse>(`${this.apiUrl}/${idOc}`)
    .pipe(catchError(this.handleError));
}


}

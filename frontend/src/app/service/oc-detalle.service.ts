import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OcDetalleService {

private apiUrl = 'http://localhost:8080/api/oc-detalles';

  constructor(private http: HttpClient) {}

  listarPorOrden(idOc: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/orden-compra/${idOc}`);
  }
}

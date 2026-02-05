import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Aprobacion } from '../model/aprobacion.model';

@Injectable({ providedIn: 'root' })
export class OrdenCompraAprobacionService {

  private baseUrl = 'http://localhost:8080/api/orden-compra-aprobaciones';

  constructor(private http: HttpClient) {}

  getHistorial(idOc: number): Observable<Aprobacion[]> {
    return this.http.get<Aprobacion[]>(`${this.baseUrl}/${idOc}`);
  }

aprobar(
  idOc: number,
  nivel: number,
  estado: 'APROBADO' | 'RECHAZADO'
): Observable<Aprobacion> {

  return this.http.post<Aprobacion>(
    `${this.baseUrl}/${idOc}/aprobar`,
    {
      nivel,
      estado,
      comentario: null
    }
  );
}

}

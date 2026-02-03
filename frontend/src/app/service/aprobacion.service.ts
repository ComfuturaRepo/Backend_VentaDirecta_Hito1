// orden-compra-aprobacion.service.ts
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Aprobacion } from '../model/aprobacion.model';

@Injectable({
  providedIn: 'root'
})
export class OrdenCompraAprobacionService {

  private baseUrl = 'http://localhost:8080/api/orden-compra-aprobaciones';

  constructor(private http: HttpClient) { }

  getHistorial(idOc: number): Observable<Aprobacion[]> {
    return this.http.get<Aprobacion[]>(`${this.baseUrl}/${idOc}`);
  }

aprobar(payload: {
  idOc: number;
  nivel: number;
  estado: string;
  aprobadoPor: string;
}): Observable<Aprobacion> {

  return this.http.post<Aprobacion>(
    `${this.baseUrl}/${payload.idOc}/${payload.nivel}/${payload.estado}`,
    { aprobadoPor: payload.aprobadoPor }
  );

}




}

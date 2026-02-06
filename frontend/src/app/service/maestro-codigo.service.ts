import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

export interface MaestroCodigoCombo {
  idMaestro: number;
  codigo: string;
  descripcion: string;
}

@Injectable({
  providedIn: 'root'
})
export class MaestroCodigoService {

  private apiUrl = `${environment.baseUrl}/api/maestro-codigo/combo`;

  constructor(private http: HttpClient) {}

  listarParaCombo(): Observable<MaestroCodigoCombo[]> {
    return this.http.get<MaestroCodigoCombo[]>(this.apiUrl);
  }
}

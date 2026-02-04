import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environment';
import { ATSListDTO, ATSRequestDTO, ATSResponseDTO, SSTFormRequestDTO, SSTFormResponseDTO } from '../model/ssoma.model';
import { PageResponseDTO } from './cliente.service';


@Injectable({
  providedIn: 'root'
})
export class ATSService {
  private baseUrl = `${environment.baseUrl}/api/ssoma`;

  constructor(private http: HttpClient) {}

  /**
   * Obtiene lista paginada de ATS
   */
  getAllATS(
    page: number = 0,
    size: number = 10,
    sort: string = 'fecha,desc',
    search?: string
  ): Observable<PageResponseDTO<ATSListDTO>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (search && search.trim() !== '') {
      params = params.set('search', search);
    }

    return this.http.get<PageResponseDTO<ATSListDTO>>(this.baseUrl, { params });
  }

  /**
   * Obtiene un ATS por ID
   */
  getATSById(id: number): Observable<ATSResponseDTO> {
    return this.http.get<ATSResponseDTO>(`${this.baseUrl}/${id}`);
  }

  /**
   * Crea un nuevo ATS
   */
  createATS(atsData: ATSRequestDTO): Observable<ATSResponseDTO> {
    return this.http.post<ATSResponseDTO>(this.baseUrl, atsData);
  }

  /**
   * Crea formulario SST completo (5 hojas)
   */
  crearFormularioCompleto(formData: SSTFormRequestDTO): Observable<SSTFormResponseDTO> {
    return this.http.post<SSTFormResponseDTO>(`${this.baseUrl}/completo`, formData);
  }

  // Los demás métodos (generateSampleATS, generateSampleFormularioCompleto) se mantienen igual
}

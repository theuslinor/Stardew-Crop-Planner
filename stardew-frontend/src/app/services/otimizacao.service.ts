import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CulturaRetornoDTO } from '../models/cultura.model';

@Injectable({
  providedIn: 'root'
})
export class OtimizacaoService {
  private readonly API_URL = 'http://localhost:8080/api/otimizar';

  constructor(private http: HttpClient) { }

  getMelhoresCulturas(jogadorId: number): Observable<CulturaRetornoDTO[]> {
    return this.http.get<CulturaRetornoDTO[]>(`${this.API_URL}/melhores-culturas?jogadorId=${jogadorId}`);
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OtimizacaoService } from './services/otimizacao.service';
import { CulturaRetornoDTO } from './models/cultura.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent implements OnInit {
  ranking: CulturaRetornoDTO[] = [];

  constructor(private otimizacaoService: OtimizacaoService) {}

  ngOnInit(): void {
    this.otimizacaoService.getMelhoresCulturas(1).subscribe({
      next: (data) => {
        console.log('Dados recebidos do Java:', data); // ADICIONE ISSO
        this.ranking = data;
      },
      error: (err) => console.error('Erro de conexão:', err)
    });
  }
}

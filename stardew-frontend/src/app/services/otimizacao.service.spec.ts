import { TestBed } from '@angular/core/testing';

import { Otimizacao } from './otimizacao';

describe('Otimizacao', () => {
  let service: Otimizacao;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Otimizacao);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

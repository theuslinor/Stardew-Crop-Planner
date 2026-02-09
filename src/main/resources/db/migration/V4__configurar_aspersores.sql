-- 1. Remove as tabelas se elas existirem (para garantir a estrutura nova)
DROP TABLE IF EXISTS inventario_aspersor CASCADE;
DROP TABLE IF EXISTS aspersor CASCADE;

-- 2. Cria a tabela com a coluna 'capacidade'
CREATE TABLE aspersor (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    capacidade INTEGER NOT NULL
);

-- 3. Insere os dados
INSERT INTO aspersor (nome, capacidade) VALUES
    ('Aspersor Normal', 4),
    ('Aspersor de Qualidade', 8),
    ('Aspersor de Iridium', 24);

-- 4. Cria a tabela de inventário
CREATE TABLE inventario_aspersor (
    id BIGSERIAL PRIMARY KEY,
    jogador_id BIGINT REFERENCES estado_jogador(id),
    aspersor_id BIGINT REFERENCES aspersor(id),
    quantidade INTEGER NOT NULL DEFAULT 0
);
-- Remover as tabelas antigas (limpeza para a nova estrutura)
DROP TABLE IF EXISTS crop CASCADE;
DROP TABLE IF EXISTS player_state CASCADE;
DROP TABLE IF EXISTS sprinkler CASCADE;

-- Criar tabelas em português
CREATE TABLE cultura (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco_normal INTEGER NOT NULL,
    preco_prata INTEGER,
    preco_ouro INTEGER,
    preco_iridium INTEGER,
    recorrente BOOLEAN DEFAULT FALSE,
    tempo_crescimento INTEGER NOT NULL,
    tempo_rebrota INTEGER,
    estacao VARCHAR(20) NOT NULL
);

CREATE TABLE estado_jogador (
    id BIGSERIAL PRIMARY KEY,
    nivel_cultivo INTEGER DEFAULT 0,
    ouro_atual INTEGER DEFAULT 0,
    dia_atual INTEGER DEFAULT 1,
    estacao_atual VARCHAR(50),
    profissao VARCHAR(50)
);

CREATE TABLE aspersor (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    raio INTEGER NOT NULL
);
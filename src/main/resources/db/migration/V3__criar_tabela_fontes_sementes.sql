CREATE TABLE fonte_semente (
    id BIGSERIAL PRIMARY KEY,
    cultura_id BIGINT NOT NULL,
    local_venda VARCHAR(50) NOT NULL, -- Ex: 'PIERRE', 'JOJA', 'CARRINHO'
    preco INTEGER NOT NULL,
    CONSTRAINT fk_cultura FOREIGN KEY (cultura_id) REFERENCES cultura(id)
);

-- Vamos popular a Chirivia (ID 1) com seus preços oficiais
INSERT INTO fonte_semente (cultura_id, local_venda, preco) VALUES
    (1, 'PIERRE', 20),
    (1, 'JOJA', 25),
    (1, 'MERCADO_NOTURNO', 20);

-- Vamos popular a Couve-Flor (ID 2 - ajuste o ID se necessário no seu banco)
INSERT INTO fonte_semente (cultura_id, local_venda, preco) VALUES
    (2, 'PIERRE', 80),
    (2, 'JOJA', 100);
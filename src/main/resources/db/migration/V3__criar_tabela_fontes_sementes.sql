CREATE TABLE fonte_semente (
    id BIGSERIAL PRIMARY KEY,
    cultura_id BIGINT NOT NULL,
    local_venda VARCHAR(50) NOT NULL, -- Ex: 'PIERRE', 'JOJA', 'CARRINHO'
    preco INTEGER NOT NULL,
    CONSTRAINT fk_cultura FOREIGN KEY (cultura_id) REFERENCES cultura(id)
);

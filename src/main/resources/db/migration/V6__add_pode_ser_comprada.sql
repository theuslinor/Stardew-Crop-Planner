ALTER TABLE cultura ADD COLUMN IF NOT EXISTS pode_ser_comprada BOOLEAN DEFAULT TRUE;

UPDATE cultura SET pode_ser_comprada = FALSE WHERE preco_normal = 0;
USE db_lavacar;

ALTER TABLE modelo
    ADD COLUMN categoria ENUM('PEQUENO', 'MEDIO', 'GRANDE', 'PADRAO', 'MOTO') NOT NULL;

ALTER TABLE veiculo
ADD COLUMN id_modelo INT,
ADD CONSTRAINT fk_veiculo_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id);

ALTER TABLE motor
CHANGE COLUMN tipoCombustivel tipo_combustivel
ENUM('GASOLINA','ETANOL','FLEX','DIESEL','GNV','OUTRO');
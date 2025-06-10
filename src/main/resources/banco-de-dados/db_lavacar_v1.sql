CREATE DATABASE IF NOT EXISTS db_lavacar;

USE db_lavacar;

CREATE TABLE cor(
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(30),
    CONSTRAINT pk_cor PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE marca (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    CONSTRAINT pk_marca PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE modelo (
    id INT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(255),
    id_marca INT,
    categoria ENUM('PEQUENO', 'MEDIO', 'GRANDE', 'PADRAO', 'MOTO') NOT NULL,
    CONSTRAINT pk_modelo PRIMARY KEY (id),
    CONSTRAINT fk_modelo_marca FOREIGN KEY (id_marca) REFERENCES marca(id)
) ENGINE=InnoDB;

CREATE TABLE motor (
    id_modelo INT NOT NULL,
    tipo_combustivel ENUM('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO'),
    potencia INT,
    CONSTRAINT pk_motor PRIMARY KEY (id_modelo),
    CONSTRAINT fk_motor_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE veiculo(
    id INT NOT NULL AUTO_INCREMENT,
    placa VARCHAR(20),
    observacoes VARCHAR(200),
    id_cor INT NOT NULL,
    id_modelo INT,
    CONSTRAINT pk_veiculo PRIMARY KEY(id),
    CONSTRAINT fk_veiculo_cor FOREIGN KEY (id_cor) REFERENCES cor(id),
    CONSTRAINT fk_veiculo_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id)
) ENGINE=InnoDB;

CREATE TABLE servico (
    id INT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(50) NOT NULL,
    valor DOUBLE,
    pontos INT,
    categoria ENUM('PEQUENO', 'MEDIO', 'GRANDE', 'PADRAO', 'MOTO') NOT NULL,
    CONSTRAINT pk_servico PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO cor (nome) VALUES ('Vermelho');
INSERT INTO marca (nome) VALUES ('Volkswagen');
INSERT INTO modelo (descricao, id_marca, categoria) VALUES ('Gol', 1, 'PEQUENO');
INSERT INTO motor (id_modelo, tipo_combustivel, potencia) VALUES (1, 'FLEX', 84);
INSERT INTO veiculo (placa, observacoes, id_cor, id_modelo) VALUES ('ABC-1234', 'Carro em bom estado', 1, 1);
INSERT INTO servico (descricao, categoria) VALUES ('Lavação Interna', 'PADRAO');
INSERT INTO servico (descricao, categoria) VALUES ('Encerar', 'PADRAO');
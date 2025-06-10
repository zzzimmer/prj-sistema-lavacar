CREATE DATABASE IF NOT EXISTS db_lavacar;
USE db_lavacar;

CREATE TABLE cor(
    id int not null auto_increment,
    nome varchar (30),
    CONSTRAINT pk_cor
        PRIMARY KEY(id)
) engine= InnoDB;

CREATE TABLE veiculo(
    id int not null auto_increment,
    placa varchar (20),
    observacoes varchar (200),
    id_cor int not null,
    CONSTRAINT pk_veiculo
        PRIMARY KEY(id),
    CONSTRAINT fk_veiculo_cor
        FOREIGN KEY (id_cor)
        REFERENCES cor(id)
) engine= InnoDB;

-- Inserir uma cor
INSERT INTO cor (nome) VALUES ('Vermelho');

-- Inserir um veículo (usando o id_cor = 1, que corresponde à cor "Vermelho")
INSERT INTO veiculo (placa, observacoes, id_cor)
VALUES ('ABC-1234', 'Carro em bom estado', 1);

CREATE TABLE servico (
	id int not null auto_increment,
    descricao varchar(50) NOT NULL,
    valor DOUBLE,
    pontos INT,
    categoria ENUM ('PEQUENO', 'MEDIO', 'GRANDE', 'PADRAO', 'MOTO') NOT NULL,
    CONSTRAINT pk_servico
		PRIMARY KEY (id)
        )engine=InnoDB;

        INSERT INTO servico(descricao) VALUES('Lavação Interna');
		INSERT INTO servico(descricao) VALUES('Encerar');

CREATE TABLE marca (
	id int not null auto_increment,
    nome varchar(50) NOT NULL,
    CONSTRAINT pk_marca
		PRIMARY KEY (id)
        )engine=InnoDB;

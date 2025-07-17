CREATE DATABASE IF NOT EXISTS db_lavacar;

USE db_lavacar;

CREATE TABLE cliente (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    celular VARCHAR(25) NOT NULL,
    email VARCHAR (50),
    data_cadastro DATE NOT NULL,
    CONSTRAINT pk_cliente PRIMARY KEY (id)
)ENGINE=InnoDB;

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
    id_marca INT NOT NULL ,
    categoria ENUM('PEQUENO', 'MEDIO', 'GRANDE', 'PADRAO', 'MOTO') NOT NULL,
    CONSTRAINT pk_modelo PRIMARY KEY (id),
    CONSTRAINT fk_modelo_marca FOREIGN KEY (id_marca) REFERENCES marca(id)
) ENGINE=InnoDB;

CREATE TABLE motor (
    id_modelo INT NOT NULL,
    tipo_combustivel ENUM('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') NOT NULL,
    potencia INT,
    CONSTRAINT pk_motor PRIMARY KEY (id_modelo),
    CONSTRAINT fk_motor_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE servico (
    id INT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(50) NOT NULL,
    valor DOUBLE,
    pontos INT,
    categoria ENUM('PEQUENO', 'MEDIO', 'GRANDE', 'PADRAO', 'MOTO') NOT NULL,
    CONSTRAINT pk_servico PRIMARY KEY (id)
) ENGINE=InnoDB;


CREATE TABLE pessoa_fisica (
    id_cliente INT NOT NULL REFERENCES cliente(id),
    cpf VARCHAR (25) NOT NULL,
    data_nascimento DATE,
    CONSTRAINT pk_pessoa_fisica PRIMARY KEY (id_cliente),
    CONSTRAINT fk_pessoa_fisica_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE pessoa_juridica (
    id_cliente INT NOT NULL REFERENCES cliente(id),
    cnpj VARCHAR (50) NOT NULL,
    inscricao_estadual VARCHAR(50),
    CONSTRAINT pk_pessoa_juridica PRIMARY KEY (id_cliente),
    CONSTRAINT fk_pessoa_juridica_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE veiculo(
    id INT NOT NULL AUTO_INCREMENT,
    placa VARCHAR(20) UNIQUE NOT NULL,
    observacoes VARCHAR(200),
    id_cor INT NOT NULL,
    id_modelo INT NOT NULL,
    id_cliente INT NOT NULL,
    CONSTRAINT pk_veiculo PRIMARY KEY(id),
    CONSTRAINT fk_veiculo_cor FOREIGN KEY (id_cor) REFERENCES cor(id),
    CONSTRAINT fk_veiculo_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id),
    CONSTRAINT fk_veiculo_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id)
) ENGINE=InnoDB;

CREATE TABLE ordem_servico(
    numero INT NOT NULL,
    total DECIMAL(10,2),
    agenda DATE,
    desconto DECIMAL(10,2),
    id_veiculo INT NOT NULL,
    CONSTRAINT pk_ordem_servico
                          PRIMARY KEY (numero),
    CONSTRAINT fk_ordem_servico_cliente
                          FOREIGN KEY (id_veiculo)
                          REFERENCES veiculo(id)
)ENGINE= InnoDB;

CREATE TABLE item_os (
    id INT NOT NULL,
    valor_servico DECIMAL(10,2),
    observacoes VARCHAR(300),
    id_ordem_servico INT NOT NULL REFERENCES ordem_servico(numero),
    id_servico INT NOT NULL,
    CONSTRAINT pk_item_os
                     PRIMARY KEY (id),
    CONSTRAINT fk_item_os_ordem_servico
                     FOREIGN KEY (id_ordem_servico)
                     REFERENCES ordem_servico(numero),
    CONSTRAINT fk_item_os_servico
                     FOREIGN KEY (id_servico)
                     REFERENCES servico(id)
)ENGINE= InnoDB;

-- ALTER TABLE `db_lavacar`.`cliente`
--     CHANGE COLUMN `data_cadastro` `data_cadastro` DATE NULL ;
--
-- ALTER TABLE `db_lavacar`.`veiculo`
--     ADD COLUMN `id_cliente` INT NOT NULL,
-- ADD CONSTRAINT `fk_veiculo_cliente`
--   FOREIGN KEY (`id_cliente`)
--   REFERENCES `db_lavacar`.`cliente` (`id`);

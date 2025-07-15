use db_lavacar;

CREATE TABLE modelo (
    id int not null auto_increment,
    descricao varchar(255),
   --  id_motor int REFERENCES motor (id),
    id_marca int REFERENCES marca (id),
    constraint pk_modelo
		PRIMARY KEY (id),
   --  CONSTRAINT fk_modelo_motor # por se tratar de composição, modelo não precisa ter um motor em sua tb
--         FOREIGN KEY (id_motor)
--         REFERENCES motor(id),
    CONSTRAINT fk_modelo_marca
        FOREIGN KEY (id_marca)
        REFERENCES marca(id)
) engine = InnoDb;

CREATE TABLE motor (
    id_modelo int not null REFERENCES modelo(id),
    tipoCombustivel ENUM ('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO'),
    constraint pk_motor
		PRIMARY KEY (id_modelo),
    CONSTRAINT fk_motor_modelo
        FOREIGN KEY (id_modelo)
        REFERENCES modelo(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) engine = InnoDb;
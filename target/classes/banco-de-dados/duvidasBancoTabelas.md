# Relações em Banco de Dados: O Uso de `ON DELETE CASCADE`

## Pergunta

> O diagrama indica que todo produto **precisa ter um** fornecedor, enquanto um fornecedor pode ter **zero ou muitos** produtos. Assim sendo, por que na foreign key da tabela `produto` (`fk_produto_fornecedor`) não se usa `ON DELETE CASCADE` e `ON UPDATE CASCADE`?

---

### Esquema do Banco de Dados

**Tabela `produto`**
```sql
CREATE TABLE produto(
    id int NOT NULL auto_increment,
    nome varchar(50) NOT NULL,
    descricao varchar(200),
    preco decimal(10,2) NOT NULL,
    id_categoria int NOT NULL,
    id_fornecedor int NOT NULL,
    CONSTRAINT pk_produto PRIMARY KEY(id),
    CONSTRAINT fk_produto_categoria FOREIGN KEY(id_categoria) REFERENCES categoria(id),
    CONSTRAINT fk_produto_fornecedor FOREIGN KEY(id_fornecedor) REFERENCES fornecedor(id)
) engine=InnoDB;
```

**Tabela `fornecedor` e especializações**
```sql
CREATE TABLE fornecedor(
    id int NOT NULL auto_increment,
    nome varchar(50) NOT NULL,
    email varchar(100),
    fone varchar(20),
    CONSTRAINT pk_fornecedor PRIMARY KEY(id)
) engine=InnoDB;

-- Tabelas de especialização que usam CASCADE
CREATE TABLE nacional(
    id_fornecedor INT NOT NULL,
    cnpj VARCHAR(20) NOT NULL,
    CONSTRAINT pk_nacional PRIMARY KEY (id_fornecedor),
    CONSTRAINT fk_nacional_fornecedor FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) engine=InnoDB;

CREATE TABLE internacional(
    id_fornecedor INT NOT NULL,
    nif VARCHAR(20) NOT NULL,
    pais VARCHAR(30) NOT NULL,
    CONSTRAINT pk_internacional PRIMARY KEY (id_fornecedor),
    CONSTRAINT fk_internacional_fornecedor FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) engine=InnoDB;
```
---

### Resposta

Sua interpretação do diagrama está correta, mas a sua conclusão sobre o `ON DELETE CASCADE` presume uma regra de negócio que geralmente **não é desejada** para essa relação.

A chave estrangeira em `produto` não usa `ON DELETE CASCADE` porque essa ação seria perigosa para a integridade dos dados e para a lógica de negócio.

#### O Que `ON DELETE CASCADE` Faria Aqui?

Se a `fk_produto_fornecedor` tivesse `ON DELETE CASCADE`, ao deletar um registro da tabela `fornecedor`, o banco de dados automaticamente **deletaria todos os produtos** associados a ele.

Isso é problemático por várias razões:

1.  **Perda de Dados Históricos:** Produtos fazem parte de registros de vendas, notas fiscais e histórico de estoque. Deletar um produto porque seu fornecedor foi desativado corromperia todos esses registros.
2.  **Lógica de Negócio Incorreta:** O mais comum é um produto trocar de fornecedor ou ser descontinuado, mas raramente ele é completamente apagado do sistema.
3.  **Deleções Acidentais:** Um único `DELETE` por engano na tabela `fornecedor` poderia apagar uma porção significativa do seu catálogo de produtos sem aviso prévio.

#### O Comportamento Padrão (`RESTRICT`) é Mais Seguro

Sem o `ON DELETE CASCADE`, o comportamento padrão do InnoDB é `RESTRICT`. Isso significa que o banco de dados irá **proibir a exclusão** de um `fornecedor` se existir qualquer `produto` ainda associado a ele.

Esta é uma medida de segurança. Ela força o sistema ou o usuário a tomar uma decisão consciente sobre o que fazer com os produtos daquele fornecedor antes de removê-lo. As ações corretas seriam:

-   Associar os produtos a outro fornecedor.
-   Marcar os produtos como "inativos" ou "descontinuados".

Convenções de Nomes em Banco de Dados: Colunas
Pergunta
E nomes de variáveis em banco de dados? Por exemplo, na UML está dataNascimento, no banco vira data_nascimento DATE NOT NULL?

Resposta
Sim, seu exemplo está exatamente correto e demonstra a aplicação perfeita da convenção.

A mesma recomendação de usar snake_case para nomes de tabelas se aplica também, e com a mesma importância, para os nomes das colunas. O processo de tradução que você descreveu é o padrão da indústria:

No seu diagrama UML ou no seu código (classes, atributos), você usa a convenção camelCase (ex: dataNascimento).
No seu banco de dados (tabelas, colunas), você "traduz" isso para snake_case (ex: data_nascimento).
Tabela de Tradução: Código vs. Banco de Dados
No Código / UML (camelCase)	No Banco de Dados (snake_case)
dataNascimento	data_nascimento
valorUnitario	valor_unitario
idCliente	id_cliente
nomeCompletoUsuario	nome_completo_usuario

Exportar para as Planilhas
Por Que Manter Essa Convenção nas Colunas?
Os motivos são os mesmos que para as tabelas, garantindo um esquema limpo e robusto:

Legibilidade Máxima: data_nascimento é instantaneamente mais legível que datanascimento ou DataNascimento.
Consistência do Esquema: Ter tabelas e colunas seguindo o mesmo padrão (snake_case) torna o banco de dados visualmente organizado e previsível. Ex: SELECT data_nascimento FROM pessoa_fisica;
Prevenção de Erros: Elimina qualquer problema relacionado à sensibilidade de maiúsculas/minúsculas (case-sensitivity) que pode variar entre sistemas operacionais onde o banco de dados está hospedado.
Mapeamento Fácil para o Código (ORMs): Ferramentas de Mapeamento Objeto-Relacional (como Hibernate, JPA, etc.) são projetadas para fazer essa conversão (data_nascimento ↔ dataNascimento) de forma automática e eficiente.
Em resumo, ao projetar seu banco de dados, pense em snake_case para tudo: tabelas e colunas. Isso criará um esquema profissional e livre de problemas.
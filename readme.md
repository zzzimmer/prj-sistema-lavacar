# 🚗 Sistema de Gerenciamento de Ordens de Serviço (Lavação)

> Aplicação desktop desenvolvida em **Java** com interface gráfica **JavaFX**, seguindo a arquitetura **MVC**.

A persistência de dados é gerenciada através do padrão **DAO** (Data Access Object), com conexão a um banco de dados **MySQL**.

---

## ✨ Funcionalidades Principais

### 📋 Cadastros (CRUD)
* **Clientes:** Gerenciamento completo, distinguindo entre *Pessoa Física* e *Pessoa Jurídica* (uso do conceito de Herança).
* **Veículos:** Cadastro associando-os a Clientes, Modelos, Marcas e Cores.
* **Serviços:** Cadastro de ofertas com valor, categoria e pontuação.
* **Itens Auxiliares:** Gerenciamento de Marcas, Modelos e Cores.

### ⚙️ Processos
* **Ordem de Serviço (OS):** Módulo central para criar, alterar e consultar.
    * Adição de múltiplos serviços (*ItensOS*) em uma única OS.
    * Definição de status: `Aberta`, `Fechada`, `Cancelada`.

### 📊 Relatórios e Gráficos - JasperReport. 
* **Relatórios:** Detalhamento das Ordens de Serviço.
* **Dashboard:** Visualização gráfica (ex: Quantidade de OS por mês) para análise gerencial.

### 🏆 Sistema de Pontuação
* Implementação de pontuação de fidelidade para clientes.

---

## 🛠️ Arquitetura e Tecnologias

O projeto está estruturado para separar responsabilidades seguindo o padrão **MVC** e **DAO**.

### Arquitetura do Projeto

#### 1. Model (Modelo)
* `model.domain`: Classes de entidade (POJOs) que representam o mundo real (ex: `Cliente`, `Veiculo`, `OrdemServico`).
* `model.dao`: Camada de acesso a dados. Contém as classes responsáveis pelo SQL (SELECT, INSERT, UPDATE, DELETE).
* `model.database`: Contém a `DatabaseFactory` para gerenciar a conexão JDBC com o MySQL.

#### 2. View (Visão)
* Arquivos **.fxml** (na pasta `view/`): Estruturam a interface gráfica baseada em XML.

#### 3. Controller (Controlador)
* `controller`: Classes que ligam a View ao Model (ex: `FXMLAnchorPaneCadastroClienteController`). Tratam eventos de clique e lógica de interface.

### 🚀 Tecnologias Utilizadas
* **Linguagem:** Java 11+
* **Interface:** JavaFX & FXML
* **Banco de Dados:** MySQL
* **Conectividade:** JDBC Driver

---

## 📂 Estrutura de Pacotes

A organização de pastas reflete a arquitetura MVC:

```tree
src/
├── controller/
│   ├── FXMLMainController.java
│   └── ...
├── model/
│   ├── dao/
│   │   ├── ClienteDAO.java
│   │   └── ...
│   ├── database/
│   │   └── DatabaseFactory.java
│   └── domain/
│       ├── Cliente.java
│       └── ...
└── view/
    ├── main.fxml
    └── ...

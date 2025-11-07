🚗 Sistema de Gerenciamento de Ordens de Serviço para Lavação (JavaFX)
A aplicação é construída em Java, utiliza JavaFX para a interface gráfica e segue a arquitetura MVC (Model-View-Controller).

A persistência de dados é gerenciada através do padrão DAO (Data Access Object), com conexão a um banco de dados MySQL.

✨ Funcionalidades Principais
Cadastros (CRUD):

Clientes: Gerenciamento completo de clientes, distinguindo entre Pessoa Física e Pessoa Jurídica. (Conceito de Herança)

Veículos: Cadastro de veículos, associando-os a Clientes, Modelos, Marcas e Cores.

Serviços: Cadastro dos serviços oferecidos, com valor, categoria e pontuação associada.

Itens Auxiliares: Gerenciamento de Marcas, Modelos e Cores.

Processos:

Ordem de Serviço (OS): Módulo principal para criar, alterar e consultar Ordens de Serviço. Permite adicionar múltiplos serviços (ItensOS) a uma única OS, aplicar descontos e definir o status (Aberta, Fechada, Cancelada).

Relatórios:

Geração de relatórios detalhados das Ordens de Serviço.

Gráficos:

Dashboard visual com gráficos, como a quantidade de Ordens de Serviço realizadas por mês, para análise gerencial.

Sistema de Pontuação:

O diagrama de classes inclui um sistema de pontuação de fidelidade para clientes.

🛠️ Arquitetura e Tecnologias
O projeto está estruturado em pacotes que separam as responsabilidades, seguindo os princípios da arquitetura MVC e do padrão DAO.

Arquitetura do projeto
Model (Modelo):

model.domain: Contém as classes de entidade (POJOs) que representam os objetos do mundo real (ex: Cliente, Veiculo, OrdemServico, Servico).

model.dao: Camada de acesso a dados. Contém as classes DAO (ex: ClienteDAO, OrdemServicoDAO) responsáveis por toda a comunicação (SELECT, INSERT, UPDATE, DELETE) com o banco de dados.

model.database: Inclui a DatabaseFactory para selecionar o SGBD (neste caso, MySQL) e gerenciar a conexão JDBC.

View (Visão):

Definida pelos arquivos .fxml (localizados na pasta view/). Estes arquivos XML estruturam a interface gráfica do usuário.

Controller (Controlador):

controller: Contém as classes JavaFX Controller (ex: FXMLAnchorPaneProcessoOrdemServicoController, FXMLAnchorPaneCadastroClienteController). Elas fazem a ligação entre a Visão (FXML) e o Modelo (DAO/Domain), tratando os eventos da interface e relacionando com a lógica de negócio.

Tecnologias Utilizadas
Java 11+

JavaFX: Para a construção da interface gráfica (GUI).

FXML: Linguagem baseada em XML para definir a estrutura da interface do usuário.

MySQL: Sistema de Gerenciamento de Banco de Dados (SGBD).

JDBC: Driver para conectividade com o banco de dados MySQL.

📂 Estrutura de Pacotes
A estrutura de pastas do projeto segue a organização MVC:
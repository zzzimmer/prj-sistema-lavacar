É criada a interface que anuncia metodos de conexão, commit, rollback e disconect, e então, uma
No initializable de alguma das telas, é chamado um:
private final Database database = DatabaseFactory.getDatabase("mysql");

A classe Database é uma interface com métodos a serem implementados

DatabaseFactory é uma classe que existe visando uma "flexibilidade". Como se pode
ver no corpo da classe, existe um metodo getDatabase que retorna uma String com o nome
da "marca" da DataBase utilizada, para então <b>Instânciar<b> o objeto certo de DataBase. 

Este objeto realiza a conexão dentro de ControllerCategoria.

Como exercício, implementei uma classe DatabaseOracleExercicio. Dessa forma
é necessário alem de criar a classe, trazer para as dependências para o projeto.



{Oracle DataBase:

2. Como adicionar o driver JDBC da Oracle ao projeto?
Opção 1: Via Maven (recomendado para versões recentes)
A partir do Oracle JDBC Driver 18c (ojdbc8.jar em diante), o driver está disponível no Maven Central Repository, mas você precisa usar as coordenadas corretas:

xml
<dependency>
<groupId>com.oracle.database.jdbc</groupId>
<artifactId>ojdbc11</artifactId> <!-- ou ojdbc10, ojdbc8 -->
<version>21.5.0.0</version> <!-- Verifique a versão mais recente -->
</dependency>

Opção 2: Instalação manual (para versões mais antigas ou casos específicos)
Se o driver não estiver no Maven Central (ex: ojdbc6.jar):

Baixe o JAR do site da Oracle:
Oracle JDBC Drivers

Instale manualmente no repositório local do Maven:

bash
mvn install:install-file \
-Dfile=ojdbc8.jar \
-DgroupId=com.oracle \
-DartifactId=ojdbc8 \
-Dversion=21.5.0.0 \
-Dpackaging=jar
Adicione a dependência no pom.xml:

xml
<dependency>
<groupId>com.oracle</groupId>
<artifactId>ojdbc8</artifactId>
<version>21.5.0.0</version>
</dependency>

-- É uma sequencia de varios passos, de configuração de maven e instalação de driver JDBC na maquina
}
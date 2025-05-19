package br.edu.ifsc.fln.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseOracleExercicio implements Database{
    private Connection connection;

    @Override
    public Connection conectar(){
        try {
            Class.forName("oracle.jdb.driver.OracleDriver"); //forName() recebe um nome qualificado e instancia uma classe e !retorna! essa instância no metodo
            this.connection = DriverManager.getConnection("jdbc:oracle:@localhost:1521.XE",
                    "oracle",
                    "oracle"
            ); return this.connection;
        }   catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseOracleExercicio.class.getName()).log(Level.SEVERE,null,ex);
            return null;
        }
    }

    @Override
    public void desconectar(Connection con) {
        try {
            con.close();
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseOracleExercicio.class.getName()).log(Level.SEVERE,null,ex);
            // Não entendo direito o que essa linha acima faz, nao consigo ler ela para manipular caso queira
        }
    }

    @Override
    public void commit(Connection con) {
        try {
            con.commit();
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseOracleExercicio.class.getName()).log(Level.SEVERE,null,ex);
        }
    }

    @Override
    public void rollback(Connection con) {
        try {
            con.rollback();
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseOracleExercicio.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
}

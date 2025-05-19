package br.edu.ifsc.fln.model.database;

public class DatabaseFactory {
    public static Database getDatabase(String nome){
        if(nome.equals("postgresql")){
            return new DatabasePostgreSQL();
        }else if(nome.equals("mysql")){
            return new DatabaseMySQL();
        } else if (nome.equals("oracle")) { //nova implementação
            return new DatabaseOracleExercicio();//nome da classe
        }
        return null;
    }
}

package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cor;
import javafx.scene.control.Alert;
//import br.edu.ifsc.fln.model.domain.Marca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CorDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir (Cor cor){
        String sql = "INSERT INTO cor(nome) VALUES(?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cor.getNome());
            stmt.execute();
            return true;
        } catch (SQLException ex){
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar (Cor cor) {
        String sql = "UPDATE cor SET nome =? WHERE id =?"; // organiza a string sql
        try {
            PreparedStatement stmt; //declara
            stmt = connection.prepareStatement(sql); //inicializa stmt
            stmt.setString(1, cor.getNome());
            stmt.setLong(2, cor.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex){
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Cor cor){
        String sql = "DELETE FROM cor WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, cor.getId());
            stmt.execute();
            return true;
        } catch (SQLIntegrityConstraintViolationException ex){
            //Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de exclusão");
            alert.setHeaderText(null);
            alert.setContentText("Não é possível excluir esta cor pois existem veículos associados.");
            alert.showAndWait();
        } catch (SQLException ex){
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    public List<Cor> listar(){
        String sql = "SELECT * FROM cor";
        List<Cor> listaRetorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()){
                Cor cor = new Cor();
                cor.setId(resultado.getInt("id"));
                cor.setNome(resultado.getString("nome"));
                listaRetorno.add(cor);
            }
        } catch (SQLException ex){
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaRetorno;
    }

    public Cor buscar (long id){
        String sql = "SELECT * FROM cor WHERE id=?";
        Cor retorno = new Cor();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1,id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){
                retorno.setId(resultSet.getLong("id"));
                retorno.setNome(resultSet.getString("nome"));
            }
        } catch (SQLException ex){
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Cor buscar (Cor cor){
        Cor retorno = buscar(cor.getId());
        return retorno;
    }

}

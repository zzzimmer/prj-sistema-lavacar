 package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir (Servico servico){
        String sql = "INSERT INTO servico(descricao, valor, pontos) VALUES(?, ?, ? )";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getValor());
            stmt.setInt(3, servico.getPontos());
            stmt.execute();
            return true;
        } catch (SQLException ex){
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar (Servico servico) {
        String sql = "UPDATE servico SET descricao =?, valor=?, pontos=? WHERE id =?"; // organiza a string sql
        try {
            PreparedStatement stmt; //declara
            stmt = connection.prepareStatement(sql); //inicializa stmt
            stmt.setString(1, servico.getDescricao());
            stmt.setString(2,String.valueOf(servico.getValor()));
            stmt.setString(3,String.valueOf(servico.getPontos()));
            stmt.setInt(4, servico.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex){
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Servico servico){
        String sql = "DELETE FROM servico WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, servico.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex){
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Servico> listar(){
        String sql = "SELECT * FROM servico";
        List<Servico> listaRetorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()){
                Servico servico = new Servico();
                servico.setId(resultado.getInt("id"));
                servico.setDescricao(resultado.getString("descricao"));
                servico.setValor(resultado.getDouble("valor"));
                servico.setPontos(resultado.getInt("pontos"));
                listaRetorno.add(servico);
            }
        } catch (SQLException ex){
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaRetorno;
    }

    public Servico buscar (int id){
        String sql = "SELECT * FROM servico WHERE id=?";
        Servico retorno = new Servico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){
                retorno.setId(resultSet.getInt("id"));
                retorno.setDescricao(resultSet.getString("descricao"));
            }
        } catch (SQLException ex){
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Servico buscar (Servico servico){
        Servico retorno = buscar(servico.getId());
        return retorno;
    }
}

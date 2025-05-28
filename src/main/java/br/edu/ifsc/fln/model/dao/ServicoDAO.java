 package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Ecategoria;
import br.edu.ifsc.fln.model.domain.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
        String sql = "INSERT INTO servico(descricao, valor, pontos, categoria) VALUES(?, ?, ?, ? )";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getValor());
            stmt.setInt(3, servico.getPontos());
            stmt.setString(4, String.valueOf(servico.getEcategoria()));
            stmt.execute();
            return true;
        } catch (SQLException ex){
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar (Servico servico) {
        String sql = "UPDATE servico SET descricao =?, valor=?, pontos=?, categoria=? WHERE id =?"; // organiza a string sql
        try {
            PreparedStatement stmt; //declara
            stmt = connection.prepareStatement(sql); //inicializa stmt
            stmt.setInt(5, servico.getId());
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2,servico.getValor());
            stmt.setInt(3,servico.getPontos());
//            stmt.setString(4, String.valueOf(servico.getEcategoria())); funciona e retorna MOTO
            stmt.setString(4, servico.getEcategoria().toString()); // funciona retorna MOTO
//            stmt.setString(4,servico.getEcategoria().name()); // funciona retorna MOTO
//            stmt.setString(4, Arrays.toString(servico.getEcategoria().values())); // faz o printLn MOTO mas retorna erro data truncated
//            System.out.println(servico.getEcategoria()); // para testes de retorno
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
//              servico.setEcategoria((Ecategoria.valueOf(Ecategoria) resultado.getObject("categoria"))));
                servico.setEcategoria(Enum.valueOf(Ecategoria.class, resultado.getString("categoria")));
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

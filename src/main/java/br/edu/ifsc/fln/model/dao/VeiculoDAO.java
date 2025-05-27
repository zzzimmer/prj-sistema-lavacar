package br.edu.ifsc.fln.model.dao;


import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.model.domain.Cor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VeiculoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Veiculo veiculo) {
        final String sql = "INSERT INTO veiculo(placa, observacoes, id_cor) VALUES(?,?,?);";
        //final String sqlEstoque = "INSERT INTO estoque(id_produto) (SELECT max(id) FROM produto);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            //registra o veiculo
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setLong(3, veiculo.getCor().getId());
            stmt.execute();
            //registra o estoque do produto imediatamente // deve registra o dono?
            //stmt = connection.prepareStatement(sqlEstoque);// linha que trabalha com a composição
            //stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar (Veiculo veiculo){
        String sql = "UPDATE veiculo SET placa =?, observacoes =?, id_cor=? WHERE id=?";
        try{
            PreparedStatement stmt;
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2,veiculo.getObservacoes());
            stmt.setLong(3,veiculo.getCor().getId());// talvez seja melhor trazer um daqueles botão que desce as opções.
            stmt.execute();
            return true;
        }  catch (SQLException ex) {
        Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        return false;
        }
    }

    public boolean remover(Veiculo veiculo){
        String sql = "DELETE FROM veiculo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex){
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Veiculo> listar(){
        String sql = "SELECT * FROM veiculo";
        List<Veiculo> listaRetorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()){
                Veiculo veiculo = new Veiculo();
                veiculo.setId(resultado.getInt("id"));
                veiculo.setPlaca(resultado.getString("placa"));
                listaRetorno.add(veiculo);
            }
        } catch (SQLException ex){
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaRetorno;
    }

    public Veiculo buscar (int id){
        String sql = "SELECT * FROM veiculo WHERE id=?";
        Veiculo retorno = new Veiculo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){
                retorno.setId(resultSet.getInt("id"));
                retorno.setPlaca(resultSet.getString("placa"));
            }
        } catch (SQLException ex){
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Veiculo buscar (Veiculo veiculo){
        Veiculo retorno = buscar(veiculo.getId());
        return retorno;
    }
}

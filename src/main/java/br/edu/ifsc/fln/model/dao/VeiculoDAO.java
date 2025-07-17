package br.edu.ifsc.fln.model.dao;


import br.edu.ifsc.fln.model.domain.*;

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
        final String sql = "INSERT INTO veiculo(placa, observacoes, id_cor, id_modelo, id_cliente) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            //registra o veiculo
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setLong(3, veiculo.getCor().getId());
            stmt.setLong(4,veiculo.getModelo().getId());
            stmt.setLong(5,veiculo.getCliente().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar (Veiculo veiculo){
        String sql = "UPDATE veiculo SET placa =?, observacoes =?, id_cor=?, id_modelo=?, id_cliente=? WHERE id=?";
        try{
            PreparedStatement stmt;
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2,veiculo.getObservacoes());
            stmt.setLong(3,veiculo.getCor().getId());
            stmt.setLong(4,veiculo.getModelo().getId());
            stmt.setLong(5, veiculo.getCliente().getId());
            stmt.setInt(6,veiculo.getId());
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
                Veiculo veiculo = populateVO(resultado);
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

//    public boolean verificiarPessoaJuridica (int id) throws SQLException {
//        String sql = "SELECT * FROM pessoa_juridica WHERE id=?";
//         PreparedStatement stmt = connection.prepareStatement(sql);
//         stmt.setInt(1,id);
//         ResultSet resultSet = stmt.executeQuery();
//        return resultSet.next() == true;
//    }

    private Veiculo populateVO(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();

        veiculo.setId(rs.getInt("id"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setObservacoes(rs.getString("observacoes"));

        Modelo modelo = new Modelo();
        modelo.setId(rs.getInt("id_modelo"));
        ModeloDAO modeloDAO = new ModeloDAO();
        modeloDAO.setConnection(connection);
        modelo = modeloDAO.buscar(modelo);
        veiculo.setModelo(modelo);

        Cor cor = new Cor();
        cor.setId(rs.getInt("id_cor"));
        CorDAO corDAO = new CorDAO();
        corDAO.setConnection(connection);
        cor = corDAO.buscar(cor);
        veiculo.setCor(cor);


        long idCliente = rs.getLong("id_cliente");
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.setConnection(connection);
        Cliente cliente = clienteDAO.buscar(idCliente);
        veiculo.setCliente(cliente);

        return veiculo;
    }
}

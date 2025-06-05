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

public class ModeloDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir (Modelo modelo){
       final String sql = "INSERT INTO modelo(descricao, id_marca, categoria) VALUES(?,?,?)";
       final String sqlMotor = "INSERT INTO motor (id_modelo) (SELECT max(id) FROM modelo)"; // isso também adiciona na tabela de modelo o valor de id_motor
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3,String.valueOf(modelo.getEcategoria()));
            stmt.execute();
            stmt = connection.prepareStatement(sqlMotor);
            stmt.execute();
            return true;
        } catch (SQLException ex){
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar (Modelo modelo) {
        String sql = "UPDATE modelo SET descricao =?, id_marca=?, categoria=? WHERE id =?"; // organiza a string sql
        try {
            PreparedStatement stmt; //declara
            stmt = connection.prepareStatement(sql); //inicializa stmt
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3,String.valueOf(modelo.getEcategoria()));
            stmt.setInt(4, modelo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex){
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Modelo modelo){
        String sql = "DELETE FROM modelo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex){
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Modelo> listar(){
        String sql = "SELECT * FROM modelo";
        List<Modelo> listaRetorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()){
                Modelo modelo = populateVO(resultado);
                listaRetorno.add(modelo);
            }
        } catch (SQLException ex){
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaRetorno;
    }

    public Modelo buscar (int id){
        String sql = "SELECT * FROM modelo WHERE id=?";
        Modelo retorno = new Modelo();
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

    public Modelo buscar (Modelo modelo){
        Modelo retorno = buscar(modelo.getId());
        return retorno;
    }

    public Modelo populateVO(ResultSet resultSet) throws SQLException { // preciso escrever sobre. Não ta claro
        // como esse resultSet funciona.
        Modelo modelo = new Modelo();

        modelo.setId(resultSet.getInt("id"));
        modelo.setDescricao(resultSet.getString("descricao"));
        modelo.setEcategoria(Ecategoria.valueOf(resultSet.getString("categoria")));

        Marca marca = new Marca();
        marca.setId(resultSet.getInt("id_marca"));
        MarcaDAO marcaDAO = new MarcaDAO();
        marcaDAO.setConnection(connection);
        marca = marcaDAO.buscar(marca);

        //pense que, ao fazer getMotor, você ta usando um objeto, da mesma forma
        //que ao instânciar uma marca, você a usa para acesssar um atributo da tabela
        // de marca, aqui você usa esse getMotor para acessar a tabela de motor.
        modelo.getMotor().setPotencia(resultSet.getInt("potencia"));
        //Esse get da certo afinal toda instância de modelo tem um motor

        modelo.setMarca(marca);

        return modelo;

    }
}

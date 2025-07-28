package br.edu.ifsc.fln.model.dao;


import br.edu.ifsc.fln.exception.DAOException;
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

    public void inserir (Modelo modelo) throws DAOException {
       final String sql = "INSERT INTO modelo(descricao, id_marca, categoria) VALUES(?,?,?)";
       final String sqlMotor = "INSERT INTO motor (id_modelo, potencia, tipo_combustivel) VALUES ((SELECT MAX(id) FROM modelo), ?,?)";
        try{
            connection.setAutoCommit(false);

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3,String.valueOf(modelo.getEcategoria()));
            stmt.execute();

            stmt = connection.prepareStatement(sqlMotor);
            stmt.setInt(1,modelo.getMotor().getPotencia());
            stmt.setString(2,String.valueOf(modelo.getMotor().getEtipoCombustivel()));

            stmt.execute();
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
                System.out.println("Erro. Rollback");
            } catch (SQLException e) {
                Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, "Erro ao realizar rollback", e);
            }
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, "Modelo e Motor não foram inseridos, tente novamente", ex);
            throw new DAOException("Verificar banco de dados!", ex);
        }
        finally{
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e){
                Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, "Erro ao restaurar autocommit", e);
            }
        }
    }

    public boolean alterar (Modelo modelo) {
        String sql = "UPDATE modelo SET descricao =?, id_marca=?, categoria=? WHERE id =?";// organiza a string sql
        String sqlMotor = "UPDATE motor SET potencia=?, tipo_combustivel =? WHERE id_modelo=? ";
        try {
            PreparedStatement stmt; //declara
            stmt = connection.prepareStatement(sql); //inicializa stmt
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3,String.valueOf(modelo.getEcategoria()));
            stmt.setInt(4, modelo.getId());
            stmt.execute();

            stmt = connection.prepareStatement(sqlMotor);
            stmt.setInt(1, modelo.getMotor().getPotencia());
            stmt.setString(2, String.valueOf(modelo.getMotor().getEtipoCombustivel()));
            stmt.setInt(3,modelo.getId());

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
        String sql = "SELECT modelo.*, motor.* FROM modelo " + "INNER JOIN motor " +
                "ON modelo.id = motor.id_modelo";
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
                retorno.setEcategoria(Ecategoria.valueOf(resultSet.getString("categoria")));

                Marca marca = new Marca();
                MarcaDAO marcaDAO = new MarcaDAO();
                marcaDAO.setConnection(connection);
                marca = marcaDAO.buscar(resultSet.getInt("id_marca"));
                retorno.setMarca(marca);


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

        modelo.getMotor().setPotencia(resultSet.getInt("potencia"));
//        System.out.println((resultSet.getInt("potencia")));
        modelo.getMotor().setEtipoCombustivel(Enum.valueOf
                (EtipoCombustivel.class,resultSet.getString("tipo_combustivel")));
//        System.out.println(resultSet.getString("tipo_combustivel"));

        modelo.setMarca(marca);

        return modelo;

    }
}

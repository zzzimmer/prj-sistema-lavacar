package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.Veiculo;

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
        final String sql = "INSERT INTO produto(placa, observacoes, id_cor) VALUES(?,?,?);";
        //final String sqlEstoque = "INSERT INTO estoque(id_produto) (SELECT max(id) FROM produto);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            //registra o produto
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setString(3, veiculo.getCor().getNome());
            stmt.execute();
            //registra o estoque do produto imediatamente
            //stmt = connection.prepareStatement(sqlEstoque);
            //stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}

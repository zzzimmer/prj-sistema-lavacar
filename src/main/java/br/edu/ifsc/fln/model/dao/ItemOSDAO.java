package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemOSDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(ItemOS itemOS) {
        String sql = "INSERT INTO item_os(valor_servico, observacoes, id_ordem_servico, id_servico) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, itemOS.getValorServico());
            stmt.setString(2, itemOS.getObservacoes());
            stmt.setLong(3,itemOS.getOrdemServico().getNumero());
            stmt.setLong(4, itemOS.getServico().getId());

            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<ItemOS> listarPorOS(OrdemServico ordemServico) {
        String sql = "SELECT * FROM item_os WHERE id_servico=?";
        List<ItemOS> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,((int) ordemServico.getNumero()));
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                ItemOS itemOS = new ItemOS();
                Servico servico = new Servico();
                OrdemServico os = new OrdemServico();
                OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();

                itemOS.setId(resultSet.getInt("id"));
                itemOS.setObservacoes(resultSet.getString("observacoes"));

                servico.setId(resultSet.getInt("id_servico"));
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico);

                itemOS.setServico(servico);
                itemOS.setValorServico(resultSet.getDouble("valor_servico"));

                os.setNumero(resultSet.getInt("id_ordem_servico"));
                os = ordemServicoDAO.buscar(os);

                itemOS.setOrdemServico(os);
                retorno.add(itemOS);
            }

        } catch (SQLException e){

        }
        return retorno;
    }




}

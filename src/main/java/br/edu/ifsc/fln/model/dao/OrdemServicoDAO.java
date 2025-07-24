package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdemServicoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public boolean inserir(OrdemServico ordemServico) { //todo 1
        String sql = "INSERT INTO ordem_servico(total,agenda,desconto,id_veiculo,status) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
//            stmt.setLong(1, ordemServico.getNumero());
            stmt.setBigDecimal(1, BigDecimal.valueOf(ordemServico.getTotal()));
            stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));//todo -->observar
            stmt.setDouble(3, ordemServico.getDesconto());
            stmt.setInt(4, ordemServico.getVeiculo().getId());
            if  (ordemServico.getStatus() != null) {
                stmt.setString(5, ordemServico.getStatus().name());
            }

            System.out.println(ordemServico.getListItemOs().getFirst().toString());


            stmt.execute();
            ItemOSDAO itemOSDAO = new ItemOSDAO();
            itemOSDAO.setConnection(connection);

            for (ItemOS itemOS: ordemServico.getListItemOs()){
                itemOS.setOrdemServico(this.buscarUltimaOs());
                itemOSDAO.inserir(itemOS);
            }
//
//            for (ItemOS itemOS : ordemServico.getListServicos()){
//                System.out.println("----------------");
////                System.out.println(itemOS.getObservacoes());
//                System.out.println(itemOS.getOrdemServico().getNumero());
//                System.out.println(itemOS.getServico().getId());
//            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        }

    public boolean alterar(OrdemServico ordemServico) {
        String sql = "UPDATE ordem_servico SET total=?, agenda=?, desconto=?, id_veiculo=?, status=? WHERE id=?";
        try {

            connection.setAutoCommit(false);

            ItemOSDAO itemOSDAO = new ItemOSDAO();

//            OrdemServico ordemServicoAnterior = buscar(ordemServico);

            List<ItemOS> itemOS = itemOSDAO.listarPorOS(ordemServico);

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1,BigDecimal.valueOf(ordemServico.getTotal()));
            stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(3, ordemServico.getDesconto());
            stmt.setInt(4, ordemServico.getVeiculo().getId());

            if  (ordemServico.getStatus() != null) { //todo admito que aqui nesse fiquei ??
                stmt.setString(5, ordemServico.getStatus().name());
            } else {
                stmt.setString(5, EStatus.ABERTA.name());
            }
            stmt.setDouble(6, ordemServico.getNumero());
            stmt.execute();

            return true;
        } catch (SQLException e){

        }
        return false;
    }

    public OrdemServico buscar(OrdemServico ordemServico) {
        String sql = "SELECT * FROM ordem_servico WHERE id=?";

        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                ordemServicoRetorno.setNumero(resultado.getLong("numero"));
                ordemServicoRetorno.setTotal(resultado.getDouble("total"));
                ordemServicoRetorno.setDesconto(resultado.getDouble("desconto"));
                ordemServicoRetorno.setAgenda(resultado.getDate("agenda").toLocalDate());

                ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                veiculo.setId(resultado.getInt("id_veiculo"));

                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }

    public List<OrdemServico> listar() {
        String sql = "SELECT * FROM ordem_servico";
        List<OrdemServico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
//                ItemOS itemOS = new ItemOS();
                List<ItemOS> itemOSList = new ArrayList<>();
                ItemOSDAO itemOSDAO = new ItemOSDAO();

                ordemServico.setNumero(resultado.getLong("numero"));
                ordemServico.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                ordemServico.setAgenda(resultado.getDate("agenda").toLocalDate());//todo observar possivel problema
                ordemServico.setDesconto(resultado.getDouble("desconto"));
                ordemServico.setTotal(resultado.getDouble("total"));

                Veiculo veiculo = new Veiculo();
                veiculo.setId(resultado.getInt("id_veiculo"));
                VeiculoDAO veiculoDAO = new VeiculoDAO();
                veiculoDAO.setConnection(connection);
                veiculo = veiculoDAO.buscar(veiculo);

                ordemServico.setVeiculo(veiculo);

                itemOSDAO.setConnection(connection);
                itemOSList = itemOSDAO.listarPorOS(ordemServico);

                ordemServico.setListItemOs(itemOSList);

                retorno.add(ordemServico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public OrdemServico buscarUltimaOs(){
        String sql = "SELECT max(numero) as max FROM ordem_servico";

        OrdemServico retorno = new OrdemServico();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()){
                retorno.setNumero(resultado.getLong("max"));
                return retorno;
            }
        } catch (SQLException e) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return retorno;
    }

    public boolean remover(OrdemServico ordemServico) {
        String sql = "DELETE FROM ordem_servico WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            try {
                connection.setAutoCommit(false);
                ItemOSDAO itemOSDAO = new ItemOSDAO();
                itemOSDAO.setConnection(connection);

                for (ItemOS itemOS: ordemServico.getListItemOs()){
                    itemOSDAO.remover(itemOS);
                }
                stmt.setLong(1, ordemServico.getNumero());
                stmt.execute();
                connection.commit();
            } catch (SQLException exc) {
                try {
                    connection.rollback();
                } catch (SQLException exc1) {
                    Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, exc1);
                }
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc);
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }



    }


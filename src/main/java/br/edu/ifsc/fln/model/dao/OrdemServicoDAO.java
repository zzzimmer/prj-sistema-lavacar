package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.*;
import br.edu.ifsc.fln.utils.AlertDialog;

import java.math.BigDecimal;
import java.sql.*;
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


    public boolean inserir(OrdemServico ordemServico) {
        String sql = "INSERT INTO ordem_servico(total,agenda,desconto,id_veiculo,status) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
//            stmt.setLong(1, ordemServico.getNumero());
            stmt.setBigDecimal(1, BigDecimal.valueOf(ordemServico.getTotal()));
            stmt.setDate(2, (Date) ordemServico.getAgenda());//todo -->observar
            stmt.setDouble(3, ordemServico.getDesconto());
            stmt.setInt(4, ordemServico.getVeiculo().getId());
            if  (ordemServico.getStatus() != null) {
                stmt.setString(5, ordemServico.getStatus().name());
            }
//            else {
//                //TODO apresentar situação clara de inconsistência de dados
//                //tratamento de exceções e a necessidade de uso de commit e rollback
//                //stmt.setString(6, "teste");
//                stmt.setString(6, EStatusOrdemServico.ABERTA.name());
//            }
//            stmt.setInt(7, venda.getCliente().getId());
            stmt.execute();
            ItemOSDAO itemOSDAO = new ItemOSDAO();
            itemOSDAO.setConnection(connection);

            for (ItemOS itemOS: ordemServico.getListServicos()){
                itemOSDAO.inserir(itemOS);
                //todo o paralelo de agendamento é estoque em Vendas. Dentro desse mesmo for
            }
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
        //todo adicionar agendamento
        }

    public boolean alterar(OrdemServico ordemServico) {
        String sql = "UPDATE ordem_servico SET total=?, agenda=?, desconto=?, id_veiculo=?, status=? WHERE id=?";
        try {

            connection.setAutoCommit(false);

            ItemOSDAO itemOSDAO = new ItemOSDAO();

            OrdemServico ordemServicoAnterior = buscar(ordemServico);

//            List<ItemOS> itemOS = ItemOSDAO;

//            Venda vendaAnterior = buscar(venda.getCdVenda());
//            Venda vendaAnterior = buscar(venda);
//            List<ItemDeVenda> itensDeVenda = itemDeVendaDAO.listarPorVenda(vendaAnterior);
//            for (ItemDeVenda iv : itensDeVenda) {
//                //Produto p = iv.getProduto(); //isto não da certo ...
//                Produto p = estoqueDAO.getEstoque(iv.getProduto());
//                p.getEstoque().repor(iv.getQuantidade());
//                estoqueDAO.atualizar(p.getEstoque());
//                itemDeVendaDAO.remover(iv);
//            }
            //atualiza os dados da venda
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, ordemServico.getTotal());
            stmt.setDate(2,Date.valueOf(ordemServico.getAgenda()));
            stmt.setBigDecimal(2, venda.getTotal());
            stmt.setBoolean(3, venda.isPago());
            stmt.setDouble(4, venda.getTaxaDesconto());
            stmt.setString(5, Venda.getEmpresa());
            if  (venda.getStatusVenda() != null) {
                stmt.setString(6, venda.getStatusVenda().name());
            } else {
                stmt.setString(6, EStatusVenda.ABERTA.name());
            }
            stmt.setInt(7, venda.getCliente().getId());
            stmt.setInt(8, venda.getId());
            stmt.execute();
            for (ItemDeVenda iv: venda.getItensDeVenda()) {
                //Produto p = iv.getProduto(); //isto não da certo ...
                Produto p = estoqueDAO.getEstoque(iv.getProduto());
                p.getEstoque().retirar(iv.getQuantidade());
                estoqueDAO.atualizar(p.getEstoque());
                itemDeVendaDAO.inserir(iv);
            }
            connection.commit();
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException exc1) {
                Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, exc1);
            }
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (MovimentacaoEstoqueException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            AlertDialog.exceptionMessage(ex);
            return false;
        }
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
                ordemServicoRetorno.setAgenda(resultado.getDate("agenda"));

                ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                veiculo.setId(resultado.getInt("id_veiculo"));

                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }





    }


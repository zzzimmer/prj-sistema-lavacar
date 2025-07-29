/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller.relatorios;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.util.JRLoader;
//import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneRelatorioOrdensServicoController implements Initializable {

    @FXML
    private TableView<OrdemServico> tableView;

    @FXML
    private TableColumn<OrdemServico, Integer> tableColumnNumeroOs;

    @FXML
    private TableColumn<OrdemServico, Double> tableColumnTotal;

    @FXML
    private TableColumn<OrdemServico, LocalDate> tableColumnAgenda;

    @FXML
    private TableColumn<OrdemServico, Double> tableColumnDesconto;

    @FXML
    private TableColumn<OrdemServico, Veiculo> tableColumnIdVeiculo;

    @FXML
    private TableColumn<OrdemServico, EStatus> tableColumnStatus;

    private List<OrdemServico> listaOrdensServico;
    private ObservableList<OrdemServico> observableListOrdensServico;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ordemServicoDAO.setConnection(connection);
        carregarTableView();
    }

    private void carregarTableView() {
        try {
            listaOrdensServico = ordemServicoDAO.listar();
        } catch (DAOException ex) {
            Logger.getLogger(FXMLAnchorPaneRelatorioOrdensServicoController.class.getName()).log(Level.SEVERE, null, ex);
            AlertDialog.exceptionMessage(ex);
            return;
        }

        tableColumnNumeroOs.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tableColumnAgenda.setCellValueFactory(new PropertyValueFactory<>("agenda"));
        tableColumnDesconto.setCellValueFactory(new PropertyValueFactory<>("desconto"));
        tableColumnIdVeiculo.setCellValueFactory(new PropertyValueFactory<>("veiculo"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        observableListOrdensServico = FXCollections.observableArrayList(listaOrdensServico);
        tableView.setItems(observableListOrdensServico);
    }

//    //@FXML
//    public void handleImprimir() throws JRException {
//        URL url = getClass().getResource("/report/PrjSistemaLavacarRelOrdemServico.jasper");
//        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(url);
//
//        //null: caso não existam filtros
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
//
//        //false: não deixa fechar a aplicação principal
//        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
//        jasperViewer.setVisible(true);
//    }
}
package br.edu.ifsc.fln.controller.processo;

import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.utils.AlertDialog;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class FXMLAnchorPaneProcessoOrdemServicoController implements Initializable {

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonInserir;

    @FXML
    private Button buttonRemover;

    @FXML
    private CheckBox checkBoxVendaPago;

    @FXML
    private Label labelOsCliente;

    @FXML
    private Label labelOsData;

    @FXML
    private Label labelOsDesconto;

    @FXML
    private Label labelOsId;

    @FXML
    private Label labelOsStatus;

    @FXML
    private Label labelOsTotal;

    @FXML
    private TableColumn<OrdemServico, LocalDate> tableColumnOsData;

    @FXML
    private TableColumn<OrdemServico, Integer> tableColumnOsId;

    @FXML
    private TableColumn<?, ?> tableColumnOsVeiculoPlaca;

    @FXML
    private TableView<OrdemServico> tableView;

    private List<OrdemServico>listOrdemServico;
    private ObservableList<OrdemServico> observableListOrdemServico;


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

        tableView.getSelectionModel().selectedItemProperty().addListener((
                observable, oldValue,
                newValue) -> selecionarItemTableView(newValue));
    }

//    @FXML
//    void handleButtonAlterar(ActionEvent event) {
//
//    }
//
//    @FXML
//    void handleButtonInserir(ActionEvent event) {
//
//    }
//
//    @FXML
//    void handleButtonRemover(ActionEvent event) {
//
//    }

    public void carregarTableView() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        tableColumnOsId.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tableColumnOsData.setCellFactory(column -> {
            return new TableCell<OrdemServico, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {

                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });

        tableColumnOsData.setCellValueFactory(new PropertyValueFactory<>("agenda"));
        tableColumnOsVeiculoPlaca.setCellValueFactory(new PropertyValueFactory<>("id_veiculo")); //todo retornar placa

        listOrdemServico = ordemServicoDAO.listar();

        observableListOrdemServico = FXCollections.observableArrayList(listOrdemServico);
        tableView.setItems(observableListOrdemServico);
    }

    public void selecionarItemTableView(OrdemServico ordemServico) {
        if (ordemServico != null){
            labelOsId.setText(Double.toString(ordemServico.getNumero()));
            labelOsData.setText(ordemServico.getAgenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            labelOsDesconto.setText(Double.toString(ordemServico.getDesconto()));
            labelOsStatus.setText(ordemServico.getStatus().name());
            labelOsCliente.setText(ordemServico.getVeiculo().getCliente().getNome());
            labelOsTotal.setText(Double.toString(ordemServico.getTotal()));
        }else {
            labelOsId.setText("");
            labelOsData.setText("");
            labelOsDesconto.setText("");
            labelOsStatus.setText("");
            labelOsCliente.setText("");
            labelOsTotal.setText("");
        }
    }

    public boolean showFXMLAnchorPaneProcessoOrdemServicoDialog(OrdemServico ordemServico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneProcessoOrdemServicoDialogController.class.getResource(
                "/view/processo/FXMLAnchorPaneProcessoOrdemServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Ordens de Serviço");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o venda ao controller
        FXMLAnchorPaneProcessoOrdemServicoDialogController controller = loader.getController();

        controller.setDialogStage(dialogStage);
        controller.setOrdemServico(ordemServico);

        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }

    @FXML
    private void handleButtonInserir(ActionEvent event) throws IOException{
        OrdemServico ordemServico = new OrdemServico();
        List<ItemOS> itemOSList = new ArrayList<>();
        ordemServico.setListServicos(itemOSList);
        boolean buttonConfirmaClicked = showFXMLAnchorPaneProcessoOrdemServicoDialog(ordemServico);
        if (buttonConfirmaClicked){
            ordemServicoDAO.setConnection(connection);
            ordemServicoDAO.inserir(ordemServico);
            carregarTableView();
        }
    }






}

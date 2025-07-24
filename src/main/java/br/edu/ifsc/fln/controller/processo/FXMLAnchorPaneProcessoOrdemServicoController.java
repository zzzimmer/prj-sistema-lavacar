package br.edu.ifsc.fln.controller.processo;

import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Veiculo;
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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private TableColumn<Veiculo, String> tableColumnOsVeiculoPlaca;

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

        tableColumnOsVeiculoPlaca.setCellValueFactory(new PropertyValueFactory<>("veiculo"));
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
//        tableColumnOsVeiculoPlaca.setCellValueFactory(new PropertyValueFactory<>("placa")); //todo retornar placa

        listOrdemServico = ordemServicoDAO.listar();

        observableListOrdemServico = FXCollections.observableArrayList(listOrdemServico);
        tableView.setItems(observableListOrdemServico);
    }

    public void selecionarItemTableView(OrdemServico ordemServico) {
        if (ordemServico != null){

//            System.out.println("=== RESUMO DA ORDEM DE SERVIÇO ===");
////            System.out.println("ID............: " + ordemServico.getNumero());
////            System.out.println("Data..........: " +
////                    ordemServico.getAgenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
////            System.out.println("Desconto......: R$ " +
////                    String.format("%.2f", ordemServico.getDesconto()));
////            System.out.println("Status........: " + ordemServico.getStatus().name());
//            System.out.println("Cliente.......: " +
//                    ordemServico.getVeiculo().getCliente().getNome());
//            System.out.println("Total (líquido): R$ " +
//                    String.format("%.2f", ordemServico.getTotal()));
//            System.out.println("==================================");

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
        //todo 6 metodo que convoca a carregar view&Controller de dialog
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneProcessoOrdemServicoDialogController.class.getResource(
                "/view/processo/FXMLAnchorPaneProcessoOrdemServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();//todo 7 chama o anchorPane. Objeto que grava view&Controller //todo 8 COMO essa linha funciona?
        // Instancia um objeto AnchorPane, mas o que é isso de "(AnchorPane) loader.load()" ao instanciar?

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Ordens de Serviço");
        Scene scene = new Scene(page);//todo 9 aqui o dialog stage recebe o objeto que contem view&Controller
        dialogStage.setScene(scene);

        //1 instancia um objeto ...ServicoDialogController 2 que recebe o retorno do méthod getController da instância loader
        // vindo da linha 206.
        //todo 11 isso é "novidade" também. Uma classe que em seu construtor recebe o retorno do método de outra classe.
        FXMLAnchorPaneProcessoOrdemServicoDialogController controller = loader.getController();

        controller.setDialogStage(dialogStage);
        //todo 12 Tendo o objeto instanciado, se usa o de seu método setOrdemServico para "preencher" a variável no objeto da classe.
        controller.setOrdemServico(ordemServico);//

        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();//todo 17 retorna aqui o método
    }

    @FXML
    private void handleButtonInserir(ActionEvent event) throws IOException{
        OrdemServico ordemServico = new OrdemServico();//todo 2-3 instancia um objeto do tipo adequado
        List<ItemOS> itemOSList = new ArrayList<>();
        ordemServico.setListItemOs(itemOSList);//todo 4 atribui uma lista de itemOs
        boolean buttonConfirmaClicked = showFXMLAnchorPaneProcessoOrdemServicoDialog(ordemServico);//todo 5 manda para o metodo que puxa o controller
        //todo 18 aqui tem o retorno de showFXMLAnchorPaneProcessoOrdemServicoDialog
        //todo esse buttonConfirmaClicked apenas assegura que o método showFXMLAnchorPaneProcessoOrdemServicoDialog foi usado
        // pois isso significa que a ordemServico instânciada percorreu completamente o caminho de aquisição dos dados e pode ser salva no banco
        System.out.println(buttonConfirmaClicked);
        if (buttonConfirmaClicked){
            ordemServicoDAO.setConnection(connection);
            ordemServicoDAO.inserir(ordemServico);//todo 19 recebe um objeto para salvar no banco
            carregarTableView();
        }

//        @FXML
//        private void handleButtonRemover(ActionEvent event) throws SQLException {
//            OrdemServico ordemServico1 = tableView.getSelectionModel().getSelectedItem();
//            if (ordemServico1 != null) {
//                if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir a venda " + ordemServico1.getNumero())) {
//                    ordemServicoDAO.setConnection(connection);
//                    ordemServicoDAO.remover(ordemServico1);
//
//                    carregarTableView();
//                }
//            } else {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setHeaderText("Por favor, escolha uma Os na tabela!");
//                alert.show();
//            }
//        }
    }








}

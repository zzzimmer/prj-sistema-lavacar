/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller.cadastro;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLAnchorPaneCadastroClienteController implements Initializable{

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbClienteCelular;

    @FXML
    private Label lbClienteEmail;

    @FXML
    private Label lbClienteId;

    @FXML
    private Label lbClienteNome;

    @FXML
    private Label lbClienteNumFiscal;

    @FXML
    private Label lbClienteTipo;

    @FXML
    private Label lbClienteDataNascimento;

    @FXML
    private Label lbDataNasc;

    @FXML
    private Label lbInscricaoEstadual;

    @FXML
    private Label lbInscricaoEstadual2;

    @FXML
    private Label lbCPFeCNPJ;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteCelular; // O segundo parametro faz o que mesmo?

    @FXML
    private TableColumn<Cliente, String > tableColumnClienteNome;

    @FXML
    private TableView<Cliente> tableViewCliente;

    
    private List<Cliente> listaCliente;
    private ObservableList<Cliente> observableListCliente;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();

    private final ClienteDAO clienteDAO = new ClienteDAO();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        carregarTableViewCliente();
        tableViewCliente.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCliente(newValue));
    }     
    
    public void carregarTableViewCliente() {
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnClienteCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));

        lbDataNasc.setVisible(false);
        lbClienteDataNascimento.setVisible(false);

        
        listaCliente = clienteDAO.listar();
        
        observableListCliente = FXCollections.observableArrayList(listaCliente);
        tableViewCliente.setItems(observableListCliente);
    }
    
    public void selecionarItemTableViewCliente(Cliente cliente) {
        if (cliente == null){
            lbCPFeCNPJ.setText("CPF/CNPJ"); // acho que isso nao funciona, talvez possa tirar
        }
        if (cliente != null) {
            lbClienteId.setText(String.valueOf(cliente.getId()));
            lbClienteNome.setText(cliente.getNome());
            lbClienteCelular.setText(cliente.getCelular());
            lbClienteEmail.setText(cliente.getEmail());
            if (cliente instanceof PessoaFisica) {
                lbCPFeCNPJ.setText("CPF");
                lbClienteTipo.setText("Pessoa Física");
                lbClienteNumFiscal.setText(((PessoaFisica)cliente).getCpf());

                lbDataNasc.setText("Data Nascimento");
                lbDataNasc.setVisible(true);
                lbClienteDataNascimento.setText(String.valueOf(((PessoaFisica) cliente).getDataNascimento()));
                lbClienteDataNascimento.setVisible(true);

                lbInscricaoEstadual.setVisible(false);
                lbInscricaoEstadual2.setVisible(false);
            } else {
                lbCPFeCNPJ.setText("CNPJ");
                lbClienteTipo.setText("Pessoa Juridica");
                lbClienteNumFiscal.setText(((PessoaJuridica)cliente).getCnpj());

                lbInscricaoEstadual.setText("Inscrição Estadual");
                lbInscricaoEstadual.setVisible(true);
                lbInscricaoEstadual2.setVisible(true);
                lbInscricaoEstadual2.setText(((PessoaJuridica) cliente).getInscricaoEstadual());

                lbDataNasc.setVisible(false);
                lbClienteDataNascimento.setVisible(false);
            }
        } else {
            lbClienteId.setText("");
            lbClienteNome.setText("");
            lbClienteCelular.setText("");
            lbClienteEmail.setText("");
            lbClienteTipo.setText("");
            lbClienteNumFiscal.setText("");
            lbClienteDataNascimento.setText("");

        }
        
    }
    
    private Cliente getTipoCliente() {
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Pessoa Fisica");
        opcoes.add("Pessoa Juridica");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Pessoa Fisica", opcoes);
        dialog.setTitle("Dialogo de Opções");
        dialog.setHeaderText("Escolha o tipo de cliente");
        dialog.setContentText("Tipo de cliente: ");
        Optional<String> escolha = dialog.showAndWait();
        if (escolha.isPresent()) {
            if (escolha.get().equalsIgnoreCase("Pessoa Fisica")){
                return new PessoaFisica();
            }
            else{
                return new PessoaJuridica();
            }
        } else {
            return null;
        }
    }

    @FXML
    public void handleBtInserir() throws IOException, DAOException {
        Cliente cliente = getTipoCliente();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
        if (cliente != null ) {
            if (btConfirmarClicked) {
                try{
                clienteDAO.inserir(cliente);
                } catch (DAOException ex){
                    Logger.getLogger(FXMLAnchorPaneCadastroClienteController.class.getName()).log(Level.SEVERE, null, ex);
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableViewCliente();
            }
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Cliente cliente = tableViewCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir o cliente " + cliente.getNome())) {
                clienteDAO.remover(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cliente na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Cliente cliente = tableViewCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Cliente na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroClienteDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClienteController.class.getResource("/view/cadastro/FXMLAnchorPaneCadastroClienteDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cliente");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o obejto cliente para o controller
        FXMLAnchorPaneCadastroClienteDialogController controller = loader.getController(); // essa linha
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);

        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }

}

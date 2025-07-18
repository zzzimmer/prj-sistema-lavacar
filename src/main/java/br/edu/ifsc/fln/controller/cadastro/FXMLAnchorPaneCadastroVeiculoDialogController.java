/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller.cadastro;

import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroVeiculoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<Cor> cbCor;

//    @FXML
//    private ComboBox<Marca> cbMarca;

    @FXML
    private ComboBox<Modelo> cbModelo;

    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private TextField tfObservacoes;

    @FXML
    private TextField tfPlaca;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Veiculo veiculo;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();

    private final MarcaDAO marcaDAO = new MarcaDAO();
    private final CorDAO corDAO = new CorDAO();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();


    private List<Marca> listaMarca;
    private ObservableList<Marca> marcaObservableList;

    private List<Cor> listaCor;
    private ObservableList<Cor> corObservableList;

    private List<Modelo> listaModelo;
    private ObservableList<Modelo> modeloObservableList;

    private List<Cliente> listaCliente;
    private ObservableList<Cliente> clienteObservableList;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);

        corDAO.setConnection(connection);
        carregarComboBoxCor();

        modeloDAO.setConnection(connection);
        carregarComboBoxModelo();

        clienteDAO.setConnection(connection);
        carregarComboBoxCliente();

//        cbCategoria.getItems().addAll(Ecategoria.values());
    }

    public void carregarComboBoxCor() {
        listaCor = corDAO.listar();

        corObservableList = FXCollections.observableArrayList(listaCor);
        cbCor.setItems(corObservableList);
    }

    public void carregarComboBoxModelo() {
        listaModelo = modeloDAO.listar();

        modeloObservableList = FXCollections.observableArrayList(listaModelo);
        cbModelo.setItems(modeloObservableList);

    }

    public void carregarComboBoxCliente(){
        listaCliente = clienteDAO.listar();

        clienteObservableList = FXCollections.observableArrayList(listaCliente);
        cbCliente.setItems(clienteObservableList);
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        this.tfObservacoes.setText(veiculo.getObservacoes());
        this.tfPlaca.setText(veiculo.getPlaca());
//        this.cbMarca.setValue(veiculo.getModelo());
        this.cbCor.setValue(veiculo.getCor());
        this.cbModelo.setValue(veiculo.getModelo());
        this.cbCliente.setValue(veiculo.getCliente());
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            veiculo.setPlaca(tfPlaca.getText());
            veiculo.setObservacoes(tfObservacoes.getText());
            veiculo.setCor(cbCor.getSelectionModel().getSelectedItem());
            veiculo.setModelo(cbModelo.getSelectionModel().getSelectedItem());
            veiculo.setCliente(cbCliente.getSelectionModel().getSelectedItem());


            btConfirmarClicked = true;
            dialogStage.close();
            }
        }

        @FXML
        public void handleBtCancelar () {
            dialogStage.close();
        }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        String placa = this.tfPlaca.getText();
        if (placa == null || placa.trim().isEmpty()) {
            errorMessage += "Placa inválida (não pode ser vazia).\n";
        } else {
            // Define o padrão Regex que aceita os dois formatos de placa
            String padraoPlaca = "^[A-Z]{3}-?\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$";

            // Converte a placa digitada para maiúsculas para a verificação
            String placaMaiuscula = placa.toUpperCase();

            // Verifica se a placa digitada corresponde a um dos padrões
            if (!placaMaiuscula.matches(padraoPlaca)) {
                errorMessage += "Formato de placa inválido.\nFormatos aceitos: AAA-1234 ou ABC1D23.\n";
            }
        }
        if (this.tfObservacoes.getText() == null || this.tfPlaca.getText().length() == 0) {
            errorMessage += "Observações inválidas.\n";
        }
        if (this.cbCor.getValue() == null) {
            errorMessage += "Selecione uma cor\n";
        }
//        if (this.cbMarca.getValue() == null) {
//            errorMessage += "Selecione uma marca\n";
//        }
        if (this.cbModelo.getValue() == null) {
            errorMessage += "Selecione um modelo\n";
        }
        if(this.cbCliente.getValue() == null){
            errorMessage+= "Selecione um Dono do veículo\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

    }



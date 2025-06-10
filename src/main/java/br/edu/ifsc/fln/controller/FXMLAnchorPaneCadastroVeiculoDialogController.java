/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

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

    @FXML
    private ComboBox<Marca> cbMarca;

    @FXML
    private ComboBox<Modelo> cbModelo;

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


    private List<Marca> listaMarca;
    private ObservableList<Marca> marcaObservableList;

    private List<Cor> listaCor;
    private ObservableList<Cor> corObservableList;

    private List<Modelo> listaModelo;
    private ObservableList<Modelo> modeloObservableList;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        carregarComboBoxMarca();

        corDAO.setConnection(connection);
        carregarComboBoxCor();

        modeloDAO.setConnection(connection);
        carregarComboBoxModelo();

//        cbCategoria.getItems().addAll(Ecategoria.values());
    }

    public void carregarComboBoxMarca(){
//        try {
        listaMarca = marcaDAO.listar();
//        } catch (DAOException ex) {
//            AlertDialog.exceptionMessage(ex);
//        }
        marcaObservableList =
                FXCollections.observableArrayList(listaMarca); // como esse operador de igualdade funciona?
        // significa que o retorno do method é um observable list? e como já tem alguem para receber - no caso
        // marcaObservableList - este só "aboserve" o resultado do method observableArrayList ?

//        marcaObservableList.observableArrayList(listaMarca);// por que não?
        cbMarca.setItems(marcaObservableList);
    }

    public void carregarComboBoxCor(){
        listaCor = corDAO.listar();

        corObservableList = FXCollections.observableArrayList(listaCor);
        cbCor.setItems(corObservableList);
    }

    public void carregarComboBoxModelo(){
        listaModelo = modeloDAO.listar();

        modeloObservableList = FXCollections.observableArrayList(listaModelo);
        cbModelo.setItems(modeloObservableList);

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
//        this.tfDescricao.setText(modelo.getDescricao());
    }
    

    @FXML
    public void handleBtConfirmar() {
        //if (validarEntradaDeDados()) {

            veiculo.setPlaca(tfPlaca.getText());
            veiculo.setObservacoes(tfObservacoes.getText());
            veiculo.setCor(cbCor.getSelectionModel().getSelectedItem());
            veiculo.setModelo(cbModelo.getSelectionModel().getSelectedItem());


            btConfirmarClicked = true;
            dialogStage.close();
        //}
    }
    
    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }
    
    //todo validar a entrada de dados além de descrição. Adicionar para enum e etc
//    private boolean validarEntradaDeDados() {
//        String errorMessage = "";
//        if (this.tfDescricao.getText() == null || this.tfDescricao.getText().length() == 0) {
//            errorMessage += "Descrição inválida.\n";
//        }
//
//        if (errorMessage.length() == 0) {
//            return true;
//        } else {
//            //exibindo uma mensagem de erro
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Erro no cadastro");
//            alert.setHeaderText("Corrija os campos inválidos!");
//            alert.setContentText(errorMessage);
//            alert.show();
//            return false;
//        }
    //}
    
}

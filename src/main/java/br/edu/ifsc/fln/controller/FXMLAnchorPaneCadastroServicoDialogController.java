/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Ecategoria;
import br.edu.ifsc.fln.model.domain.Servico;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroServicoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfDescricao;

    @FXML
    private TextField tfPontos;

    @FXML
    private TextField tfValor;
    
    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Servico servico;

//    @FXML - Errado.
//    private ChoiceBox <Ecategoria> cbCategoria = new ChoiceBox<>();

    @FXML
    private ChoiceBox<Ecategoria> cbCategoria;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbCategoria.getItems().addAll(Ecategoria.values()); // Por que tem mais de um value? e valueOf?
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

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        this.tfDescricao.setText(servico.getDescricao());
        this.tfValor.setText(String.valueOf(servico.getValor()));
        this.tfPontos.setText(String.valueOf(servico.getPontos()));
    }


    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            servico.setDescricao(tfDescricao.getText());
            servico.setValor(Float.parseFloat(tfValor.getText()));
            servico.setPontos(Integer.parseInt(tfPontos.getText()));
            servico.setEcategoria(cbCategoria.getValue());

            btConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }
    
    //método para validar a entrada de dados
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfDescricao.getText() == null || this.tfDescricao.getText().length() == 0) {
            errorMessage += "Descrição inválida.\n";
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

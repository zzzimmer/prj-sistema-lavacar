/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller.cadastro;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
//import br.edu.ifsc.fln.utils.ValidadorDocumentos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroClienteDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private Group gbTipo;

    @FXML
    private RadioButton rbPessoaFisica;

    @FXML
    private RadioButton rbPessoaJuridica;

    @FXML
    private TextField tfCelular;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfNumFiscal;

    @FXML
    private DatePicker dpDataNascimento;

    @FXML
    private Label lbDataNasc;

    @FXML
    private Label lbInscrEstad;

    @FXML
    private TextField tfInscricaoEstadual;

    @FXML
    private ToggleGroup tgTipo;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Cliente cliente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;

        this.tfNome.setText(this.cliente.getNome());
        this.tfEmail.setText(this.cliente.getEmail());
        this.tfCelular.setText(this.cliente.getCelular());
        this.gbTipo.setDisable(true);
        if (cliente instanceof PessoaFisica) {
            lbInscrEstad.setVisible(false);
            dpDataNascimento.setVisible(true);
            rbPessoaFisica.setSelected(true);
            tfNumFiscal.setText(((PessoaFisica) this.cliente).getCpf());
            dpDataNascimento.setValue(((PessoaFisica) this.cliente).getDataNascimento());
            tfInscricaoEstadual.setVisible(false);


        } else {
            lbDataNasc.setVisible(false);
            tfInscricaoEstadual.setVisible(true);
            rbPessoaJuridica.setSelected(true);
            tfNumFiscal.setText(((PessoaJuridica) this.cliente).getCnpj());
            tfInscricaoEstadual.setText(((PessoaJuridica) this.cliente).getInscricaoEstadual());
            dpDataNascimento.setVisible(false);

        }
        this.tfNome.requestFocus();
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            cliente.setNome(tfNome.getText());

            cliente.setEmail(tfEmail.getText());
            cliente.setCelular(tfCelular.getText());
            if (rbPessoaFisica.isSelected()) {
                ((PessoaFisica) cliente).setCpf(tfNumFiscal.getText());
                ((PessoaFisica) cliente).setDataNascimento(dpDataNascimento.getValue());
            } else {
                ((PessoaJuridica) cliente).setCnpj(tfNumFiscal.getText());
                ((PessoaJuridica) cliente).setInscricaoEstadual(tfInscricaoEstadual.getText());
            }

            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }

    @FXML
    public void handleRbPessoaFisica() {

    }

    @FXML
    public void handleRbPessoaJuridica() {
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfNome.getText() == null || this.tfNome.getText().length() == 0) {
            errorMessage += "Nome inválido.\n";
        }

        if (this.tfCelular.getText() == null || this.tfCelular.getText().length() == 0) {
            errorMessage += "Telefone inválido.\n";
        }

        if (this.tfEmail.getText() == null || this.tfEmail.getText().length() == 0 || !this.tfEmail.getText().contains("@")) {
            errorMessage += "Email inválido.\n";
        }

        if(this.dpDataNascimento.isVisible() && this.dpDataNascimento.getValue() == null){
            errorMessage += "Insira Data de Nascimento.\n";
        }

//        if (rbPessoaFisica.isSelected()) {
//            if (!ValidadorDocumentos.validarCPF(this.tfNumFiscal.getText())) {
//                errorMessage += "CPF inválido.\n";
//            }
//        } else {
//            if (!ValidadorDocumentos.validarCNPJ(this.tfNumFiscal.getText())) {
//                errorMessage += "CNPJ inválido.\n";
//            }
//        }
//            }

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

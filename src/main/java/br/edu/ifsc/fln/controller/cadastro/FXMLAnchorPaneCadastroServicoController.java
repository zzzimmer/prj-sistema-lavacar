/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller.cadastro;

import br.edu.ifsc.fln.controller.processo.FXMLAnchorPaneProcessoOrdemServicoController;
import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Servico;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroServicoController implements Initializable {

    
    @FXML
    private Button btnAlterar;

    @FXML
    private Button btExcluir;
    
    @FXML
    private Button btInserir;

    @FXML
    private Label lbServicoDescricao;

    @FXML
    private Label lbServicoId;

    @FXML
    private Label lbServicoPontos;

    @FXML
    private Label lbServicoValor;

    @FXML
    private Label lbServicoCategoria;

    @FXML
    private TableColumn<Servico, String> tableColumnServicoDescricao;

    @FXML
    private TableView<Servico> tableViewServico;
    
    private List<Servico> listaServico;
    private ObservableList<Servico> observableListServico;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicoDAO.setConnection(connection);
        carregarTableViewServico();
        
        tableViewServico.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewServico(newValue));
    }     
    
    public void carregarTableViewServico() {
        tableColumnServicoDescricao.setCellValueFactory(new PropertyValueFactory<>("Descricao"));
        
        listaServico = servicoDAO.listar();
        
        observableListServico = FXCollections.observableArrayList(listaServico);
        tableViewServico.setItems(observableListServico);
    }
    
    public void selecionarItemTableViewServico(Servico servico) {
        if (servico != null) {
            lbServicoId.setText(String.valueOf(servico.getId()));
            lbServicoDescricao.setText(servico.getDescricao());
            lbServicoValor.setText(String.valueOf(servico.getValor()));
            lbServicoPontos.setText(String.valueOf(servico.getPontos()));
            lbServicoCategoria.setText(String.valueOf(servico.getEcategoria()));

        } else {
            lbServicoId.setText("");
            lbServicoDescricao.setText("");
            lbServicoValor.setText("");
            lbServicoPontos.setText("");
            lbServicoCategoria.setText("");
        }

    }

    @FXML
    public void handleBtInserir() throws DAOException, IOException {
        Servico servico = new Servico();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
        if (btConfirmarClicked) {
            try{
            servicoDAO.inserir(servico);

            } catch (DAOException ex){
                Logger.getLogger(FXMLAnchorPaneCadastroServicoController.class.getName()).log(Level.SEVERE, null, ex);
                AlertDialog.exceptionMessage(ex);
            }
            carregarTableViewServico();

        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Servico servico = tableViewServico.getSelectionModel().getSelectedItem();
        if (servico != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
            if (btConfirmarClicked) {
                servicoDAO.alterar(servico);
                carregarTableViewServico();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Servico na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Servico servico = tableViewServico.getSelectionModel().getSelectedItem();
        if (servico != null) {
            servicoDAO.remover(servico);
            carregarTableViewServico();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Servico na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroServicoDialog(Servico servico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroServicoController.class.getResource("/view/cadastro/FXMLAnchorPaneCadastroServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Servico");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o obejto servico para o controller
        FXMLAnchorPaneCadastroServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setServico(servico);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
    
}

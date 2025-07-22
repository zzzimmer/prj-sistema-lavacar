/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller.processo;

import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneProcessoOrdemServicoDialogController implements Initializable {

    @FXML
    private Button buttonAdicionar;

    @FXML
    private Button buttonCancelar;

    @FXML
    private Button buttonConfirmar;

    @FXML
    private Button buttonLimpar;

    @FXML
    private ChoiceBox<EStatus> choiceBoxSituacao;

    @FXML
    private ComboBox<Servico> comboBoxServico;

    @FXML
    private ComboBox<Veiculo> comboBoxVeiculoPlaca;

    @FXML
    private TextField textFieldCliente;

    @FXML
    private MenuItem contextMenuItemAtualizarQtd;

    @FXML
    private MenuItem contextMenuItemRemoverItem;

    @FXML
    private ContextMenu contextMenuTableView;

    @FXML
    private DatePicker datePickerData;

    @FXML
    private TableColumn<ItemOS, String> tableColumnObs;

    @FXML
    private TableColumn<ItemOS, String> tableColumnServico;

    @FXML
    private TableColumn<ItemOS, Double> tableColumnValor;

    @FXML
    private TableView<ItemOS> tableViewItensOrdemServico;

    @FXML
    private TextField textFieldDesconto;

    @FXML
    private TextField textFieldObsServico;

    @FXML
    private TextField textFieldValor;

    private List<Veiculo> listaVeiculos;
    private List<Servico> listaServicos;
    private ObservableList<Veiculo> observableListVeiculos;
    private ObservableList<Servico> observableListServicos;
    private ObservableList<ItemOS> observableListItemOs;

    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private OrdemServico ordemServico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);
        servicoDAO.setConnection(connection);
        carregarComboBoxVeiculos();
        carregarComboBoxServicos();
        carregarChoiceBoxSituacao();
        setFocusLostHandle();
        configurarComboBoxVeiculo();
        configurarCampoCliente();
        configurarTableView();
        datePickerData.setValue(LocalDate.now());

        // Inicializar a ObservableList da tabela
        observableListItemOs = FXCollections.observableArrayList();
        tableViewItensOrdemServico.setItems(observableListItemOs);
    }

    private void carregarComboBoxVeiculos() {
        listaVeiculos = veiculoDAO.listar();
        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        comboBoxVeiculoPlaca.setItems(observableListVeiculos);
    }

    private void carregarComboBoxServicos() {
        listaServicos = servicoDAO.listar();
        observableListServicos = FXCollections.observableArrayList(listaServicos);
        comboBoxServico.setItems(observableListServicos);
    }

    public void carregarChoiceBoxSituacao() {
        choiceBoxSituacao.setItems(FXCollections.observableArrayList(EStatus.values()));
        choiceBoxSituacao.getSelectionModel().select(0);
    }

    private void setFocusLostHandle() {
        textFieldDesconto.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) {
                if (textFieldDesconto.getText() != null && !textFieldDesconto.getText().isEmpty()) {
                    try {
                        if (ordemServico != null) {
                            ordemServico.setDesconto(Double.parseDouble(textFieldDesconto.getText()));
                            textFieldValor.setText(String.format("%.2f", ordemServico.getTotal()));
                        }
                    } catch (NumberFormatException e) {
                        textFieldDesconto.setText("0.00");
                    }
                }
            }
        });
    } // atualiza o valor total da OS

    private void configurarCampoCliente() {
        textFieldCliente.setEditable(false);
        textFieldCliente.setStyle(" -fx-alignment: center;");
    }

    private void atualizarCampoCliente(Veiculo veiculo) {
        if (veiculo != null && veiculo.getCliente() != null) {
            textFieldCliente.setText(veiculo.getCliente().getNome());
        } else {
            textFieldCliente.setText("");
        }
    }

    public void configurarComboBoxVeiculo() {
        comboBoxVeiculoPlaca.getSelectionModel().selectedItemProperty().addListener((
                observable, oldValue, newValue) -> {
            if (newValue != null) {
                atualizarCampoCliente(newValue);
            }
        });
    }

    private void configurarTableView() {
        // Configurar coluna de serviço para mostrar a descrição
        tableColumnServico.setCellValueFactory(cellData -> {
            ItemOS item = cellData.getValue();
            if (item != null && item.getServico() != null) {
                return new SimpleStringProperty(item.getServico().getDescricao());
            }
            return new SimpleStringProperty("");
        });

        tableColumnObs.setCellValueFactory(cellData -> {
            ItemOS item = cellData.getValue();
            String obs = item.getObservacoes();
            return new SimpleStringProperty(obs != null ? obs : "");
        });

        tableColumnValor.setCellValueFactory(cellData -> {
            ItemOS item = cellData.getValue();
            double valor = 0.0;

            if (item.getServico() != null ) {
                valor = item.getServico().getValor();
            }

            return new SimpleObjectProperty<>(valor);
        });

        tableColumnValor.setCellFactory(column -> new TableCell<ItemOS, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(String.format("R$ %.2f", item));
                }
            }
        });
    }

    /**
     * @return the dialogStage
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;

        if (ordemServico != null) {

            if (ordemServico.getListServicos() == null) {
                ordemServico.setListServicos(new ArrayList<>());
            }

            if (ordemServico.getNumero() != 0) {
                comboBoxVeiculoPlaca.getSelectionModel().select(ordemServico.getVeiculo());
                datePickerData.setValue(ordemServico.getAgenda());

                choiceBoxSituacao.getSelectionModel().select(ordemServico.getStatus());

                observableListItemOs.clear();
//                observableListItemOs.addAll(ordemServico.getListServicos());
                observableListItemOs = FXCollections.observableArrayList(
                        this.ordemServico.getListServicos()
                );

                tableViewItensOrdemServico.setItems(observableListItemOs);

                textFieldValor.setText(String.format("%.2f", ordemServico.getTotal()));
                textFieldDesconto.setText(String.format("%.2f", ordemServico.getDesconto()));

                if (ordemServico.getVeiculo() != null &&
                        ordemServico.getVeiculo().getCliente() != null) {
                    textFieldCliente.setText(ordemServico.getVeiculo().getCliente().getNome());
                }
            } else {
                // Nova ordem de serviço - limpar campos
//                limparTodosCampos();
            }
        }
    }

    @FXML
    public void handleButtonAdicionar() {
        LocalDate data = datePickerData.getValue();
        if (data == null) {
            mostrarAlerta("Atenção", "Selecione uma data!");
            return;
        }
        ordemServico.setAgenda(data);

        atualizarValorTotal();
        if (comboBoxServico.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta("Atenção", "Selecione um serviço!");
            return;
        }

        if (ordemServico == null) {
            mostrarAlerta("Erro", "Ordem de serviço não foi inicializada!");
            return;
        }

        try {
            // Obter o serviço selecionado
            Servico servicoSelecionado = comboBoxServico.getSelectionModel().getSelectedItem();

            Servico servico = servicoDAO.buscar(servicoSelecionado);

            // Criar novo item da ordem de serviço
            ItemOS itemOS = new ItemOS();
            itemOS.setServico(servico);
            itemOS.setObservacoes(textFieldObsServico.getText().trim());
            itemOS.setOrdemServico(ordemServico);

            // Adicionar a lista da ordem de serviço
            if (ordemServico.getListServicos() == null) {
                ordemServico.setListServicos(new ArrayList<>());
            }
            ordemServico.getListServicos().add(itemOS);

            // Atualizar a ObservableList da tabela
            observableListItemOs.add(itemOS);

            // Atualizar o valor total
            atualizarValorTotal();
            limparCamposEntrada();

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao adicionar serviço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLimparServicos() {
        // limpa lista do domínio
        ordemServico.getListServicos().clear();
        // limpa tabela
        observableListItemOs.clear();
        // zera desconto e valor
        ordemServico.setDesconto(0.0);
        textFieldDesconto.setText("0.00");
        textFieldValor.setText("0.00");
        // limpa seleção de inputs
        comboBoxServico.getSelectionModel().clearSelection();
        textFieldObsServico.clear();
    }

    @FXML
    public void handleButtonConfirmar() {
        ordemServico.setVeiculo(comboBoxVeiculoPlaca.getSelectionModel().getSelectedItem());
        ordemServico.setAgenda(datePickerData.getValue());

        ordemServico.setListServicos(new ArrayList<>(observableListItemOs));

        ordemServico.setStatus(choiceBoxSituacao.getValue());


        buttonConfirmarClicked = true;
        dialogStage.close();
    }

    @FXML
    public void handleButtonCancelar() {
        dialogStage.close();
    }

    @FXML
    void handleTableViewMouseClicked(MouseEvent event) {
        ItemOS itemSelecionado = tableViewItensOrdemServico.getSelectionModel().getSelectedItem();

        if (itemSelecionado != null) {
            // Preencher campos com dados do item selecionado para possível edição
            comboBoxServico.getSelectionModel().select(itemSelecionado.getServico());
            textFieldObsServico.setText(itemSelecionado.getObservacoes() != null ?
                    itemSelecionado.getObservacoes() : "");

        }
    }

    // Métodos auxiliares
    private void atualizarValorTotal() {
        if (ordemServico != null) {
            double total = ordemServico.getTotal();
            textFieldValor.setText(String.format("%.2f", total));
        }
    }

    private void limparCamposEntrada() {
        comboBoxServico.getSelectionModel().clearSelection();
        textFieldObsServico.setText("");
        comboBoxServico.requestFocus();
    }

    private void limparTodosCampos() {
        comboBoxVeiculoPlaca.getSelectionModel().clearSelection();
        comboBoxServico.getSelectionModel().clearSelection();
        datePickerData.setValue(null);
        textFieldCliente.setText("");
        textFieldObsServico.setText("");
        textFieldDesconto.setText("0.00");
        textFieldValor.setText("0.00");

        if (observableListItemOs != null) {
            observableListItemOs.clear();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
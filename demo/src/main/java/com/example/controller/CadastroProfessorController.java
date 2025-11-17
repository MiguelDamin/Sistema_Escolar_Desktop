package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.example.model.Professor;
import com.example.repository.AtividadeRecenteDAO;
import com.example.repository.ProfessorDAO;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CadastroProfessorController {
    
    // ==================== CAMPOS DO FORMULÁRIO ====================
    @FXML private TextField txtNomeProfessor;
    @FXML private TextField txtIdadeProfessor;
    @FXML private TextField txtCpfProfessor;
    @FXML private TextField txtEmailProfessor;
    @FXML private TextField txtTelefoneProfessor;
    @FXML private Label LabelProfessor;
    
    // ==================== TABELA ====================
    @FXML private TableView<Professor> tblProfessores;
    @FXML private TableColumn<Professor, Integer> colId;
    @FXML private TableColumn<Professor, String> colNome;
    @FXML private TableColumn<Professor, String> colCpf;
    @FXML private TableColumn<Professor, String> colEmail;
    @FXML private TableColumn<Professor, Void> colAcoes;
    
    // ==================== DAOs ====================
    private ProfessorDAO professorDAO = new ProfessorDAO();
    private AtividadeRecenteDAO atividadeDAO = new AtividadeRecenteDAO();
    
    private ObservableList<Professor> professoresLista = FXCollections.observableArrayList();
    
    // ==================== CONTROLE DE EDIÇÃO ====================
    private Professor professorEmEdicao = null;

    @FXML
    public void initialize() {
        System.out.println("🎬 CadastroProfessorController inicializado!");
        
        configurarTabela();
        carregarProfessores();
        
        // Limitadores de caracteres
        txtCpfProfessor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > 11) {
                txtCpfProfessor.setText(oldValue);
            }
        });

        txtTelefoneProfessor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > 11) {
                txtTelefoneProfessor.setText(oldValue);
            }
        });

        if (LabelProfessor != null) {
            LabelProfessor.setVisible(false);
        }
    }
    
    /**
     * Configura as colunas da tabela e o botão de ações
     */
    private void configurarTabela() {
        colId.setCellValueFactory(cell -> 
            new SimpleIntegerProperty(cell.getValue().getIdProfessor()).asObject()
        );
        
        colNome.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getNome())
        );
        
        colCpf.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getCpf())
        );
        
        colEmail.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getEmail())
        );
        
        // Coluna de ações com botões Editar e Deletar
        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnDeletar = new Button("Deletar");
            private final HBox hbox = new HBox(8, btnEditar, btnDeletar);
            
            {
                btnEditar.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 12px;");
                btnDeletar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 12px;");
                
                btnEditar.setOnAction(e -> {
                    Professor professor = getTableView().getItems().get(getIndex());
                    carregarDadosParaEdicao(professor);
                });
                
                btnDeletar.setOnAction(e -> {
                    Professor professor = getTableView().getItems().get(getIndex());
                    deletarProfessor(professor);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
        
        tblProfessores.setItems(professoresLista);
    }
    
    /**
     * Carrega os dados de um professor para edição
     */
    private void carregarDadosParaEdicao(Professor professor) {
        try {
            professorEmEdicao = professorDAO.buscarPorId(professor.getIdProfessor());
            
            if (professorEmEdicao == null) {
                showMessage("❌ Professor não encontrado!");
                return;
            }
            
            // Preenche os campos
            txtNomeProfessor.setText(professorEmEdicao.getNome());
            txtIdadeProfessor.setText(String.valueOf(professorEmEdicao.getIdade()));
            txtCpfProfessor.setText(professorEmEdicao.getCpf());
            txtEmailProfessor.setText(professorEmEdicao.getEmail());
            txtTelefoneProfessor.setText(professorEmEdicao.getTelefone());
            
            showMessage("✏️ Modo Edição: Altere os dados e clique em Salvar");
            
            // Scroll para o topo do formulário
            txtNomeProfessor.requestFocus();
            
        } catch (SQLException e) {
            showMessage("❌ Erro ao carregar professor: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Deleta um professor com confirmação
     */
    private void deletarProfessor(Professor professor) {
        try {
            // Verifica se tem horários vinculados
            boolean temHorarios = professorDAO.temHorariosVinculados(professor.getIdProfessor());
            
            if (temHorarios) {
                // Mostra opção de deletar com horários
                Alert aviso = new Alert(Alert.AlertType.WARNING);
                aviso.setTitle("Professor com Horários");
                aviso.setHeaderText("Este professor possui horários cadastrados!");
                aviso.setContentText(
                    "Professor: " + professor.getNome() + 
                    "\n\nO que deseja fazer?" +
                    "\n\n• Clique em 'OK' para deletar o professor E todos os horários" +
                    "\n• Clique em 'Cancelar' para manter o professor"
                );
                
                if (aviso.showAndWait().get() == ButtonType.OK) {
                    // Deleta os horários primeiro
                    professorDAO.deletarHorariosDoProfessor(professor.getIdProfessor());
                    System.out.println("✅ Horários deletados");
                    
                    // Agora deleta o professor
                    if (professorDAO.deletar(professor.getIdProfessor())) {
                        atividadeDAO.registrar("PROFESSOR", "Professor e horários deletados: " + professor.getNome());
                        showMessage("✅ Professor e horários deletados com sucesso!");
                        carregarProfessores();
                        
                        if (professorEmEdicao != null && 
                            professorEmEdicao.getIdProfessor() == professor.getIdProfessor()) {
                            onLimpar();
                        }
                    }
                }
            } else {
                // Não tem horários, deleta normalmente
                Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacao.setTitle("Confirmar Exclusão");
                confirmacao.setHeaderText("Deseja realmente deletar este professor?");
                confirmacao.setContentText(
                    "Professor: " + professor.getNome() + 
                    "\nCPF: " + professor.getCpf() +
                    "\n\nEsta ação não pode ser desfeita!"
                );
                
                if (confirmacao.showAndWait().get() == ButtonType.OK) {
                    if (professorDAO.deletar(professor.getIdProfessor())) {
                        atividadeDAO.registrar("PROFESSOR", "Professor deletado: " + professor.getNome());
                        showMessage("✅ Professor deletado com sucesso!");
                        carregarProfessores();
                        
                        if (professorEmEdicao != null && 
                            professorEmEdicao.getIdProfessor() == professor.getIdProfessor()) {
                            onLimpar();
                        }
                    }
                }
            }
            
        } catch (SQLException e) {
            String mensagem = e.getMessage();
            
            if (mensagem.contains("foreign key constraint")) {
                showMessage("❌ Erro de vínculo: Este professor ainda possui dependências no sistema.");
            } else {
                showMessage("❌ Erro ao deletar: " + mensagem);
            }
            
            e.printStackTrace();
        }
    }
    
    /**
     * Carrega a lista de professores na tabela
     */
    private void carregarProfessores() {
        Task<ObservableList<Professor>> task = new Task<>() {
            @Override
            protected ObservableList<Professor> call() throws Exception {
                ObservableList<Professor> lista = FXCollections.observableArrayList();
                lista.addAll(professorDAO.listarTodos());
                return lista;
            }
        };
        
        task.setOnSucceeded(event -> {
            professoresLista.clear();
            professoresLista.addAll(task.getValue());
            System.out.println("✅ " + professoresLista.size() + " professores carregados na tabela");
        });
        
        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            showMessage("❌ Erro ao carregar professores: " + ex.getMessage());
            ex.printStackTrace();
        });
        
        new Thread(task).start();
    }

    @FXML
    private void onSalvarProfessor(ActionEvent event) {
        // Validações
        String nome = txtNomeProfessor.getText() != null ? txtNomeProfessor.getText().trim() : "";
        String idadeTxt = txtIdadeProfessor.getText() != null ? txtIdadeProfessor.getText().trim() : "";
        String cpf = txtCpfProfessor.getText() != null ? txtCpfProfessor.getText().trim() : "";
        String email = txtEmailProfessor.getText() != null ? txtEmailProfessor.getText().trim() : "";
        String telefone = txtTelefoneProfessor.getText() != null ? txtTelefoneProfessor.getText().trim() : "";

        if (nome.isBlank() || idadeTxt.isBlank() || cpf.isBlank() || email.isBlank() || telefone.isBlank()) {
            showMessage("❌ Erro: Todos os campos são obrigatórios");
            return;
        }

        final int idade;
        try {
            idade = Integer.parseInt(idadeTxt);
            if (idade <= 0 || idade > 120) {
                showMessage("❌ Erro: Idade inválida (deve estar entre 1 e 120)");
                return;
            }
        } catch (NumberFormatException e) {
            showMessage("❌ Erro: Idade deve ser um número inteiro");
            return;
        }
        
        // Validação de CPF (11 dígitos)
        if (cpf.length() != 11) {
            showMessage("❌ Erro: CPF deve ter 11 dígitos");
            return;
        }

        // ==================== MODO EDIÇÃO ====================
        if (professorEmEdicao != null) {
            Task<Boolean> atualizarTask = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    // Só verifica CPF se foi alterado
                    if (!cpf.equals(professorEmEdicao.getCpf())) {
                        if (professorDAO.existePorCpf(cpf)) {
                            throw new SQLException("Já existe outro professor com este CPF!");
                        }
                    }
                    
                    professorEmEdicao.setNome(nome);
                    professorEmEdicao.setIdade(idade);
                    professorEmEdicao.setCpf(cpf);
                    professorEmEdicao.setEmail(email);
                    professorEmEdicao.setTelefone(telefone);
                    
                    return professorDAO.atualizar(professorEmEdicao);
                }
            };
            
            atualizarTask.setOnSucceeded(event1 -> {
                boolean sucesso = atualizarTask.getValue();
                if (sucesso) {
                    atividadeDAO.registrar("PROFESSOR", "Professor atualizado: " + nome);
                    showMessage("✅ Professor atualizado com sucesso!");
                    carregarProfessores();
                    onLimpar();
                } else {
                    showMessage("❌ Erro ao atualizar professor!");
                }
            });
            
            atualizarTask.setOnFailed(event1 -> {
                Throwable ex = atualizarTask.getException();
                showMessage("❌ Erro: " + (ex != null ? ex.getMessage() : "Desconhecido"));
                ex.printStackTrace();
            });
            
            new Thread(atualizarTask).start();
            
        } else {
            // ==================== MODO CADASTRO ====================
            Task<Boolean> salvarTask = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    // Verifica se o CPF já existe
                    if (professorDAO.existePorCpf(cpf)) {
                        throw new SQLException("Já existe um professor com este CPF!");
                    }
                    
                    Professor professor = new Professor(nome, idade, cpf, email, telefone);
                    return professorDAO.salvar(professor);
                }
            };

            salvarTask.setOnSucceeded(workerStateEvent -> {
                boolean sucesso = salvarTask.getValue();
                if (sucesso) {
                    atividadeDAO.registrar("PROFESSOR", "Professor cadastrado: " + nome);
                    showMessage("✅ Professor salvo com sucesso!");
                    carregarProfessores();
                    clearFields();
                } else {
                    showMessage("❌ Erro ao salvar o professor. Veja o console para detalhes.");
                }
            });

            salvarTask.setOnFailed(workerStateEvent -> {
                Throwable ex = salvarTask.getException();
                showMessage("❌ Erro: " + (ex != null ? ex.getMessage() : "Desconhecido"));
                ex.printStackTrace();
            });

            new Thread(salvarTask).start();
        }
    }

    @FXML
    private void onVoltar(ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/Telainicial.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onLimpar() {
        clearFields();
        professorEmEdicao = null;
        showMessage("🔄 Formulário limpo - Modo Cadastro");
    }

    private void clearFields() {
        Platform.runLater(() -> {
            txtNomeProfessor.clear();
            txtIdadeProfessor.clear();
            txtCpfProfessor.clear();
            txtEmailProfessor.clear();
            txtTelefoneProfessor.clear();
        });
    }

    private void showMessage(String msg) {
        Platform.runLater(() -> {
            if (LabelProfessor != null) {
                LabelProfessor.setText(msg);
                LabelProfessor.setVisible(true);
                
                // Define cor baseada no tipo de mensagem
                if (msg.startsWith("✅")) {
                    LabelProfessor.setStyle("-fx-text-fill: #4ade80; -fx-font-size: 14px;");
                } else if (msg.startsWith("❌")) {
                    LabelProfessor.setStyle("-fx-text-fill: #f44336; -fx-font-size: 14px;");
                } else if (msg.startsWith("✏️") || msg.startsWith("🔄")) {
                    LabelProfessor.setStyle("-fx-text-fill: #2196f3; -fx-font-size: 14px;");
                } else {
                    LabelProfessor.setStyle("-fx-text-fill: #d4d4d8; -fx-font-size: 14px;");
                }
            } else {
                System.out.println(msg);
            }
        });
    }
}
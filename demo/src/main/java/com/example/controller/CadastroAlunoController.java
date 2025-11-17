package com.example.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.example.model.Estudante;
import com.example.model.Responsavel;
import com.example.model.Turma;
import com.example.repository.AtividadeRecenteDAO;
import com.example.repository.EstudanteDAO;
import com.example.repository.MatriculaDAO;
import com.example.repository.ResponsavelDAO;
import com.example.repository.TurmaDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CadastroAlunoController {
    
    // ========== CAMPOS DO FORMULÁRIO ==========
    @FXML private TextField txtNome;
    @FXML private TextField txtIdade;
    @FXML private TextField txtCpf;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private DatePicker dtDataNascimento;
    @FXML private ComboBox<Turma> cbTurma;
    
    // ========== CAMPOS DO RESPONSÁVEL ==========
    @FXML private TextField txtNomeResponsavel;
    @FXML private TextField txtCpfResponsavel;
    @FXML private TextField txtTelefoneResponsavel;
    @FXML private TextField txtEmailResponsavel;
    @FXML private TextField txtEnderecoResponsavel;
    @FXML private ComboBox<String> cbParentesco;
    
    // ========== TABELA DE ESTUDANTES ==========
    @FXML private TableView<Estudante> tblEstudantes;
    @FXML private TableColumn<Estudante, Integer> colId;
    @FXML private TableColumn<Estudante, String> colNome;
    @FXML private TableColumn<Estudante, String> colCpf;
    @FXML private TableColumn<Estudante, String> colTurma;
    @FXML private TableColumn<Estudante, Void> colAcoes;
    
    // ========== DAOs ==========
    private ResponsavelDAO responsavelDAO = new ResponsavelDAO();
    private EstudanteDAO estudanteDAO = new EstudanteDAO();
    private MatriculaDAO matriculaDAO = new MatriculaDAO();
    private TurmaDAO turmaDAO = new TurmaDAO();
    private AtividadeRecenteDAO atividadeDAO = new AtividadeRecenteDAO();
    
    private ObservableList<Turma> turmasCadastradas = FXCollections.observableArrayList();
    private ObservableList<Estudante> estudantesCadastrados = FXCollections.observableArrayList();
    
    // ========== CONTROLE DE MODO EDIÇÃO ==========
    private boolean modoEdicao = false;
    private Estudante estudanteEmEdicao = null;
    
    @FXML
    public void initialize() {
        System.out.println("🎓 CadastroAlunoController inicializado!");
        
        // Configura ComboBox de Turmas
        if (cbTurma != null) {
            carregarTurmas();
        }
        
        // Configura ComboBox de Parentesco
        if (cbParentesco != null) {
            cbParentesco.setItems(FXCollections.observableArrayList(
                "Pai", "Mãe", "Avô", "Avó", "Tio", "Tia", "Tutor Legal", "Outro"
            ));
        }
        
        // Limita CPF a 11 caracteres
        if (txtCpf != null) {
            txtCpf.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue.length() > 11) txtCpf.setText(oldValue);
            });
        }
        
        if (txtTelefone != null) {
            txtTelefone.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue.length() > 11) txtTelefone.setText(oldValue);
            });
        }
        
        if (txtCpfResponsavel != null) {
            txtCpfResponsavel.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue.length() > 11) txtCpfResponsavel.setText(oldValue);
            });
        }
        
        if (txtTelefoneResponsavel != null) {
            txtTelefoneResponsavel.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue.length() > 11) txtTelefoneResponsavel.setText(oldValue);
            });
        }
        
        // Configura TableView e carrega dados
        configurarTabela();
        carregarEstudantes();
    }
    
    // ========================================
    // CONFIGURAÇÃO DA TABLEVIEW
    // ========================================
    
    /**
     * Configura as colunas da TableView
     */
    private void configurarTabela() {
        if (tblEstudantes == null) {
            System.out.println("⚠️ TableView não encontrada no FXML");
            return;
        }
        
        // Coluna ID
        colId.setCellValueFactory(new PropertyValueFactory<>("idEstudante"));
        
        // Coluna Nome
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        // Coluna CPF
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        
        // Coluna Turma (busca da matrícula)
        colTurma.setCellValueFactory(cellData -> {
            try {
                int idEstudante = cellData.getValue().getIdEstudante();
                int idTurma = matriculaDAO.buscarTurmaAtual(idEstudante);
                
                if (idTurma == -1) {
                    return new javafx.beans.property.SimpleStringProperty("Sem turma");
                }
                
                Turma turma = turmaDAO.buscarPorId(idTurma);
                return new javafx.beans.property.SimpleStringProperty(
                    turma != null ? turma.getNome() : "Sem turma"
                );
            } catch (Exception e) {
                e.printStackTrace();
                return new javafx.beans.property.SimpleStringProperty("Erro");
            }
        });
        
        // Coluna Ações (botão Editar)
        colAcoes.setCellFactory(col -> new TableCell<Estudante, Void>() {
            private final Button btnEditar = new Button("✏️ Editar");
            
            {
                btnEditar.setOnAction(event -> {
                    Estudante estudante = getTableView().getItems().get(getIndex());
                    carregarDadosParaEdicao(estudante);
                });
                btnEditar.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; " +
                                  "-fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 5 10;");
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnEditar);
            }
        });
        
        tblEstudantes.setItems(estudantesCadastrados);
        System.out.println("✅ TableView configurada!");
    }
    
    /**
     * Carrega a lista de estudantes do banco
     */
    private void carregarEstudantes() {
        try {
            estudantesCadastrados.clear();
            List<Estudante> lista = estudanteDAO.listarTodos();
            estudantesCadastrados.addAll(lista);
            System.out.println("✅ " + lista.size() + " estudantes carregados!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", 
                     "Erro ao carregar estudantes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Carrega as turmas disponíveis no ComboBox
     */
    private void carregarTurmas() {
        try {
            turmasCadastradas.clear();
            List<Turma> turmas = turmaDAO.listarTodos();
            turmasCadastradas.addAll(turmas);
            cbTurma.setItems(turmasCadastradas);
            System.out.println("✅ " + turmas.size() + " turmas carregadas!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Banco de Dados", 
                     "Não foi possível carregar as turmas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ========================================
    // MODO EDIÇÃO
    // ========================================
    
    /**
     * Carrega os dados do estudante selecionado para edição
     */
    private void carregarDadosParaEdicao(Estudante estudante) {
        try {
            System.out.println("════════════════════════════════");
            System.out.println("📝 CARREGANDO DADOS PARA EDIÇÃO");
            System.out.println("Aluno: " + estudante.getNome());
            System.out.println("ID: " + estudante.getIdEstudante());
            System.out.println("════════════════════════════════");
            
            // Buscar dados completos
            estudanteEmEdicao = estudanteDAO.buscarPorId(estudante.getIdEstudante());
            
            if (estudanteEmEdicao == null) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Estudante não encontrado no banco!");
                return;
            }
            
            // Ativar modo edição
            modoEdicao = true;
            
            // Preencher campos do ESTUDANTE
            txtNome.setText(estudanteEmEdicao.getNome());
            txtIdade.setText(String.valueOf(estudanteEmEdicao.getIdade()));
            txtCpf.setText(estudanteEmEdicao.getCpf());
            txtEmail.setText(estudanteEmEdicao.getEmail());
            txtTelefone.setText(estudanteEmEdicao.getTelefone());
            dtDataNascimento.setValue(LocalDate.parse(estudanteEmEdicao.getData_nascimento()));
            
            // Selecionar turma atual
            int idTurmaAtual = matriculaDAO.buscarTurmaAtual(estudanteEmEdicao.getIdEstudante());
            for (Turma t : cbTurma.getItems()) {
                if (t.getId_turma() == idTurmaAtual) {
                    cbTurma.setValue(t);
                    break;
                }
            }
            
            // Preencher campos do RESPONSÁVEL
            Responsavel resp = estudanteEmEdicao.getResponsavel();
            if (resp != null) {
                txtNomeResponsavel.setText(resp.getNome());
                txtCpfResponsavel.setText(resp.getCpf());
                txtTelefoneResponsavel.setText(resp.getTelefone());
                txtEmailResponsavel.setText(resp.getEmail());
                txtEnderecoResponsavel.setText(resp.getEndereco());
                cbParentesco.setValue(resp.getParentesco());
            }
            
            System.out.println("✅ Dados carregados com sucesso!");
            showAlert(Alert.AlertType.INFORMATION, "Modo Edição Ativado", 
                     "Dados do aluno carregados!\n\n" +
                     "Modifique os campos desejados e clique em SALVAR para atualizar.");
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", 
                     "Erro ao carregar dados do aluno: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ========================================
    // SALVAR (CADASTRO OU EDIÇÃO)
    // ========================================
    
    /**
     * Salva ou atualiza o aluno dependendo do modo
     */
    @FXML
    private void onSalvar() {
        try {
            // Validações básicas
            if (!validarCampos()) {
                return;
            }
            
            if (modoEdicao) {
                // MODO EDIÇÃO - Atualizar registro existente
                atualizarEstudante();
            } else {
                // MODO CADASTRO - Inserir novo registro
                cadastrarNovoEstudante();
            }
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * CADASTRAR novo estudante
     */
    private void cadastrarNovoEstudante() throws SQLException {
        System.out.println("➕ CADASTRANDO NOVO ALUNO...");
        
        // ========== CRIAR RESPONSÁVEL ==========
        Responsavel responsavel = new Responsavel(
            txtNomeResponsavel.getText().trim(),
            txtCpfResponsavel.getText().trim(),
            txtTelefoneResponsavel.getText().trim(),
            txtEmailResponsavel.getText().trim(),
            txtEnderecoResponsavel.getText().trim(),
            cbParentesco.getValue()
        );
        
        int idResponsavel = responsavelDAO.salvar(responsavel);
        System.out.println("✅ Responsável salvo com ID: " + idResponsavel);
        
        // ========== CRIAR ESTUDANTE ==========
        Estudante estudante = new Estudante(
            txtNome.getText().trim(),
            Integer.parseInt(txtIdade.getText().trim()),
            txtCpf.getText().trim(),
            txtEmail.getText().trim(),
            txtTelefone.getText().trim(),
            dtDataNascimento.getValue().toString(),
            idResponsavel
        );
        
        int idEstudante = estudanteDAO.salvar(estudante);
        System.out.println("✅ Estudante salvo com ID: " + idEstudante);
        
        // ========== REGISTRAR MATRÍCULA ==========
        Turma turmaSelecionada = cbTurma.getValue();
        int idTurma = turmaSelecionada.getId_turma();
        int idMatricula = matriculaDAO.salvar(idEstudante, idTurma);
        System.out.println("✅ Matrícula registrada com ID: " + idMatricula);
        
        // ========== REGISTRAR ATIVIDADE ==========
        String nomeAluno = txtNome.getText().trim();
        String nomeTurma = turmaSelecionada.getNome();
        atividadeDAO.registrar("ALUNO", 
            "Novo aluno matriculado: " + nomeAluno + " - Turma " + nomeTurma);
        
        // ========== SUCESSO ==========
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", 
                 "Aluno, Responsável e Matrícula cadastrados com sucesso!");
        onLimpar();
        carregarEstudantes();
    }
    
    /**
     * ATUALIZAR estudante existente
     */
    private void atualizarEstudante() throws SQLException {
        System.out.println("════════════════════════════════");
        System.out.println("🔄 ATUALIZANDO ALUNO EXISTENTE");
        System.out.println("════════════════════════════════");
        
        // ========== ATUALIZAR RESPONSÁVEL ==========
        Responsavel responsavel = estudanteEmEdicao.getResponsavel();
        responsavel.setNome(txtNomeResponsavel.getText().trim());
        responsavel.setCpf(txtCpfResponsavel.getText().trim());
        responsavel.setTelefone(txtTelefoneResponsavel.getText().trim());
        responsavel.setEmail(txtEmailResponsavel.getText().trim());
        responsavel.setEndereco(txtEnderecoResponsavel.getText().trim());
        responsavel.setParentesco(cbParentesco.getValue());
        
        boolean respAtualizado = responsavelDAO.atualizar(responsavel);
        System.out.println("Responsável atualizado: " + respAtualizado);
        
        // ========== ATUALIZAR ESTUDANTE ==========
        estudanteEmEdicao.setNome(txtNome.getText().trim());
        estudanteEmEdicao.setIdade(Integer.parseInt(txtIdade.getText().trim()));
        estudanteEmEdicao.setCpf(txtCpf.getText().trim());
        estudanteEmEdicao.setEmail(txtEmail.getText().trim());
        estudanteEmEdicao.setTelefone(txtTelefone.getText().trim());
        estudanteEmEdicao.setData_nascimento(dtDataNascimento.getValue().toString());
        
        boolean estudanteAtualizado = estudanteDAO.atualizar(estudanteEmEdicao);
        System.out.println("Estudante atualizado: " + estudanteAtualizado);
        
        // ========== ATUALIZAR TURMA (se foi alterada) ==========
        Turma turmaSelecionada = cbTurma.getValue();
        int idTurmaAtual = matriculaDAO.buscarTurmaAtual(estudanteEmEdicao.getIdEstudante());
        
        if (turmaSelecionada.getId_turma() != idTurmaAtual) {
            boolean turmaAtualizada = matriculaDAO.atualizarTurma(
                estudanteEmEdicao.getIdEstudante(), 
                turmaSelecionada.getId_turma()
            );
            System.out.println("Turma atualizada: " + turmaAtualizada);
        } else {
            System.out.println("Turma não foi alterada");
        }
        
        // ========== REGISTRAR ATIVIDADE ==========
        atividadeDAO.registrar("ALUNO", 
            "Aluno atualizado: " + estudanteEmEdicao.getNome());
        
        System.out.println("════════════════════════════════");
        System.out.println("✅ ATUALIZAÇÃO CONCLUÍDA!");
        System.out.println("════════════════════════════════");
        
        // ========== SUCESSO ==========
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", 
                 "Dados do aluno atualizados com sucesso!");
        
        modoEdicao = false;
        estudanteEmEdicao = null;
        onLimpar();
        carregarEstudantes();
    }
    
    // ========================================
    // VALIDAÇÃO
    // ========================================
    
    /**
     * Valida todos os campos obrigatórios
     */
    private boolean validarCampos() {
        if (txtNome == null || txtNome.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Digite o nome do aluno!");
            return false;
        }
        
        if (cbTurma == null || cbTurma.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Selecione a turma do aluno!");
            return false;
        }
        
        if (txtCpf == null || txtCpf.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Digite o CPF do aluno!");
            return false;
        }
        
        if (txtIdade == null || txtIdade.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Digite a idade do aluno!");
            return false;
        }
        
        if (dtDataNascimento == null || dtDataNascimento.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Selecione a data de nascimento!");
            return false;
        }
        
        if (txtNomeResponsavel == null || txtNomeResponsavel.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Digite o nome do responsável!");
            return false;
        }
        
        if (txtCpfResponsavel == null || txtCpfResponsavel.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Digite o CPF do responsável!");
            return false;
        }
        
        if (cbParentesco == null || cbParentesco.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Selecione o parentesco!");
            return false;
        }
        
        return true;
    }
    
    // ========================================
    // BOTÕES AUXILIARES
    // ========================================
    
    /**
     * Limpa todos os campos do formulário
     */
    @FXML
    private void onLimpar() {
        if (txtNome != null) txtNome.clear();
        if (txtIdade != null) txtIdade.clear();
        if (txtCpf != null) txtCpf.clear();
        if (txtEmail != null) txtEmail.clear();
        if (txtTelefone != null) txtTelefone.clear();
        if (dtDataNascimento != null) dtDataNascimento.setValue(null);
        if (cbTurma != null) cbTurma.setValue(null);
        
        if (txtNomeResponsavel != null) txtNomeResponsavel.clear();
        if (txtCpfResponsavel != null) txtCpfResponsavel.clear();
        if (txtTelefoneResponsavel != null) txtTelefoneResponsavel.clear();
        if (txtEmailResponsavel != null) txtEmailResponsavel.clear();
        if (txtEnderecoResponsavel != null) txtEnderecoResponsavel.clear();
        if (cbParentesco != null) cbParentesco.setValue(null);
        
        // Desativar modo edição
        modoEdicao = false;
        estudanteEmEdicao = null;
        System.out.println("🔄 Modo edição desativado - Campos limpos");
    }
    
    /**
     * Botão para editar aluno selecionado na tabela
     */
    @FXML
    private void onEditarAluno() {
        Estudante selecionado = tblEstudantes.getSelectionModel().getSelectedItem();
        
        if (selecionado == null) {
            showAlert(Alert.AlertType.WARNING, "Atenção", 
                     "Selecione um aluno na tabela para editar!");
            return;
        }
        
        carregarDadosParaEdicao(selecionado);
    }
    
    /**
     * Botão para atualizar a lista de alunos
     */
    @FXML
    private void onAtualizarLista() {
        carregarEstudantes();
        showAlert(Alert.AlertType.INFORMATION, "Atualizado", 
                 "Lista de alunos atualizada com sucesso!");
    }
    
    /**
     * Exibe um alerta para o usuário
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
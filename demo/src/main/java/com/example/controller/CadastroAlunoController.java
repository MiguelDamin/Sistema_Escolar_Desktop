package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroAlunoController {
    
    // CAMPOS DO ESTUDANTE
    @FXML private TextField txtNome;
    @FXML private TextField txtIdade;
    @FXML private TextField txtCpf;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private DatePicker dtDataNascimento;
    @FXML private ComboBox<Turma> cbTurma;
    
    // CAMPOS DO RESPONSAVEL
    @FXML private TextField txtNomeResponsavel;
    @FXML private TextField txtCpfResponsavel;
    @FXML private TextField txtTelefoneResponsavel;
    @FXML private TextField txtEmailResponsavel;
    @FXML private TextField txtEnderecoResponsavel;
    @FXML private ComboBox<String> cbParentesco;
    
    @FXML private Label lblMensagem;
    @FXML private Label print;
    
    // DAOs - TODOS DECLARADOS AQUI NO TOPO
    private ResponsavelDAO responsavelDAO = new ResponsavelDAO();
    private EstudanteDAO estudanteDAO = new EstudanteDAO();
    private MatriculaDAO matriculaDAO = new MatriculaDAO();
    private TurmaDAO turmaDAO = new TurmaDAO();
    private AtividadeRecenteDAO atividadeDAO = new AtividadeRecenteDAO(); // ADICIONADO
    
    private ObservableList<Turma> turmasCadastradas = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        System.out.println("Controller carregado com sucesso!");
        
        carregarTurmas();
        
        cbParentesco.setItems(FXCollections.observableArrayList(
            "Pai", "Mae", "Avo", "Ava", "Tio", "Tia", "Tutor Legal", "Outro"
        ));
        
        // Limitar CPF do aluno a 11 caracteres
        txtCpf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 11) {
                txtCpf.setText(oldValue);
            }
        });
        
        // Limitar telefone do aluno a 11 caracteres
        txtTelefone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 11) {
                txtTelefone.setText(oldValue);
            }
        });
        
        // Limitar CPF do responsavel a 11 caracteres
        txtCpfResponsavel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 11) {
                txtCpfResponsavel.setText(oldValue);
            }
        });
        
        // Limitar telefone do responsavel a 11 caracteres
        txtTelefoneResponsavel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 11) {
                txtTelefoneResponsavel.setText(oldValue);
            }
        });
    }
    
    @FXML
    private void onSalvar() {
        try {
            // VALIDACOES BASICAS
            if (txtNome == null || txtNome.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Digite o nome do aluno!");
                return;
            }
            
            Turma turmaSelecionada = cbTurma.getValue();
            if (turmaSelecionada == null) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Selecione a turma do aluno!");
                return;
            }
            
            if (txtCpf == null || txtCpf.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Digite o CPF do aluno!");
                return;
            }
            
            if (txtIdade == null || txtIdade.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Digite a idade do aluno!");
                return;
            }
            
            if (dtDataNascimento == null || dtDataNascimento.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Selecione a data de nascimento!");
                return;
            }
            
            if (txtNomeResponsavel == null || txtNomeResponsavel.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Digite o nome do responsavel!");
                return;
            }
            
            if (txtCpfResponsavel == null || txtCpfResponsavel.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Digite o CPF do responsavel!");
                return;
            }
            
            if (cbParentesco == null || cbParentesco.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Selecione o parentesco!");
                return;
            }
            
            // CRIAR OBJETO RESPONSAVEL
            Responsavel responsavel = new Responsavel(
                txtNomeResponsavel.getText().trim(),
                txtCpfResponsavel.getText().trim(),
                txtTelefoneResponsavel.getText().trim(),
                txtEmailResponsavel.getText().trim(),
                txtEnderecoResponsavel.getText().trim(),
                cbParentesco.getValue()
            );
            
            // SALVAR RESPONSAVEL E PEGAR O ID
            int idResponsavel = responsavelDAO.salvar(responsavel);
            System.out.println("Responsavel salvo com ID: " + idResponsavel);
            
            // CRIAR OBJETO ESTUDANTE COM O ID DO RESPONSAVEL
            Estudante estudante = new Estudante(
                txtNome.getText().trim(),
                Integer.parseInt(txtIdade.getText().trim()),
                txtCpf.getText().trim(),
                txtEmail.getText().trim(),
                txtTelefone.getText().trim(),
                dtDataNascimento.getValue().toString(),
                idResponsavel
            );
            
            // SALVAR ESTUDANTE E OBTER O ID GERADO
            int idEstudante = estudanteDAO.salvar(estudante);
            System.out.println("Estudante salvo com ID: " + idEstudante);
            
            // REGISTRAR MATRICULA
            int idTurma = turmaSelecionada.getId_turma(); 
            int idMatricula = matriculaDAO.salvar(idEstudante, idTurma);
            System.out.println("Matricula registrada com ID: " + idMatricula + " na Turma ID: " + idTurma);
            
            // REGISTRAR ATIVIDADE RECENTE - AQUI, APOS TUDO DAR CERTO
            String nomeAluno = txtNome.getText().trim();
            String nomeTurma = turmaSelecionada.getNome();
            atividadeDAO.registrar("ALUNO", "Novo aluno matriculado: " + nomeAluno + " - Turma " + nomeTurma);
            
            // SUCESSO!
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Aluno, Responsavel e Matricula cadastrados com sucesso!");
            onLimpar();
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "A idade deve ser um numero valido!");
            e.printStackTrace();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar no banco de dados: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onLimpar() {
        txtNome.clear();
        txtIdade.clear();
        txtCpf.clear();
        txtEmail.clear();
        txtTelefone.clear();
        dtDataNascimento.setValue(null);
        cbTurma.setValue(null);
        
        txtNomeResponsavel.clear();
        txtCpfResponsavel.clear();
        txtTelefoneResponsavel.clear();
        txtEmailResponsavel.clear();
        txtEnderecoResponsavel.clear();
        cbParentesco.setValue(null);
    }
    
    @FXML
    private void onVoltar(ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/Telainicial.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void carregarTurmas() {
        try {
            turmasCadastradas.clear();
            turmasCadastradas.addAll(turmaDAO.listarTodos());
            cbTurma.setItems(turmasCadastradas);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Banco de Dados", "Nao foi possivel carregar as turmas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
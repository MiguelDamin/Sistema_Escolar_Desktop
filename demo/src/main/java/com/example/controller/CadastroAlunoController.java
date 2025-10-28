package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.example.model.Estudante;
import com.example.model.Responsavel;
import com.example.repository.EstudanteDAO;
import com.example.repository.MatriculaDAO;
import com.example.repository.ResponsavelDAO;

import javafx.collections.FXCollections;
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
    @FXML private DatePicker dtDataNascimento;  // ← ADICIONAR NO FXML
    @FXML private ComboBox<String> cbTurma;
    
    // CAMPOS DO RESPONSÁVEL
    @FXML private TextField txtNomeResponsavel;
    @FXML private TextField txtCpfResponsavel;
    @FXML private TextField txtTelefoneResponsavel;
    @FXML private TextField txtEmailResponsavel;
    @FXML private TextField txtEnderecoResponsavel;
    @FXML private ComboBox<String> cbParentesco;  // ← ADICIONAR NO FXML
    
    @FXML private Label lblMensagem;
    @FXML private Label print;
    
    private ResponsavelDAO responsavelDAO = new ResponsavelDAO();
    private EstudanteDAO estudanteDAO = new EstudanteDAO();
    private MatriculaDAO matriculaDAO = new MatriculaDAO(); // NOVO DAO
    
    @FXML
    public void initialize() {
        System.out.println("Controller carregado com sucesso!");
        
        // Preencher ComboBox de turmas
        cbTurma.setItems(FXCollections.observableArrayList(
            "1º Ano", "2º Ano", "3º Ano", "4º Ano", "5º Ano",
            "6º Ano", "7º Ano", "8º Ano", "9º Ano"
        ));
        
        // Preencher ComboBox de parentesco
        cbParentesco.setItems(FXCollections.observableArrayList(
            "Pai", "Mãe", "Avô", "Avó", "Tio", "Tia", "Tutor Legal", "Outro"
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
        
        // Limitar CPF do responsável a 11 caracteres
        txtCpfResponsavel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 11) {
                txtCpfResponsavel.setText(oldValue);
            }
        });
        
        // Limitar telefone do responsável a 11 caracteres
        txtTelefoneResponsavel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 11) {
                txtTelefoneResponsavel.setText(oldValue);
            }
        });
    }
    @FXML
    private void onSalvar() {
        try {
            // VALIDAÇÕES BÁSICAS
            if (txtNome == null || txtNome.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Digite o nome do aluno!");
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
                showAlert(Alert.AlertType.ERROR, "Erro", "Digite o nome do responsável!");
                return;
            }
            
            if (txtCpfResponsavel == null || txtCpfResponsavel.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Digite o CPF do responsável!");
                return;
            }
            
            if (cbParentesco == null || cbParentesco.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Selecione o parentesco!");
                return;
            }
            
            // 1️⃣ CRIAR OBJETO RESPONSÁVEL
            Responsavel responsavel = new Responsavel(
                txtNomeResponsavel.getText().trim(),
                txtCpfResponsavel.getText().trim(),
                txtTelefoneResponsavel.getText().trim(),
                txtEmailResponsavel.getText().trim(),
                txtEnderecoResponsavel.getText().trim(),
                cbParentesco.getValue()
            );
            
            // 2️⃣ SALVAR RESPONSÁVEL E PEGAR O ID
            int idResponsavel = responsavelDAO.salvar(responsavel);
            System.out.println("Responsável salvo com ID: " + idResponsavel);
            
            // 3️⃣ CRIAR OBJETO ESTUDANTE COM O ID DO RESPONSÁVEL
            Estudante estudante = new Estudante(
                txtNome.getText().trim(),
                Integer.parseInt(txtIdade.getText().trim()),
                txtCpf.getText().trim(),
                txtEmail.getText().trim(),
                txtTelefone.getText().trim(),
                dtDataNascimento.getValue().toString(),
                idResponsavel
            );
            
            // 4️⃣ SALVAR ESTUDANTE E OBTER O ID GERADO
            int idEstudante = estudanteDAO.salvar(estudante);
            System.out.println("Estudante salvo com ID: " + idEstudante);
            
            // 5️⃣ REGISTRAR MATRÍCULA
            int idMatricula = matriculaDAO.salvar(idEstudante);
            System.out.println("Matrícula registrada com ID: " + idMatricula);
            
            // SUCESSO!
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Aluno, Responsável e Matrícula cadastrados com sucesso!");
            onLimpar();
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "A idade deve ser um número válido!");
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
        // Limpar campos do aluno
        txtNome.clear();
        txtIdade.clear();
        txtCpf.clear();
        txtEmail.clear();
        txtTelefone.clear();
        dtDataNascimento.setValue(null);
        cbTurma.setValue(null);
        
        // Limpar campos do responsável
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
}

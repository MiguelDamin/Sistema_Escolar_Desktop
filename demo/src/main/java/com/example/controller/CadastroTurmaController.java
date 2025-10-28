package com.example.controller;

import com.example.model.Turma;
import com.example.repository.TurmaDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Year;

/**
 * Controller para a tela de Cadastro de Turmas.
 */
public class CadastroTurmaController {
    
    @FXML private TextField txtNomeTurma;
    @FXML private TextField txtAnoLetivo;
    @FXML private Label lblMensagemErro;
    @FXML private Label lblMensagemSucesso;
    
    private TurmaDAO turmaDAO = new TurmaDAO();
    
    @FXML
    public void initialize() {
        System.out.println("🎬 Controller de Turma inicializado!");
        
        // Limitar ano letivo a apenas números
        txtAnoLetivo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Regex: apenas dígitos
                txtAnoLetivo.setText(oldValue);
            }
        });
        
        limparMensagens();
    }
    
    /**
     * Método chamado quando o usuário clica em "Salvar Turma"
     */
    @FXML
    private void onSalvarTurma() {
        limparMensagens();
        
        try {
            // ========== VALIDAÇÕES ==========
            
            String nome = txtNomeTurma.getText().trim();
            String anoStr = txtAnoLetivo.getText().trim();
            
            if (nome.isEmpty()) {
                mostrarErro("O nome da turma é obrigatório!");
                txtNomeTurma.requestFocus();
                return;
            }
            
            if (anoStr.isEmpty()) {
                mostrarErro("O ano letivo é obrigatório!");
                txtAnoLetivo.requestFocus();
                return;
            }
            
            int ano;
            try {
                ano = Integer.parseInt(anoStr);
            } catch (NumberFormatException e) {
                mostrarErro("O ano letivo deve ser um número válido!");
                txtAnoLetivo.requestFocus();
                return;
            }
            
            // Regra de negócio: Ano deve ser razoável
            int anoAtual = Year.now().getValue();
            if (ano < anoAtual - 5 || ano > anoAtual + 5) {
                mostrarErro("O ano letivo parece inválido. Use um ano próximo ao atual.");
                txtAnoLetivo.requestFocus();
                return;
            }
            
            // ========== CRIAÇÃO DO OBJETO E INSERÇÃO ==========
            
            Turma novaTurma = new Turma(nome, ano);
            
            // Chama DAO para salvar
            int id = turmaDAO.salvar(novaTurma);
            
            mostrarSucesso("Turma cadastrada com sucesso! ID: " + id);
            onLimpar();
            
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar turma: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            mostrarErro("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Limpa todos os campos do formulário
     */
    @FXML
    private void onLimpar() {
        txtNomeTurma.clear();
        txtAnoLetivo.clear();
        limparMensagens();
    }
    
    /**
     * Volta para a tela inicial
     */
    @FXML
    private void onVoltar(ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(
            getClass().getResource("/com/example/fxml/Telainicial.fxml")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
    }
    
    // ========== MÉTODOS AUXILIARES ==========
    
    private void limparMensagens() {
        lblMensagemErro.setText("");
        lblMensagemErro.setVisible(false);
        lblMensagemSucesso.setText("");
        lblMensagemSucesso.setVisible(false);
    }
    
    private void mostrarErro(String mensagem) {
        limparMensagens();
        lblMensagemErro.setText("❌ " + mensagem);
        lblMensagemErro.setVisible(true);
    }
    
    private void mostrarSucesso(String mensagem) {
        limparMensagens();
        lblMensagemSucesso.setText("✅ " + mensagem);
        lblMensagemSucesso.setVisible(true);
    }
}

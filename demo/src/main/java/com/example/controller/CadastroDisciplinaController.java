package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.example.model.Disciplina;
import com.example.repository.DisciplinaDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller para a tela de Cadastro de Disciplinas.
 */
public class CadastroDisciplinaController {
    
    @FXML private TextField txtNomeDisciplina;
    @FXML private TextField txtCargaHoraria;
    @FXML private TextArea txtDescricao;
    @FXML private Label lblMensagemErro;
    @FXML private Label lblMensagemSucesso;
    
    private DisciplinaDAO dao = new DisciplinaDAO();
    
    @FXML
    public void initialize() {
        System.out.println("🎬 Controller de Disciplina inicializado!");
        
        // Limitar carga horária a apenas números
        txtCargaHoraria.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCargaHoraria.setText(oldValue);
            }
        });
        
        limparMensagens();
    }
    
    /**
     * Método chamado quando o usuário clica em "Salvar Disciplina"
     */
    @FXML
    private void onSalvarDisciplina() {
        limparMensagens();
        
        try {
            // ========== VALIDAÇÕES ==========
            
            String nome = txtNomeDisciplina.getText().trim();
            String cargaStr = txtCargaHoraria.getText().trim();
            String descricao = txtDescricao.getText().trim();
            
            if (nome.isEmpty()) {
                mostrarErro("O nome da disciplina é obrigatório!");
                txtNomeDisciplina.requestFocus();
                return;
            }
            
            if (cargaStr.isEmpty()) {
                mostrarErro("A carga horária é obrigatória!");
                txtCargaHoraria.requestFocus();
                return;
            }
            
            int cargaHoraria;
            try {
                cargaHoraria = Integer.parseInt(cargaStr);
            } catch (NumberFormatException e) {
                mostrarErro("A carga horária deve ser um número válido!");
                txtCargaHoraria.requestFocus();
                return;
            }
            
            if (cargaHoraria <= 0) {
                mostrarErro("A carga horária deve ser maior que zero!");
                txtCargaHoraria.requestFocus();
                return;
            }
            
            if (cargaHoraria > 200) {
                mostrarErro("A carga horária parece muito alta. Verifique!");
                txtCargaHoraria.requestFocus();
                return;
            }
            
            // Verifica se já existe disciplina com esse nome
            if (dao.existePorNome(nome)) {
                mostrarErro("Já existe uma disciplina com esse nome!");
                txtNomeDisciplina.requestFocus();
                return;
            }
            
            // ========== CRIAÇÃO DO OBJETO E INSERÇÃO ==========
            
            Disciplina novaDisciplina = new Disciplina(nome, cargaHoraria, descricao);
            
            int id = dao.salvar(novaDisciplina);
            
            mostrarSucesso("Disciplina cadastrada com sucesso! ID: " + id);
            onLimpar();
            
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar disciplina: " + e.getMessage());
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
        if (txtNomeDisciplina != null) txtNomeDisciplina.clear();
        if (txtCargaHoraria != null) txtCargaHoraria.clear();
        if (txtDescricao != null) txtDescricao.clear();
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
        if (lblMensagemErro != null) {
            lblMensagemErro.setText("");
            lblMensagemErro.setVisible(false);
        }
        if (lblMensagemSucesso != null) {
            lblMensagemSucesso.setText("");
            lblMensagemSucesso.setVisible(false);
        }
    }
    
    private void mostrarErro(String mensagem) {
        limparMensagens();
        if (lblMensagemErro != null) {
            lblMensagemErro.setText("❌ " + mensagem);
            lblMensagemErro.setVisible(true);
        }
    }
    
    private void mostrarSucesso(String mensagem) {
        limparMensagens();
        if (lblMensagemSucesso != null) {
            lblMensagemSucesso.setText("✅ " + mensagem);
            lblMensagemSucesso.setVisible(true);
        }
    }
}
package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import com.example.model.PeriodoLetivo;
import com.example.repository.PeriodoLetivoDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller para a tela de Cadastro de Período Letivo.
 */
public class CadastroPeriodoLetivoController {
    
    @FXML private TextField txtNomePeriodo;
    @FXML private DatePicker dtDataInicio;
    @FXML private DatePicker dtDataFim;
    @FXML private Label lblMensagemErro;
    @FXML private Label lblMensagemSucesso;
    
    private PeriodoLetivoDAO periodoDAO = new PeriodoLetivoDAO();
    
    @FXML
    public void initialize() {
        System.out.println("🎬 Controller de Período Letivo inicializado!");
        limparMensagens();
    }
    
    /**
     * Método chamado quando o usuário clica em "Salvar Período Letivo"
     */
    @FXML
    private void onSalvarPeriodoLetivo() {
        limparMensagens();
        
        try {
            // ========== VALIDAÇÕES ==========
            
            String nome = txtNomePeriodo.getText().trim();
            LocalDate dataInicio = dtDataInicio.getValue();
            LocalDate dataFim = dtDataFim.getValue();
            
            if (nome.isEmpty()) {
                mostrarErro("O nome do período é obrigatório!");
                txtNomePeriodo.requestFocus();
                return;
            }
            
            if (dataInicio == null) {
                mostrarErro("A data de início é obrigatória!");
                dtDataInicio.requestFocus();
                return;
            }
            
            if (dataFim == null) {
                mostrarErro("A data de fim é obrigatória!");
                dtDataFim.requestFocus();
                return;
            }
            
            // Regra de negócio: Data fim deve ser após data início
            if (dataFim.isBefore(dataInicio)) {
                mostrarErro("A data de fim deve ser posterior à data de início!");
                dtDataFim.requestFocus();
                return;
            }
            
            // ========== CRIAÇÃO DO OBJETO E INSERÇÃO ==========
            
            PeriodoLetivo novoPeriodo = new PeriodoLetivo(nome, dataInicio, dataFim);
            
            // Chama DAO para salvar
            int id = periodoDAO.salvar(novoPeriodo);
            
            mostrarSucesso("Período letivo cadastrado com sucesso! ID: " + id);
            onLimpar();
            
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar período letivo: " + e.getMessage());
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
        txtNomePeriodo.clear();
        dtDataInicio.setValue(null);
        dtDataFim.setValue(null);
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
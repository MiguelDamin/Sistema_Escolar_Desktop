package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.example.model.PeriodoLetivo;
import com.example.model.Turma;
import com.example.repository.PeriodoLetivoDAO;
import com.example.repository.TurmaDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller para a tela de Cadastro de Turmas.
 * Permite selecionar o Período Letivo disponível.
 */
public class CadastroTurmaController {
    
    @FXML private TextField txtNomeTurma;
    @FXML private ComboBox<PeriodoLetivo> cbPeriodoLetivo;
    @FXML private Label lblMensagemErro;
    @FXML private Label lblMensagemSucesso;
    
    private TurmaDAO turmaDAO = new TurmaDAO();
    private PeriodoLetivoDAO periodoDAO = new PeriodoLetivoDAO();
    private ObservableList<PeriodoLetivo> periodosDisponiveis = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        System.out.println("🎬 Controller de Turma inicializado!");
        
        // Proteção: só carrega se o ComboBox existir
        if (cbPeriodoLetivo != null) {
            carregarPeriodosLetivos();
        } else {
            System.err.println("⚠️ cbPeriodoLetivo é NULL! Verifique o fx:id no FXML!");
        }
        
        limparMensagens();
    }
    
    /**
     * Carrega os períodos letivos do banco e preenche o ComboBox
     */
    private void carregarPeriodosLetivos() {
        try {
            System.out.println("📋 Carregando períodos letivos...");
            periodosDisponiveis.clear();
            periodosDisponiveis.addAll(periodoDAO.listarTodos());
            cbPeriodoLetivo.setItems(periodosDisponiveis);
            System.out.println("✅ " + periodosDisponiveis.size() + " períodos carregados!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao carregar períodos: " + e.getMessage());
            mostrarErro("Não foi possível carregar os períodos letivos: " + e.getMessage());
            e.printStackTrace();
        }
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
            PeriodoLetivo periodoSelecionado = cbPeriodoLetivo.getValue();
            
            if (nome.isEmpty()) {
                mostrarErro("O nome da turma é obrigatório!");
                txtNomeTurma.requestFocus();
                return;
            }
            
            if (periodoSelecionado == null) {
                mostrarErro("Selecione um período letivo!");
                cbPeriodoLetivo.requestFocus();
                return;
            }
            
            // ========== CRIAÇÃO DO OBJETO E INSERÇÃO ==========
            
            Turma novaTurma = new Turma();
            novaTurma.setNome(nome);
            novaTurma.setId_periodo_letivo(periodoSelecionado.getId_periodo_letivo());
            
            System.out.println("💾 Salvando turma: " + nome + " (Período: " + periodoSelecionado.getId_periodo_letivo() + ")");
            
            // Chama DAO para salvar
            int id = turmaDAO.salvar(novaTurma);
            
            System.out.println("✅ Turma salva com ID: " + id);
            mostrarSucesso("Turma cadastrada com sucesso! ID: " + id);
            onLimpar();
            
        } catch (SQLException e) {
            System.err.println("❌ Erro SQL: " + e.getMessage());
            mostrarErro("Erro ao salvar turma: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            mostrarErro("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Limpa todos os campos do formulário
     */
    @FXML
    private void onLimpar() {
        if (txtNomeTurma != null) txtNomeTurma.clear();
        if (cbPeriodoLetivo != null) cbPeriodoLetivo.setValue(null);
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
        } else {
            System.err.println("⚠️ lblMensagemErro é NULL!");
        }
    }
    
    private void mostrarSucesso(String mensagem) {
        limparMensagens();
        if (lblMensagemSucesso != null) {
            lblMensagemSucesso.setText("✅ " + mensagem);
            lblMensagemSucesso.setVisible(true);
        } else {
            System.out.println("⚠️ lblMensagemSucesso é NULL!");
        }
    }
}
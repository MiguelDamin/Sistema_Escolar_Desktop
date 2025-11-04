package com.example.controller;

import java.io.IOException;

import com.example.model.PeriodoLetivo;
import com.example.model.Turma;
import com.example.repository.PeriodoLetivoDAO;
import com.example.repository.TurmaDAO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
        
        if (cbPeriodoLetivo != null) {
            carregarPeriodosLetivosAsync();
        } else {
            System.err.println("⚠️ cbPeriodoLetivo é NULL! Verifique o fx:id no FXML!");
        }
        
        limparMensagens();
    }
    
    /**
     * 🔥 CARREGA PERÍODOS EM BACKGROUND - NÃO TRAVA A UI
     */
    private void carregarPeriodosLetivosAsync() {
        Task<ObservableList<PeriodoLetivo>> task = new Task<>() {
            @Override
            protected ObservableList<PeriodoLetivo> call() throws Exception {
                System.out.println("📋 Carregando períodos letivos...");
                ObservableList<PeriodoLetivo> periodos = FXCollections.observableArrayList();
                periodos.addAll(periodoDAO.listarTodos());
                return periodos;
            }
        };
        
        task.setOnSucceeded(event -> {
            periodosDisponiveis = task.getValue();
            cbPeriodoLetivo.setItems(periodosDisponiveis);
            System.out.println("✅ " + periodosDisponiveis.size() + " períodos carregados!");
        });
        
        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            System.err.println("❌ Erro ao carregar períodos: " + ex.getMessage());
            mostrarErro("Não foi possível carregar os períodos letivos: " + ex.getMessage());
            ex.printStackTrace();
        });
        
        new Thread(task).start();
    }
    
    @FXML
    private void onSalvarTurma() {
        limparMensagens();
        
        try {
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
            
            // Captura dados antes da Task
            final String nomeF = nome;
            final int idPeriodo = periodoSelecionado.getId_periodo_letivo();
            
            // 🔥 SALVA EM BACKGROUND
            Task<Integer> salvarTask = new Task<>() {
                @Override
                protected Integer call() throws Exception {
                    Turma novaTurma = new Turma();
                    novaTurma.setNome(nomeF);
                    novaTurma.setId_periodo_letivo(idPeriodo);
                    
                    System.out.println("💾 Salvando turma: " + nomeF + " (Período: " + idPeriodo + ")");
                    return turmaDAO.salvar(novaTurma);
                }
            };
            
            salvarTask.setOnSucceeded(event -> {
                int id = salvarTask.getValue();
                System.out.println("✅ Turma salva com ID: " + id);
                mostrarSucesso("Turma cadastrada com sucesso! ID: " + id);
                onLimpar();
            });
            
            salvarTask.setOnFailed(event -> {
                Throwable ex = salvarTask.getException();
                System.err.println("❌ Erro: " + ex.getMessage());
                mostrarErro("Erro ao salvar turma: " + ex.getMessage());
                ex.printStackTrace();
            });
            
            new Thread(salvarTask).start();
            
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            mostrarErro("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onLimpar() {
        Platform.runLater(() -> {
            if (txtNomeTurma != null) txtNomeTurma.clear();
            if (cbPeriodoLetivo != null) cbPeriodoLetivo.setValue(null);
            limparMensagens();
        });
    }
    
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
    
    private void limparMensagens() {
        Platform.runLater(() -> {
            if (lblMensagemErro != null) {
                lblMensagemErro.setText("");
                lblMensagemErro.setVisible(false);
            }
            if (lblMensagemSucesso != null) {
                lblMensagemSucesso.setText("");
                lblMensagemSucesso.setVisible(false);
            }
        });
    }
    
    private void mostrarErro(String mensagem) {
        Platform.runLater(() -> {
            limparMensagens();
            if (lblMensagemErro != null) {
                lblMensagemErro.setText("❌ " + mensagem);
                lblMensagemErro.setVisible(true);
            } else {
                System.err.println("⚠️ lblMensagemErro é NULL!");
            }
        });
    }
    
    private void mostrarSucesso(String mensagem) {
        Platform.runLater(() -> {
            limparMensagens();
            if (lblMensagemSucesso != null) {
                lblMensagemSucesso.setText("✅ " + mensagem);
                lblMensagemSucesso.setVisible(true);
            } else {
                System.out.println("⚠️ lblMensagemSucesso é NULL!");
            }
        });
    }
}
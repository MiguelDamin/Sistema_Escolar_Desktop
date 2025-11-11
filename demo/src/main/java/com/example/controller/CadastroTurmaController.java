package com.example.controller;

import java.io.IOException;

import com.example.model.PeriodoLetivo;
import com.example.model.Turma;
import com.example.repository.AtividadeRecenteDAO;
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
    
    // DAOs
    private TurmaDAO turmaDAO = new TurmaDAO();
    private PeriodoLetivoDAO periodoDAO = new PeriodoLetivoDAO();
    private AtividadeRecenteDAO atividadeDAO = new AtividadeRecenteDAO(); // ADICIONADO
    
    private ObservableList<PeriodoLetivo> periodosDisponiveis = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        System.out.println("Controller de Turma inicializado!");
        
        if (cbPeriodoLetivo != null) {
            carregarPeriodosLetivosAsync();
        } else {
            System.err.println("cbPeriodoLetivo e NULL! Verifique o fx:id no FXML!");
        }
        
        limparMensagens();
    }
    
    private void carregarPeriodosLetivosAsync() {
        Task<ObservableList<PeriodoLetivo>> task = new Task<>() {
            @Override
            protected ObservableList<PeriodoLetivo> call() throws Exception {
                System.out.println("Carregando periodos letivos...");
                ObservableList<PeriodoLetivo> periodos = FXCollections.observableArrayList();
                periodos.addAll(periodoDAO.listarTodos());
                return periodos;
            }
        };
        
        task.setOnSucceeded(event -> {
            periodosDisponiveis = task.getValue();
            cbPeriodoLetivo.setItems(periodosDisponiveis);
            System.out.println(periodosDisponiveis.size() + " periodos carregados!");
        });
        
        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            System.err.println("Erro ao carregar periodos: " + ex.getMessage());
            mostrarErro("Nao foi possivel carregar os periodos letivos: " + ex.getMessage());
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
                mostrarErro("O nome da turma e obrigatorio!");
                txtNomeTurma.requestFocus();
                return;
            }
            
            if (periodoSelecionado == null) {
                mostrarErro("Selecione um periodo letivo!");
                cbPeriodoLetivo.requestFocus();
                return;
            }
            
            final String nomeF = nome;
            final int idPeriodo = periodoSelecionado.getId_periodo_letivo();
            
            Task<Integer> salvarTask = new Task<>() {
                @Override
                protected Integer call() throws Exception {
                    Turma novaTurma = new Turma();
                    novaTurma.setNome(nomeF);
                    novaTurma.setId_periodo_letivo(idPeriodo);
                    
                    System.out.println("Salvando turma: " + nomeF + " (Periodo: " + idPeriodo + ")");
                    return turmaDAO.salvar(novaTurma);
                }
            };
            
            salvarTask.setOnSucceeded(event -> {
                int id = salvarTask.getValue();
                System.out.println("Turma salva com ID: " + id);
                
                // REGISTRAR ATIVIDADE APOS SUCESSO
                atividadeDAO.registrar("TURMA", "Nova turma criada: " + nomeF);
                
                mostrarSucesso("Turma cadastrada com sucesso! ID: " + id);
                onLimpar();
            });
            
            salvarTask.setOnFailed(event -> {
                Throwable ex = salvarTask.getException();
                System.err.println("Erro: " + ex.getMessage());
                mostrarErro("Erro ao salvar turma: " + ex.getMessage());
                ex.printStackTrace();
            });
            
            new Thread(salvarTask).start();
            
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
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
                lblMensagemErro.setText(mensagem);
                lblMensagemErro.setVisible(true);
            } else {
                System.err.println("lblMensagemErro e NULL!");
            }
        });
    }
    
    private void mostrarSucesso(String mensagem) {
        Platform.runLater(() -> {
            limparMensagens();
            if (lblMensagemSucesso != null) {
                lblMensagemSucesso.setText(mensagem);
                lblMensagemSucesso.setVisible(true);
            } else {
                System.out.println("lblMensagemSucesso e NULL!");
            }
        });
    }
}
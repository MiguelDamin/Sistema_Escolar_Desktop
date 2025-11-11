package com.example.controller;

import java.io.IOException;

import com.example.model.Professor;
import com.example.repository.AtividadeRecenteDAO;
import com.example.repository.ProfessorDAO;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroProfessorController {
    @FXML private TextField txtNomeProfessor;
    @FXML private TextField txtIdadeProfessor;
    @FXML private TextField txtCpfProfessor;
    @FXML private TextField txtEmailProfessor;
    @FXML private TextField txtTelefoneProfessor;
    @FXML private Label LabelProfessor;
    
    // DAOs
    private ProfessorDAO professorDAO = new ProfessorDAO();
    private AtividadeRecenteDAO atividadeDAO = new AtividadeRecenteDAO(); // ADICIONADO

    @FXML
    public void initialize(){
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

    @FXML
    private void onSalvarProfessor(ActionEvent event){
        String nome = txtNomeProfessor.getText() != null ? txtNomeProfessor.getText().trim() : "";
        String idadeTxt = txtIdadeProfessor.getText() != null ? txtIdadeProfessor.getText().trim() : "";
        String cpf = txtCpfProfessor.getText() != null ? txtCpfProfessor.getText().trim() : "";
        String email = txtEmailProfessor.getText() != null ? txtEmailProfessor.getText().trim() : "";
        String telefone = txtTelefoneProfessor.getText() != null ? txtTelefoneProfessor.getText().trim() : "";

        if (nome.isBlank() || idadeTxt.isBlank() || cpf.isBlank() || email.isBlank() || telefone.isBlank()) {
            showMessage("Erro: Todos os campos sao obrigatorios");
            return;
        }

        final int idade;
        try {
            idade = Integer.parseInt(idadeTxt);
            if (idade <= 0) {
                showMessage("Erro: Idade invalida");
                return;
            }
        } catch (NumberFormatException e) {
            showMessage("Erro: Idade deve ser um numero inteiro");
            return;
        }

        Professor professor = new Professor(nome, idade, cpf, email, telefone);

        Task<Boolean> salvarTask = new Task<>() {
            @Override
            protected Boolean call() {
                return professorDAO.salvar(professor);
            }
        };

        salvarTask.setOnSucceeded(workerStateEvent -> {
            boolean sucesso = salvarTask.getValue();
            if (sucesso) {
                // REGISTRAR ATIVIDADE APOS SUCESSO
                atividadeDAO.registrar("PROFESSOR", "Professor cadastrado: " + nome);
                
                showMessage("Professor salvo com sucesso!");
                clearFields();
            } else {
                showMessage("Erro ao salvar o professor. Veja o console para detalhes.");
            }
        });

        salvarTask.setOnFailed(workerStateEvent -> {
            Throwable ex = salvarTask.getException();
            ex.printStackTrace();
            showMessage("Erro inesperado ao salvar: " + (ex != null ? ex.getMessage() : ""));
        });

        new Thread(salvarTask).start();
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
    private void onLimpar(){
        clearFields();
        if (LabelProfessor != null) {
            LabelProfessor.setVisible(false);
        }
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
            } else {
                System.out.println(msg);
            }
        });
    }
}
package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.example.model.Curso;
import com.example.repository.AtividadeRecenteDAO;
import com.example.repository.CursoDAO;

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

public class CadastroCursoController {
    
    @FXML private TextField txtNomeCurso;
    @FXML private TextField txtDuracaoCurso;
    @FXML private TextArea txtDescricaoCurso;
    @FXML private Label LabelCurso;
    @FXML private Label LabelConfirmacao;
    
    // DAOs
    private CursoDAO cursoDAO = new CursoDAO();
    private AtividadeRecenteDAO atividadeDAO = new AtividadeRecenteDAO(); // ADICIONADO
    
    @FXML
    public void initialize() {
        System.out.println("Controller de Curso inicializado!");
        
        txtDuracaoCurso.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtDuracaoCurso.setText(oldValue);
            }
        });
        
        LabelCurso.setVisible(false);
        LabelConfirmacao.setVisible(false);
    }
    
    @FXML
    private void onSalvarCurso() {
        limparMensagens();
        
        try {
            String nome = txtNomeCurso.getText().trim();
            String duracaoStr = txtDuracaoCurso.getText().trim();
            String descricao = txtDescricaoCurso.getText().trim();
            
            if (nome.isEmpty()) {
                mostrarErro("O nome do curso e obrigatorio!");
                txtNomeCurso.requestFocus();
                return;
            }
            
            if (duracaoStr.isEmpty()) {
                mostrarErro("A duracao do curso e obrigatoria!");
                txtDuracaoCurso.requestFocus();
                return;
            }
            
            int duracao;
            try {
                duracao = Integer.parseInt(duracaoStr);
            } catch (NumberFormatException e) {
                mostrarErro("A duracao deve ser um numero inteiro valido!");
                txtDuracaoCurso.requestFocus();
                return;
            }
            
            if (duracao <= 0) {
                mostrarErro("A duracao deve ser maior que zero!");
                txtDuracaoCurso.requestFocus();
                return;
            }
            
            if (cursoDAO.existePorNome(nome)) {
                mostrarErro("Ja existe um curso com esse nome!");
                txtNomeCurso.requestFocus();
                return;
            }
            
            Curso novoCurso = new Curso(nome, duracao, descricao);
            int id = cursoDAO.salvar(novoCurso);
            
            // REGISTRAR ATIVIDADE APOS SUCESSO
            atividadeDAO.registrar("CURSO", "Novo curso criado: " + nome);
            
            mostrarSucesso("Curso cadastrado com sucesso! ID: " + id);
            onLimpar();
            
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onLimpar() {
        txtNomeCurso.clear();
        txtDuracaoCurso.clear();
        txtDescricaoCurso.clear();
        limparMensagens();
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
        LabelCurso.setText("");
        LabelCurso.setVisible(false);
        LabelConfirmacao.setText("");
        LabelConfirmacao.setVisible(false);
    }
    
    private void mostrarErro(String mensagem) {
        limparMensagens();
        LabelCurso.setText(mensagem);
        LabelCurso.setVisible(true);
    }
    
    private void mostrarSucesso(String mensagem) {
        limparMensagens();
        LabelConfirmacao.setText(mensagem);
        LabelConfirmacao.setVisible(true);
    }
}
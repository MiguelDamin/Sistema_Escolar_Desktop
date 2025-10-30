package com.example.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TelaInicialController {
    
    // ========================================
    // MÉTODOS DE NAVEGAÇÃO PARA CADASTROS
    // ========================================
    
    @FXML
    private void onCadastrarAluno(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroAluno.fxml", event);
    }

    @FXML
    private void onCadastroHorario(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroHorario.fxml", event);
    }
    
    @FXML
    private void onCadastrarTurma(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroTurma.fxml", event);
    }
    
    @FXML
    private void onCadastrarCurso(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroCurso.fxml", event);
    }
    
    @FXML
    private void onCadastrarProfessor(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroProfessor.fxml", event);
    }
    
    @FXML
    private void onPeriodoLetivo(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/PeriodoLetivo.fxml", event);
    }

        @FXML
    private void onCadastrarDisciplina(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroDisciplina.fxml", event);
    }
    
    // ========================================
    // MÉTODOS DO MENU PRINCIPAL
    // ========================================
    
    @FXML
    private void onDashboard(ActionEvent event) {
        System.out.println("Dashboard - Já está na tela inicial");
        // Não precisa fazer nada, já está na tela dashboard
    }
    
    @FXML
    private void onEstatisticas(ActionEvent event) {
        System.out.println("Estatísticas - Em desenvolvimento");
        // TODO: Criar tela de estatísticas quando necessário
    }
    
    @FXML
    private void onRelatorios(ActionEvent event) {
        System.out.println("Relatórios - Em desenvolvimento");
        // TODO: Criar tela de relatórios quando necessário
    }
    
    @FXML
    private void onVoltar(ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/register.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
    }
    
    // ========================================
    // MÉTODO AUXILIAR PARA CARREGAR CENAS
    // ========================================
    
    /**
     * Método auxiliar para carregar uma nova cena com ActionEvent.
     * 
     * @param caminho Caminho do arquivo FXML
     * @param event Evento do JavaFX (ActionEvent)
     * @throws IOException Se o arquivo FXML não for encontrado
     */
    private void carregarCena(String caminho, ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(getClass().getResource(caminho));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
    }

  
}
package com.example.controller;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller para a Tela Inicial (Dashboard)
 * ✅ ATUALIZADO: Suporta tanto ActionEvent quanto MouseEvent
 */
public class TelaInicialController {
    
    // ========================================
    // MÉTODOS DE NAVEGAÇÃO PARA CADASTROS
    // ========================================
    
    @FXML
    private void onCadastrarAluno(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroAluno.fxml", event);
    }
    
    @FXML
    private void onCadastrarAluno(MouseEvent event) throws IOException {
        carregarCenaMouse("/com/example/fxml/CadastroAluno.fxml", event);
    }

    @FXML
    private void onCadastroHorario(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroHorario.fxml", event);
    }
    
    @FXML
    private void onCadastroHorario(MouseEvent event) throws IOException {
        carregarCenaMouse("/com/example/fxml/CadastroHorario.fxml", event);
    }
    
    @FXML
    private void onCadastrarTurma(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroTurma.fxml", event);
    }
    
    @FXML
    private void onCadastrarTurma(MouseEvent event) throws IOException {
        carregarCenaMouse("/com/example/fxml/CadastroTurma.fxml", event);
    }
    
    @FXML
    private void onCadastrarCurso(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroCurso.fxml", event);
    }
    
    @FXML
    private void onCadastrarCurso(MouseEvent event) throws IOException {
        carregarCenaMouse("/com/example/fxml/CadastroCurso.fxml", event);
    }
    
    @FXML
    private void onCadastrarProfessor(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroProfessor.fxml", event);
    }
    
    @FXML
    private void onCadastrarProfessor(MouseEvent event) throws IOException {
        carregarCenaMouse("/com/example/fxml/CadastroProfessor.fxml", event);
    }
    
    @FXML
    private void onPeriodoLetivo(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/PeriodoLetivo.fxml", event);
    }
    
    @FXML
    private void onPeriodoLetivo(MouseEvent event) throws IOException {
        carregarCenaMouse("/com/example/fxml/PeriodoLetivo.fxml", event);
    }

    @FXML
    private void onCadastrarDisciplina(ActionEvent event) throws IOException {
        carregarCena("/com/example/fxml/CadastroDisciplina.fxml", event);
    }
    
    @FXML
    private void onCadastrarDisciplina(MouseEvent event) throws IOException {
        carregarCenaMouse("/com/example/fxml/CadastroDisciplina.fxml", event);
    }
    
    // ========================================
    // MÉTODOS DO MENU PRINCIPAL
    // ========================================
    
    @FXML
    private void onDashboard(ActionEvent event) {
        System.out.println("📊 Dashboard - Já está na tela inicial");
    }
    
    @FXML
    private void onDashboard(MouseEvent event) {
        System.out.println("📊 Dashboard - Já está na tela inicial");
    }
    
    @FXML
    private void onCertificados(ActionEvent event) {
        System.out.println("🎓 Certificados - Em desenvolvimento");
    }
    
    @FXML
    private void onCertificados(MouseEvent event) {
        System.out.println("🎓 Certificados - Em desenvolvimento");
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
    // MÉTODOS AUXILIARES PARA CARREGAR CENAS
    // ========================================
    
    /**
     * Método auxiliar para carregar uma nova cena com ActionEvent.
     */
    private void carregarCena(String caminho, ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(getClass().getResource(caminho));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);

        // 🔧 CORRIGIDO: Aplicar CSS à nova cena
        String cssPath = "/com/example/css/styles.css";
        URL cssUrl = getClass().getResource(cssPath);
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método auxiliar para carregar uma nova cena com MouseEvent.
     */
    private void carregarCenaMouse(String caminho, MouseEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(getClass().getResource(caminho));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);

        // 🔧 CORRIGIDO: Aplicar CSS à nova cena
        String cssPath = "/com/example/css/styles.css";
        URL cssUrl = getClass().getResource(cssPath);
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setScene(scene);
        stage.show();
    }
}
package com.example.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TelaInicialController {
    
    @FXML
    private void onDashboard(MouseEvent event) {
        System.out.println("📊 Dashboard clicado");
    }
    
    @FXML
    private void onCadastrarProfessor(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroProfessor.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarAluno(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroAluno.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarTurma(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroTurma.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarCurso(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroCurso.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarDisciplina(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroDisciplina.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onPeriodoLetivo(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/PeriodoLetivo.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastroHorario(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroHorario.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
       @FXML
    private void onCadastrarCurso2(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroCurso.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
       @FXML
    private void onCadastrarProfessor2(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroProfessor.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
       @FXML
    private void onCadastrarAluno2(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroAluno.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
       @FXML
    private void onCadastrarTurma2(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroTurma.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCertificados(MouseEvent event) {
        System.out.println("🎓 Certificados - Em desenvolvimento");
    }
    
    // ========================================
    // 🔧 MÉTODO CORRIGIDO COM REDIMENSIONAMENTO FORÇADO
    // ========================================
    
    private void carregarCenaComRedimensionamento(String caminho, MouseEvent event) throws IOException {
        System.out.println("🔄 Carregando: " + caminho);
        
        Parent novaCena = FXMLLoader.load(getClass().getResource(caminho));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // 🔧 FORÇA O REDIMENSIONAMENTO DA CENA
        Scene scene = new Scene(novaCena);
        
        // 🔧 Aplica CSS se existir
        String css = getClass().getResource("/com/example/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        stage.setScene(scene);
        
        // 🔧 FORÇA ATUALIZAÇÃO DO LAYOUT
        stage.sizeToScene();  // Ajusta o stage ao tamanho da cena
        
        // 🔧 Se ainda estiver muito pequeno, define tamanho mínimo
        if (stage.getWidth() < 800) {
            stage.setWidth(1000);
        }
        if (stage.getHeight() < 600) {
            stage.setHeight(700);
        }
        
        stage.show();
        
        System.out.println("✅ Cena carregada: " + stage.getWidth() + "x" + stage.getHeight());
    }
    
    @FXML
    private void onVoltar(ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/register.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        
        String css = getClass().getResource("/com/example/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
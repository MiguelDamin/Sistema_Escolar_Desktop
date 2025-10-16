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
    private void onCadastrarAluno(MouseEvent event) throws IOException{
        carregarCena("/com/example/fxml/CadastroAluno.fxml", event);
    }

    @FXML
    private void onCadastrarCurso(MouseEvent event) throws IOException{
        carregarCena("/com/example/fxml/CadastroCurso.fxml", event);
    }

    @FXML 
    private void onCadastrarProfessor(MouseEvent event) throws IOException{
        carregarCena("/com/example/fxml/CadastroProfessor.fxml", event);
    }

    @FXML
    private void onPeriodoLetivo(MouseEvent event) throws IOException{
        carregarCena("/com/example/fxml/PeriodoLetivo.fxml", event);
    }

    @FXML
    private void onVoltar(ActionEvent event) throws IOException{
        Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/register.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
    }
    
    // Método auxiliar para MouseEvent
    private void carregarCena(String caminho, MouseEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(getClass().getResource(caminho));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
    }
}
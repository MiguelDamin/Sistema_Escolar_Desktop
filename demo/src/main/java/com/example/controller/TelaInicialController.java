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
      @FXML
      private void onCadastrarAluno(ActionEvent event) throws IOException{
        Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/CadastroAluno.fxml"));

    // Pega a janela atual a partir do botão clicado
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Troca a cena
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
      }

      @FXML
      private void onCadastrarCurso(ActionEvent event) throws IOException{
         Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/CadastroCurso.fxml"));

    // Pega a janela atual a partir do botão clicado
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Troca a cena
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();

      }

      @FXML 
      private void onCadastrarProfessor(ActionEvent event) throws IOException{
        Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/CadastroProfessor.fxml"));

    // Pega a janela atual a partir do botão clicado
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Troca a cena
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
      }

      @FXML
      private void onVoltar(ActionEvent event) throws IOException{
         Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/register.fxml"));

    // Pega a janela atual a partir do botão clicado
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Troca a cena
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
      }

      @FXML
      private void onPeriodoLetivo(ActionEvent event) throws IOException{
        Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/PeriodoLetivo.fxml"));

    // Pega a janela atual a partir do botão clicado
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Troca a cena
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
      
      }
}


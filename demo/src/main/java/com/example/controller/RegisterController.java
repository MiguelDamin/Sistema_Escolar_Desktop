package com.example.controller;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
    @FXML private PasswordField txtSenha;
    @FXML private TextField txtUsuario;
    @FXML private Label txtLabel;    

    @FXML
    private void onEntrar(ActionEvent event) throws IOException {
        
        if (txtSenha.getText().isEmpty() || txtUsuario.getText().isEmpty()){
            txtLabel.setText("Não é permitido deixar o login em branco!!");
        } else {
            // ✅ CARREGA O MAINLAYOUT AGORA!
            Parent novaCena = FXMLLoader.load(
                getClass().getResource("/com/example/fxml/MainLayout.fxml")
            );
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(novaCena);
            
            // Aplica CSS
            String cssPath = "/com/example/css/styles.css";
            URL cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
            
            stage.setScene(scene);
            stage.setMaximized(true); // ✅ TELA CHEIA!
            stage.show();
            
            System.out.println("✅ Login realizado! MainLayout carregado!");
        }
    }
    
    @FXML
    private void onTelaCadastro(ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(
            getClass().getResource("/com/example/fxml/cadastrousuario.fxml")
        );
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
    }
}
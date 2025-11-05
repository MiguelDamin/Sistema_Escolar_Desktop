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

    // Carrega o novo arquivo FXML
    if (txtSenha.getText().isEmpty() || txtUsuario.getText().isEmpty()){
        txtLabel.setText("Não é permitido deixar o login em branco!!");
    }else{
         Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/Telainicial.fxml"));

        // Pega a janela atual a partir do botão clicado
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Troca a cena
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
@FXML
private void onTelaCadastro(ActionEvent event) throws IOException {
     // Carrega o novo arquivo FXML

    Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/cadastrousuario.fxml"));

    // Pega a janela atual a partir do botão clicado
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    // Troca a cena
    Scene scene = new Scene(novaCena);
    stage.setScene(scene);
    stage.show();
}
}
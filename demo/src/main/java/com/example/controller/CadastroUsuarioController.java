package com.example.controller;

/**
 * Fornece classes para o sistema de entrada e saída (I/O) de dados.
 * É usada aqui especificamente para tratar a exceção 'IOException', que pode ocorrer
 * ao carregar um arquivo FXML, caso o arquivo não seja encontrado.
 */
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroUsuarioController {
    @FXML private PasswordField txtSenhaCadastro;
    @FXML private TextField txtEmailCadastro;
    @FXML private TextField txtUsuarioCadastro;
    
    
    @FXML 
    private void onCadastrar(){
        String senha = txtSenhaCadastro.getText();
        String email = txtEmailCadastro.getText();
        String usuario = txtUsuarioCadastro.getText();

        // if(senha.isEmpty() || email.isEmpty() || usuario.isEmpty()){
            
        // }


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
    
    
}
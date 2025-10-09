package com.example.controller;

/**
 * Fornece classes para o sistema de entrada e saída (I/O) de dados.
 * É usada aqui especificamente para tratar a exceção 'IOException', que pode ocorrer
 * ao carregar um arquivo FXML, caso o arquivo não seja encontrado.
 */
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CadastroUsuarioController {
    @FXML private PasswordField txtSenhaCadastro;
    @FXML private TextField txtEmailCadastro;
    @FXML private TextField txtUsuarioCadastro;
    
    
    
    
    
}
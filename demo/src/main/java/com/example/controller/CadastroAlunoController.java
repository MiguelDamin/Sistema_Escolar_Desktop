package com.example.controller;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class CadastroAlunoController {
    // @FXML private TextField txtName;
    // @FXML private TextField txtEmail;
    // @FXML private TextField txtUserName;
    // @FXML private PasswordField txtPassword;
    // @FXML private Label lblMessage;



    // @FXML 
    // private void onSalvar() {
    //     String nome = txtName.getText();
    //     String email = txtEmail.getText();
    //     lblMessage.setText("Usuário cadastrado: " + nome + " - " + email);
    // }
    

    @FXML private TextField txtNome;
    @FXML private TextField txtIdade;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private ComboBox<String> cbTurma;
    @FXML private TextField txtResponsavel;
    @FXML private Label lblMensagem;
    @FXML private TextField txtCpf;
    @FXML private Label print;



     @FXML
    public void initialize() {
        System.out.println("Controller carregado com sucesso!");
        
        // Preencher o ComboBox de turmas
        cbTurma.setItems(FXCollections.observableArrayList(
            "1º Ano",
            "2º Ano",
            "3º Ano",
            "4º Ano",
            "5º Ano",
            "6º Ano",
            "7º Ano",
            "8º Ano",
            "9º Ano"
        ));

    System.out.println("Controller carregado!");
    
    // Limitar telefone a 15 caracteres
    txtTelefone.textProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue.length() > 11) {
            txtTelefone.setText(oldValue);
        }
    
    });
    txtCpf.textProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue.length() > 11) {
            txtCpf.setText(oldValue);
        }
    
    });
}

private void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

    @FXML
    private void onSalvar(){
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String idade = txtIdade.getText();
        String telefone = txtTelefone.getText();
        String turma = cbTurma.getValue();
        String responsavel = txtResponsavel.getText();
        String cpf = txtCpf.getText();
        if (nome == null || nome.trim().isEmpty() || 
        cpf == null || cpf.trim().isEmpty() || 
        turma == null){ 
            print.setText("Erro: Todos os campos são obrigatórios");
            print.setVisible(true);
        }else{
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Aluno salvo com sucesso!!");
        }

    }
    @FXML
    private void onLimpar(){
        txtNome.clear();
        txtIdade.clear();
        txtEmail.clear();
        txtTelefone.clear();
        cbTurma.setValue(null);
        txtResponsavel.clear();

    }

    @FXML
    private void onVoltar(ActionEvent event) throws IOException{
            Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/Telainicial.fxml"));

        // Pega a janela atual a partir do botão clicado
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Troca a cena
            Scene scene = new Scene(novaCena);
            stage.setScene(scene);
            stage.show();


    }

}




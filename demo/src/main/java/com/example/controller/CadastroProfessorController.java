package com.example.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroProfessorController {
    @FXML private TextField txtNomeProfessor;
    @FXML private TextField txtIdadeProfessor;
    @FXML private TextField txtCpfProfessor;
    @FXML private TextField txtEmailProfessor;
    @FXML private TextField txtTelefoneProfessor;
    @FXML private Label LabelProfessor;



    @FXML
    private void onSalvarProfessor(){
        String nomeprofessor = txtNomeProfessor.getText();
        String idadeprofessor = txtIdadeProfessor.getText();
        String cpfprofessor = txtCpfProfessor.getText();
        String emailprofessor = txtEmailProfessor.getText();
        String telefoneprofessor = txtTelefoneProfessor.getText();

        if(nomeprofessor.isEmpty() || idadeprofessor.isEmpty() || cpfprofessor == null || 
        emailprofessor.isEmpty() || telefoneprofessor.isEmpty()){
            LabelProfessor.setText("Erro, preencha todos os campos!!!");
        }else{
            LabelProfessor.setText("Usuário Altamente Cadastrado!!");
        }

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

    @FXML
    private void onLimpar(){
        txtNomeProfessor.clear();
        txtIdadeProfessor.clear();
        txtCpfProfessor.clear();
        txtEmailProfessor.clear();
        txtTelefoneProfessor.clear();

    }
}

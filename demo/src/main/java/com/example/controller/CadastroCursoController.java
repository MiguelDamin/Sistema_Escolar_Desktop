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


public class CadastroCursoController {
    @FXML private TextField txtNomeCurso;
    @FXML private TextField txtDuracaoCurso;
    @FXML private Label LabelCurso;
    @FXML private Label LabelConfirmacao;

    @FXML
    private void onSalvarCurso(){
        String nomecurso = txtNomeCurso.getText();
        String duracaocurso = txtDuracaoCurso.getText();
        if(nomecurso.isEmpty() || duracaocurso.isEmpty()){
            LabelCurso.setText("Erro, é impossível continuar com o nome do curso em branco!!");
        }else{
            LabelConfirmacao.setText("Curso Cadastrado");
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
}

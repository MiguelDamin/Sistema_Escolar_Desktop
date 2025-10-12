package com.example.controller;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PeriodoLetivo{
    @FXML private TextField txtHorarioInicio; 
    @FXML private TextField txtHorarioFim;
    @FXML private DatePicker DataInicio;
    @FXML private DatePicker DataFim;
    @FXML private Label LabelPeriodoLetivo;

    @FXML
    public void onSalvarPeriodoLetivo(ActionEvent event){ 
        // ou simplesmente: public void onSalvarPeriodoLetivo(){
        String horainicial = txtHorarioInicio.getText();
        String horafinal = txtHorarioFim.getText();
     
        if(horainicial.isEmpty() || horafinal.isEmpty() || DataInicio.getValue() == null || DataFim.getValue() == null){
            LabelPeriodoLetivo.setText("Erro: Todos os campos são obrigatórios");

        }else{
            LabelPeriodoLetivo.setText("Cadastro realizado com sucesso");
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
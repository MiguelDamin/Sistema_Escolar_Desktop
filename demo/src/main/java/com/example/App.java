package com.example;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Caminho para o arquivo FXML
        String fxmlPath = "/com/example/fxml/register.fxml";
        URL fxmlUrl = getClass().getResource(fxmlPath);
        
        if (fxmlUrl == null) {
            System.err.println("Não foi possível encontrar o arquivo FXML: " + fxmlPath);
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Parent root = fxmlLoader.load();
        
        // Caminho correto para o CSS
        String cssPath = "/com/example/css/styles.css";
        URL cssUrl = getClass().getResource(cssPath);
        
        Scene scene = new Scene(root, 1200, 800);
        
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Aviso: CSS não encontrado em: " + cssPath);
        }

        stage.setTitle("Sistema de Gestão Escolar - Login");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
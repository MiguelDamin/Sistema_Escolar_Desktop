package com.example;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Caminho para o arquivo FXML
        String fxmlPath = "/com/example/view/register.fxml";
        URL fxmlUrl = App.class.getResource("/com/example/fxml/register.fxml");

        if (fxmlUrl == null) {
            System.err.println("Não foi possível encontrar o arquivo FXML: " + fxmlPath);
            return; // Encerra se o FXML não for encontrado
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        // Carrega e aplica o arquivo CSS à cena
        String cssPath = "/com/example/style.css";
        URL cssUrl = App.class.getResource(cssPath);
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setTitle("Cadastro de Usuário");
        stage.setMinWidth(400); // Define uma largura mínima para a janela
        stage.setMinHeight(400); // Define uma altura mínima para a janela
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(); // inicia o JavaFX
    }
}
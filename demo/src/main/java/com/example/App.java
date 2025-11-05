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
        // ✅ CAMINHO CORRETO DO FXML
        String fxmlPath = "/com/example/fxml/register.fxml";
        URL fxmlUrl = App.class.getResource(fxmlPath);

        if (fxmlUrl == null) {
            System.err.println("❌ Não foi possível encontrar o arquivo FXML: " + fxmlPath);
            System.err.println("📁 Verifique se o arquivo existe em: src/main/resources/com/example/fxml/register.fxml");
            return;
        }

        System.out.println("✅ FXML encontrado: " + fxmlUrl);

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

        // ✅ CAMINHO CORRETO DO CSS
        String cssPath = "/com/example/css/styles.css";
        URL cssUrl = App.class.getResource(cssPath);
        
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            System.out.println("✅ CSS carregado: " + cssUrl);
        } else {
            System.err.println("⚠️ CSS não encontrado: " + cssPath);
        }

        stage.setTitle("Sistema de Gestão Escolar");
        stage.setMinWidth(1366);
        stage.setMinHeight(768);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
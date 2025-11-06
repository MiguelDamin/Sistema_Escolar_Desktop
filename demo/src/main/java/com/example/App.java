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
        try {
            String fxmlPath = "/com/example/fxml/register.fxml";
            URL fxmlUrl = App.class.getResource(fxmlPath);

            if (fxmlUrl == null) {
                System.err.println("❌ FXML não encontrado: " + fxmlPath);
                return;
            }

            System.out.println("✅ FXML encontrado: " + fxmlUrl);

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Parent root = fxmlLoader.load();

            // 🔧 Cena responsiva
            Scene scene = new Scene(root);

            // 🔧 CSS
            String cssPath = "/com/example/css/styles.css";
            URL cssUrl = App.class.getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
                System.out.println("✅ CSS carregado");
            } else {
                System.err.println("⚠️ CSS não encontrado: " + cssPath);
            }

            // 🔧 JANELA RESPONSIVA E MAXIMIZÁVEL
            stage.setTitle("Sistema de Gestão Escolar");
            stage.setMinWidth(1000);  // Tamanho mínimo
            stage.setMinHeight(700);
            stage.setWidth(1280);     // Tamanho inicial
            stage.setHeight(800);
            stage.setMaximized(false); // Permite maximizar
            stage.setResizable(true);  // Permite redimensionar
            
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            
            System.out.println("✅ Aplicação iniciada!");

        } catch (IOException e) {
            System.err.println("❌ ERRO:");
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        System.out.println("🚀 Iniciando...");
        launch(args);
    }
}
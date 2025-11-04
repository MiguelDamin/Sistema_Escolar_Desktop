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
            // 🔧 CORRIGIDO: Caminho consistente
            String fxmlPath = "/com/example/fxml/register.fxml";
            URL fxmlUrl = App.class.getResource(fxmlPath);

            if (fxmlUrl == null) {
                System.err.println("❌ ERRO: Arquivo FXML não encontrado: " + fxmlPath);
                System.err.println("🔍 Verifique se o arquivo está em: src/main/resources" + fxmlPath);
                return;
            }

            System.out.println("✅ FXML encontrado: " + fxmlUrl);

            // Carrega o FXML
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Parent root = fxmlLoader.load();

            // 🔧 CORRIGIDO: Deixa o tamanho ser definido pelo conteúdo
            Scene scene = new Scene(root);

            // 🔧 CORRIGIDO: Caminho correto do CSS
            String cssPath = "/com/example/css/styles.css";
            URL cssUrl = App.class.getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
                System.out.println("✅ CSS carregado: " + cssUrl);
            } else {
                System.err.println("⚠️ CSS NÃO ENCONTRADO: " + cssPath);
                System.err.println("🔍 Verifique se o arquivo está em: src/main/resources" + cssPath);
            }

            // 🔧 CORRIGIDO: Configurações da janela
            stage.setTitle("Sistema de Gestão Escolar - Login");
            
            // Define tamanhos ANTES de setScene
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setWidth(1200);
            stage.setHeight(800);
            
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            
            System.out.println("✅ Aplicação iniciada!");
            System.out.println("📐 Tamanho: " + stage.getWidth() + "x" + stage.getHeight());

        } catch (IOException e) {
            System.err.println("❌ ERRO ao carregar:");
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Sistema...");
        launch(args);
    }
}
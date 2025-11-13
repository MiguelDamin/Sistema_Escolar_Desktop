package com.example.util;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe utilitária para navegação entre telas mantendo a TelaInicial sempre disponível.
 * Evita a repetição de código de navegação em todos os controllers.
 */
public class NavigationHelper {
    
    /**
     * Volta para a Tela Inicial (Dashboard)
     * 
     * @param event O evento do botão que foi clicado
     * @throws IOException Se houver erro ao carregar o FXML
     */
    public static void voltarParaTelaInicial(ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(
            NavigationHelper.class.getResource("/com/example/fxml/Telainicial.fxml")
        );
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        
        // Aplica o CSS
        String css = NavigationHelper.class.getResource("/com/example/css/styles.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        }
        
        stage.setScene(scene);
        stage.setMaximized(true); // Tela cheia
        stage.show();
        
        System.out.println("✅ Retornou para Tela Inicial");
    }
    
    /**
     * Carrega uma tela de cadastro
     * 
     * @param event O evento do botão
     * @param caminhoFXML Caminho do arquivo FXML (ex: "/com/example/fxml/CadastroAluno.fxml")
     * @throws IOException Se houver erro ao carregar o FXML
     */
    public static void carregarTelaCadastro(ActionEvent event, String caminhoFXML) throws IOException {
        Parent novaCena = FXMLLoader.load(
            NavigationHelper.class.getResource(caminhoFXML)
        );
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        
        String css = NavigationHelper.class.getResource("/com/example/css/styles.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        }
        
        stage.setScene(scene);
        stage.sizeToScene();
        
        // Garante tamanho mínimo
        if (stage.getWidth() < 800) {
            stage.setWidth(1000);
        }
        if (stage.getHeight() < 600) {
            stage.setHeight(700);
        }
        
        stage.show();
    }
    
    /**
     * Carrega uma tela usando MouseEvent (para HBox clicável)
     * 
     * @param event O MouseEvent
     * @param caminhoFXML Caminho do FXML
     * @throws IOException Se houver erro
     */
    public static void carregarTela(javafx.scene.input.MouseEvent event, String caminhoFXML) throws IOException {
        Parent novaCena = FXMLLoader.load(
            NavigationHelper.class.getResource(caminhoFXML)
        );
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        
        String css = NavigationHelper.class.getResource("/com/example/css/styles.css").toExternalForm();
        if (css != null) {
            scene.getStylesheets().add(css);
        }
        
        stage.setScene(scene);
        stage.sizeToScene();
        
        if (stage.getWidth() < 800) {
            stage.setWidth(1000);
        }
        if (stage.getHeight() < 600) {
            stage.setHeight(700);
        }
        
        stage.show();
    }
}
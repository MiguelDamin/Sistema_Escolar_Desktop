import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Caminho para o arquivo FXML
        String fxmlPath = "/com/example/view/register.fxml";
        URL fxmlUrl = App.class.getResource(fxmlPath);
        
        if (fxmlUrl == null) {
            System.err.println("Não foi possível encontrar o arquivo FXML: " + fxmlPath);
            return; // Encerra se o FXML não for encontrado
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        
        // Carrega a antiga o arquivo CSS à cena
        String cssPath = "/com/example/style/styles.css";
        URL cssUrl = App.class.getResource(cssPath);
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setTitle("Cadastro de usuário");
        stage.setMinWidth(800); // Define uma largura mínima para a janela
        stage.setMinHeight(600); // Define uma altura mínima para a janela
        stage.setWidth(1200);
        stage.setHeight(600);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(); // Inicia o JavaFX
    }
}
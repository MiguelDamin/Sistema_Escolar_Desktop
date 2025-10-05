module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;  // ← ADICIONE ESTA LINHA!
    
    exports com.example;
}
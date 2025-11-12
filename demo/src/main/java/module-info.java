module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    // ✅ ADICIONE ESTA LINHA PARA FONTAWESOME
    requires de.jensd.fx.glyphs.fontawesome;
    
    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    opens com.example.model to javafx.fxml;
    opens com.example.repository to javafx.fxml;
    
    exports com.example;
    exports com.example.controller;
    exports com.example.model;
    exports com.example.repository;
}
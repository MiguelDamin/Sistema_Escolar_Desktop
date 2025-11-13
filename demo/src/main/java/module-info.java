
module com.example {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    
    // MySQL
    requires java.sql;
    
    // FontAwesome
    requires de.jensd.fx.glyphs.fontawesome;
    
    // Exporta os pacotes necessários
    exports com.example;
    exports com.example.controller;
    exports com.example.model;
    exports com.example.repository;
    exports com.example.util;
    
    // Permite que JavaFX acesse os controllers via reflection
    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    opens com.example.model to javafx.base;
}
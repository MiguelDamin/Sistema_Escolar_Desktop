module com.example {
    // ========== JAVAFX ==========
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    
    // ========== BANCO DE DADOS ==========
    requires java.sql;
    
    // ========== FONTAWESOME ==========
    requires de.jensd.fx.glyphs.fontawesome;
    
    // ========== iTEXT 7 (PDF) ==========
    requires kernel;
    requires layout;
    requires io;
    requires forms;
    requires pdfa;
    
    // ========== JAVA DESKTOP (para abrir PDF) ==========
    requires java.desktop;
    
    // ========== EXPORTS ==========
    exports com.example;
    exports com.example.controller;
    exports com.example.model;
    exports com.example.repository;
    exports com.example.service;  // ⭐ NOVO
    exports com.example.util;
    
    // ========== OPENS (para FXML reflection) ==========
    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    opens com.example.model to javafx.base;
}
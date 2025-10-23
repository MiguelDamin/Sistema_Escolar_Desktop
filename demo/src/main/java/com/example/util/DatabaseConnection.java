package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    
    public static Connection conectar() {
        try {
            // Informações do banco
            String url = "jdbc:mysql://localhost:3306/gestaoescola";
            String usuario = "root";
            String senha = "Miguel11!"; // COLOQUE SUA SENHA DO MYSQL AQUI
            
            // Conectar
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            
            System.out.println("✅ Conectado ao banco!");
            return conexao;
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
            return null;
        }
    }
}
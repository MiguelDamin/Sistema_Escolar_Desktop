package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.model.Estudante;
import com.example.util.DatabaseConnection;

public class EstudanteDAO {
    
    public void salvar(Estudante estudante) throws SQLException {
        String sql = "INSERT INTO estudantes (nome, idade, cpf, email, telefone, data_nascimento, id_responsavel) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estudante.getNome());
            stmt.setInt(2, estudante.getIdade());
            stmt.setString(3, estudante.getCpf());
            stmt.setString(4, estudante.getEmail());
            stmt.setString(5, estudante.getTelefone());
            stmt.setString(6, estudante.getData_nascimento());
            stmt.setInt(7, estudante.getId_responsavel());
            
            stmt.executeUpdate(); 
        }
    }
}
package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.model.Responsavel;
import com.example.util.DatabaseConnection;

public class ResponsavelDAO {
    
    // Salva responsável e RETORNA o ID gerado
    public int salvar(Responsavel responsavel) throws SQLException {
        String sql = "INSERT INTO responsavel (nome, cpf, telefone, email, endereco, parentesco) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, responsavel.getNome());
            stmt.setString(2, responsavel.getCpf());
            stmt.setString(3, responsavel.getTelefone());
            stmt.setString(4, responsavel.getEmail());
            stmt.setString(5, responsavel.getEndereco());
            stmt.setString(6, responsavel.getParentesco());
            
            stmt.executeUpdate();
            
            // Pega o ID gerado pelo banco
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
            throw new SQLException("Erro ao obter ID do responsável");
        }
    }
    /**
 * Atualizar dados do responsável
 */
public boolean atualizar(Responsavel responsavel) throws SQLException {
    String sql = "UPDATE responsavel SET nome = ?, cpf = ?, telefone = ?, " +
                 "email = ?, endereco = ?, parentesco = ? " +
                 "WHERE id_responsavel = ?";
    
    try (Connection conn = DatabaseConnection.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, responsavel.getNome());
        stmt.setString(2, responsavel.getCpf());
        stmt.setString(3, responsavel.getTelefone());
        stmt.setString(4, responsavel.getEmail());
        stmt.setString(5, responsavel.getEndereco());
        stmt.setString(6, responsavel.getParentesco());
        stmt.setInt(7, responsavel.getIdResponsavel());
        
        int linhas = stmt.executeUpdate();
        return linhas > 0;
    }
}
}

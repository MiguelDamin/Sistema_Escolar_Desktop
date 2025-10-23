package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.model.Professor;
import com.example.util.DatabaseConnection;

public class ProfessorDAO {
    
    // Salvar professor no banco
    public boolean salvar(Professor professor) {
        String sql = "INSERT INTO professor (nome, idade, cpf, email, telefone) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            // 1. Conectar no banco
            conn = DatabaseConnection.conectar();
            if (conn == null) {
                System.out.println("Falha na conexao com o banco");
                return false;
            }
            
            // 2. Preparar comando SQL
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, professor.getNome());
            pstmt.setInt(2, professor.getIdade());
            pstmt.setString(3, professor.getCpf());
            pstmt.setString(4, professor.getEmail());
            pstmt.setString(5, professor.getTelefone());
            
            // 3. Executar
            int linhas = pstmt.executeUpdate();
            
            if (linhas > 0) {
                System.out.println("Professor salvo com sucesso!");
                return true;
            } else {
                System.out.println("Nenhuma linha afetada");
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // 4. Fechar conexoes
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
                System.out.println("Conexao fechada");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
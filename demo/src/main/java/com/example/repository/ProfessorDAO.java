package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Professor;
import com.example.util.DatabaseConnection;

public class ProfessorDAO {
    
    // Salvar professor no banco
    public boolean salvar(Professor professor) {
        String sql = "INSERT INTO professor (nome, idade, cpf, email, telefone) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.conectar();
            if (conn == null) {
                System.out.println("Falha na conexao com o banco");
                return false;
            }
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, professor.getNome());
            pstmt.setInt(2, professor.getIdade());
            pstmt.setString(3, professor.getCpf());
            pstmt.setString(4, professor.getEmail());
            pstmt.setString(5, professor.getTelefone());
            
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
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
                System.out.println("Conexao fechada");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * NOVO MÉTODO: Retorna TODOS os professores cadastrados.
     */
    public List<Professor> listarTodos() throws SQLException {
        String sql = "SELECT id_professor, nome, idade, cpf, email, telefone FROM professor ORDER BY nome ASC";
        List<Professor> professores = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Professor professor = new Professor();
                professor.setIdProfessor(rs.getInt("id_professor"));
                professor.setNome(rs.getString("nome"));
                professor.setIdade(rs.getInt("idade"));
                professor.setCpf(rs.getString("cpf"));
                professor.setEmail(rs.getString("email"));
                professor.setTelefone(rs.getString("telefone"));
                
                professores.add(professor);
            }
            
            System.out.println("✅ " + professores.size() + " professores carregados!");
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar professores: " + e.getMessage());
            throw e;
        }
        
        return professores;
    }
}
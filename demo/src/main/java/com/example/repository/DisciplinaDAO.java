package com.example.repository;  // ✅ CORRETO

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Disciplina;
import com.example.util.DatabaseConnection;

/**
 * Data Access Object para a entidade Disciplina.
 */
public class DisciplinaDAO {
    
    /**
     * Insere uma nova disciplina no banco de dados.
     */
    public int salvar(Disciplina disciplina) throws SQLException {
        String sql = "INSERT INTO disciplinas (nome, carga_horaria, descricao) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, disciplina.getNome());
            stmt.setInt(2, disciplina.getCarga_horaria());
            stmt.setString(3, disciplina.getDescricao());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("✅ Disciplina salva com ID: " + id);
                return id;
            }
            
            throw new SQLException("Erro ao obter ID da disciplina inserida");
        }
    }
    
    /**
     * Retorna TODAS as disciplinas cadastradas.
     */
    public List<Disciplina> listarTodos() throws SQLException {
        String sql = "SELECT id_disciplina, nome, carga_horaria, descricao FROM disciplinas ORDER BY nome ASC";
        List<Disciplina> disciplinas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setId_disciplina(rs.getInt("id_disciplina"));
                disciplina.setNome(rs.getString("nome"));
                disciplina.setCarga_horaria(rs.getInt("carga_horaria"));
                disciplina.setDescricao(rs.getString("descricao"));
                
                disciplinas.add(disciplina);
            }
            
            System.out.println("✅ " + disciplinas.size() + " disciplinas carregadas!");
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar disciplinas: " + e.getMessage());
            throw e;
        }
        
        return disciplinas;
    }
    
    /**
     * Busca uma disciplina pelo ID.
     */
    public Disciplina buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_disciplina, nome, carga_horaria, descricao FROM disciplinas WHERE id_disciplina = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setId_disciplina(rs.getInt("id_disciplina"));
                disciplina.setNome(rs.getString("nome"));
                disciplina.setCarga_horaria(rs.getInt("carga_horaria"));
                disciplina.setDescricao(rs.getString("descricao"));
                return disciplina;
            }
            
            return null;
        }
    }
    
    /**
     * Atualiza uma disciplina existente.
     */
    public void atualizar(Disciplina disciplina) throws SQLException {
        String sql = "UPDATE disciplinas SET nome = ?, carga_horaria = ?, descricao = ? WHERE id_disciplina = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, disciplina.getNome());
            stmt.setInt(2, disciplina.getCarga_horaria());
            stmt.setString(3, disciplina.getDescricao());
            stmt.setInt(4, disciplina.getId_disciplina());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✅ Disciplina atualizada com sucesso!");
            } else {
                System.out.println("⚠️ Nenhuma disciplina encontrada com o ID: " + disciplina.getId_disciplina());
            }
        }
    }
    
    /**
     * Remove uma disciplina do banco de dados.
     */
    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM disciplinas WHERE id_disciplina = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✅ Disciplina removida com sucesso!");
                return true;
            } else {
                System.out.println("⚠️ Disciplina não encontrada!");
                return false;
            }
        }
    }
    
    /**
     * Verifica se já existe uma disciplina com o mesmo nome.
     */
    public boolean existePorNome(String nome) throws SQLException {
        String sql = "SELECT COUNT(*) FROM disciplinas WHERE nome = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            
            return false;
        }
    }
}

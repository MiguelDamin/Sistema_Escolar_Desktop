package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Turma;
import com.example.util.DatabaseConnection;

/**
 * Data Access Object para a entidade Turma.
 */
public class TurmaDAO {
    
    /**
     * Insere uma nova turma no banco de dados.
     */
    public int salvar(Turma turma) throws SQLException {
        // Insere com id_periodo_letivo e id_grade = 1 (padrão)
        String sql = "INSERT INTO turma (nome_turma, id_periodo_letivo, id_grade) VALUES (?, ?, 1)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, turma.getNome());
            stmt.setInt(2, turma.getId_periodo_letivo());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
            throw new SQLException("Erro ao obter ID da turma inserida");
        }
    }
    
    /**
     * Retorna TODAS as turmas cadastradas.
     */
    public List<Turma> listarTodos() throws SQLException {
        String sql = "SELECT id_turma, nome_turma, id_periodo_letivo FROM turma ORDER BY nome_turma ASC";
        List<Turma> turmas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Turma turma = new Turma();
                turma.setId_turma(rs.getInt("id_turma"));
                turma.setNome(rs.getString("nome_turma"));
                turma.setId_periodo_letivo(rs.getInt("id_periodo_letivo"));
                
                turmas.add(turma);
            }
        }
        
        return turmas;
    }
}
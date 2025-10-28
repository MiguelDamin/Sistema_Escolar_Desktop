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
 * 
 * Responsabilidades:
 * - Persistir turmas no banco de dados
 * - Recuperar turmas do banco de dados
 */
public class TurmaDAO {
    
    /**
     * Insere uma nova turma no banco de dados.
     * 
     * @param turma Objeto Turma com os dados a serem salvos
     * @return ID da turma inserida
     * @throws SQLException se houver erro na comunicação com o banco
     */
    public int salvar(Turma turma) throws SQLException {
        String sql = "INSERT INTO turma (nome_turma, ano) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, turma.getNome());
            stmt.setInt(2, turma.getAno());
            
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
     * 
     * @return Lista com todas as turmas (pode ser vazia)
     * @throws SQLException se houver erro na comunicação com o banco
     */
    public List<Turma> listarTodos() throws SQLException {
        String sql = "SELECT id_turma, nome_turma, ano FROM turma ORDER BY ano DESC, nome_turma ASC";
        List<Turma> turmas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Turma turma = new Turma();
                turma.setId_turma(rs.getInt("id_turma"));
                turma.setNome(rs.getString("nome"));
                turma.setAno(rs.getInt("ano"));
                
                turmas.add(turma);
            }
        }
        
        return turmas;
    }
}


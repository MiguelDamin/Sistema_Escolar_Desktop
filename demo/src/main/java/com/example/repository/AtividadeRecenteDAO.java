package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.model.AtividadeRecente;
import com.example.util.DatabaseConnection;

public class AtividadeRecenteDAO {
    
    /**
     * Registra uma nova atividade
     */
    public void registrar(String tipo, String descricao) {
        String sql = "INSERT INTO atividades_recentes (tipo, descricao) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo);
            stmt.setString(2, descricao);
            stmt.executeUpdate();
            
            System.out.println("Atividade registrada: " + descricao);
            
        } catch (SQLException e) {
            System.err.println("Erro ao registrar atividade: " + e.getMessage());
        }
    }
    
    /**
     * Busca as ultimas 5 atividades
     */
    public List<AtividadeRecente> listarRecentes(int limite) {
        String sql = "SELECT * FROM atividades_recentes ORDER BY data_hora DESC LIMIT ?";
        List<AtividadeRecente> atividades = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limite);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                AtividadeRecente atividade = new AtividadeRecente();
                atividade.setId_atividade(rs.getInt("id_atividade"));
                atividade.setTipo(rs.getString("tipo"));
                atividade.setDescricao(rs.getString("descricao"));
                
                Timestamp timestamp = rs.getTimestamp("data_hora");
                if (timestamp != null) {
                    atividade.setData_hora(timestamp.toLocalDateTime());
                }
                
                atividades.add(atividade);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar atividades: " + e.getMessage());
        }
        
        return atividades;
    }
    
    /**
     * Limpa atividades antigas (mais de 30 dias)
     */
    public void limparAntigas() {
        String sql = "DELETE FROM atividades_recentes WHERE data_hora < DATE_SUB(NOW(), INTERVAL 30 DAY)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int deletados = stmt.executeUpdate();
            System.out.println(deletados + " atividades antigas removidas");
            
        } catch (SQLException e) {
            System.err.println("Erro ao limpar atividades: " + e.getMessage());
        }
    }
}
package com.example.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.model.PeriodoLetivo;
import com.example.util.DatabaseConnection;

/**
 * Data Access Object para a entidade PeriodoLetivo.
 */
public class PeriodoLetivoDAO {
    
    /**
     * Insere um novo período letivo no banco de dados.
     */
    public int salvar(PeriodoLetivo periodo) throws SQLException {
        String sql = "INSERT INTO periodoLetivo (nome_periodo, data_inicio, data_fim) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, periodo.getNome_periodo());
            stmt.setDate(2, Date.valueOf(periodo.getData_inicio()));
            stmt.setDate(3, Date.valueOf(periodo.getData_fim()));
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("✅ Período letivo salvo com ID: " + rs.getInt(1));
                return rs.getInt(1);
            }
            
            throw new SQLException("Erro ao obter ID do período letivo inserido");
        }
    }
    
    /**
     * Retorna TODOS os períodos letivos cadastrados.
     */
    public List<PeriodoLetivo> listarTodos() throws SQLException {
        String sql = "SELECT id_periodo_letivo, nome_periodo, data_inicio, data_fim FROM periodoLetivo ORDER BY data_inicio DESC";
        List<PeriodoLetivo> periodos = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                try {
                    PeriodoLetivo periodo = new PeriodoLetivo();
                    periodo.setId_periodo_letivo(rs.getInt("id_periodo_letivo"));
                    
                    // Proteção contra valores NULL
                    String nomePeriodo = rs.getString("nome_periodo");
                    periodo.setNome_periodo(nomePeriodo != null ? nomePeriodo : "Sem nome");
                    
                    // Proteção para datas NULL
                    Date dataInicio = rs.getDate("data_inicio");
                    if (dataInicio != null) {
                        periodo.setData_inicio(dataInicio.toLocalDate());
                    }
                    
                    Date dataFim = rs.getDate("data_fim");
                    if (dataFim != null) {
                        periodo.setData_fim(dataFim.toLocalDate());
                    }
                    
                    periodos.add(periodo);
                    System.out.println("📅 Período carregado: " + periodo.getNome_periodo());
                    
                } catch (Exception e) {
                    System.err.println("⚠️ Erro ao processar um período: " + e.getMessage());
                    e.printStackTrace();
                    // Continua para o próximo registro
                }
            }
            
            if (periodos.isEmpty()) {
                System.out.println("⚠️ Nenhum período letivo encontrado no banco!");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar períodos: " + e.getMessage());
            throw e;
        }
        
        return periodos;
    }
}
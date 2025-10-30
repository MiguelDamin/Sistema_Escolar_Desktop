package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Horario;
import com.example.util.DatabaseConnection;

/**
 * Data Access Object para a entidade Horario.
 */
public class HorarioDAO {
    
    /**
     * Insere um novo horário no banco de dados.
     */
    public int salvar(Horario horario) throws SQLException {
        String sql = "INSERT INTO horario (id_turma, id_professor, id_periodo_letivo, horario_inicio, horario_fim, dia_semana) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, horario.getId_turma());
            stmt.setInt(2, horario.getId_professor());
            stmt.setInt(3, horario.getId_periodo_letivo());
            stmt.setTime(4, Time.valueOf(horario.getHorario_inicio()));
            stmt.setTime(5, Time.valueOf(horario.getHorario_fim()));
            stmt.setString(6, horario.getDia_semana());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("✅ Horário salvo com ID: " + id);
                return id;
            }
            
            throw new SQLException("Erro ao obter ID do horário inserido");
        }
    }
    
    /**
     * Retorna TODOS os horários cadastrados.
     */
    public List<Horario> listarTodos() throws SQLException {
        String sql = "SELECT h.id_horario, h.id_turma, h.id_professor, h.id_periodo_letivo, " +
                     "h.horario_inicio, h.horario_fim, h.dia_semana, " +
                     "t.nome_turma, p.nome as nome_professor, pl.nome_periodo " +
                     "FROM horario h " +
                     "INNER JOIN turma t ON h.id_turma = t.id_turma " +
                     "INNER JOIN professor p ON h.id_professor = p.id_professor " +
                     "INNER JOIN periodoLetivo pl ON h.id_periodo_letivo = pl.id_periodo_letivo " +
                     "ORDER BY h.dia_semana, h.horario_inicio";
        
        List<Horario> horarios = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Horario horario = new Horario();
                horario.setId_horario(rs.getInt("id_horario"));
                horario.setId_turma(rs.getInt("id_turma"));
                horario.setId_professor(rs.getInt("id_professor"));
                horario.setId_periodo_letivo(rs.getInt("id_periodo_letivo"));
                
                Time inicio = rs.getTime("horario_inicio");
                if (inicio != null) {
                    horario.setHorario_inicio(inicio.toLocalTime());
                }
                
                Time fim = rs.getTime("horario_fim");
                if (fim != null) {
                    horario.setHorario_fim(fim.toLocalTime());
                }
                
                horario.setDia_semana(rs.getString("dia_semana"));
                
                horarios.add(horario);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar horários: " + e.getMessage());
            throw e;
        }
        
        return horarios;
    }
    
    /**
     * Deleta um horário pelo ID.
     */
    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM horario WHERE id_horario = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            
            return linhasAfetadas > 0;
        }
    }
}

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
 * 
 * ✅ ATUALIZADO: Agora suporta id_disciplina, sala e observacoes
 */
public class HorarioDAO {
    
    /**
     * Insere um novo horário no banco de dados.
     * ✅ ATUALIZADO para incluir disciplina, sala e observações
     */
    public int salvar(Horario horario) throws SQLException {
        String sql = "INSERT INTO horario (id_turma, id_professor, id_periodo_letivo, id_disciplina, " +
                     "horario_inicio, horario_fim, dia_semana, sala, observacoes, ativo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, horario.getId_turma());
            stmt.setInt(2, horario.getId_professor());
            stmt.setInt(3, horario.getId_periodo_letivo());
            stmt.setInt(4, horario.getId_disciplina());  // ✅ NOVO
            stmt.setTime(5, Time.valueOf(horario.getHorario_inicio()));
            stmt.setTime(6, Time.valueOf(horario.getHorario_fim()));
            stmt.setString(7, horario.getDia_semana());
            stmt.setString(8, horario.getSala());  // ✅ NOVO
            stmt.setString(9, horario.getObservacoes());  // ✅ NOVO
            
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
     * Retorna TODOS os horários cadastrados com JOIN nas tabelas relacionadas.
     * ✅ ATUALIZADO para incluir disciplina
     */
    public List<Horario> listarTodos() throws SQLException {
        String sql = "SELECT h.id_horario, h.id_turma, h.id_professor, h.id_periodo_letivo, h.id_disciplina, " +
                     "h.horario_inicio, h.horario_fim, h.dia_semana, h.sala, h.observacoes, " +
                     "t.nome_turma, p.nome as nome_professor, pl.nome_periodo, d.nome as nome_disciplina " +
                     "FROM horario h " +
                     "INNER JOIN turma t ON h.id_turma = t.id_turma " +
                     "INNER JOIN professor p ON h.id_professor = p.id_professor " +
                     "INNER JOIN periodoLetivo pl ON h.id_periodo_letivo = pl.id_periodo_letivo " +
                     "LEFT JOIN disciplinas d ON h.id_disciplina = d.id_disciplina " +
                     "WHERE h.ativo = 1 " +
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
                horario.setId_disciplina(rs.getInt("id_disciplina"));  // ✅ NOVO
                
                Time inicio = rs.getTime("horario_inicio");
                if (inicio != null) {
                    horario.setHorario_inicio(inicio.toLocalTime());
                }
                
                Time fim = rs.getTime("horario_fim");
                if (fim != null) {
                    horario.setHorario_fim(fim.toLocalTime());
                }
                
                horario.setDia_semana(rs.getString("dia_semana"));
                horario.setSala(rs.getString("sala"));  // ✅ NOVO
                horario.setObservacoes(rs.getString("observacoes"));  // ✅ NOVO
                
                horarios.add(horario);
            }
            
            System.out.println("✅ " + horarios.size() + " horários carregados!");
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar horários: " + e.getMessage());
            throw e;
        }
        
        return horarios;
    }
    
    /**
     * Deleta um horário pelo ID (soft delete - marca como inativo).
     */
    public boolean deletar(int id) throws SQLException {
        String sql = "UPDATE horario SET ativo = 0 WHERE id_horario = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            
            return linhasAfetadas > 0;
        }
    }
    
    /**
     * Verifica se há conflito de horário para o mesmo professor.
     * Retorna true se houver conflito.
     */
    public boolean verificarConflitoProfessor(int idProfessor, String diaSemana, 
                                             String horaInicio, String horaFim) throws SQLException {
        String sql = "SELECT COUNT(*) FROM horario " +
                     "WHERE id_professor = ? AND dia_semana = ? AND ativo = 1 " +
                     "AND ((horario_inicio < ? AND horario_fim > ?) " +
                     "OR (horario_inicio < ? AND horario_fim > ?) " +
                     "OR (horario_inicio >= ? AND horario_fim <= ?))";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProfessor);
            stmt.setString(2, diaSemana);
            stmt.setString(3, horaFim);
            stmt.setString(4, horaInicio);
            stmt.setString(5, horaFim);
            stmt.setString(6, horaFim);
            stmt.setString(7, horaInicio);
            stmt.setString(8, horaFim);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        
        return false;
    }
}
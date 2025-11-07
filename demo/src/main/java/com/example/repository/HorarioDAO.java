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

public class HorarioDAO {
    
    /**
     * Insere um novo horario no banco de dados.
     */
    public int salvar(Horario horario) throws SQLException {
        String sql = "INSERT INTO horario (id_turma, id_professor, id_periodo_letivo, id_disciplina, " +
                     "horario_inicio, horario_fim, dia_semana) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, horario.getId_turma());
            stmt.setInt(2, horario.getId_professor());
            stmt.setInt(3, horario.getId_periodo_letivo());
            stmt.setInt(4, horario.getId_disciplina());
            stmt.setTime(5, Time.valueOf(horario.getHorario_inicio()));
            stmt.setTime(6, Time.valueOf(horario.getHorario_fim()));
            stmt.setString(7, horario.getDia_semana());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Horario salvo com ID: " + id);
                return id;
            }
            
            throw new SQLException("Erro ao obter ID do horario inserido");
        }
    }
    
    /**
     * Retorna TODOS os horarios cadastrados com JOIN nas tabelas relacionadas.
     */
    public List<Horario> listarTodos() throws SQLException {
        String sql = "SELECT h.id_horario, h.id_turma, h.id_professor, h.id_periodo_letivo, h.id_disciplina, " +
                     "h.horario_inicio, h.horario_fim, h.dia_semana, " +
                     "t.nome_turma, p.nome as nome_professor, pl.nome_periodo, d.nome as nome_disciplina " +
                     "FROM horario h " +
                     "INNER JOIN turma t ON h.id_turma = t.id_turma " +
                     "INNER JOIN professor p ON h.id_professor = p.id_professor " +
                     "INNER JOIN periodoLetivo pl ON h.id_periodo_letivo = pl.id_periodo_letivo " +
                     "LEFT JOIN disciplinas d ON h.id_disciplina = d.id_disciplina " +
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
                horario.setId_disciplina(rs.getInt("id_disciplina"));
                
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
            
            System.out.println(horarios.size() + " horarios carregados!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar horarios: " + e.getMessage());
            throw e;
        }
        
        return horarios;
    }
    
    /**
     * Deleta um horario pelo ID (delete fisico).
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
    
    /**
     * Verifica se ha conflito de horario para o mesmo professor.
     * Retorna true se houver conflito.
     */
    public boolean verificarConflitoProfessor(int idProfessor, String diaSemana, 
                                             String horaInicio, String horaFim) throws SQLException {
        String sql = "SELECT COUNT(*) FROM horario " +
                     "WHERE id_professor = ? AND dia_semana = ? " +
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
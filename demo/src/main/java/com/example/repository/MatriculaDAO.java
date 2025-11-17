package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.util.DatabaseConnection;

/**
 * Data Access Object para a entidade Matricula.
 * Responsável por gerenciar a persistência de matrículas no banco de dados.
 */
public class MatriculaDAO {

    /**
     * Registra uma nova matrícula para um estudante em uma turma específica.
     *
     * @param idEstudante ID do estudante recém-cadastrado.
     * @param idTurma ID da turma selecionada.
     * @return O ID da matrícula gerado.
     * @throws SQLException se houver erro na comunicação com o banco.
     */
    public int salvar(int idEstudante, int idTurma) throws SQLException {
        String sql = "INSERT INTO matricula (id_estudante, id_turma) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idEstudante);
            stmt.setInt(2, idTurma);

            stmt.executeUpdate();

            // Recupera o ID da matrícula gerado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException("Erro ao obter ID da matrícula inserida.");
        }
    }
    /**
 * Atualizar turma da matrícula
 */
public boolean atualizarTurma(int idEstudante, int novaTurmaId) throws SQLException {
    String sql = "UPDATE matricula SET id_turma = ? WHERE id_estudante = ?";
    
    try (Connection conn = DatabaseConnection.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, novaTurmaId);
        stmt.setInt(2, idEstudante);
        
        int linhas = stmt.executeUpdate();
        return linhas > 0;
    }
}

/**
 * Buscar ID da turma atual do aluno
 */
    public int buscarTurmaAtual(int idEstudante) throws SQLException {
        String sql = "SELECT id_turma FROM matricula WHERE id_estudante = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEstudante);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id_turma");
            }
        }
        return -1; // Não encontrado
    }
}

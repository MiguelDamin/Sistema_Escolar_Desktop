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
     * Registra uma nova matrícula para um estudante.
     * 
     * CORREÇÃO: id_turma está sendo inserido com o valor '1' para contornar o erro
     * de "NOT NULL" na coluna, já que o usuário ainda não tem um cadastro de Turmas.
     * 
     * É CRUCIAL que o usuário crie uma turma com id_turma = 1 no banco de dados.
     * 
     * @param idEstudante ID do estudante recém-cadastrado.
     * @return O ID da matrícula gerado.
     * @throws SQLException se houver erro na comunicação com o banco.
     */
    public int salvar(int idEstudante) throws SQLException {
        // SQL: id_turma é inserido com o valor 1 para contornar o NOT NULL
        String sql = "INSERT INTO matricula (id_estudante, id_turma) VALUES (?, 1)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, idEstudante);
            
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
}

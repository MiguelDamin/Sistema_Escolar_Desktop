package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.model.Estudante;
import com.example.util.DatabaseConnection;

public class EstudanteDAO {
    
    // Adicionado método para buscar o ID do estudante pelo CPF (útil para verificar duplicidade)
    public int buscarIdPorCpf(String cpf) throws SQLException {
        String sql = "SELECT id_estudante FROM estudantes WHERE cpf = ?";
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_estudante");
                }
            }
        }
        return -1; // Retorna -1 se não encontrar
    }
    
    /**
     * Salva um novo estudante no banco de dados.
     * 
     * @param estudante O objeto Estudante a ser salvo.
     * @return O ID gerado do estudante inserido.
     * @throws SQLException se houver erro na comunicação com o banco.
     */
    public int salvar(Estudante estudante) throws SQLException {
        String sql = "INSERT INTO estudantes (nome, idade, cpf, email, telefone, data_nascimento, id_responsavel) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, estudante.getNome());
            stmt.setInt(2, estudante.getIdade());
            stmt.setString(3, estudante.getCpf());
            stmt.setString(4, estudante.getEmail());
            stmt.setString(5, estudante.getTelefone());
            stmt.setString(6, estudante.getData_nascimento());
            stmt.setInt(7, estudante.getId_responsavel());
            
            stmt.executeUpdate();
            
            // Recupera o ID gerado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
            throw new SQLException("Erro ao obter ID do estudante inserido.");
        }
    }
}

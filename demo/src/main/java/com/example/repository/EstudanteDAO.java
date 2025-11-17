package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.example.model.Estudante;
import com.example.model.Responsavel;
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
    public Estudante buscarPorId(int idEstudante) throws SQLException {
    String sql = "SELECT e.*, r.nome as nome_responsavel, r.cpf as cpf_responsavel, " +
                 "r.telefone as telefone_responsavel, r.email as email_responsavel, " +
                 "r.endereco as endereco_responsavel, r.parentesco " +
                 "FROM estudantes e " +
                 "INNER JOIN responsavel r ON e.id_responsavel = r.id_responsavel " +
                 "WHERE e.id_estudante = ?";
    
    try (Connection conn = DatabaseConnection.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, idEstudante);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            Estudante estudante = new Estudante();
            estudante.setIdEstudante(rs.getInt("id_estudante"));
            estudante.setNome(rs.getString("nome"));
            estudante.setIdade(rs.getInt("idade"));
            estudante.setCpf(rs.getString("cpf"));
            estudante.setEmail(rs.getString("email"));
            estudante.setTelefone(rs.getString("telefone"));
            estudante.setData_nascimento(rs.getString("data_nascimento"));
            estudante.setId_responsavel(rs.getInt("id_responsavel"));
            
            // Criar objeto Responsavel associado
            Responsavel responsavel = new Responsavel();
            responsavel.setIdResponsavel(rs.getInt("id_responsavel"));
            responsavel.setNome(rs.getString("nome_responsavel"));
            responsavel.setCpf(rs.getString("cpf_responsavel"));
            responsavel.setTelefone(rs.getString("telefone_responsavel"));
            responsavel.setEmail(rs.getString("email_responsavel"));
            responsavel.setEndereco(rs.getString("endereco_responsavel"));
            responsavel.setParentesco(rs.getString("parentesco"));
            
            estudante.setResponsavel(responsavel);
            
            return estudante;
        }
    }
    return null;
}

/**
 * 2. Atualizar dados do estudante
 */
public boolean atualizar(Estudante estudante) throws SQLException {
    String sql = "UPDATE estudantes SET nome = ?, idade = ?, cpf = ?, email = ?, " +
                 "telefone = ?, data_nascimento = ? WHERE id_estudante = ?";
    
    try (Connection conn = DatabaseConnection.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, estudante.getNome());
        stmt.setInt(2, estudante.getIdade());
        stmt.setString(3, estudante.getCpf());
        stmt.setString(4, estudante.getEmail());
        stmt.setString(5, estudante.getTelefone());
        stmt.setString(6, estudante.getData_nascimento());
        stmt.setInt(7, estudante.getIdEstudante());
        
        int linhas = stmt.executeUpdate();
        return linhas > 0;
    }
}

/**
 * 3. Listar todos os estudantes (para preencher a TableView)
 */
public List<Estudante> listarTodos() throws SQLException {
    String sql = "SELECT e.*, t.nome_turma " +
                 "FROM estudantes e " +
                 "LEFT JOIN matricula m ON e.id_estudante = m.id_estudante " +
                 "LEFT JOIN turma t ON m.id_turma = t.id_turma " +
                 "ORDER BY e.nome";
    
    List<Estudante> estudantes = new ArrayList<>();
    
    try (Connection conn = DatabaseConnection.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            Estudante e = new Estudante();
            e.setIdEstudante(rs.getInt("id_estudante"));
            e.setNome(rs.getString("nome"));
            e.setIdade(rs.getInt("idade"));
            e.setCpf(rs.getString("cpf"));
            // Adicionar nome da turma como propriedade temporária se necessário
            estudantes.add(e);
        }
    }
    
    return estudantes;
}
}

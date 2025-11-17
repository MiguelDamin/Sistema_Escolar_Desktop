package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Professor;
import com.example.util.DatabaseConnection;

public class ProfessorDAO {
    
    /**
     * Salva um novo professor no banco de dados.
     * Retorna true se salvou com sucesso.
     */
    public boolean salvar(Professor professor) {
        String sql = "INSERT INTO professor (nome, idade, cpf, email, telefone) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.conectar();
            if (conn == null) {
                System.out.println("❌ Falha na conexão com o banco");
                return false;
            }
            
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, professor.getNome());
            pstmt.setInt(2, professor.getIdade());
            pstmt.setString(3, professor.getCpf());
            pstmt.setString(4, professor.getEmail());
            pstmt.setString(5, professor.getTelefone());
            
            int linhas = pstmt.executeUpdate();
            
            if (linhas > 0) {
                // Recupera o ID gerado
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    professor.setIdProfessor(idGerado);
                    System.out.println("✅ Professor salvo com ID: " + idGerado);
                }
                return true;
            } else {
                System.out.println("⚠️ Nenhuma linha afetada");
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erro SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Atualiza os dados de um professor existente.
     */
    public boolean atualizar(Professor professor) throws SQLException {
        String sql = "UPDATE professor SET nome = ?, idade = ?, cpf = ?, email = ?, telefone = ? " +
                     "WHERE id_professor = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, professor.getNome());
            stmt.setInt(2, professor.getIdade());
            stmt.setString(3, professor.getCpf());
            stmt.setString(4, professor.getEmail());
            stmt.setString(5, professor.getTelefone());
            stmt.setInt(6, professor.getIdProfessor());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✅ Professor atualizado com sucesso! ID: " + professor.getIdProfessor());
                return true;
            } else {
                System.out.println("⚠️ Nenhum professor encontrado com o ID: " + professor.getIdProfessor());
                return false;
            }
        }
    }
    
    /**
     * Busca um professor pelo ID.
     */
    public Professor buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_professor, nome, idade, cpf, email, telefone FROM professor WHERE id_professor = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Professor professor = new Professor();
                professor.setIdProfessor(rs.getInt("id_professor"));
                professor.setNome(rs.getString("nome"));
                professor.setIdade(rs.getInt("idade"));
                professor.setCpf(rs.getString("cpf"));
                professor.setEmail(rs.getString("email"));
                professor.setTelefone(rs.getString("telefone"));
                
                System.out.println("✅ Professor encontrado: " + professor.getNome());
                return professor;
            }
            
            System.out.println("⚠️ Professor não encontrado com ID: " + id);
            return null;
        }
    }
    
    /**
     * Lista TODOS os professores cadastrados.
     */
    public List<Professor> listarTodos() throws SQLException {
        String sql = "SELECT id_professor, nome, idade, cpf, email, telefone FROM professor ORDER BY nome ASC";
        List<Professor> professores = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Professor professor = new Professor();
                professor.setIdProfessor(rs.getInt("id_professor"));
                professor.setNome(rs.getString("nome"));
                professor.setIdade(rs.getInt("idade"));
                professor.setCpf(rs.getString("cpf"));
                professor.setEmail(rs.getString("email"));
                professor.setTelefone(rs.getString("telefone"));
                
                professores.add(professor);
            }
            
            System.out.println("✅ " + professores.size() + " professores carregados!");
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar professores: " + e.getMessage());
            throw e;
        }
        
        return professores;
    }
    
    /**
     * Deleta um professor do banco de dados.
     * ATENÇÃO: Falhará se houver horários vinculados.
     */
    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM professor WHERE id_professor = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✅ Professor deletado com sucesso! ID: " + id);
                return true;
            } else {
                System.out.println("⚠️ Professor não encontrado! ID: " + id);
                return false;
            }
        } catch (SQLException e) {
            // Re-lança a exceção para ser tratada no controller
            System.err.println("❌ Erro ao deletar professor ID " + id + ": " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Verifica se o professor tem horários vinculados.
     */
    public boolean temHorariosVinculados(int idProfessor) throws SQLException {
        String sql = "SELECT COUNT(*) FROM horario WHERE id_professor = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProfessor);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("📊 Professor ID " + idProfessor + " possui " + count + " horário(s) vinculado(s)");
                return count > 0;
            }
            
            return false;
        }
    }
    
    /**
     * Deleta todos os horários de um professor (use com cuidado!).
     */
    public boolean deletarHorariosDoProfessor(int idProfessor) throws SQLException {
        String sql = "DELETE FROM horario WHERE id_professor = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProfessor);
            int linhasAfetadas = stmt.executeUpdate();
            
            System.out.println("🗑️ " + linhasAfetadas + " horário(s) deletado(s) do professor ID: " + idProfessor);
            return true;
        }
    }
    
    /**
     * Verifica se já existe um professor com o mesmo CPF.
     * Útil para evitar duplicatas no cadastro.
     */
    public boolean existePorCpf(String cpf) throws SQLException {
        String sql = "SELECT COUNT(*) FROM professor WHERE cpf = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            
            return false;
        }
    }
    
    /**
     * Verifica se existe outro professor com o mesmo CPF (exceto o próprio).
     * Útil na edição para evitar CPF duplicado.
     */
    public boolean existePorCpfExceto(String cpf, int idProfessor) throws SQLException {
        String sql = "SELECT COUNT(*) FROM professor WHERE cpf = ? AND id_professor != ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            stmt.setInt(2, idProfessor);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            
            return false;
        }
    }
}
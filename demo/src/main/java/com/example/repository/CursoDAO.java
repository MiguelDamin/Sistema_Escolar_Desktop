package com.example.repository;

import com.example.model.Curso;
import com.example.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para a entidade Curso
 * 
 * Responsabilidades:
 * - Persistir cursos no banco de dados
 * - Recuperar cursos do banco de dados
 * - Atualizar cursos existentes
 * - Remover cursos
 * 
 * @author Seu Nome
 * @version 1.0
 */
public class CursoDAO {
    
    /**
     * Insere um novo curso no banco de dados
     * 
     * Fluxo:
     * 1. Abre conexão com o banco
     * 2. Prepara o SQL de INSERT
     * 3. Substitui os placeholders (?) pelos valores
     * 4. Executa a query
     * 5. Retorna o ID gerado automaticamente
     * 
     * @param curso Objeto Curso com os dados a serem salvos
     * @return ID do curso inserido
     * @throws SQLException se houver erro na comunicação com o banco
     */
    public int salvar(Curso curso) throws SQLException {
        // SQL com placeholders (?) para evitar SQL Injection
        String sql = "INSERT INTO curso (nome, duracao_anos, descricao) VALUES (?, ?, ?)";
        
        // Try-with-resources: garante que conexão e statement serão fechados
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Substitui os placeholders pelos valores reais
            stmt.setString(1, curso.getNome());           // ? posição 1
            stmt.setInt(2, curso.getDuracao_anos());      // ? posição 2
            stmt.setString(3, curso.getDescricao());      // ? posição 3
            
            // Executa o INSERT
            stmt.executeUpdate();
            
            // Recupera o ID que o MySQL gerou (AUTO_INCREMENT)
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("✅ Curso salvo com ID: " + idGerado);
                return idGerado;
            }
            
            throw new SQLException("Erro ao obter ID do curso inserido");
        }
    }
    
    /**
     * Busca um curso específico pelo ID
     * 
     * @param id Identificador único do curso
     * @return Objeto Curso preenchido com os dados do banco, ou null se não encontrado
     * @throws SQLException se houver erro na comunicação com o banco
     */
    public Curso buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM curso WHERE id_curso = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            // Se encontrou o curso
            if (rs.next()) {
                Curso curso = new Curso();
                // Preenche o objeto com os dados do banco
                curso.setId_curso(rs.getInt("id_curso"));
                curso.setNome(rs.getString("nome"));
                curso.setDuracao_anos(rs.getInt("duracao_anos"));
                curso.setDescricao(rs.getString("descricao"));
                return curso;
            }
            
            return null;  // Não encontrou
        }
    }
    
    /**
     * Retorna TODOS os cursos cadastrados
     * 
     * Este método é essencial para preencher tabelas e ComboBoxes
     * 
     * @return Lista com todos os cursos (pode ser vazia)
     * @throws SQLException se houver erro na comunicação com o banco
     */
    public List<Curso> listarTodos() throws SQLException {
        String sql = "SELECT * FROM curso ORDER BY nome";
        List<Curso> cursos = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            // Itera sobre todos os resultados
            while (rs.next()) {
                Curso curso = new Curso();
                curso.setId_curso(rs.getInt("id_curso"));
                curso.setNome(rs.getString("nome"));
                curso.setDuracao_anos(rs.getInt("duracao_anos"));
                curso.setDescricao(rs.getString("descricao"));
                
                cursos.add(curso);  // Adiciona na lista
            }
        }
        
        return cursos;
    }
    
    /**
     * Atualiza um curso existente
     * 
     * Importante: O curso DEVE ter um ID válido
     * 
     * @param curso Objeto com os novos dados e ID do curso a ser atualizado
     * @throws SQLException se houver erro na comunicação com o banco
     */
    public void atualizar(Curso curso) throws SQLException {
        String sql = "UPDATE curso SET nome = ?, duracao_anos = ?, descricao = ? WHERE id_curso = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, curso.getNome());
            stmt.setInt(2, curso.getDuracao_anos());
            stmt.setString(3, curso.getDescricao());
            stmt.setInt(4, curso.getId_curso());  // WHERE (qual curso atualizar)
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✅ Curso atualizado com sucesso!");
            } else {
                System.out.println("⚠️ Nenhum curso encontrado com o ID: " + curso.getId_curso());
            }
        }
    }
    
    /**
     * Remove um curso do banco de dados
     * 
     * ATENÇÃO: Esta operação é irreversível!
     * Considere implementar "soft delete" (flag ativo/inativo) em produção
     * 
     * @param id ID do curso a ser removido
     * @return true se removeu com sucesso, false se não encontrou
     * @throws SQLException se houver erro na comunicação com o banco
     */
    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM curso WHERE id_curso = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✅ Curso removido com sucesso!");
                return true;
            } else {
                System.out.println("⚠️ Curso não encontrado!");
                return false;
            }
        }
    }
    
    /**
     * Verifica se já existe um curso com o mesmo nome
     * 
     * Útil para validações antes de inserir
     * 
     * @param nome Nome do curso a verificar
     * @return true se já existe, false caso contrário
     * @throws SQLException se houver erro na comunicação com o banco
     */
    public boolean existePorNome(String nome) throws SQLException {
        String sql = "SELECT COUNT(*) FROM curso WHERE nome = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            
            return false;
        }
    }
}

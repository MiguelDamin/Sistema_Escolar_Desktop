package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Certificado;
import com.example.util.DatabaseConnection;

public class CertificadoDAO {
    
    /**
     * Verifica se o aluno possui todas as 4 notas de bimestre para a disciplina
     */
    public boolean possuiTodasNotas(int idMatricula, int idDisciplina) {
        String sql = "SELECT COUNT(DISTINCT bimestre) as total FROM notas " +
                     "WHERE id_matricula = ? AND id_disciplina = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMatricula);
            stmt.setInt(2, idDisciplina);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("📊 Aluno possui " + total + " bimestres cadastrados");
                return total == 4;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao verificar notas: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Calcula a média final do aluno na disciplina
     */
    public double calcularMediaFinal(int idMatricula, int idDisciplina) {
        String sql = "SELECT AVG(nota_valor) as media FROM notas " +
                     "WHERE id_matricula = ? AND id_disciplina = ?";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMatricula);
            stmt.setInt(2, idDisciplina);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                double media = rs.getDouble("media");
                System.out.println("📊 Média calculada: " + media);
                return media;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao calcular média: " + e.getMessage());
        }
        return 0.0;
    }
    
    /**
     * Busca informações completas do aluno e disciplina para o certificado
     */
    public Certificado buscarDadosCertificado(int idMatricula, int idDisciplina) {
        String sql = "SELECT e.nome as nome_aluno, d.nome as nome_disciplina, " +
                     "t.nome_turma, AVG(n.nota_valor) as media " +
                     "FROM notas n " +
                     "INNER JOIN matricula m ON n.id_matricula = m.id_matricula " +
                     "INNER JOIN estudantes e ON m.id_estudante = e.id_estudante " +
                     "INNER JOIN disciplinas d ON n.id_disciplina = d.id_disciplina " +
                     "INNER JOIN turma t ON m.id_turma = t.id_turma " +
                     "WHERE n.id_matricula = ? AND n.id_disciplina = ? " +
                     "GROUP BY e.nome, d.nome, t.nome_turma";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMatricula);
            stmt.setInt(2, idDisciplina);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Certificado cert = new Certificado();
                cert.setIdMatricula(idMatricula);
                cert.setIdDisciplina(idDisciplina);
                cert.setNomeAluno(rs.getString("nome_aluno"));
                cert.setNomeDisciplina(rs.getString("nome_disciplina"));
                cert.setNomeTurma(rs.getString("nome_turma"));
                cert.setMediaFinal(rs.getDouble("media"));
                
                System.out.println("✅ Dados do certificado carregados!");
                return cert;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar dados: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Registra o certificado emitido no banco de dados
     */
    public boolean registrarCertificado(Certificado certificado) {
        String sql = "INSERT INTO certificados (id_matricula, id_disciplina, media_final, " +
                     "data_emissao, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, certificado.getIdMatricula());
            stmt.setInt(2, certificado.getIdDisciplina());
            stmt.setDouble(3, certificado.getMediaFinal());
            stmt.setDate(4, java.sql.Date.valueOf(certificado.getDataEmissao()));
            stmt.setString(5, certificado.getStatus());
            
            int rows = stmt.executeUpdate();
            System.out.println("✅ Certificado registrado no banco!");
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao registrar certificado: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Lista todos os certificados emitidos
     */
    public List<Certificado> listarTodos() {
        String sql = "SELECT c.*, e.nome as nome_aluno, d.nome as nome_disciplina " +
                     "FROM certificados c " +
                     "INNER JOIN matricula m ON c.id_matricula = m.id_matricula " +
                     "INNER JOIN estudantes e ON m.id_estudante = e.id_estudante " +
                     "INNER JOIN disciplinas d ON c.id_disciplina = d.id_disciplina " +
                     "ORDER BY c.data_emissao DESC";
        
        List<Certificado> certificados = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Certificado cert = new Certificado();
                cert.setIdCertificado(rs.getInt("id_certificado"));
                cert.setIdMatricula(rs.getInt("id_matricula"));
                cert.setIdDisciplina(rs.getInt("id_disciplina"));
                cert.setMediaFinal(rs.getDouble("media_final"));
                cert.setDataEmissao(rs.getDate("data_emissao").toLocalDate());
                cert.setStatus(rs.getString("status"));
                cert.setNomeAluno(rs.getString("nome_aluno"));
                cert.setNomeDisciplina(rs.getString("nome_disciplina"));
                
                certificados.add(cert);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar certificados: " + e.getMessage());
        }
        
        return certificados;
    }
}
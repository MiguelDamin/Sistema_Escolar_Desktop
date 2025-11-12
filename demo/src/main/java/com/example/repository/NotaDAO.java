package com.example.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Nota;
import com.example.util.DatabaseConnection;

public class NotaDAO {

    // ==================== INSERIR NOTA ====================
    public boolean inserir(Nota nota) {
        String sql = "INSERT INTO notas (id_matricula, id_disciplina, bimestre, nota_valor, data_lancamento) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, nota.getIdMatricula());
            stmt.setInt(2, nota.getIdDisciplina());
            stmt.setInt(3, nota.getBimestre());
            stmt.setDouble(4, nota.getNotaValor());
            stmt.setDate(5, Date.valueOf(nota.getDataLancamento()));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    nota.setIdNota(rs.getInt(1));
                }
                System.out.println("✅ Nota inserida com sucesso! ID: " + nota.getIdNota());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir nota: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ==================== ATUALIZAR NOTA ====================
    public boolean atualizar(Nota nota) {
        String sql = "UPDATE notas SET nota_valor = ?, data_lancamento = ? WHERE id_nota = ?";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, nota.getNotaValor());
            stmt.setDate(2, Date.valueOf(nota.getDataLancamento()));
            stmt.setInt(3, nota.getIdNota());

            int rows = stmt.executeUpdate();
            System.out.println("✅ Nota atualizada: " + rows + " linha(s)");
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erro ao atualizar nota: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ==================== LISTAR TODAS AS NOTAS ====================
    public List<Nota> listarTodas() {
        String sql = "SELECT n.id_nota, n.id_matricula, n.id_disciplina, n.bimestre, n.nota_valor, n.data_lancamento, " +
                     "e.nome AS nome_aluno, d.nome AS nome_disciplina " +
                     "FROM notas n " +
                     "INNER JOIN matricula m ON n.id_matricula = m.id_matricula " +
                     "INNER JOIN estudantes e ON m.id_estudante = e.id_estudante " +
                     "INNER JOIN disciplinas d ON n.id_disciplina = d.id_disciplina " +
                     "ORDER BY e.nome, d.nome, n.bimestre";

        List<Nota> notas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                notas.add(criarNotaDoResultSet(rs));
            }
            System.out.println("✅ " + notas.size() + " notas carregadas");

        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar notas: " + e.getMessage());
            e.printStackTrace();
        }

        return notas;
    }

    // ==================== BUSCAR NOTAS DE UM ALUNO EM UMA DISCIPLINA ====================
    public List<Nota> buscarPorAlunoEDisciplina(int idMatricula, int idDisciplina) {
        String sql = "SELECT n.id_nota, n.id_matricula, n.id_disciplina, n.bimestre, n.nota_valor, n.data_lancamento, " +
                     "e.nome AS nome_aluno, d.nome AS nome_disciplina " +
                     "FROM notas n " +
                     "INNER JOIN matricula m ON n.id_matricula = m.id_matricula " +
                     "INNER JOIN estudantes e ON m.id_estudante = e.id_estudante " +
                     "INNER JOIN disciplinas d ON n.id_disciplina = d.id_disciplina " +
                     "WHERE n.id_matricula = ? AND n.id_disciplina = ? " +
                     "ORDER BY n.bimestre";

        List<Nota> notas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMatricula);
            stmt.setInt(2, idDisciplina);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                notas.add(criarNotaDoResultSet(rs));
            }

            System.out.println("✅ " + notas.size() + " notas encontradas para aluno " + idMatricula);

        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar notas: " + e.getMessage());
            e.printStackTrace();
        }

        return notas;
    }

    // ==================== VERIFICAR SE JÁ EXISTE NOTA PARA O BIMESTRE ====================
    public boolean notaExiste(int idMatricula, int idDisciplina, int bimestre) {
        String sql = "SELECT COUNT(*) FROM notas WHERE id_matricula = ? AND id_disciplina = ? AND bimestre = ?";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMatricula);
            stmt.setInt(2, idDisciplina);
            stmt.setInt(3, bimestre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                boolean existe = rs.getInt(1) > 0;
                System.out.println("🔍 Nota existe? " + existe);
                return existe;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao verificar nota: " + e.getMessage());
        }
        return false;
    }

    // ==================== CALCULAR MÉDIA DE UM ALUNO EM UMA DISCIPLINA ====================
    public double calcularMedia(int idMatricula, int idDisciplina) {
        String sql = "SELECT AVG(nota_valor) AS media FROM notas WHERE id_matricula = ? AND id_disciplina = ?";

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

    // ==================== DELETAR NOTA ====================
    public boolean deletar(int idNota) {
        String sql = "DELETE FROM notas WHERE id_nota = ?";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idNota);
            int rows = stmt.executeUpdate();
            System.out.println("🗑️ Nota deletada: " + rows + " linha(s)");
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erro ao deletar nota: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ==================== HELPER: CRIAR NOTA DO RESULTSET ====================
    private Nota criarNotaDoResultSet(ResultSet rs) throws SQLException {
        Nota nota = new Nota(
            rs.getInt("id_nota"),
            rs.getInt("id_matricula"),
            rs.getInt("id_disciplina"),
            rs.getInt("bimestre"),
            rs.getDouble("nota_valor"),
            rs.getDate("data_lancamento").toLocalDate()
        );
        
        nota.setNomeAluno(rs.getString("nome_aluno"));
        nota.setNomeDisciplina(rs.getString("nome_disciplina"));
        
        return nota;
    }
}
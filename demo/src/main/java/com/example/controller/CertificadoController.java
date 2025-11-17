package com.example.controller;

import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.example.model.Certificado;
import com.example.repository.CertificadoDAO;
import com.example.service.CertificadoService;
import com.example.util.DatabaseConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class CertificadoController {
    
    @FXML private ComboBox<String> comboAluno;
    @FXML private ComboBox<String> comboDisciplina;
    @FXML private Label lblMediaAtual;
    @FXML private Label lblStatus;
    @FXML private Label lblBimestres;
    
    private CertificadoDAO certificadoDAO = new CertificadoDAO();
    private CertificadoService certificadoService = new CertificadoService();
    
    private Map<String, Integer> alunosMap = new HashMap<>();
    private Map<String, Integer> disciplinasMap = new HashMap<>();
    
    @FXML
    public void initialize() {
        System.out.println("🎓 CertificadoController inicializado!");
        
        carregarAlunos();
        carregarDisciplinas();
        configurarListeners();
    }
    
    /**
     * Carrega os alunos matriculados
     */
    private void carregarAlunos() {
        String sql = "SELECT DISTINCT m.id_matricula, e.nome " +
                     "FROM matricula m " +
                     "INNER JOIN estudantes e ON m.id_estudante = e.id_estudante " +
                     "ORDER BY e.nome";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            ObservableList<String> alunos = FXCollections.observableArrayList();
            
            while (rs.next()) {
                int idMatricula = rs.getInt("id_matricula");
                String nome = rs.getString("nome");
                alunos.add(nome);
                alunosMap.put(nome, idMatricula);
            }
            
            comboAluno.setItems(alunos);
            System.out.println("✅ " + alunos.size() + " alunos carregados!");
            
        } catch (Exception e) {
            mostrarErro("Erro ao carregar alunos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Carrega as disciplinas disponíveis
     */
    private void carregarDisciplinas() {
        String sql = "SELECT id_disciplina, nome FROM disciplinas ORDER BY nome";
        
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            ObservableList<String> disciplinas = FXCollections.observableArrayList();
            
            while (rs.next()) {
                int idDisciplina = rs.getInt("id_disciplina");
                String nome = rs.getString("nome");
                disciplinas.add(nome);
                disciplinasMap.put(nome, idDisciplina);
            }
            
            comboDisciplina.setItems(disciplinas);
            System.out.println("✅ " + disciplinas.size() + " disciplinas carregadas!");
            
        } catch (Exception e) {
            mostrarErro("Erro ao carregar disciplinas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Configura listeners para atualizar informações automaticamente
     */
    private void configurarListeners() {
        comboAluno.valueProperty().addListener((obs, old, novo) -> {
            if (novo != null && comboDisciplina.getValue() != null) {
                atualizarInformacoes();
            }
        });
        
        comboDisciplina.valueProperty().addListener((obs, old, novo) -> {
            if (novo != null && comboAluno.getValue() != null) {
                atualizarInformacoes();
            }
        });
    }
    
    /**
     * Atualiza as informações de média e status
     */
    private void atualizarInformacoes() {
        if (comboAluno.getValue() == null || comboDisciplina.getValue() == null) {
            return;
        }
        
        int idMatricula = alunosMap.get(comboAluno.getValue());
        int idDisciplina = disciplinasMap.get(comboDisciplina.getValue());
        
        // Verifica quantos bimestres tem
        boolean tem4Bimestres = certificadoDAO.possuiTodasNotas(idMatricula, idDisciplina);
        
        if (!tem4Bimestres) {
            lblBimestres.setText("⚠️ INCOMPLETO");
            lblBimestres.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
            lblMediaAtual.setText("--");
            lblStatus.setText("Sem todas as notas");
            lblStatus.setStyle("-fx-text-fill: #95a5a6;");
            return;
        }
        
        lblBimestres.setText("✅ 4 Bimestres");
        lblBimestres.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        
        // Calcula a média
        double media = certificadoDAO.calcularMediaFinal(idMatricula, idDisciplina);
        lblMediaAtual.setText(String.format("%.2f", media));
        
        // Define o status
        if (media >= 6.0) {
            lblStatus.setText("✅ APROVADO");
            lblStatus.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 16px;");
        } else {
            lblStatus.setText("❌ REPROVADO");
            lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 16px;");
        }
    }
    
    /**
     * Gera o certificado em PDF
     */
    @FXML
    private void gerarCertificado() {
        System.out.println("📄 Gerando certificado...");
        
        // Validações
        if (comboAluno.getValue() == null) {
            mostrarErro("Selecione um aluno!");
            return;
        }
        
        if (comboDisciplina.getValue() == null) {
            mostrarErro("Selecione uma disciplina!");
            return;
        }
        
        int idMatricula = alunosMap.get(comboAluno.getValue());
        int idDisciplina = disciplinasMap.get(comboDisciplina.getValue());
        
        // Verifica se tem todas as notas
        if (!certificadoDAO.possuiTodasNotas(idMatricula, idDisciplina)) {
            mostrarErro("❌ O aluno não possui as 4 notas de bimestre cadastradas!\n\n" +
                       "Para emitir o certificado, é necessário ter notas em todos os 4 bimestres.");
            return;
        }
        
        // Calcula a média
        double media = certificadoDAO.calcularMediaFinal(idMatricula, idDisciplina);
        
        // Verifica se foi aprovado
        if (media < 6.0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aluno Reprovado");
            alert.setHeaderText("Média insuficiente");
            alert.setContentText(String.format(
                "O aluno obteve média %.2f, que é inferior a 6.0.\n\n" +
                "Deseja gerar um certificado de participação mesmo assim?", media));
            
            if (alert.showAndWait().get() != javafx.scene.control.ButtonType.OK) {
                return;
            }
        }
        
        try {
            // Busca dados completos
            Certificado certificado = certificadoDAO.buscarDadosCertificado(idMatricula, idDisciplina);
            
            if (certificado == null) {
                mostrarErro("Erro ao buscar dados do certificado!");
                return;
            }
            
            // Gera o PDF
            String caminhoArquivo = certificadoService.gerarCertificadoPDF(certificado);
            
            // Registra no banco
            certificadoDAO.registrarCertificado(certificado);
            
            // Mostra sucesso
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Certificado Gerado");
            alert.setHeaderText("✅ Sucesso!");
            alert.setContentText("Certificado gerado com sucesso!\n\n" +
                                "Arquivo: " + caminhoArquivo + "\n\n" +
                                "Deseja abrir o PDF agora?");
            
            if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
                abrirPDF(caminhoArquivo);
            }
            
            limparCampos();
            
        } catch (Exception e) {
            mostrarErro("Erro ao gerar certificado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Abre o PDF gerado
     */
    private void abrirPDF(String caminho) {
        try {
            File arquivo = new File(caminho);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(arquivo);
            } else {
                System.out.println("Desktop não suportado. PDF salvo em: " + caminho);
            }
        } catch (Exception e) {
            System.err.println("Erro ao abrir PDF: " + e.getMessage());
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    @FXML
    private void limparCampos() {
        comboAluno.setValue(null);
        comboDisciplina.setValue(null);
        lblMediaAtual.setText("--");
        lblStatus.setText("--");
        lblStatus.setStyle("");
        lblBimestres.setText("--");
        lblBimestres.setStyle("");
    }
    
    /**
     * Exibe mensagem de erro
     */
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
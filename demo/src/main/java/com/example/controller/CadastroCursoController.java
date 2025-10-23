package com.example.controller;

import com.example.model.Curso;
import com.example.repository.CursoDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller para a tela de Cadastro de Cursos
 * 
 * Responsabilidades:
 * - Capturar eventos da interface (cliques, digitação)
 * - Validar dados antes de enviar ao DAO
 * - Exibir mensagens de sucesso/erro
 * 
 * Padrão: MVC (Model-View-Controller)
 * 
 * NOTA DE INTEGRAÇÃO: Este código foi adaptado para o FXML simplificado do usuário,
 * removendo a funcionalidade de listagem (TableView) e focando apenas no Cadastro (INSERT).
 * O método onSalvarCurso agora realiza apenas a inserção de novos cursos.
 */
public class CadastroCursoController {
    
    // ========== COMPONENTES DA INTERFACE (Mapeados do FXML do usuário) ==========
    
    @FXML private TextField txtNomeCurso;
    @FXML private TextField txtDuracaoCurso;
    @FXML private TextArea txtDescricaoCurso; // Corrigido para o nome do FXML
    @FXML private Label LabelCurso;          // Para mensagens de erro/validação
    @FXML private Label LabelConfirmacao;    // Para mensagens de sucesso
    
    // ========== CAMADA DE ACESSO A DADOS ==========
    
    private CursoDAO cursoDAO = new CursoDAO();
    
    /**
     * Método chamado automaticamente quando a tela é carregada
     */
    @FXML
    public void initialize() {
        System.out.println("🎬 Controller de Curso inicializado!");
        
        // Limitar duração a apenas números
        txtDuracaoCurso.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Regex: apenas dígitos
                txtDuracaoCurso.setText(oldValue);
            }
        });
        
        // Garante que as mensagens estejam ocultas ao iniciar
        LabelCurso.setVisible(false);
        LabelConfirmacao.setVisible(false);
    }
    
    /**
     * Método chamado quando o usuário clica em "Salvar"
     * 
     * Realiza a validação e a inserção de um novo curso.
     */
    @FXML
    private void onSalvarCurso() {
        // Limpa mensagens anteriores
        limparMensagens();
        
        try {
            // ========== VALIDAÇÕES ==========
            
            String nome = txtNomeCurso.getText().trim();
            String duracaoStr = txtDuracaoCurso.getText().trim();
            String descricao = txtDescricaoCurso.getText().trim();
            
            // Regra de negócio: Nome é obrigatório
            if (nome.isEmpty()) {
                mostrarErro("O nome do curso é obrigatório!");
                txtNomeCurso.requestFocus();
                return;
            }
            
            // Regra de negócio: Duração é obrigatória
            if (duracaoStr.isEmpty()) {
                mostrarErro("A duração do curso é obrigatória!");
                txtDuracaoCurso.requestFocus();
                return;
            }
            
            int duracao;
            try {
                duracao = Integer.parseInt(duracaoStr);
            } catch (NumberFormatException e) {
                mostrarErro("A duração deve ser um número inteiro válido!");
                txtDuracaoCurso.requestFocus();
                return;
            }
            
            // Regra de negócio: Duração deve ser positiva
            if (duracao <= 0) {
                mostrarErro("A duração deve ser maior que zero!");
                txtDuracaoCurso.requestFocus();
                return;
            }
            
            // Verifica se já existe curso com esse nome (evita duplicatas)
            // Assumindo que a regra de negócio do código Claude é importante
            if (cursoDAO.existePorNome(nome)) {
                mostrarErro("Já existe um curso com esse nome!");
                txtNomeCurso.requestFocus();
                return;
            }
            
            // ========== CRIAÇÃO DO OBJETO E INSERÇÃO ==========
            
            Curso novoCurso = new Curso(nome, duracao, descricao);
            
            // Chama DAO para salvar
            int id = cursoDAO.salvar(novoCurso);
            
            // Exibe sucesso e limpa o formulário
            mostrarSucesso("Curso cadastrado com sucesso! ID: " + id);
            onLimpar();
            
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Limpa todos os campos do formulário
     */
    @FXML
    private void onLimpar() {
        txtNomeCurso.clear();
        txtDuracaoCurso.clear();
        txtDescricaoCurso.clear();
        limparMensagens();
    }
    
    /**
     * Volta para a tela inicial
     */
    @FXML
    private void onVoltar(ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(
            getClass().getResource("/com/example/fxml/Telainicial.fxml")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        stage.setScene(scene);
        stage.show();
    }
    
    // ========== MÉTODOS AUXILIARES ==========
    
    /**
     * Limpa e oculta ambas as Labels de mensagem
     */
    private void limparMensagens() {
        LabelCurso.setText("");
        LabelCurso.setVisible(false);
        LabelConfirmacao.setText("");
        LabelConfirmacao.setVisible(false);
    }
    
    /**
     * Exibe mensagem de erro (usa LabelCurso)
     */
    private void mostrarErro(String mensagem) {
        limparMensagens();
        LabelCurso.setText("❌ " + mensagem);
        LabelCurso.setVisible(true);
    }
    
    /**
     * Exibe mensagem de sucesso (usa LabelConfirmacao)
     */
    private void mostrarSucesso(String mensagem) {
        limparMensagens();
        LabelConfirmacao.setText("✅ " + mensagem);
        LabelConfirmacao.setVisible(true);
    }
}

package com.example.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.model.AtividadeRecente;
import com.example.repository.AtividadeRecenteDAO;
import com.example.repository.DashboardDAO;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaInicialController {
    
    // ==================== LABELS DO DASHBOARD ====================
    @FXML private Label lblTotalAlunos;
    @FXML private Label lblPercentualAlunos;
    @FXML private Label lblTotalProfessores;
    @FXML private Label lblNovosProfessores;
    @FXML private Label lblTotalCursos;
    @FXML private Label lblStatusCursos;
    @FXML private Label lblTotalTurmas;
    @FXML private Label lblStatusTurmas;
    
    @FXML private VBox vboxAtividades;
    
    private AtividadeRecenteDAO atividadeDAO = new AtividadeRecenteDAO();
    private DashboardDAO dashboardDAO = new DashboardDAO();
    
    @FXML
    public void initialize() {
        System.out.println("🎬 TelaInicial carregada!");
        
        // Carrega estatísticas do Dashboard
        carregarEstatisticasDashboard();
        
        // Carrega atividades recentes
        carregarAtividadesRecentes();
    }
    
    /**
     * NOVO: Carrega as estatísticas do Dashboard de forma assíncrona
     */
    private void carregarEstatisticasDashboard() {
        Task<Map<String, Integer>> task = new Task<>() {
            @Override
            protected Map<String, Integer> call() {
                System.out.println("📊 Carregando estatísticas do Dashboard...");
                return dashboardDAO.getEstatisticasCompletas();
            }
        };
        
        task.setOnSucceeded(event -> {
            Map<String, Integer> stats = task.getValue();
            atualizarDashboardUI(stats);
        });
        
        task.setOnFailed(event -> {
            System.err.println("❌ Erro ao carregar estatísticas: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });
        
        new Thread(task).start();
    }
    
    /**
     * Atualiza os valores da UI com as estatísticas
     */
    private void atualizarDashboardUI(Map<String, Integer> stats) {
        Platform.runLater(() -> {
            try {
                // Total de Alunos
                int totalAlunos = stats.getOrDefault("totalAlunos", 0);
                updateLabel(lblTotalAlunos, String.valueOf(totalAlunos));
                
                // Percentual de crescimento (fictício 12%)
                double percentual = dashboardDAO.getPercentualCrescimentoAlunos();
                updateLabel(lblPercentualAlunos, String.format("+%.0f%% este mês", percentual));
                
                // Total de Professores
                int totalProfessores = stats.getOrDefault("totalProfessores", 0);
                updateLabel(lblTotalProfessores, String.valueOf(totalProfessores));
                
                // Novos Professores
                int novosProfessores = stats.getOrDefault("novosProfessores", 0);
                updateLabel(lblNovosProfessores, "+" + novosProfessores + " novos");
                
                // Total de Cursos
                int totalCursos = stats.getOrDefault("totalCursos", 0);
                updateLabel(lblTotalCursos, String.valueOf(totalCursos));
                updateLabel(lblStatusCursos, "Em andamento");
                
                // Total de Turmas
                int totalTurmas = stats.getOrDefault("totalTurmas", 0);
                updateLabel(lblTotalTurmas, String.valueOf(totalTurmas));
                
                // Turmas Ativas
                int turmasAtivas = stats.getOrDefault("turmasAtivas", 0);
                if (turmasAtivas > 0) {
                    updateLabel(lblStatusTurmas, turmasAtivas + " ativas no período");
                } else {
                    updateLabel(lblStatusTurmas, "Ativas no período");
                }
                
                System.out.println("✅ Dashboard atualizado com sucesso!");
                
            } catch (Exception e) {
                System.err.println("⚠️ Erro ao atualizar UI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Helper para atualizar labels com segurança
     */
    private void updateLabel(Label label, String text) {
        if (label != null) {
            label.setText(text);
        } else {
            System.err.println("⚠️ Label não encontrado no FXML!");
        }
    }
    
    // @FXML
    // private void onDashboard(MouseEvent event) {
    //     System.out.println("🔄 Recarregando Dashboard...");
    //     carregarEstatisticasDashboard();
    //     carregarAtividadesRecentes();
    // }
    
    // @FXML
    // private void onCadastrarProfessor(MouseEvent event) {
    //     try {
    //         carregarCenaComRedimensionamento("/com/example/fxml/CadastroProfessor.fxml", event);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    
    // @FXML
    // private void onCadastrarAluno(MouseEvent event) {
    //     try {
    //         carregarCenaComRedimensionamento("/com/example/fxml/CadastroAluno.fxml", event);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    
    // @FXML
    // private void onCadastrarTurma(MouseEvent event) {
    //     try {
    //         carregarCenaComRedimensionamento("/com/example/fxml/CadastroTurma.fxml", event);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    
    // @FXML
    // private void onCadastrarCurso(MouseEvent event) {
    //     try {
    //         carregarCenaComRedimensionamento("/com/example/fxml/CadastroCurso.fxml", event);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    
    // @FXML
    // private void onCadastrarDisciplina(MouseEvent event) {
    //     try {
    //         carregarCenaComRedimensionamento("/com/example/fxml/CadastroDisciplina.fxml", event);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    
    // @FXML
    // private void onPeriodoLetivo(MouseEvent event) {
    //     try {
    //         carregarCenaComRedimensionamento("/com/example/fxml/PeriodoLetivo.fxml", event);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    
    // @FXML
    // private void onCadastroHorario(MouseEvent event) {
    //     try {
    //         carregarCenaComRedimensionamento("/com/example/fxml/CadastroHorario.fxml", event);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    
    @FXML
    private void onCadastrarCurso2(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroCurso.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarProfessor2(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroProfessor.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarAluno2(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroAluno.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarTurma2(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroTurma.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarNotas(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroNota.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // @FXML
    // private void onCertificados(MouseEvent event) {
    //     System.out.println("📜 Certificados - Em desenvolvimento");
    // }
    
    /**
     * Carrega as ultimas atividades recentes do banco
     */
    private void carregarAtividadesRecentes() {
        if (vboxAtividades == null) {
            System.err.println("❌ vboxAtividades é NULL! Verifique o fx:id no FXML!");
            return;
        }
        
        Task<List<AtividadeRecente>> task = new Task<>() {
            @Override
            protected List<AtividadeRecente> call() {
                return atividadeDAO.listarRecentes(5);
            }
        };
        
        task.setOnSucceeded(event -> {
            List<AtividadeRecente> atividades = task.getValue();
            
            Platform.runLater(() -> {
                vboxAtividades.getChildren().clear();
                
                if (atividades.isEmpty()) {
                    Label lblVazio = new Label("Nenhuma atividade recente");
                    lblVazio.setStyle("-fx-text-fill: #737373; -fx-font-size: 14px;");
                    vboxAtividades.getChildren().add(lblVazio);
                    return;
                }
                
                for (AtividadeRecente atividade : atividades) {
                    HBox item = criarItemAtividade(atividade);
                    vboxAtividades.getChildren().add(item);
                }
                
                System.out.println("✅ " + atividades.size() + " atividades carregadas!");
            });
        });
        
        task.setOnFailed(event -> {
            System.err.println("❌ Erro ao carregar atividades: " + task.getException().getMessage());
        });
        
        new Thread(task).start();
    }
    
    /**
     * Cria um item visual de atividade COM FontAwesome
     */
    private HBox criarItemAtividade(AtividadeRecente atividade) {
        HBox hbox = new HBox(15);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setStyle("-fx-padding: 12; -fx-background-color: #18181b; -fx-background-radius: 8;");
        
        // ✅ ÍCONE COM FONTAWESOME
        FontAwesomeIconView icone = new FontAwesomeIconView(getIconeFontAwesome(atividade.getTipo()));
        icone.setSize("28");
        icone.setFill(javafx.scene.paint.Color.web(getCor(atividade.getTipo())));
        
        // Descrição
        VBox vboxTexto = new VBox(2);
        Label lblDescricao = new Label(atividade.getDescricao());
        lblDescricao.setStyle("-fx-font-weight: 600; -fx-text-fill: white; -fx-font-size: 14px;");
        
        Label lblTipo = new Label(atividade.getTipo());
        lblTipo.setStyle("-fx-text-fill: #737373; -fx-font-size: 11px; -fx-font-weight: 500;");
        
        vboxTexto.getChildren().addAll(lblDescricao, lblTipo);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Tempo
        Label lblTempo = new Label(atividade.getTempoDecorrido());
        lblTempo.setStyle("-fx-text-fill: #737373; -fx-font-size: 12px;");
        
        hbox.getChildren().addAll(icone, vboxTexto, spacer, lblTempo);
        
        return hbox;
    }
    
    /**
     * Retorna o ícone FontAwesome baseado no tipo
     */
    private FontAwesomeIcon getIconeFontAwesome(String tipo) {
        switch (tipo) {
            case "ALUNO":
                return FontAwesomeIcon.USER_PLUS;
            case "PROFESSOR":
                return FontAwesomeIcon.USER;
            case "CURSO":
                return FontAwesomeIcon.BOOK;
            case "TURMA":
                return FontAwesomeIcon.USERS;
            default:
                return FontAwesomeIcon.INFO_CIRCLE;
        }
    }
    
    /**
     * Retorna a cor baseada no tipo (em formato hexadecimal)
     */
    private String getCor(String tipo) {
        switch (tipo) {
            case "ALUNO":
                return "#4ade80";  // Verde
            case "PROFESSOR":
                return "#60a5fa";  // Azul
            case "CURSO":
                return "#fbbf24";  // Amarelo
            case "TURMA":
                return "#a78bfa";  // Roxo
            default:
                return "#ffffff";
        }
    }
    
    private void carregarCenaComRedimensionamento(String caminho, MouseEvent event) throws IOException {
        System.out.println("📂 Carregando: " + caminho);
        
        Parent novaCena = FXMLLoader.load(getClass().getResource(caminho));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        Scene scene = new Scene(novaCena);
        
        String css = getClass().getResource("/com/example/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        stage.setScene(scene);
        stage.sizeToScene();
        
        if (stage.getWidth() < 800) {
            stage.setWidth(1000);
        }
        if (stage.getHeight() < 600) {
            stage.setHeight(700);
        }
        
        stage.show();
        
        System.out.println("✅ Cena carregada: " + stage.getWidth() + "x" + stage.getHeight());
    }
    
    // @FXML
    // private void onVoltar(ActionEvent event) throws IOException {
    //     Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/register.fxml"));
    //     Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    //     Scene scene = new Scene(novaCena);
        
    //     String css = getClass().getResource("/com/example/css/styles.css").toExternalForm();
    //     scene.getStylesheets().add(css);
        
    //     stage.setScene(scene);
    //     stage.sizeToScene();
    //     stage.show();
    // }
}
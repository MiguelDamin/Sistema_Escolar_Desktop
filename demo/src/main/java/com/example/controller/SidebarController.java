package com.example.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SidebarController {
    
    @FXML private VBox sidebarRoot;
    @FXML private HBox menuDashboard;
    @FXML private HBox menuProfessores;
    @FXML private HBox menuAlunos;
    @FXML private HBox menuTurmas;
    @FXML private HBox menuCursos;
    @FXML private HBox menuDisciplinas;
    @FXML private HBox menuPeriodoLetivo;
    @FXML private HBox menuHorarios;
    @FXML private HBox menuNotas;
    @FXML private HBox menuCertificados;
    
    // Referência para o MainLayoutController
    private MainLayoutController mainController;
    
    public void setMainController(MainLayoutController controller) {
        this.mainController = controller;
        System.out.println("✅ MainController conectado ao Sidebar!");
    }
    
    @FXML
    public void initialize() {
        System.out.println("🎬 SidebarController inicializado!");
    }
    
    @FXML
    private void onDashboard(MouseEvent event) {
        System.out.println("📊 Navegando para Dashboard");
        if (mainController != null) {
            mainController.carregarPagina(
                "Telainicial.fxml",
                "Dashboard",
                "Visão geral do sistema",
                "DASHBOARD"
            );
            marcarMenuAtivo(menuDashboard);
        }
    }
    
    @FXML
    private void onProfessores(MouseEvent event) {
        System.out.println("👨‍🏫 Navegando para Professores");
        if (mainController != null) {
            mainController.carregarPagina(
                "CadastroProfessor.fxml",
                "Professores",
                "Cadastro de professores",
                "USER"
            );
            marcarMenuAtivo(menuProfessores);
        }
    }
    
    @FXML
    private void onAlunos(MouseEvent event) {
        System.out.println("👨‍🎓 Navegando para Alunos");
        if (mainController != null) {
            mainController.carregarPagina(
                "CadastroAluno.fxml",
                "Alunos",
                "Cadastro de alunos",
                "USERS"
            );
            marcarMenuAtivo(menuAlunos);
        }
    }
    
    @FXML
    private void onTurmas(MouseEvent event) {
        System.out.println("🏫 Navegando para Turmas");
        if (mainController != null) {
            mainController.carregarPagina(
                "CadastroTurma.fxml",
                "Turmas",
                "Cadastro de turmas",
                "BUILDING"
            );
            marcarMenuAtivo(menuTurmas);
        }
    }
    
    @FXML
    private void onCursos(MouseEvent event) {
        System.out.println("📚 Navegando para Cursos");
        if (mainController != null) {
            mainController.carregarPagina(
                "CadastroCurso.fxml",
                "Cursos",
                "Cadastro de cursos",
                "BOOK"
            );
            marcarMenuAtivo(menuCursos);
        }
    }
    
    @FXML
    private void onDisciplinas(MouseEvent event) {
        System.out.println("📖 Navegando para Disciplinas");
        if (mainController != null) {
            mainController.carregarPagina(
                "CadastroDisciplina.fxml",
                "Disciplinas",
                "Cadastro de disciplinas",
                "BOOKMARK"
            );
            marcarMenuAtivo(menuDisciplinas);
        }
    }
    
    @FXML
    private void onPeriodoLetivo(MouseEvent event) {
        System.out.println("📅 Navegando para Período Letivo");
        if (mainController != null) {
            mainController.carregarPagina(
                "PeriodoLetivo.fxml",
                "Período Letivo",
                "Gestão de períodos letivos",
                "CALENDAR"
            );
            marcarMenuAtivo(menuPeriodoLetivo);
        }
    }
    
    @FXML
    private void onHorarios(MouseEvent event) {
        System.out.println("🕐 Navegando para Horários");
        if (mainController != null) {
            mainController.carregarPagina(
                "CadastroHorario.fxml",
                "Horários",
                "Gestão de horários",
                "CLOCK_ALT"
            );
            marcarMenuAtivo(menuHorarios);
        }
    }
    
    @FXML
    private void onNotas(MouseEvent event) {
        System.out.println("📝 Navegando para Notas");
        if (mainController != null) {
            mainController.carregarPagina(
                "CadastroNota.fxml",
                "Notas",
                "Lançamento de notas",
                "EDIT"
            );
            marcarMenuAtivo(menuNotas);
        }
    }
    
    @FXML
    private void onCertificados(MouseEvent event) {
        System.out.println("🏆 Certificados - Em desenvolvimento");
        marcarMenuAtivo(menuCertificados);
    }
    
    @FXML
    private void onSair() {
        System.out.println("🚪 Saindo do sistema...");
        try {
            Parent loginScene = FXMLLoader.load(
                getClass().getResource("/com/example/fxml/register.fxml")
            );
            
            Stage stage = (Stage) sidebarRoot.getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(loginScene);
            
            String css = getClass().getResource("/com/example/css/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
            
        } catch (IOException e) {
            System.err.println("❌ Erro ao voltar para login: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Marca o item do menu como ativo
     */
    private void marcarMenuAtivo(HBox itemAtivo) {
        desmarcarTodos();
        
        if (itemAtivo != null && !itemAtivo.getStyleClass().contains("menu-item-active")) {
            itemAtivo.getStyleClass().add("menu-item-active");
        }
    }
    
    /**
     * Remove a marcação ativa de todos os itens
     */
    private void desmarcarTodos() {
        removerClasse(menuDashboard);
        removerClasse(menuProfessores);
        removerClasse(menuAlunos);
        removerClasse(menuTurmas);
        removerClasse(menuCursos);
        removerClasse(menuDisciplinas);
        removerClasse(menuPeriodoLetivo);
        removerClasse(menuHorarios);
        removerClasse(menuNotas);
        removerClasse(menuCertificados);
    }
    
    private void removerClasse(HBox item) {
        if (item != null) {
            item.getStyleClass().remove("menu-item-active");
        }
    }
}
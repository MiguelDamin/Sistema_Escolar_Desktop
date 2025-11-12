package com.example.controller;

import java.io.IOException;
import java.util.List;

import com.example.model.AtividadeRecente;
import com.example.repository.AtividadeRecenteDAO;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
    
    @FXML private VBox vboxAtividades;
    
    private AtividadeRecenteDAO atividadeDAO = new AtividadeRecenteDAO();
    
    @FXML
    public void initialize() {
        System.out.println("TelaInicial carregada!");
        carregarAtividadesRecentes();
    }
    
    @FXML
    private void onDashboard(MouseEvent event) {
        System.out.println("Dashboard clicado");
    }
    
    @FXML
    private void onCadastrarProfessor(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroProfessor.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarAluno(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroAluno.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarTurma(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroTurma.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarCurso(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroCurso.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastrarDisciplina(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroDisciplina.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onPeriodoLetivo(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/PeriodoLetivo.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onCadastroHorario(MouseEvent event) {
        try {
            carregarCenaComRedimensionamento("/com/example/fxml/CadastroHorario.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
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
    
    @FXML
    private void onCertificados(MouseEvent event) {
        System.out.println("Certificados - Em desenvolvimento");
    }
    
    /**
     * Carrega as ultimas atividades recentes do banco
     */
    private void carregarAtividadesRecentes() {
        if (vboxAtividades == null) {
            System.err.println("vboxAtividades e NULL! Adicione fx:id no FXML!");
            return;
        }
        
        List<AtividadeRecente> atividades = atividadeDAO.listarRecentes(5);
        
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
    }
    
    /**
     * Cria um item visual de atividade COM FontAwesome
     */
    private HBox criarItemAtividade(AtividadeRecente atividade) {
        HBox hbox = new HBox(15);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setStyle("-fx-padding: 12; -fx-background-color: #0a0a0a; -fx-background-radius: 8;");
        
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
        System.out.println("Carregando: " + caminho);
        
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
        
        System.out.println("Cena carregada: " + stage.getWidth() + "x" + stage.getHeight());
    }
    
    @FXML
    private void onVoltar(ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/register.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        
        String css = getClass().getResource("/com/example/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
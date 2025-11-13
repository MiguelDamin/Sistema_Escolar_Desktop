package com.example.controller;

import java.io.IOException;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class MainLayoutController {

    @FXML private HBox topbar;
    @FXML private HBox logoBox;
    @FXML private StackPane sidebarContainer;
    @FXML private StackPane contentArea;
    @FXML private Button btnToggleMenu;
    @FXML private FontAwesomeIconView iconToggleMenu;
    @FXML private FontAwesomeIconView iconTopBar;
    @FXML private Label lblTituloTopBar;
    @FXML private Label lblSubtituloTopBar;

    private Parent sidebarNode;
    private boolean sidebarVisivel = true;
    private final double SIDEBAR_WIDTH = 260d;
    private final Duration DURATION = Duration.millis(220);

    @FXML
    public void initialize() {
        try {
            carregarSidebar();

            // quando a Scene estiver disponível, aplicamos bindings responsivos
            if (topbar != null) {
                topbar.sceneProperty().addListener((obs, oldScene, newScene) -> {
                    if (newScene != null) applyResponsiveBindings(newScene);
                });
                if (topbar.getScene() != null) applyResponsiveBindings(topbar.getScene());
            }

            Platform.runLater(() -> carregarPagina("Telainicial.fxml", "Dashboard", "Visão geral do sistema", "DASHBOARD"));

            if (iconToggleMenu != null) try { iconToggleMenu.setIcon(FontAwesomeIcon.TIMES); } catch (Exception ignored) {}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyResponsiveBindings(Scene scene) {
        try {
            // sidebarContainer altura = scene.height - topbar.height (fica exatamente abaixo do topbar)
            sidebarContainer.prefHeightProperty().bind(scene.heightProperty().subtract(topbar.heightProperty()));
            sidebarContainer.maxHeightProperty().bind(scene.heightProperty().subtract(topbar.heightProperty()));
            // largura fixa do sidebar (pode manter gerenciado pelo BorderPane.left)
            sidebarContainer.setPrefWidth(SIDEBAR_WIDTH);
            sidebarContainer.setMinWidth(SIDEBAR_WIDTH);
        } catch (Exception ignored) {}
    }

    private void carregarSidebar() {
        if (sidebarContainer == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxml/components/Sidebar.fxml"));
            sidebarNode = loader.load();

            // passa referência ao main controller se o SidebarController expuser setMainController
            Object ctrl = loader.getController();
            if (ctrl != null) {
                try {
                    if (ctrl instanceof SidebarController) {
                        ((SidebarController) ctrl).setMainController(this);
                    } else {
                        try {
                            ctrl.getClass().getMethod("setMainController", MainLayoutController.class)
                                    .invoke(ctrl, this);
                        } catch (NoSuchMethodException ignored) { }
                    }
                } catch (Exception e) {
                    System.err.println("Aviso: não foi possível setMainController no sidebar: " + e.getMessage());
                }
            }

            // coloca sidebar dentro de ScrollPane (sem padding) para comportamento responsivo
            ScrollPane scroll = new ScrollPane();
            scroll.setContent(sidebarNode);
            scroll.setFitToWidth(true);
            scroll.setFitToHeight(true);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scroll.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

            sidebarContainer.getChildren().clear();
            sidebarContainer.getChildren().add(scroll);

            sidebarContainer.setVisible(true);
            sidebarContainer.setManaged(true);
        } catch (IOException e) {
            System.err.println("Erro ao carregar Sidebar: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void carregarPagina(String nomeFXML, String titulo, String subtitulo, String nomeIcone) {
        if (contentArea == null) return;
        try {
            Parent conteudo;
            try {
                conteudo = FXMLLoader.load(getClass().getResource("/com/example/fxml/" + nomeFXML));
            } catch (Exception ex) {
                conteudo = FXMLLoader.load(getClass().getResource("/com/example/fxml/pages/" + nomeFXML));
            }
            contentArea.getChildren().clear();
            contentArea.getChildren().add(conteudo);
            atualizarTopBar(titulo, subtitulo, nomeIcone);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void atualizarTopBar(String titulo, String subtitulo, String nomeIcone) {
        if (lblTituloTopBar != null) lblTituloTopBar.setText(titulo);
        if (lblSubtituloTopBar != null) lblSubtituloTopBar.setText(subtitulo);
        if (iconTopBar != null && nomeIcone != null) {
            try { iconTopBar.setIcon(FontAwesomeIcon.valueOf(nomeIcone)); } catch (Exception ignored) {}
        }
    }

    @FXML
    private void onToggleMenu() {
        if (sidebarContainer == null || sidebarNode == null || btnToggleMenu == null) return;

        // animação rápida no botão
        ScaleTransition st = new ScaleTransition(Duration.millis(120), btnToggleMenu);
        st.setFromX(1.0); st.setFromY(1.0);
        st.setToX(0.92); st.setToY(0.92);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();

        if (sidebarVisivel) {
            TranslateTransition tt = new TranslateTransition(DURATION, sidebarNode);
            tt.setToX(-SIDEBAR_WIDTH);
            tt.setOnFinished(evt -> {
                sidebarContainer.setManaged(false);
                sidebarContainer.setVisible(false);
                sidebarNode.setTranslateX(0);
            });
            tt.play();
            try { iconToggleMenu.setIcon(FontAwesomeIcon.BARS); } catch (Exception ignored) {}
            sidebarVisivel = false;
        } else {
            sidebarContainer.setManaged(true);
            sidebarContainer.setVisible(true);
            sidebarContainer.setPrefWidth(SIDEBAR_WIDTH);
            sidebarContainer.setMinWidth(SIDEBAR_WIDTH);
            sidebarNode.setTranslateX(-SIDEBAR_WIDTH);
            TranslateTransition tt = new TranslateTransition(DURATION, sidebarNode);
            tt.setToX(0);
            tt.play();
            try { iconToggleMenu.setIcon(FontAwesomeIcon.TIMES); } catch (Exception ignored) {}
            sidebarVisivel = true;
        }
    }
}
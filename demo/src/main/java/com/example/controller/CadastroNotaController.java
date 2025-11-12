package com.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.model.Nota;
import com.example.repository.NotaDAO;
import com.example.util.DatabaseConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CadastroNotaController {

    @FXML private ComboBox<String> comboAluno;
    @FXML private ComboBox<String> comboDisciplina;
    @FXML private ComboBox<Integer> comboBimestre;
    @FXML private TextField txtNota;
    @FXML private Label lblMediaAtual;
    @FXML private Label lblStatus;
    @FXML private Button btnSalvar;
    @FXML private TableView<Nota> tabelaNotas;
    @FXML private TableColumn<Nota, String> colAluno;
    @FXML private TableColumn<Nota, String> colDisciplina;
    @FXML private TableColumn<Nota, Integer> colBimestre;
    @FXML private TableColumn<Nota, Double> colNota;
    @FXML private TableColumn<Nota, String> colData;

    private NotaDAO notaDAO;
    private ObservableList<Nota> listaNotas;
    private Map<String, Integer> alunosMap;
    private Map<String, Integer> disciplinasMap;
    private Nota notaSelecionada;

    @FXML
    public void initialize() {
        System.out.println("🎬 Iniciando CadastroNotaController...");
        
        notaDAO = new NotaDAO();
        listaNotas = FXCollections.observableArrayList();
        alunosMap = new HashMap<>();
        disciplinasMap = new HashMap<>();

        configurarTabela();
        carregarAlunos();
        carregarDisciplinas();
        carregarBimestres();
        configurarListeners();
        
        tabelaNotas.setItems(listaNotas);
        System.out.println("✅ Controller inicializado!");
    }

    private void configurarTabela() {
        colAluno.setCellValueFactory(new PropertyValueFactory<>("nomeAluno"));
        colDisciplina.setCellValueFactory(new PropertyValueFactory<>("nomeDisciplina"));
        colBimestre.setCellValueFactory(new PropertyValueFactory<>("bimestre"));
        colNota.setCellValueFactory(new PropertyValueFactory<>("notaValor"));
        
        colData.setCellValueFactory(cellData -> {
            LocalDate data = cellData.getValue().getDataLancamento();
            return new javafx.beans.property.SimpleStringProperty(
                data != null ? data.toString() : ""
            );
        });

        tabelaNotas.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    notaSelecionada = newSelection;
                    preencherCampos(newSelection);
                }
            }
        );
    }

    private void carregarAlunos() {
        String sql = "SELECT m.id_matricula, e.nome FROM matricula m INNER JOIN estudantes e ON m.id_estudante = e.id_estudante ORDER BY e.nome";

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
            System.out.println("✅ " + alunos.size() + " alunos carregados");

        } catch (SQLException e) {
            mostrarErro("Erro ao carregar alunos: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
            System.out.println("✅ " + disciplinas.size() + " disciplinas carregadas");

        } catch (SQLException e) {
            mostrarErro("Erro ao carregar disciplinas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarBimestres() {
        comboBimestre.setItems(FXCollections.observableArrayList(1, 2, 3, 4));
    }

    private void configurarListeners() {
        comboAluno.valueProperty().addListener((obs, old, novo) -> {
            if (novo != null && comboDisciplina.getValue() != null) {
                atualizarMedia();
            }
        });

        comboDisciplina.valueProperty().addListener((obs, old, novo) -> {
            if (novo != null && comboAluno.getValue() != null) {
                atualizarMedia();
            }
        });
    }

    private void atualizarMedia() {
        if (comboAluno.getValue() == null || comboDisciplina.getValue() == null) {
            lblMediaAtual.setText("0.00");
            lblStatus.setText("-");
            return;
        }

        int idMatricula = alunosMap.get(comboAluno.getValue());
        int idDisciplina = disciplinasMap.get(comboDisciplina.getValue());

        double media = notaDAO.calcularMedia(idMatricula, idDisciplina);
        lblMediaAtual.setText(String.format("%.2f", media));

        if (media >= 7.0) {
            lblStatus.setText("APROVADO");
            lblStatus.setStyle("-fx-text-fill: #4caf50; -fx-font-weight: bold;");
        } else if (media >= 5.0) {
            lblStatus.setText("RECUPERAÇÃO");
            lblStatus.setStyle("-fx-text-fill: #ff9800; -fx-font-weight: bold;");
        } else if (media > 0) {
            lblStatus.setText("REPROVADO");
            lblStatus.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
        } else {
            lblStatus.setText("SEM NOTAS");
            lblStatus.setStyle("-fx-text-fill: #737373;");
        }
    }

    @FXML
    private void salvarNota() {
        System.out.println("🔵 Botão Salvar clicado");
        
        if (!validarCampos()) {
            return;
        }

        try {
            int idMatricula = alunosMap.get(comboAluno.getValue());
            int idDisciplina = disciplinasMap.get(comboDisciplina.getValue());
            int bimestre = comboBimestre.getValue();
            double notaValor = Double.parseDouble(txtNota.getText());

            System.out.println("📝 Dados: Matricula=" + idMatricula + ", Disciplina=" + idDisciplina + ", Bimestre=" + bimestre + ", Nota=" + notaValor);

            if (notaSelecionada == null && notaDAO.notaExiste(idMatricula, idDisciplina, bimestre)) {
                mostrarErro("Já existe uma nota cadastrada para este aluno nesta disciplina no " + bimestre + "º bimestre!");
                return;
            }

            Nota nota = notaSelecionada != null ? notaSelecionada : new Nota();
            nota.setIdMatricula(idMatricula);
            nota.setIdDisciplina(idDisciplina);
            nota.setBimestre(bimestre);
            nota.setNotaValor(notaValor);
            nota.setDataLancamento(LocalDate.now());

            boolean sucesso;
            if (notaSelecionada == null) {
                sucesso = notaDAO.inserir(nota);
            } else {
                sucesso = notaDAO.atualizar(nota);
            }

            if (sucesso) {
                mostrarSucesso(notaSelecionada == null ? 
                    "Nota do " + bimestre + "º bimestre cadastrada com sucesso!" : 
                    "Nota atualizada com sucesso!");
                limparCampos();
                atualizarMedia();
                
                if (!listaNotas.isEmpty()) {
                    listarNotasDoAluno();
                }
            } else {
                mostrarErro("Erro ao salvar nota!");
            }

        } catch (NumberFormatException e) {
            mostrarErro("A nota deve ser um número válido!");
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        if (comboAluno.getValue() == null) {
            mostrarErro("Selecione um aluno!");
            return false;
        }
        if (comboDisciplina.getValue() == null) {
            mostrarErro("Selecione uma disciplina!");
            return false;
        }
        if (comboBimestre.getValue() == null) {
            mostrarErro("Selecione o bimestre!");
            return false;
        }
        if (txtNota.getText().isEmpty()) {
            mostrarErro("Digite a nota!");
            return false;
        }

        try {
            double nota = Double.parseDouble(txtNota.getText());
            if (nota < 0 || nota > 10) {
                mostrarErro("A nota deve estar entre 0 e 10!");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarErro("A nota deve ser um número válido!");
            return false;
        }

        return true;
    }

    private void preencherCampos(Nota nota) {
        comboAluno.setValue(nota.getNomeAluno());
        comboDisciplina.setValue(nota.getNomeDisciplina());
        comboBimestre.setValue(nota.getBimestre());
        txtNota.setText(String.valueOf(nota.getNotaValor()));
        btnSalvar.setText("Atualizar Nota");
    }

    @FXML
    private void limparCampos() {
        comboAluno.setValue(null);
        comboDisciplina.setValue(null);
        comboBimestre.setValue(null);
        txtNota.clear();
        lblMediaAtual.setText("0.00");
        lblStatus.setText("-");
        lblStatus.setStyle("");
        notaSelecionada = null;
        btnSalvar.setText("Salvar Nota");
        tabelaNotas.getSelectionModel().clearSelection();
    }

    @FXML
    private void listarNotasDoAluno() {
        if (comboAluno.getValue() == null || comboDisciplina.getValue() == null) {
            mostrarErro("Selecione um aluno e uma disciplina para listar as notas!");
            return;
        }

        int idMatricula = alunosMap.get(comboAluno.getValue());
        int idDisciplina = disciplinasMap.get(comboDisciplina.getValue());

        listaNotas.clear();
        List<Nota> notas = notaDAO.buscarPorAlunoEDisciplina(idMatricula, idDisciplina);
        
        if (notas.isEmpty()) {
            mostrarAviso("Nenhuma nota encontrada para este aluno nesta disciplina.");
        } else {
            listaNotas.addAll(notas);
            mostrarSucesso(notas.size() + " nota(s) encontrada(s)!");
        }
    }

    @FXML
    private void listarTodasNotas() {
        listaNotas.clear();
        List<Nota> notas = notaDAO.listarTodas();
        
        if (notas.isEmpty()) {
            mostrarAviso("Nenhuma nota cadastrada no sistema.");
        } else {
            listaNotas.addAll(notas);
            mostrarSucesso(notas.size() + " nota(s) carregada(s)!");
        }
    }

    @FXML
    private void deletarNota() {
        Nota notaSelecionada = tabelaNotas.getSelectionModel().getSelectedItem();
        if (notaSelecionada == null) {
            mostrarErro("Selecione uma nota na tabela para deletar!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente deletar esta nota?");
        confirmacao.setContentText(
            "Aluno: " + notaSelecionada.getNomeAluno() + 
            "\nDisciplina: " + notaSelecionada.getNomeDisciplina() +
            "\nBimestre: " + notaSelecionada.getBimestre() + "º" +
            "\nNota: " + notaSelecionada.getNotaValor()
        );

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            if (notaDAO.deletar(notaSelecionada.getIdNota())) {
                mostrarSucesso("Nota deletada com sucesso!");
                listarNotasDoAluno();
                limparCampos();
                atualizarMedia();
            } else {
                mostrarErro("Erro ao deletar nota!");
            }
        }
    }

    @FXML
    private void onVoltar(ActionEvent event) throws IOException {
        Parent novaCena = FXMLLoader.load(getClass().getResource("/com/example/fxml/Telainicial.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaCena);
        
        String css = getClass().getResource("/com/example/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        stage.setScene(scene);
        stage.show();
    }

    private void mostrarErro(String mensagem) {
        System.err.println("❌ " + mensagem);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        System.out.println("✅ " + mensagem);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAviso(String mensagem) {
        System.out.println("⚠️ " + mensagem);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;

import com.example.model.Disciplina;
import com.example.model.Horario;
import com.example.model.PeriodoLetivo;
import com.example.model.Professor;
import com.example.model.Turma;
import com.example.repository.DisciplinaDAO;
import com.example.repository.HorarioDAO;
import com.example.repository.PeriodoLetivoDAO;
import com.example.repository.ProfessorDAO;
import com.example.repository.TurmaDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroHorarioController {
    
    @FXML private ComboBox<Turma> cbTurma;
    @FXML private ComboBox<Professor> cbProfessor;
    @FXML private ComboBox<PeriodoLetivo> cbPeriodoLetivo;
    @FXML private ComboBox<Disciplina> cbDisciplina;
    @FXML private ComboBox<String> cbDiaSemana;
    @FXML private TextField txtHorarioInicio;
    @FXML private TextField txtHorarioFim;
    @FXML private Label lblMensagemErro;
    @FXML private Label lblMensagemSucesso;
    
    private HorarioDAO horarioDAO = new HorarioDAO();
    private TurmaDAO turmaDAO = new TurmaDAO();
    private ProfessorDAO professorDAO = new ProfessorDAO();
    private PeriodoLetivoDAO periodoDAO = new PeriodoLetivoDAO();
    private DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    
    private ObservableList<Turma> turmasDisponiveis = FXCollections.observableArrayList();
    private ObservableList<Professor> professoresDisponiveis = FXCollections.observableArrayList();
    private ObservableList<PeriodoLetivo> periodosDisponiveis = FXCollections.observableArrayList();
    private ObservableList<Disciplina> disciplinasDisponiveis = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        System.out.println("Controller de Horario inicializado!");
        
        carregarTurmas();
        carregarProfessores();
        carregarPeriodosLetivos();
        carregarDisciplinas();
        
        cbDiaSemana.setItems(FXCollections.observableArrayList(
            "Segunda-feira", "Terca-feira", "Quarta-feira", 
            "Quinta-feira", "Sexta-feira", "Sabado"
        ));
        
        limparMensagens();
    }
    
    private void carregarTurmas() {
        try {
            turmasDisponiveis.clear();
            turmasDisponiveis.addAll(turmaDAO.listarTodos());
            cbTurma.setItems(turmasDisponiveis);
            System.out.println(turmasDisponiveis.size() + " turmas carregadas!");
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar turmas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void carregarProfessores() {
        try {
            professoresDisponiveis.clear();
            professoresDisponiveis.addAll(professorDAO.listarTodos());
            cbProfessor.setItems(professoresDisponiveis);
            System.out.println(professoresDisponiveis.size() + " professores carregados!");
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar professores: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void carregarPeriodosLetivos() {
        try {
            periodosDisponiveis.clear();
            periodosDisponiveis.addAll(periodoDAO.listarTodos());
            cbPeriodoLetivo.setItems(periodosDisponiveis);
            System.out.println(periodosDisponiveis.size() + " periodos carregados!");
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar periodos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void carregarDisciplinas() {
        try {
            disciplinasDisponiveis.clear();
            disciplinasDisponiveis.addAll(disciplinaDAO.listarTodos());
            cbDisciplina.setItems(disciplinasDisponiveis);
            System.out.println(disciplinasDisponiveis.size() + " disciplinas carregadas!");
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar disciplinas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onSalvarHorario() {
        limparMensagens();
        
        try {
            Turma turmaSelecionada = cbTurma.getValue();
            Professor professorSelecionado = cbProfessor.getValue();
            PeriodoLetivo periodoSelecionado = cbPeriodoLetivo.getValue();
            Disciplina disciplinaSelecionada = cbDisciplina.getValue();
            String diaSemana = cbDiaSemana.getValue();
            String horaInicioStr = txtHorarioInicio.getText().trim();
            String horaFimStr = txtHorarioFim.getText().trim();
            
            if (turmaSelecionada == null) {
                mostrarErro("Selecione uma turma!");
                cbTurma.requestFocus();
                return;
            }
            
            if (professorSelecionado == null) {
                mostrarErro("Selecione um professor!");
                cbProfessor.requestFocus();
                return;
            }
            
            if (periodoSelecionado == null) {
                mostrarErro("Selecione um periodo letivo!");
                cbPeriodoLetivo.requestFocus();
                return;
            }
            
            if (disciplinaSelecionada == null) {
                mostrarErro("Selecione uma disciplina!");
                cbDisciplina.requestFocus();
                return;
            }
            
            if (diaSemana == null || diaSemana.isEmpty()) {
                mostrarErro("Selecione o dia da semana!");
                cbDiaSemana.requestFocus();
                return;
            }
            
            if (horaInicioStr.isEmpty()) {
                mostrarErro("Digite o horario de inicio (Ex: 08:00)!");
                txtHorarioInicio.requestFocus();
                return;
            }
            
            if (horaFimStr.isEmpty()) {
                mostrarErro("Digite o horario de fim (Ex: 10:00)!");
                txtHorarioFim.requestFocus();
                return;
            }
            
            LocalTime horaInicio;
            LocalTime horaFim;
            
            try {
                horaInicio = LocalTime.parse(horaInicioStr);
                horaFim = LocalTime.parse(horaFimStr);
            } catch (Exception e) {
                mostrarErro("Formato de horario invalido! Use HH:MM (Ex: 08:00)");
                return;
            }
            
            if (horaFim.isBefore(horaInicio)) {
                mostrarErro("O horario de fim deve ser apos o horario de inicio!");
                txtHorarioFim.requestFocus();
                return;
            }
            
            Horario novoHorario = new Horario(
            turmaSelecionada.getId_turma() ,           // sem underscore
            professorSelecionado.getIdProfessor(),   
            periodoSelecionado.getId_periodo_letivo(), // sem underscore
            disciplinaSelecionada.getId_disciplina(), // sem underscore
            horaInicio,
            horaFim,
            diaSemana
        );
            
            int id = horarioDAO.salvar(novoHorario);
            
            mostrarSucesso("Horario cadastrado com sucesso! ID: " + id);
            onLimpar();
            
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar horario: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            mostrarErro("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onLimpar() {
        if (cbTurma != null) cbTurma.setValue(null);
        if (cbProfessor != null) cbProfessor.setValue(null);
        if (cbPeriodoLetivo != null) cbPeriodoLetivo.setValue(null);
        if (cbDisciplina != null) cbDisciplina.setValue(null);
        if (cbDiaSemana != null) cbDiaSemana.setValue(null);
        if (txtHorarioInicio != null) txtHorarioInicio.clear();
        if (txtHorarioFim != null) txtHorarioFim.clear();
        limparMensagens();
    }
    
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
    
    private void limparMensagens() {
        if (lblMensagemErro != null) {
            lblMensagemErro.setText("");
            lblMensagemErro.setVisible(false);
        }
        if (lblMensagemSucesso != null) {
            lblMensagemSucesso.setText("");
            lblMensagemSucesso.setVisible(false);
        }
    }
    
    private void mostrarErro(String mensagem) {
        limparMensagens();
        if (lblMensagemErro != null) {
            lblMensagemErro.setText(mensagem);
            lblMensagemErro.setVisible(true);
        }
    }
    
    private void mostrarSucesso(String mensagem) {
        limparMensagens();
        if (lblMensagemSucesso != null) {
            lblMensagemSucesso.setText(mensagem);
            lblMensagemSucesso.setVisible(true);
        }
    }
}
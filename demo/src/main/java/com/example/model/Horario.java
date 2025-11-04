package com.example.model;

import java.time.LocalTime;

/**
 * Model para a entidade Horario.
 * Representa os horários das aulas no sistema.
 * 
 * ✅ ATUALIZADO: Agora inclui id_disciplina, sala e observacoes
 */
public class Horario {
    private int id_horario;
    private int id_turma;
    private int id_professor;
    private int id_periodo_letivo;
    private int id_disciplina;  // ✅ NOVO CAMPO
    private LocalTime horario_inicio;
    private LocalTime horario_fim;
    private String dia_semana;
    private String sala;  // ✅ NOVO CAMPO
    private String observacoes;  // ✅ NOVO CAMPO
    
    // Objetos relacionados (opcional, para exibição)
    private Turma turma;
    private Professor professor;
    private PeriodoLetivo periodoLetivo;
    private Disciplina disciplina;  // ✅ NOVO
    
    // Construtor vazio
    public Horario() {}
    
    // Construtor para cadastro (sem id) - ATUALIZADO
    public Horario(int id_turma, int id_professor, int id_periodo_letivo, int id_disciplina,
                   LocalTime horario_inicio, LocalTime horario_fim, String dia_semana, 
                   String sala, String observacoes) {
        this.id_turma = id_turma;
        this.id_professor = id_professor;
        this.id_periodo_letivo = id_periodo_letivo;
        this.id_disciplina = id_disciplina;
        this.horario_inicio = horario_inicio;
        this.horario_fim = horario_fim;
        this.dia_semana = dia_semana;
        this.sala = sala;
        this.observacoes = observacoes;
    }

    // ========== GETTERS ==========
    
    public int getId_horario() {
        return id_horario;
    }

    public int getId_turma() {
        return id_turma;
    }

    public int getId_professor() {
        return id_professor;
    }

    public int getId_periodo_letivo() {
        return id_periodo_letivo;
    }

    public int getId_disciplina() {
        return id_disciplina;
    }

    public LocalTime getHorario_inicio() {
        return horario_inicio;
    }

    public LocalTime getHorario_fim() {
        return horario_fim;
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public String getSala() {
        return sala;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public Turma getTurma() {
        return turma;
    }

    public Professor getProfessor() {
        return professor;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    // ========== SETTERS ==========
    
    public void setId_horario(int id_horario) {
        this.id_horario = id_horario;
    }

    public void setId_turma(int id_turma) {
        this.id_turma = id_turma;
    }

    public void setId_professor(int id_professor) {
        this.id_professor = id_professor;
    }

    public void setId_periodo_letivo(int id_periodo_letivo) {
        this.id_periodo_letivo = id_periodo_letivo;
    }

    public void setId_disciplina(int id_disciplina) {
        this.id_disciplina = id_disciplina;
    }

    public void setHorario_inicio(LocalTime horario_inicio) {
        this.horario_inicio = horario_inicio;
    }

    public void setHorario_fim(LocalTime horario_fim) {
        this.horario_fim = horario_fim;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void setPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    @Override
    public String toString() {
        String disciplinaNome = (disciplina != null) ? disciplina.getNome() : "Sem disciplina";
        return dia_semana + " - " + horario_inicio + " às " + horario_fim + " (" + disciplinaNome + ")";
    }
}
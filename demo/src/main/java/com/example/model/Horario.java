package com.example.model;

import java.time.LocalTime;

/**
 * Model para a entidade Horario.
 * Representa os horários das aulas no sistema.
 */
public class Horario {
    private int id_horario;
    private int id_turma;
    private int id_professor;
    private int id_periodo_letivo;
    private LocalTime horario_inicio;
    private LocalTime horario_fim;
    private String dia_semana;
    
    // Objetos relacionados (opcional, para exibição)
    private Turma turma;
    private Professor professor;
    private PeriodoLetivo periodoLetivo;
    
    // Construtor vazio
    public Horario() {}
    
    // Construtor para cadastro (sem id)
    public Horario(int id_turma, int id_professor, int id_periodo_letivo, 
                   LocalTime horario_inicio, LocalTime horario_fim, String dia_semana) {
        this.id_turma = id_turma;
        this.id_professor = id_professor;
        this.id_periodo_letivo = id_periodo_letivo;
        this.horario_inicio = horario_inicio;
        this.horario_fim = horario_fim;
        this.dia_semana = dia_semana;
    }

    // Getters
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

    public LocalTime getHorario_inicio() {
        return horario_inicio;
    }

    public LocalTime getHorario_fim() {
        return horario_fim;
    }

    public String getDia_semana() {
        return dia_semana;
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

    // Setters
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

    public void setHorario_inicio(LocalTime horario_inicio) {
        this.horario_inicio = horario_inicio;
    }

    public void setHorario_fim(LocalTime horario_fim) {
        this.horario_fim = horario_fim;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
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

    @Override
    public String toString() {
        return dia_semana + " - " + horario_inicio + " às " + horario_fim;
    }
}

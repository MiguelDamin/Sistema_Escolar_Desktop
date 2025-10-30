package com.example.model;

/**
 * Model para a entidade Turma.
 * Representa as turmas cadastradas no sistema.
 */
public class Turma {
    private int id_turma;
    private String nome;
    private int id_periodo_letivo;
    private int id_grade;
    
    // Construtor vazio
    public Turma() {}
    
    // Construtor para cadastro (sem id)
    public Turma(String nome, int id_periodo_letivo) {
        this.nome = nome;
        this.id_periodo_letivo = id_periodo_letivo;
    }

    // Getters
    public int getId_turma() {
        return id_turma;
    }

    public String getNome() {
        return nome;
    }

    public int getId_periodo_letivo() {
        return id_periodo_letivo;
    }

    public int getId_grade() {
        return id_grade;
    }

    // Setters
    public void setId_turma(int id_turma) {
        this.id_turma = id_turma;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId_periodo_letivo(int id_periodo_letivo) {
        this.id_periodo_letivo = id_periodo_letivo;
    }

    public void setId_grade(int id_grade) {
        this.id_grade = id_grade;
    }

    // Método para exibição em ComboBox
    @Override
    public String toString() {
        return nome;
    }
}
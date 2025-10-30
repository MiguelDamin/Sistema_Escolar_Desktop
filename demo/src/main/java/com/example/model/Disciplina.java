package com.example.model;

/**
 * Model para a entidade Disciplina.
 * Representa as disciplinas/matérias do sistema.
 */
public class Disciplina {
    private int id_disciplina;
    private String nome;
    private int carga_horaria;
    private String descricao;
    
    // Construtor vazio
    public Disciplina() {}
    
    // Construtor para cadastro (sem id)
    public Disciplina(String nome, int carga_horaria, String descricao) {
        this.nome = nome;
        this.carga_horaria = carga_horaria;
        this.descricao = descricao;
    }

    // Getters
    public int getId_disciplina() {
        return id_disciplina;
    }

    public String getNome() {
        return nome;
    }

    public int getCarga_horaria() {
        return carga_horaria;
    }

    public String getDescricao() {
        return descricao;
    }

    // Setters
    public void setId_disciplina(int id_disciplina) {
        this.id_disciplina = id_disciplina;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCarga_horaria(int carga_horaria) {
        this.carga_horaria = carga_horaria;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Método para exibição em ComboBox
    @Override
    public String toString() {
        return nome + " (" + carga_horaria + "h)";
    }
}
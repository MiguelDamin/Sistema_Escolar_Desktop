package com.example.model;

/**
 * Model para a entidade Turma.
 */
public class Turma {
    private int id_turma;
    private String nome;
    private int ano;
    
    // Construtor vazio
    public Turma() {}
    
    // Construtor para cadastro (sem id)
    public Turma(String nome, int ano) {
        this.nome = nome;
        this.ano = ano;
    }

    // Getters
    public int getId_turma() {
        return id_turma;
    }

    public String getNome() {
        return nome;
    }

    public int getAno() {
        return ano;
    }

    // Setters
    public void setId_turma(int id_turma) {
        this.id_turma = id_turma;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    // Método para exibição em ComboBox/TableView
    @Override
    public String toString() {
        return nome + " (" + ano + ")";
    }
}

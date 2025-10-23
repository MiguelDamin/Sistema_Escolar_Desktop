package com.example.model;


public class Curso {
    private int id_curso;
    private String nome;
    private int duracao_anos;
    private String descricao;
    
    // Construtor vazio
    public Curso() {}
    
    // Construtor com parâmetros
    public Curso(String nome, int duracao_anos, String descricao) {
        this.nome = nome;
        this.duracao_anos = duracao_anos;
        this.descricao = descricao;
    }

    public int getId_curso() {return id_curso;}
    public String getNome() {return nome;}
    public int getDuracao_anos() {return duracao_anos;}
    public String getDescricao() {return descricao;}

    public void setId_curso(int id_curso) {
        this.id_curso = id_curso;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setDuracao_anos(int duracao_anos) {
        this.duracao_anos = duracao_anos;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}





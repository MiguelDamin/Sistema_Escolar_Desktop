package com.example.model;

public class Responsavel {
    private int id_responsavel;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String endereco;
    private String parentesco;

    public Responsavel() {}

    public Responsavel(String nome, String cpf, String telefone, String email, String endereco, String parentesco) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.parentesco = parentesco;
    }

    public int getIdResponsavel() { return id_responsavel; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public String getEndereco() { return endereco; }
    public String getParentesco() { return parentesco; }

    public void setIdResponsavel(int id_responsavel) { this.id_responsavel = id_responsavel; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEmail(String email) { this.email = email; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setParentesco(String parentesco) { this.parentesco = parentesco; }
}


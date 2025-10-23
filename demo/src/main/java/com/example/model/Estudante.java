package com.example.model;

public class Estudante {
    private int id_estudante;
    private String nome;
    private int idade;
    private String cpf;
    private String email;
    private String telefone;
    private String data_nascimento;
    private int id_responsavel;

    private Responsavel responsavel; // associação direta opcional

    public Estudante() {}

    public Estudante(String nome, int idade, String cpf, String email, String telefone, String data_nascimento, int id_responsavel) {
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.data_nascimento = data_nascimento;
        this.id_responsavel = id_responsavel;
    }

    public int getIdEstudante() { return id_estudante; }
    public String getNome() { return nome; }
    public int getIdade() { return idade; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getData_nascimento() { return data_nascimento; }
    public int getId_responsavel() { return id_responsavel; }

    public void setIdEstudante(int id_estudante) { this.id_estudante = id_estudante; }
    public void setNome(String nome) { this.nome = nome; }
    public void setIdade(int idade) { this.idade = idade; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setData_nascimento(String data_nascimento) { this.data_nascimento = data_nascimento; }
    public void setId_responsavel(int id_responsavel) { this.id_responsavel = id_responsavel; }
    public void setResponsavel(Responsavel responsavel) { this.responsavel = responsavel; }
    public Responsavel getResponsavel() { return responsavel; }
}

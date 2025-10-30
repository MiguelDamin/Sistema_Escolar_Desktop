package com.example.model;

public class Professor {
    private int id_professor;
    private String nome;
    private int idade;
    private String cpf;
    private String email;
    private String telefone;
    
    public Professor() {}
    
    public Professor(String nome, int idade, String cpf, String email, String telefone) {
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }
    
    // Getters
    public int getIdProfessor() { 
        return id_professor; 
    }
    
    public String getNome() { 
        return nome; 
    }
    
    public int getIdade() { 
        return idade; 
    }
    
    public String getCpf() { 
        return cpf; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public String getTelefone() { 
        return telefone; 
    }
    
    // Setters
    public void setIdProfessor(int idProfessor) { 
        this.id_professor = idProfessor; 
    }
    
    public void setNome(String nome) { 
        this.nome = nome; 
    }
    
    public void setIdade(int idade) { 
        this.idade = idade; 
    }
    
    public void setCpf(String cpf) { 
        this.cpf = cpf; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public void setTelefone(String telefone) { 
        this.telefone = telefone; 
    }
    
    // Para exibição no ComboBox
    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }
}
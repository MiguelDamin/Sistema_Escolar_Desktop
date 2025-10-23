package com.example.model;


public class Professor {
    // VARIÁVEIS = CAIXINHAS PARA GUARDAR DADOS
    private int id_professor;      // ID do professor (número)
    private String nome;           // Nome do professor
    private int idade;             // Idade do professor
    private String cpf;            // CPF do professor
    private String email;          // Email do professor
    private String telefone;       // Telefone do professor
    
    // CONSTRUTOR VAZIO = Criar professor sem dados
    public Professor() {}
    
    // CONSTRUTOR COMPLETO = Criar professor já com dados
    public Professor(String nome, int idade, String cpf, String email, String telefone) {
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }
    
    // GETTERS = PEGAR VALOR DE DENTRO DA CAIXINHA
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
    
    // SETTERS = COLOCAR VALOR DENTRO DA CAIXINHA
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
    
}

package com.example.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class AtividadeRecente {
    private int id_atividade;
    private String tipo; // "ALUNO", "PROFESSOR", "CURSO", "TURMA"
    private String descricao;
    private LocalDateTime data_hora;
    
    public AtividadeRecente() {}
    
    public AtividadeRecente(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.data_hora = LocalDateTime.now();
    }
    
    // Getters
    public int getId_atividade() {
        return id_atividade;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public LocalDateTime getData_hora() {
        return data_hora;
    }
    
    // Setters
    public void setId_atividade(int id_atividade) {
        this.id_atividade = id_atividade;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public void setData_hora(LocalDateTime data_hora) {
        this.data_hora = data_hora;
    }
    
    /**
     * Retorna tempo decorrido em formato legivel
     */
    public String getTempoDecorrido() {
        Duration duracao = Duration.between(data_hora, LocalDateTime.now());
        
        long minutos = duracao.toMinutes();
        long horas = duracao.toHours();
        long dias = duracao.toDays();
        
        if (minutos < 60) {
            return minutos == 1 ? "1 minuto atras" : minutos + " minutos atras";
        } else if (horas < 24) {
            return horas == 1 ? "1 hora atras" : horas + " horas atras";
        } else {
            return dias == 1 ? "1 dia atras" : dias + " dias atras";
        }
    }
    
    /**
     * Retorna o icone baseado no tipo
     */
    public String getIcone() {
        switch (tipo) {
            case "ALUNO": return "USER_PLUS";
            case "PROFESSOR": return "CHALKBOARD_TEACHER";
            case "CURSO": return "BOOK";
            case "TURMA": return "USERS";
            default: return "INFO_CIRCLE";
        }
    }
    
    /**
     * Retorna a cor baseada no tipo
     */
    public String getCor() {
        switch (tipo) {
            case "ALUNO": return "#4ade80";      // Verde
            case "PROFESSOR": return "#60a5fa";  // Azul
            case "CURSO": return "#fbbf24";      // Amarelo
            case "TURMA": return "#a78bfa";      // Roxo
            default: return "#ffffff";
        }
    }
}
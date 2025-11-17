package com.example.model;

import java.time.LocalDate;

public class Certificado {
    private int idCertificado;
    private int idMatricula;
    private int idDisciplina;
    private double mediaFinal;
    private LocalDate dataEmissao;
    private String status; // "APROVADO" ou "REPROVADO"
    
    // Campos auxiliares para exibição
    private String nomeAluno;
    private String nomeDisciplina;
    private String nomeTurma;
    
    public Certificado() {
        this.dataEmissao = LocalDate.now();
    }
    
    public Certificado(int idMatricula, int idDisciplina, double mediaFinal) {
        this.idMatricula = idMatricula;
        this.idDisciplina = idDisciplina;
        this.mediaFinal = mediaFinal;
        this.dataEmissao = LocalDate.now();
        this.status = mediaFinal >= 6.0 ? "APROVADO" : "REPROVADO";
    }
    
    // ========== GETTERS E SETTERS ==========
    
    public int getIdCertificado() {
        return idCertificado;
    }
    
    public void setIdCertificado(int idCertificado) {
        this.idCertificado = idCertificado;
    }
    
    public int getIdMatricula() {
        return idMatricula;
    }
    
    public void setIdMatricula(int idMatricula) {
        this.idMatricula = idMatricula;
    }
    
    public int getIdDisciplina() {
        return idDisciplina;
    }
    
    public void setIdDisciplina(int idDisciplina) {
        this.idDisciplina = idDisciplina;
    }
    
    public double getMediaFinal() {
        return mediaFinal;
    }
    
    public void setMediaFinal(double mediaFinal) {
        this.mediaFinal = mediaFinal;
        this.status = mediaFinal >= 6.0 ? "APROVADO" : "REPROVADO";
    }
    
    public LocalDate getDataEmissao() {
        return dataEmissao;
    }
    
    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNomeAluno() {
        return nomeAluno;
    }
    
    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }
    
    public String getNomeDisciplina() {
        return nomeDisciplina;
    }
    
    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }
    
    public String getNomeTurma() {
        return nomeTurma;
    }
    
    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }
    
    public boolean isAprovado() {
        return mediaFinal >= 6.0;
    }
}
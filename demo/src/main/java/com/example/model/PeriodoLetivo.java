package com.example.model;

import java.time.LocalDate;

/**
 * Model para a entidade PeriodoLetivo.
 * Representa os períodos letivos do sistema.
 */
public class PeriodoLetivo {
    private int id_periodo_letivo;
    private String nome_periodo;
    private LocalDate data_inicio;
    private LocalDate data_fim;
    
    // Construtor vazio
    public PeriodoLetivo() {}
    
    // Construtor para cadastro (sem id)
    public PeriodoLetivo(String nome_periodo, LocalDate data_inicio, LocalDate data_fim) {
        this.nome_periodo = nome_periodo;
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
    }

    // Getters
    public int getId_periodo_letivo() {
        return id_periodo_letivo;
    }

    public String getNome_periodo() {
        return nome_periodo;
    }

    public LocalDate getData_inicio() {
        return data_inicio;
    }

    public LocalDate getData_fim() {
        return data_fim;
    }

    // Setters
    public void setId_periodo_letivo(int id_periodo_letivo) {
        this.id_periodo_letivo = id_periodo_letivo;
    }

    public void setNome_periodo(String nome_periodo) {
        this.nome_periodo = nome_periodo;
    }

    public void setData_inicio(LocalDate data_inicio) {
        this.data_inicio = data_inicio;
    }

    public void setData_fim(LocalDate data_fim) {
        this.data_fim = data_fim;
    }

    // Método para exibição em ComboBox
    @Override
    public String toString() {
        return nome_periodo + " (" + data_inicio + " a " + data_fim + ")";
    }
}
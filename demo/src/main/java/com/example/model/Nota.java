package com.example.model;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Nota {
    private final IntegerProperty idNota;
    private final IntegerProperty idMatricula;
    private final IntegerProperty idDisciplina;
    private final IntegerProperty bimestre;
    private final DoubleProperty notaValor;
    private final ObjectProperty<LocalDate> dataLancamento;
    
    // Campos auxiliares para exibição
    private final StringProperty nomeAluno;
    private final StringProperty nomeDisciplina;

    public Nota() {
        this(0, 0, 0, 1, 0.0, LocalDate.now());
    }

    public Nota(int idNota, int idMatricula, int idDisciplina, 
                int bimestre, double notaValor, LocalDate dataLancamento) {
        this.idNota = new SimpleIntegerProperty(idNota);
        this.idMatricula = new SimpleIntegerProperty(idMatricula);
        this.idDisciplina = new SimpleIntegerProperty(idDisciplina);
        this.bimestre = new SimpleIntegerProperty(bimestre);
        this.notaValor = new SimpleDoubleProperty(notaValor);
        this.dataLancamento = new SimpleObjectProperty<>(dataLancamento);
        this.nomeAluno = new SimpleStringProperty("");
        this.nomeDisciplina = new SimpleStringProperty("");
    }

    // Getters e Setters
    public int getIdNota() { return idNota.get(); }
    public void setIdNota(int value) { idNota.set(value); }
    
    public int getIdMatricula() { return idMatricula.get(); }
    public void setIdMatricula(int value) { idMatricula.set(value); }
    
    public int getIdDisciplina() { return idDisciplina.get(); }
    public void setIdDisciplina(int value) { idDisciplina.set(value); }
    
    public int getBimestre() { return bimestre.get(); }
    public void setBimestre(int value) { bimestre.set(value); }
    
    public double getNotaValor() { return notaValor.get(); }
    public void setNotaValor(double value) { notaValor.set(value); }
    
    public LocalDate getDataLancamento() { return dataLancamento.get(); }
    public void setDataLancamento(LocalDate value) { dataLancamento.set(value); }
    
    public String getNomeAluno() { return nomeAluno.get(); }
    public void setNomeAluno(String value) { nomeAluno.set(value); }
    
    public String getNomeDisciplina() { return nomeDisciplina.get(); }
    public void setNomeDisciplina(String value) { nomeDisciplina.set(value); }
    
    // Properties para TableView
    public IntegerProperty bimestreProperty() { return bimestre; }
    public DoubleProperty notaValorProperty() { return notaValor; }
    public StringProperty nomeAlunoProperty() { return nomeAluno; }
    public StringProperty nomeDisciplinaProperty() { return nomeDisciplina; }
}
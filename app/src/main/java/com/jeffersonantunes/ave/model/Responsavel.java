package com.jeffersonantunes.ave.model;

import com.google.firebase.database.Exclude;

public class Responsavel {

    private String idUsuario;
    private String nome_filho;
    private int matricula;

    public Responsavel() {
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome_filho() {
        return nome_filho;
    }

    public void setNome_filho(String nome_filho) {
        this.nome_filho = nome_filho;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
}

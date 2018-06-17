package com.jeffersonantunes.ave.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.jeffersonantunes.ave.config.ConfigFirebase;

public class Professor {

    private int matricula;
    private String nome;
    private String disciplina;

    public Professor() {
    }

    public void salvar(){
        try {
            DatabaseReference databaseReference = ConfigFirebase.getDbAveReference();
            databaseReference.child("professor").child(String.valueOf(getMatricula())).setValue(Professor.this);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("ERROR:Professor:","salvar:" + e.getMessage());
        }

    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }
}

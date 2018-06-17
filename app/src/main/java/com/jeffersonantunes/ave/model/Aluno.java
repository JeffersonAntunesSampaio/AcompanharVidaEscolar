package com.jeffersonantunes.ave.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.jeffersonantunes.ave.config.ConfigFirebase;

public class Aluno {

    private String nome;
    private String turma;
    private int matricula;
    private boolean marcado;



    public Aluno() {
    }

    @Override
    public String toString() {
        return this.matricula + " - " + this.nome;
    }

    public void salvar(){
        try {
            DatabaseReference databaseReference = ConfigFirebase.getDbAveReference();
            databaseReference.child("aluno").child(String.valueOf(getMatricula())).setValue(Aluno.this);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("ERROR:Aluno:","salvar:" + e.getMessage());
        }

    }

    @Exclude
    public boolean getMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
}

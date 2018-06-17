package com.jeffersonantunes.ave.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.jeffersonantunes.ave.config.ConfigFirebase;

import java.util.Date;

public class Aula {

    private Date date;
    private String data;
    private String presente;
    private int matriculaAluno;

    public Aula() {
    }

    public void salvar(){
        try {
            DatabaseReference databaseReference = ConfigFirebase.getDbAveReference();
            databaseReference.child("aula").child(data).child(String.valueOf(getMatriculaAluno())).setValue(Aula.this);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("ERROR:Aula:","salvar:" + e.getMessage());
        }

    }

    @Override
    public String toString() {
        return "matriculaAluno=" + matriculaAluno;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPresente() {
        return presente;
    }

    public void setPresente(String presente) {
        this.presente = presente;
    }

    public int getMatriculaAluno() {
        return matriculaAluno;
    }

    public void setMatriculaAluno(int matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }
}

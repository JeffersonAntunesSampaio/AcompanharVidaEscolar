package com.jeffersonantunes.ave.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.jeffersonantunes.ave.config.ConfigFirebase;

public class Nota {

    private int matricula;
    private int matriculaProfessor;
    private int nota;
    private String disciplina;

    public Nota() {
    }

    public void salvar(){
        try {
            DatabaseReference databaseReference = ConfigFirebase.getDbAveReference();
            databaseReference.child("nota").child(String.valueOf(getMatricula()))
                    .child(getDisciplina()).setValue(Nota.this);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("ERROR:Nota:","salvar:" + e.getMessage());
        }

    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getMatriculaProfessor() {
        return matriculaProfessor;
    }

    public void setMatriculaProfessor(int matriculaProfessor) {
        this.matriculaProfessor = matriculaProfessor;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }
}

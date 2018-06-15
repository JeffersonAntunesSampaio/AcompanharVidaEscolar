package com.jeffersonantunes.ave.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.jeffersonantunes.ave.config.ConfigFirebase;

public class Turma {

    private String turma;
    private String turno;

    private int sala;
    private int dom;
    private int seg;
    private int ter;
    private int qua;
    private int qui;
    private int sex;
    private int sab;

    public Turma() {

    }

    public void salvar(){
        try {
            DatabaseReference databaseReference = ConfigFirebase.getDbAveReference();
            databaseReference.child("turma").child(getTurma()).setValue(Turma.this);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("ERROR:Turma:","salvar:" + e.getMessage());
        }

    }

    @Exclude
    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public int getSala() {
        return sala;
    }

    public void setSala(int sala) {
        this.sala = sala;
    }

    public int getDom() {
        return dom;
    }

    public void setDom(int dom) {
        this.dom = dom;
    }

    public int getSeg() {
        return seg;
    }

    public void setSeg(int seg) {
        this.seg = seg;
    }

    public int getTer() {
        return ter;
    }

    public void setTer(int ter) {
        this.ter = ter;
    }

    public int getQua() {
        return qua;
    }

    public void setQua(int qua) {
        this.qua = qua;
    }

    public int getQui() {
        return qui;
    }

    public void setQui(int qui) {
        this.qui = qui;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSab() {
        return sab;
    }

    public void setSab(int sab) {
        this.sab = sab;
    }




}

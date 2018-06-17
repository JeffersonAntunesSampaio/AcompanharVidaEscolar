package com.jeffersonantunes.ave.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.jeffersonantunes.ave.config.ConfigFirebase;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String uid;
    private String acesso;

    public Usuario() {


    }

    public void salvar(){
        try {
            DatabaseReference databaseReference = ConfigFirebase.getDbAveReference();
            databaseReference.child("usuario").child(getId()).setValue(Usuario.this);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("ERROR:Usuario:","salvar:" + e.getMessage());
        }

    }


    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAcesso() {
        return acesso;
    }

    public void setAcesso(String acesso) {
        this.acesso = acesso;
    }
}

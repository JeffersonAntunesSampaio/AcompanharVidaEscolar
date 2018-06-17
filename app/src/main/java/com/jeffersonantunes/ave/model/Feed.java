package com.jeffersonantunes.ave.model;

public class Feed {

    private String idUsuario;
    private String data;
    private String mensagem;

    public Feed() {
    }

    @Override
    public String toString() {
        return this.data + " - " + this.mensagem;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }


}

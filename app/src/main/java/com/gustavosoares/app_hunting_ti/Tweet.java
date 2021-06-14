package com.gustavosoares.app_hunting_ti;

public class Tweet {

    private String uid;
    private String usuario;
    private String texto;

    public Tweet(){

    }

    public Tweet(String uid, String usuario, String texto){
        this.uid = uid;
        this.usuario = usuario;
        this.texto = texto;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}

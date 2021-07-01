package com.max.firebaseapp.model;

import androidx.annotation.Nullable;

public class User {

    private String id,email,nome,photUrl;

    //armazena se o usuário recebeu a solicitação

    private boolean receiveRequest;

    public void setReceiveRequest(boolean b){
        this.receiveRequest = b;
    }

    public boolean getReceiveRequest() {
        return receiveRequest;
    }

    public User(){

    }

    public User(String id, String email, String nome) {
        this.id = id;
        this.email = email;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPhotUrl() {
        return photUrl;
    }

    public void setPhotUrl(String photUrl) {
        this.photUrl = photUrl;
    }

        public boolean equals(User u) {

        return this.id.equals(u.getId());
    }
}

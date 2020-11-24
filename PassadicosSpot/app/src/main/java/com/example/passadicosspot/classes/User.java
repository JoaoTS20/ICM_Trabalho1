package com.example.passadicosspot.classes;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String tipo;

    public User() {

    }

    public User(String username, String tipo) {
        this.username = username;
        this.tipo = tipo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}

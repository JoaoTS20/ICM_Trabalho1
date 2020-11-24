package com.example.passadicosspot.classes;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Imagem implements Serializable {

    private String id;
    private String Description;
    private String Especialista;
    private String PhotoURL;
    private String Username;
    private ArrayList<String> AnimaisIdentificados;
    private GeoPoint Location;
    private java.util.Date Date;

    public Imagem() {

    }
    public Imagem(String Description, String Especialista, GeoPoint Location, String PhotoURL, String Username, ArrayList<String> AnimaisIdentificados, java.util.Date Date){
        this.Description=Description;
        this.Especialista=Especialista;
        this.Location=Location;
        this.PhotoURL =PhotoURL;
        this.Username=Username;
        this.AnimaisIdentificados=AnimaisIdentificados;
        this.Date=Date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEspecialista() {
        return Especialista;
    }

    public void setEspecialista(String especialista) {
        Especialista = especialista;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public ArrayList<String> getAnimaisIdentificados() {
        return AnimaisIdentificados;
    }

    public void setAnimaisIdentificados(ArrayList<String> animaisIdentificados) {
        AnimaisIdentificados = animaisIdentificados;
    }

    public GeoPoint getLocation() {
        return Location;
    }

    public void setLocation(GeoPoint location) {
        Location = location;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    @Override
    public String toString() {
        return "Imagem{" +
                "id='" + id + '\'' +
                ", Description='" + Description + '\'' +
                ", Especialista='" + Especialista + '\'' +
                ", PhotoURL='" + PhotoURL + '\'' +
                ", Username='" + Username + '\'' +
                ", AnimaisIdentificados=" + AnimaisIdentificados +
                ", Location=" + Location +
                ", Date=" + Date +
                '}';
    }
}

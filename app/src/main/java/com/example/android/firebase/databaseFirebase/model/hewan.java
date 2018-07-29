package com.example.android.firebase.databaseFirebase.model;

public class hewan {
    String id_hewan;
    String name;
    String info;
    String url;

    public hewan(String id_hewan, String name, String info, String url) {
        this.id_hewan = id_hewan;
        this.name = name;
        this.info = info;
        this.url = url;
    }

    public hewan() {

    }

    public String getId_hewan() {
        return id_hewan;
    }

    public void setId_hewan(String id_hewan) {
        this.id_hewan = id_hewan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}

package com.mgajinugrh.hariketiga;

public class UserModel {
    private String email;
    private String nama;

    public UserModel(String n, String e){
        this.nama = n;
        this.email = e;
    }

    public UserModel(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}

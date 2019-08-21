package com.mgajinugrh.hariketiga;

public class AsistenModel {
    private String namaAsisten;
    private String tahunAngkatan;
    private String key;

    public AsistenModel(String n, String t){
        this.namaAsisten = n;
        this.tahunAngkatan = t;
    }

    public AsistenModel(String n, String t, String k){
        this.namaAsisten = n;
        this.tahunAngkatan = t;
        this.key = k;
    }

    public AsistenModel(){

    }

    public String getNamaAsisten() {
        return namaAsisten;
    }

    public void setNamaAsisten(String namaAsisten) {
        this.namaAsisten = namaAsisten;
    }

    public String getTahunAngkatan() {
        return tahunAngkatan;
    }

    public void setTahunAngkatan(String tahunAngkatan) {
        this.tahunAngkatan = tahunAngkatan;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

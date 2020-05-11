package com.example.helloimage.data;

public class DataMahasiswa {
    // inisialisasi variabel
    int idMahasiswa; // idMahasiswa didatabase merupakan int (Auto Increment)
    String photoMahasiswa; // photoMahasiswa didatabase merupakan string
    String namaMahasiswa; // namaMahasiswa didatabase merupakan string
    String nimMahasiswa; // nimMahasiswa didatabase merupakan string
    String jurusanMahasiswa; // jurusanMahasiswa didatabase merupakan string
    String noHpMahasiswa; // noHpMahasiswa didatabase merupakan string

    //construktor datamahasiswa
    public DataMahasiswa(int idMahasiswa, String photoMahasiswa, String namaMahasiswa, String nimMahasiswa, String jurusanMahasiswa, String noHpMahasiswa) {
        this.idMahasiswa = idMahasiswa;
        this.photoMahasiswa = photoMahasiswa;
        this.namaMahasiswa = namaMahasiswa;
        this.nimMahasiswa = nimMahasiswa;
        this.jurusanMahasiswa = jurusanMahasiswa;
        this.noHpMahasiswa = noHpMahasiswa;
    }

    //getter dan setter
    public int getIdMahasiswa() {
        return idMahasiswa;
    }

    public void setIdMahasiswa(int idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }

    public String getPhotoMahasiswa() {
        return photoMahasiswa;
    }

    public void setPhotoMahasiswa(String photoMahasiswa) {
        this.photoMahasiswa = photoMahasiswa;
    }

    public String getNamaMahasiswa() {
        return namaMahasiswa;
    }

    public void setNamaMahasiswa(String namaMahasiswa) {
        this.namaMahasiswa = namaMahasiswa;
    }

    public String getNimMahasiswa() {
        return nimMahasiswa;
    }

    public void setNimMahasiswa(String nimMahasiswa) {
        this.nimMahasiswa = nimMahasiswa;
    }

    public String getJurusanMahasiswa() {
        return jurusanMahasiswa;
    }

    public void setJurusanMahasiswa(String jurusanMahasiswa) {
        this.jurusanMahasiswa = jurusanMahasiswa;
    }

    public String getNoHpMahasiswa() {
        return noHpMahasiswa;
    }

    public void setNoHpMahasiswa(String noHpMahasiswa) {
        this.noHpMahasiswa = noHpMahasiswa;
    }
}

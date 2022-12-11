package com.example.simple;

public class Siswa {
    private int id;
    private String nama;
    private String kelas;
    private String alamat;
    private String hobi;

    public Siswa(int id, String nama, String kelas, String alamat, String hobi) {
        this.id = id;
        this.nama = nama;
        this.kelas = kelas;
        this.alamat = alamat;
        this.hobi = hobi;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getKelas() {
        return kelas;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getHobi() {
        return hobi;
    }
}

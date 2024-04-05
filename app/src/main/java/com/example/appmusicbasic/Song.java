package com.example.appmusicbasic;

public class Song {
    private String tenbaihat;
    private String tencasi;
    private int image;
    private int file;

    public Song(String tenbaihat, int file) {
        this.tenbaihat = tenbaihat;
        this.file = file;
    }

    public Song(String tenbaihat, String tencasi, int image, int file) {
        this.tenbaihat = tenbaihat;
        this.tencasi = tencasi;
        this.image = image;
        this.file = file;
    }

    public String getTencasi() {
        return tencasi;
    }

    public void setTencasi(String tencasi) {
        this.tencasi = tencasi;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTenbaihat() {
        return tenbaihat;
    }

    public void setTenbaihat(String tenbaihat) {
        this.tenbaihat = tenbaihat;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int file) {
        this.file = file;
    }
}

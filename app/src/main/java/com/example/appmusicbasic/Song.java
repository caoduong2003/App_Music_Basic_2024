package com.example.appmusicbasic;

public class Song {
    private String tenbaihat;
    private int file;

    public Song(String tenbaihat, int file) {
        this.tenbaihat = tenbaihat;
        this.file = file;
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

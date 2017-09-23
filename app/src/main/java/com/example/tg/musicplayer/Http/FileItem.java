package com.example.tg.musicplayer.Http;


public class FileItem {
    public String fileName;
    public boolean isFile;
    public boolean isGoBack;

    public FileItem(String fileName, boolean isFile) {
        this.fileName = fileName;
        this.isFile = isFile;
        this.isGoBack=false;
    }

    public FileItem(String fileName, boolean isFile, boolean isTop){
        this.fileName = fileName;
        this.isFile = isFile;
        this.isGoBack=isTop;
    }

}

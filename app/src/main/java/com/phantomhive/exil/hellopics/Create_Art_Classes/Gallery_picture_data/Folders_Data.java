package com.phantomhive.exil.hellopics.Create_Art_Classes.Gallery_picture_data;

public class Folders_Data {
    private  String path;
    private  String FolderName;
    private int numberOfPics = 0;
    private String firstPic;

    public Folders_Data() {
    }

    public Folders_Data(String path, String folderName, int numberOfPics, String firstPic) {
        this.path = path;
        FolderName = folderName;
        this.numberOfPics = numberOfPics;
        this.firstPic = firstPic;
    }

    public String getPath() {
        return path;
    }

    public String getFolderName() {
        return FolderName;
    }

    public int getNumberOfPics() {
        return numberOfPics;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public void setNumberOfPics(int numberOfPics) {
        this.numberOfPics = numberOfPics;
    }

    public void addpics(){
        this.numberOfPics++;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }
}

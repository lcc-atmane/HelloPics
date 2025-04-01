package com.phantomhive.exil.hellopics.Create_Art_Classes.Gallery_picture_data;

public class Images_data {
    private String picturName;
    private String picturePath;
    private  String pictureSize;
    private  String imageUri;
    private Boolean selected = false;

    public Images_data() {
    }

    public Images_data(String picturName, String picturePath, String pictureSize, String imageUri, Boolean selected) {
        this.picturName = picturName;
        this.picturePath = picturePath;
        this.pictureSize = pictureSize;
        this.imageUri = imageUri;
        this.selected = selected;
    }

    public String getPicturName() {
        return picturName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public String getPictureSize() {
        return pictureSize;
    }

    public String getImageUri() {
        return imageUri;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setPicturName(String picturName) {
        this.picturName = picturName;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public void setPictureSize(String pictureSize) {
        this.pictureSize = pictureSize;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}

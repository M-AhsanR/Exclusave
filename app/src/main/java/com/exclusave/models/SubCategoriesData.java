package com.exclusave.models;

public class SubCategoriesData {
    String BackgroundImage;
    String CategoryID;
    String Image;
    String Title;

    public SubCategoriesData(String backgroundImage, String categoryID, String image, String title) {
        BackgroundImage = backgroundImage;
        CategoryID = categoryID;
        Image = image;
        Title = title;
    }

    public String getBackgroundImage() {
        return BackgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        BackgroundImage = backgroundImage;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

}

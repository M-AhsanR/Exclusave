package com.exclusave.models;

public class InterestsData {

    String BackgroundImage, CategoryID, Image, Title,SortOrder,GoldImage,Corporate;


    public InterestsData(String backgroundImage, String categoryID, String image, String title, String sortOrder, String goldImage, String corporate) {
        BackgroundImage = backgroundImage;
        CategoryID = categoryID;
        Image = image;
        Title = title;
        SortOrder = sortOrder;
        GoldImage = goldImage;
        Corporate = corporate;
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

    public String getSortOrder() {
        return SortOrder;
    }

    public void setSortOrder(String sortOrder) {
        SortOrder = sortOrder;
    }

    public String getGoldImage() {
        return GoldImage;
    }

    public void setGoldImage(String goldImage) {
        GoldImage = goldImage;
    }

    public String getCorporate() {
        return Corporate;
    }

    public void setCorporate(String corporate) {
        Corporate = corporate;
    }
}

package com.exclusave.models;

public class WhatsNew_Data {

    String Address,CategoryID,CategoryTitle,CityID,CityTitle,CoverImage,CreatedAt,
            CreatedBy,DistrictID,Hide,Image,IsActive,Latitude, Longitude,
            SortOrder,StoreID, StoreTextID,SystemLanguageID,Title,UpdatedAt,UpdatedBy;
    Integer IsAddedToFavourite;
    String CategoryImage, UserID;

    public WhatsNew_Data(String address, String categoryID, String categoryTitle, String cityID, String cityTitle, String coverImage, String createdAt, String createdBy, String districtID, String hide, String image, String isActive, String latitude, String longitude, String sortOrder, String storeID, String storeTextID, String systemLanguageID, String title, String updatedAt, String updatedBy, Integer isAddedToFavourite, String categoryImage, String userID) {
        Address = address;
        CategoryID = categoryID;
        CategoryTitle = categoryTitle;
        CityID = cityID;
        CityTitle = cityTitle;
        CoverImage = coverImage;
        CreatedAt = createdAt;
        CreatedBy = createdBy;
        DistrictID = districtID;
        Hide = hide;
        Image = image;
        IsActive = isActive;
        Latitude = latitude;
        Longitude = longitude;
        SortOrder = sortOrder;
        StoreID = storeID;
        StoreTextID = storeTextID;
        SystemLanguageID = systemLanguageID;
        Title = title;
        UpdatedAt = updatedAt;
        UpdatedBy = updatedBy;
        IsAddedToFavourite = isAddedToFavourite;
        CategoryImage = categoryImage;
        UserID = userID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryTitle() {
        return CategoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        CategoryTitle = categoryTitle;
    }

    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }

    public String getCityTitle() {
        return CityTitle;
    }

    public void setCityTitle(String cityTitle) {
        CityTitle = cityTitle;
    }

    public String getCoverImage() {
        return CoverImage;
    }

    public void setCoverImage(String coverImage) {
        CoverImage = coverImage;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(String districtID) {
        DistrictID = districtID;
    }

    public String getHide() {
        return Hide;
    }

    public void setHide(String hide) {
        Hide = hide;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getSortOrder() {
        return SortOrder;
    }

    public void setSortOrder(String sortOrder) {
        SortOrder = sortOrder;
    }

    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }

    public String getStoreTextID() {
        return StoreTextID;
    }

    public void setStoreTextID(String storeTextID) {
        StoreTextID = storeTextID;
    }

    public String getSystemLanguageID() {
        return SystemLanguageID;
    }

    public void setSystemLanguageID(String systemLanguageID) {
        SystemLanguageID = systemLanguageID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public Integer getIsAddedToFavourite() {
        return IsAddedToFavourite;
    }

    public void setIsAddedToFavourite(Integer isAddedToFavourite) {
        IsAddedToFavourite = isAddedToFavourite;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}

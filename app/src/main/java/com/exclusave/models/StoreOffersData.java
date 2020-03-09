package com.exclusave.models;

import java.util.ArrayList;

public class StoreOffersData {
    String Description;
    String Discount;
    String EndDate;
    String PromotionID;
    String QRCodeType;
    String StartDate;
    String StoreID;
    String StoreTitle;
    String Title;
    String StoreType;
    ArrayList<OfferDetailImagesData> offerDetailImagesData;

    public StoreOffersData(String description, String discount, String endDate, String promotionID, String QRCodeType, String startDate, String storeID, String storeTitle, String title, String storeType, ArrayList<OfferDetailImagesData> offerDetailImagesData) {
        Description = description;
        Discount = discount;
        EndDate = endDate;
        PromotionID = promotionID;
        this.QRCodeType = QRCodeType;
        StartDate = startDate;
        StoreID = storeID;
        StoreTitle = storeTitle;
        Title = title;
        StoreType = storeType;
        this.offerDetailImagesData = offerDetailImagesData;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getPromotionID() {
        return PromotionID;
    }

    public void setPromotionID(String promotionID) {
        PromotionID = promotionID;
    }

    public String getQRCodeType() {
        return QRCodeType;
    }

    public void setQRCodeType(String QRCodeType) {
        this.QRCodeType = QRCodeType;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }

    public String getStoreTitle() {
        return StoreTitle;
    }

    public void setStoreTitle(String storeTitle) {
        StoreTitle = storeTitle;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getStoreType() {
        return StoreType;
    }

    public void setStoreType(String storeType) {
        StoreType = storeType;
    }

    public ArrayList<OfferDetailImagesData> getOfferDetailImagesData() {
        return offerDetailImagesData;
    }

    public void setOfferDetailImagesData(ArrayList<OfferDetailImagesData> offerDetailImagesData) {
        this.offerDetailImagesData = offerDetailImagesData;
    }
}

package com.exclusave.models;

public class CardsData {
    String CardCode;
    String CardID;
    String CardType;
    String CardImage;
    String Title;
    String Amount;
    String IsCouponCodeRequired;
    String CardLogoImage;

    public CardsData(String cardCode, String cardID, String cardType, String cardImage, String title, String amount, String isCouponCodeRequired, String cardLogoImage) {
        CardCode = cardCode;
        CardID = cardID;
        CardType = cardType;
        CardImage = cardImage;
        Title = title;
        Amount = amount;
        IsCouponCodeRequired = isCouponCodeRequired;
        CardLogoImage = cardLogoImage;
    }

    public String getCardCode() {
        return CardCode;
    }

    public void setCardCode(String cardCode) {
        CardCode = cardCode;
    }

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getCardImage() {
        return CardImage;
    }

    public void setCardImage(String cardImage) {
        CardImage = cardImage;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getIsCouponCodeRequired() {
        return IsCouponCodeRequired;
    }

    public void setIsCouponCodeRequired(String isCouponCodeRequired) {
        IsCouponCodeRequired = isCouponCodeRequired;
    }

    public String getCardLogoImage() {
        return CardLogoImage;
    }

    public void setCardLogoImage(String cardLogoImage) {
        CardLogoImage = cardLogoImage;
    }
}

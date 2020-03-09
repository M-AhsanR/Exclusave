package com.exclusave.ApiStructure;

/**
 * Created by M on 3/7/2018.
 */

public interface Constants {

    interface URL {
        String BASE_URL = "http://exclusave.schopfen.com/api/";
        String IMG_URL = "http://exclusave.schopfen.com/";

        String LOGIN = BASE_URL + "login";
        String GENERATETOKEN = BASE_URL + "generateTokenByApi";
        String GETUSERDETAILS = BASE_URL + "getUserDetail";
        String GETCARDS = BASE_URL + "cards";
        String getUserDetail = BASE_URL + "getUserDetail";
        String CITIES = BASE_URL + "cities";
        String SignUp = BASE_URL + "signUp";
        String FORGET_PASSWORD = BASE_URL+ "forgotPassword";
        String SIGNUP = BASE_URL + "signUp";
        String LOGOUT = BASE_URL + "logout?UserID=";
        String UPDATE_PROFILE = BASE_URL+"updateProfile";
        String CATEGORIES = BASE_URL + "categories?UserID=";
        String ApplyCoupon = BASE_URL + "applyCoupon";
        String STORE = BASE_URL+ "stores?UserID=";
        String addStoreToFavourite = BASE_URL + "addStoreToFavourite";
        String OFFERS_FOR_CARDCS = BASE_URL + "offersForCard?UserID=";
        String favouriteStores = BASE_URL + "favouriteStores";
        String offerDetail = BASE_URL + "offerDetail?UserID=";
        String sponsoredStores = BASE_URL + "sponsoredStores?UserID=";
        String offersForStore = BASE_URL + "offersForStore?UserID=";
        String BRANCHES = BASE_URL + "branches?UserID=";
        String SUBCATOGARIES = BASE_URL+"subCategories?UserID=";
        String APPLYCODE = BASE_URL+"getStore";
        String CREATEBOOKING = BASE_URL + "createBooking";
        String saveTransaction = BASE_URL + "saveTransaction";
        String saveRating = BASE_URL + "saveRating";
        String getChat = BASE_URL + "getChat";
        String sendMessage = BASE_URL + "sendMessage";
        String STARTCHAT = BASE_URL +"startChat";
        String GETNOTIFICATIONS = BASE_URL+"getNotifications?UserID=";
        String GETTRANSACTION = BASE_URL + "getTransaction";
        String getSiteSettings = BASE_URL + "getSiteSettings?";
        String UPDATELANGUAGE = BASE_URL + "updateLanguageForUser";
    }
}

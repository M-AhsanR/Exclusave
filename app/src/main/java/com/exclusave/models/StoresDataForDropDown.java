package com.exclusave.models;

public class StoresDataForDropDown {

    String StoreID,Title;

    public StoresDataForDropDown(String storeID, String title) {
        StoreID = storeID;
        Title = title;
    }

    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}

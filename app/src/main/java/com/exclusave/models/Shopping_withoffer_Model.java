package com.exclusave.models;

public class Shopping_withoffer_Model {

    String img_url,id;

    public Shopping_withoffer_Model(String img_url, String id) {
        this.img_url = img_url;
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

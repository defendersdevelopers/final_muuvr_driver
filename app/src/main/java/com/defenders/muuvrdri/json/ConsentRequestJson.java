package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class ConsentRequestJson {

    @SerializedName("photo")
    @Expose
    private String photo;


    public ConsentRequestJson() {
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

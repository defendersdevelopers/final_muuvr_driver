package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BiddingSingleDataResponseJson {

    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("data")
    @Expose
    private BiddingSingleDataJson data;




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public BiddingSingleDataJson getData() {
        return data;
    }

    public void setData(BiddingSingleDataJson data) {
        this.data = data;
    }
}

package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BiddingResponseSingleJson {

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("data")
    private BiddngJson data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BiddngJson getData() {
        return data;
    }

    public void setData(BiddngJson data) {
        this.data = data;
    }
}

package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BiddingDriversResponseJson {

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("data")
    private List<BiddingDriversJson> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BiddingDriversJson> getData() {
        return data;
    }

    public void setData(List<BiddingDriversJson> data) {
        this.data = data;
    }
}

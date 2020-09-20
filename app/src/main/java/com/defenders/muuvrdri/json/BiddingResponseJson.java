package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BiddingResponseJson {

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("data")
    private List<BiddngJson> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public List<BiddngJson> getData() {
        return data;
    }

    public void setData(List<BiddngJson> data) {
        this.data = data;
    }
}

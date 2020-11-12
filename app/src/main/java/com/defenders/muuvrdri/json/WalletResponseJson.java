package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.defenders.muuvrdri.models.WalletModel;

import java.util.ArrayList;
import java.util.List;



public class WalletResponseJson {

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("data")
    private List<WalletModel> data = new ArrayList<>();


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<WalletModel> getData() {
        return data;
    }

    public void setData(List<WalletModel> data) {
        this.data = data;
    }
}

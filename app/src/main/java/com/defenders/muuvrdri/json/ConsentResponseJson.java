package com.defenders.muuvrdri.json;

import com.defenders.muuvrdri.models.BankModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ConsentResponseJson {

    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("data")
    @Expose
    private String data;




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

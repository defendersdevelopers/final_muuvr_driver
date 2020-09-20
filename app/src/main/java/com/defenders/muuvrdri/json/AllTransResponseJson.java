package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.defenders.muuvrdri.models.AllTransaksiModel;
import com.defenders.muuvrdri.models.PelangganModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ourdevelops Team on 10/19/2019.
 */

public class AllTransResponseJson {

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("data")
    private List<AllTransaksiModel> data = new ArrayList<>();

    @Expose
    @SerializedName("driver")
    private List<PelangganModel> driver = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AllTransaksiModel> getData() {
        return data;
    }

    public void setData(List<AllTransaksiModel> data) {
        this.data = data;
    }

    public List<PelangganModel> getDriver() {
        return driver;
    }

    public void setDriver(List<PelangganModel> driver) {
        this.driver = driver;
    }
}

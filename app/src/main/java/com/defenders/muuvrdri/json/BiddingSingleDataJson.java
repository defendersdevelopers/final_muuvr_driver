package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BiddingSingleDataJson implements Serializable {


    @Expose
    @SerializedName("driver_id")
    private String driverId;

    @Expose
    @SerializedName("user_id")
    private String user_id;
    @Expose
    @SerializedName("bidding_price")
    private Double bidding_price;


    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Double getBidding_price() {
        return bidding_price;
    }

    public void setBidding_price(Double bidding_price) {
        this.bidding_price = bidding_price;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

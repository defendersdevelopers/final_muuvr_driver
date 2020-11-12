package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BiddingDriversJson implements Serializable {


    @Expose
    @SerializedName("driver_id")
    private String driverId;

    @Expose
    @SerializedName("driver_image")
    private String driver_image;
    @Expose
    @SerializedName("bidding_price")
    private Double bidding_price;
    @Expose
    @SerializedName("driver_name")
    private String driver_name;


    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriver_image() {
        return driver_image;
    }

    public void setDriver_image(String driver_image) {
        this.driver_image = driver_image;
    }

    public Double getBidding_price() {
        return bidding_price;
    }

    public void setBidding_price(Double bidding_price) {
        this.bidding_price = bidding_price;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }
}

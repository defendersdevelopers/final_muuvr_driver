package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BiddngJson implements Serializable {


    @Expose
    @SerializedName("distance")
    private double distance;

    @Expose
    @SerializedName("start_latitude")
    private Double start_latitude;
    @Expose
    @SerializedName("start_longitude")
    private Double start_longitude;
    @Expose
    @SerializedName("end_latitude")
    private Double end_latitude;
    @Expose
    @SerializedName("end_longitude")
    private Double end_longitude;

    @Expose
    @SerializedName("pickup")
    private String pickup;
    @Expose
    @SerializedName("icon")
    private String icon;
    @Expose
    @SerializedName("layanan")
    private String layanan;
    @Expose
    @SerializedName("layanandesk")
    private String layanandesk;

    @Expose
    @SerializedName("destination")
    private String destination;

    @Expose
    @SerializedName("price")
    String price;
    @Expose
    @SerializedName("biayaminimum")
    String biayaminimum;
    @Expose
    @SerializedName("timeDistance")
    String timeDistance;
    @Expose
    @SerializedName("selectedFitur")
    int selectedFitur;

    @Expose
    @SerializedName("user_id")
    String user_id;

    @Expose
    @SerializedName("time_scheduled")
    String timeScheduled;


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Double getStart_latitude() {
        return start_latitude;
    }

    public void setStart_latitude(Double start_latitude) {
        this.start_latitude = start_latitude;
    }

    public Double getStart_longitude() {
        return start_longitude;
    }

    public void setStart_longitude(Double start_longitude) {
        this.start_longitude = start_longitude;
    }

    public Double getEnd_latitude() {
        return end_latitude;
    }

    public void setEnd_latitude(Double end_latitude) {
        this.end_latitude = end_latitude;
    }

    public Double getEnd_longitude() {
        return end_longitude;
    }

    public void setEnd_longitude(Double end_longitude) {
        this.end_longitude = end_longitude;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLayanan() {
        return layanan;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }

    public String getLayanandesk() {
        return layanandesk;
    }

    public void setLayanandesk(String layanandesk) {
        this.layanandesk = layanandesk;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBiayaminimum() {
        return biayaminimum;
    }

    public void setBiayaminimum(String biayaminimum) {
        this.biayaminimum = biayaminimum;
    }

    public String getTimeDistance() {
        return timeDistance;
    }

    public void setTimeDistance(String timeDistance) {
        this.timeDistance = timeDistance;
    }

    public int getSelectedFitur() {
        return selectedFitur;
    }

    public void setSelectedFitur(int selectedFitur) {
        this.selectedFitur = selectedFitur;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTimeScheduled() {
        return timeScheduled;
    }

    public void setTimeScheduled(String timeScheduled) {
        this.timeScheduled = timeScheduled;
    }
}

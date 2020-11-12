package com.defenders.muuvrdri.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class SettingsModel implements Serializable {
    @Expose
    @SerializedName("app_privacy_policy")
    public String privacy;

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }


}

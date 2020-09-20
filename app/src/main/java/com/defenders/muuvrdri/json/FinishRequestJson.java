package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FinishRequestJson {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_transaksi")
    @Expose
    private String idtrans;

    @SerializedName("consent_letter")
    @Expose
    private String consent;


    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdtrans() {
        return idtrans;
    }

    public void setIdtrans(String idtrans) {
        this.idtrans = idtrans;
    }
}

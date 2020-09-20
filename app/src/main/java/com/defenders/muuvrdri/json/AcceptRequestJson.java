package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class AcceptRequestJson {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_transaksi")
    @Expose
    private String idtrans;

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

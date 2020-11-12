package com.defenders.muuvrdri.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class GetOnRequestJson {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("is_turn")
    @Expose
    private boolean turn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getOn() {
        return turn;
    }

    public void setOn(boolean turn) {
        this.turn = turn;
    }
}

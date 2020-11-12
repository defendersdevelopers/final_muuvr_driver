package com.defenders.muuvrdri.models;

import java.io.Serializable;

import static com.defenders.muuvrdri.json.fcm.FCMType.BIDDING;
import static com.defenders.muuvrdri.json.fcm.FCMType.CHAT;


public class Bid implements Serializable{
    public int type = BIDDING;
}

package com.defenders.muuvrdri.models;

import com.defenders.muuvrdri.json.fcm.FCMType;

import java.io.Serializable;


public class OrderFCM implements Serializable{
    public int type = FCMType.ORDER;
    public String id_driver;
    public String id_pelanggan;
    public String id_transaksi;
    public String response;
    public String desc;
    public String invoice;
    public String ordertime;
}

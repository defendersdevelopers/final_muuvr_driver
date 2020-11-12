package com.defenders.muuvrdri.models;

import java.io.Serializable;

import static com.defenders.muuvrdri.json.fcm.FCMType.OTHER;


public class Notif implements Serializable{
    public int type = OTHER;
    public String title;
    public String message;
}

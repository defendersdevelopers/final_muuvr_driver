package com.defenders.muuvrdri.constants;

import java.text.SimpleDateFormat;
import java.util.Locale;



public class Constants {

    private static final String BASE_URL = "https://ride.defendersdevelopers.tech/";
    public static final String FCM_KEY = "AAAAwUc-2do:APA91bHbHoHE1jALKaScxDUOq2rSmwRn2the5_W9jnNI8idGZoXzh60jqu8cCgkadFwwcLKfw6L7kcRBLZiLhk-0-VHokHetz5WiUOVBrCln1JdXA4obiwfi5luHxwjDTwMWNT9DRHwL";
    public static final String CONNECTION = BASE_URL + "api/";
    public static final String IMAGESFITUR = BASE_URL + "images/fitur/";
    public static final String IMAGESBANK = BASE_URL + "images/bank/";
    public static final String IMAGESDRIVER = BASE_URL + "images/fotodriver/";
    public static final String IMAGESUSER = BASE_URL + "images/pelanggan/";
    public static final String IMAGESMERCHANT = BASE_URL + "images/merchant/";
    public static String CURRENCY = "";

    public static Double LATITUDE;
    public static Double LONGITUDE;
    public static String LOCATION;

    public static String TOKEN = "token";

    public static String USERID = "uid";

    public static String PREF_NAME = "pref_name";

    public static int permission_camera_code = 786;
    public static int permission_write_data = 788;
    public static int permission_Read_data = 789;
    public static int permission_Recording_audio = 790;

    public static SimpleDateFormat df =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    public static String versionname = "1.0";


}

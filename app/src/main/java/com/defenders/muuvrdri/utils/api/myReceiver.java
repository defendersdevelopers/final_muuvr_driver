package com.defenders.muuvrdri.utils.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.defenders.muuvrdri.activity.MainActivity;

public class myReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newAct = new Intent(context, MainActivity.class);
        newAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newAct);
    }
}

package com.defenders.muuvrdri.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.defenders.muuvrdri.R;


public class AboutDevelopers extends AppCompatActivity {

    Button bt_contact_dev;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developers);

        bt_contact_dev = findViewById(R.id.bt_contact_dev);

        bt_contact_dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://defendersdevelopers.tech"));
                startActivity(i);
            }
        });
    }
}

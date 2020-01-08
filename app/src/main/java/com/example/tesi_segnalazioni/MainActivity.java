package com.example.tesi_segnalazioni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    int timeout = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        final String name = b.getString("name");
        final String email = b.getString("email");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, NavigationActivity.class);
                i.putExtra("name", name);
                i.putExtra("email", email);
                finish();
                startActivity(i);

            }
        }, timeout);
    }
}

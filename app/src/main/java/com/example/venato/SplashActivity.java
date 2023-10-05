package com.example.venato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.venato.database.DataBaseHelper;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginChoice.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}
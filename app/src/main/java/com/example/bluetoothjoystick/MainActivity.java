package com.example.bluetoothjoystick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent nextIntent = new Intent(MainActivity.this, JoystickActivity.class);
                startActivity(nextIntent);            }

        }, 3000);
        Intent nextIntent = new Intent(this, JoystickActivity.class);
        startActivity(nextIntent);

    }
}
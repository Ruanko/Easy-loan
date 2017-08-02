package com.ruanko.easyloanadmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVUser;
import com.ruanko.easyloanadmin.R;
import com.ruanko.easyloanadmin.data.UserContract;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (AVUser.getCurrentUser() != null) {
                    if (AVUser.getCurrentUser().getInt(UserContract.UserEntry.COLUMN_ROLE) == 10012)
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    else
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}

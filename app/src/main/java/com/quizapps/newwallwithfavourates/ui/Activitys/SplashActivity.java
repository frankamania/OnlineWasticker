package com.quizapps.newwallwithfavourates.ui.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.appodeal.ads.Appodeal;
import com.quizapps.newwallwithfavourates.BuildConfig;
import com.quizapps.newwallwithfavourates.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Appodeal.disableLocationPermissionCheck();
        Appodeal.disableWriteExternalStoragePermissionCheck();
        Appodeal.initialize(this, getString(R.string.appodeal_app_ky), Appodeal.BANNER|Appodeal.INTERSTITIAL, true);

        if(BuildConfig.DEBUG){
            Appodeal.setTesting(true);
        }



        new Handler().postDelayed(() -> {

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }, 1000);
    }
}
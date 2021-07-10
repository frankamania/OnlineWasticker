package com.quizapps.newwallwithfavourates;


import androidx.multidex.MultiDexApplication;

import com.downloader.PRDownloader;
import com.facebook.drawee.backends.pipeline.Fresco;

public class Myapp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        PRDownloader.initialize(this);

    }


}

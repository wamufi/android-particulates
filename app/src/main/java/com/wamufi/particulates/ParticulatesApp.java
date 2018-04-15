package com.wamufi.particulates;

import android.app.Application;

public class ParticulatesApp extends Application {

    private static ParticulatesApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static ParticulatesApp get() {
        return instance;
    }

}

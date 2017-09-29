package com.lyx.sample;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * App
 * <p/>
 * Created by luoyingxing on 2017/8/20.
 */
public class App extends Application {
    private static App mApp;

    public static App getApp() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        Fresco.initialize(this);
    }
}
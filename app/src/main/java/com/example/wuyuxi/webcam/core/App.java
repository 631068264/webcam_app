package com.example.wuyuxi.webcam.core;

import android.app.Application;

/**
 * @Annotation //APP 初始化
 */
public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


    }

    public static synchronized App getInstance() {
        return instance;
    }
}

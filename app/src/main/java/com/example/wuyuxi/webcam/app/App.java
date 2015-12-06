package com.example.wuyuxi.webcam.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by wuyuxi on 2015/12/6.
 */
public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static Context getAppContext() {
        return app;
    }
}

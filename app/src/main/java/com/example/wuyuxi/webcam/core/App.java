package com.example.wuyuxi.webcam.core;

import android.app.Application;

import com.example.wuyuxi.webcam.BuildConfig;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * @Annotation //APP 初始化
 */
public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        /**
         * Log 初始化
         */
        Logger.init("TAG")
                .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)
                .hideThreadInfo();


    }

    public static synchronized App getInstance() {
        return instance;
    }
}

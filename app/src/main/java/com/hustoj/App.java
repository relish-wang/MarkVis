package com.hustoj;

import android.app.Application;

import com.bumptech.glide.Glide;

/**
 * @author Relish Wang
 * @since 2018/01/07
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Glide.get(App.this).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(App.this).clearDiskCache();
            }
        }).start();
    }
}

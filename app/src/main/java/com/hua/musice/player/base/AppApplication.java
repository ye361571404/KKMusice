package com.hua.musice.player.base;

import android.app.Application;
import android.content.Context;

public class AppApplication extends Application {


    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = getApplicationContext();
    }


    public static Context getContext(){
        return mContext;
    }
}

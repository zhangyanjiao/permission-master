package com.magic.module.permission.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.magic.module.permission.demo.suggest.TestService;

/**
 * @author zhangyanjiao
 * @data on ${date}:${hour}:${minute}
 * @desc
 */
public class MyApplication extends Application {

    private static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(mContext,TestService.class));
        }
    }

    public static Context getContext(){
        return mContext;
    }
}

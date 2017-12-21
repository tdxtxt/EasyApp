package com.easy.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.easy.callback.loadsir.EmptyCallback;
import com.easy.callback.loadsir.ErrorCallback;
import com.easy.callback.loadsir.LoadingCallback;
import com.kingja.loadsir.core.LoadSir;

import butterknife.ButterKnife;
import comm.imageload.ImageLoaderManager;

/**
 * Created by Administrator on 2017/7/17.
 */

public class EasyApp extends Application{
    private static EasyApp instance;
    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ForegroundCallbacks.init(this);
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())//添加各种状态页
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
//                .addCallback(new TimeoutCallback())
//                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .commit();
        ButterKnife.setDebug(true);
        ImageLoaderManager.getInstance().init(this);
    }
}

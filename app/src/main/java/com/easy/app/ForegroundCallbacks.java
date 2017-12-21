package com.easy.app;

/**
 * Created by Administrator on 2017/7/17.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.easy.android.R;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tdx on 2017/5/5.
 * 用于 监听/判断 app前后台
 */

public class ForegroundCallbacks implements Application.ActivityLifecycleCallbacks {
    public static final long CHECK_DELAY = 500;
    public interface Listener {
        /**
         * 切换到前台时调用
         */
        public void onBecameForeground();
        /**
         * 切换到后台时调用
         */
        public void onBecameBackground();
    }
    private static ForegroundCallbacks instance;
    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
    private Runnable check;
    public static ForegroundCallbacks init(Application application){
        if (instance == null) {
            instance = new ForegroundCallbacks();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }
    public static ForegroundCallbacks get(Application application){
        if (instance == null) {
            init(application);
        }
        return instance;
    }
    public static ForegroundCallbacks get(Context ctx){
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application)appCtx);
            }
            throw new IllegalStateException(
                    "Foreground is not initialised and " +
                            "cannot obtain the Application object");
        }
        return instance;
    }
    public static ForegroundCallbacks get(){
        if (instance == null) {
            throw new IllegalStateException(
                    "Foreground is not initialised - invoke " +
                            "at least once with parameterised init/get");
        }
        return instance;
    }
    public boolean isForeground(){
        return foreground;
    }
    public boolean isBackground(){
        return !foreground;
    }
    public void addListener(Listener listener){
        listeners.add(listener);
    }
    public void removeListener(Listener listener){
        listeners.remove(listener);
    }
    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;
        if (check != null)
            handler.removeCallbacks(check);
        if (wasBackground){
//            LogUtil.e("went foreground");
            for (Listener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
//                    LogUtil.e("Listener threw exception!", exc);
                }
            }
        } else {
//            LogUtil.e("still foreground");
        }
    }
    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;
        if (check != null)
            handler.removeCallbacks(check);
        handler.postDelayed(check = new Runnable(){
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
//                    LogUtil.e("went background");
                    for (Listener l : listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exc) {
//                            LogUtil.e("Listener threw exception!", exc);
                        }
                    }
                } else {
//                    LogUtil.e("still foreground");
                }
            }
        }, CHECK_DELAY);
    }
    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        //这里全局给Activity设置toolbar和title,你想象力有多丰富,这里就有多强大,以前放到BaseActivity的操作都可以放到这里
        if (activity.findViewById(R.id.toolbar) != null) { //找到 Toolbar 并且替换 Actionbar
            if (activity instanceof AppCompatActivity) {
                ((AppCompatActivity) activity).setSupportActionBar((Toolbar) activity.findViewById(R.id.toolbar));
                ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.setActionBar((android.widget.Toolbar) activity.findViewById(R.id.toolbar));
                    activity.getActionBar().setDisplayShowTitleEnabled(false);
                }
            }
        }
        if (activity.findViewById(R.id.tv_title_center) != null) { //找到 Toolbar 的标题栏并设置标题名
            ((TextView) activity.findViewById(R.id.tv_title_center)).setText(activity.getTitle());
        }
        if (activity.findViewById(R.id.iv_back) != null) { //找到 Toolbar 的返回按钮,并且设置点击事件,点击关闭这个 Activity
            activity.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
        }
    }
    @Override
    public void onActivityStarted(Activity activity) {}
    @Override
    public void onActivityStopped(Activity activity) {}
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
    @Override
    public void onActivityDestroyed(Activity activity) {}
}

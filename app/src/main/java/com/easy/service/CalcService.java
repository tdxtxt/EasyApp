package com.easy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/10/31 11:49
 * 传参：
 * 返回:
 */
public class CalcService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("CalcService","onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("CalcService","onCreate");
        super.onCreate();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("CalcService","onUnbind");
        return super.onUnbind(intent);
    }
//    ICalcAIDL.Stub mBinder = new ICalcAIDL.Stub() {
//        @Override
//        public int add(int x, int y) throws RemoteException {
//            return x + y;
//        }
//    };
}

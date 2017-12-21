package comm.helper;

import android.util.Log;

/**
 * Created by Administrator on 2017/7/17.
 */

public class Logger {
    public static final boolean DEBUG = true;
    public static void log( String msg) {
        if (DEBUG) {
            Log.i("tdx","====" + msg);
        }
    }
    public static void log(String log, String msg) {
        if (DEBUG) {
            Log.i("tdx", log + "=====" + msg);
        }
    }
    public static void log(String TAG , String msg , String data){
        if(DEBUG){
            Log.i("tdx", TAG  + "=====" + msg + "............."+data);
        }
    }
}

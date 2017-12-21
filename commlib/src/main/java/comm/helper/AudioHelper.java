package comm.helper;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;

/**
 * Created by Administrator on 2017/5/31.
 */

public class AudioHelper {
    private Context context;
    AudioManager mAudioManager;
    public AudioHelper(Context context){
        this.context = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        BluetoothConnectionReceiver audioNoisyReceiver = new BluetoothConnectionReceiver();
        //蓝牙状态广播监听
        IntentFilter audioFilter = new IntentFilter();
        audioFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        audioFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(audioNoisyReceiver, audioFilter);

    }
    /**
     * 切换到外放
     */
    public void changeToSpeaker(){
        //注意此处，蓝牙未断开时使用MODE_IN_COMMUNICATION而不是MODE_NORMAL
        mAudioManager.setMode(true ? AudioManager.MODE_IN_COMMUNICATION : AudioManager.MODE_NORMAL);
        mAudioManager.stopBluetoothSco();
        mAudioManager.setBluetoothScoOn(false);
        mAudioManager.setSpeakerphoneOn(true);
    }
    /**
     * 切换到蓝牙音箱
     */
    public void changeToBothset(){
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        mAudioManager.startBluetoothSco();
        mAudioManager.setBluetoothScoOn(true);
        mAudioManager.setSpeakerphoneOn(false);
    }
/************************************************************/
//注意：以下两个方法还未验证
/************************************************************/
    /**
     * 切换到耳机模式
     */
    public void changeToHeadset(){
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        mAudioManager.stopBluetoothSco();
        mAudioManager.setBluetoothScoOn(false);
        mAudioManager.setSpeakerphoneOn(false);
    }
    /**
     * 切换到听筒
     */
    public void changeToReceiver(){
        mAudioManager.setSpeakerphoneOn(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

    public class BluetoothConnectionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(intent.getAction())) {		//蓝牙连接状态
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1);
                if (state == BluetoothAdapter.STATE_CONNECTED || state == BluetoothAdapter.STATE_DISCONNECTED) {
                    //连接或失联，切换音频输出（到蓝牙、或者强制仍然扬声器外放）
                }
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){	//本地蓝牙打开或关闭
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                if (state == BluetoothAdapter.STATE_OFF || state == BluetoothAdapter.STATE_TURNING_OFF) {
                    //断开，切换音频输出
                }
            }
        }
    }
}

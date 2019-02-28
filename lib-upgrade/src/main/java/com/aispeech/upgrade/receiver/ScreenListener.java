package com.aispeech.upgrade.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by Byron on 2018/4/21.
 * 亮屏,息屏,解锁的广播接收
 */

public class ScreenListener {
    public static final String TAG = "ScreenListener";
    private Context mContext;
    private ScreenBroadcastReceiver receiver;
    private ScreenStateListener mScreenStateListener;

    public ScreenListener(Context context) {
        mContext = context;
        receiver = new ScreenBroadcastReceiver();
    }

    public void register(ScreenStateListener screenStateListener) {
        if (screenStateListener != null) {
            mScreenStateListener = screenStateListener;
        }
        if (receiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            mContext.registerReceiver(receiver, filter);
        }
    }

    public void unregister() {
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
    }


    private class ScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    if (mScreenStateListener != null) {
                        Log.e(TAG, "ScreenBroadcastReceiver --> ACTION_SCREEN_ON");
                        mScreenStateListener.onScreenOn();
                    }
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    if (mScreenStateListener != null) {
                        Log.e(TAG, "ScreenBroadcastReceiver --> ACTION_SCREEN_OFF");
                        mScreenStateListener.onScreenOff();
                    }
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    if (mScreenStateListener != null) {
                        Log.e(TAG, "ScreenBroadcastReceiver --> ACTION_USER_PRESENT");
                        mScreenStateListener.onUserPresent();
                    }
                }
            }
        }
    }

    public interface ScreenStateListener {
        /***
         * 亮屏
         */
        void onScreenOn();

        /**
         * 息屏
         */
        void onScreenOff();

        /**
         * 解锁
         */
        void onUserPresent();
    }
}

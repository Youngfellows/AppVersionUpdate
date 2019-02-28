package com.aispeech.upgrade.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by Jie.Chen on 2018/1/10.
 * 监听home事件
 */

public class HomeEventReceiver extends BroadcastReceiver {
    private String TAG = this.getClass().getSimpleName();
    private static HomeKeyeventListener homeKeyeventListener;

    public static void setHomeKeyeventListener(HomeKeyeventListener listener) {
        homeKeyeventListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "Home键按下了外面==" + action + " homeKeyeventListener = " + homeKeyeventListener);
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            Log.i(TAG, "Home键按下了");
//            MusicManager.getInstance(context).stop();
            if (homeKeyeventListener != null) {
                homeKeyeventListener.homePress();
            }
        }
    }

    public interface HomeKeyeventListener {
        public void homePress();
    }
}


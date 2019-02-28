package com.aispeech.upgrade.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jie.Chen on 2018/5/2.
 * 开机广播监听
 */
public class BootReceiver extends BroadcastReceiver {
    private String TAG = this.getClass().getSimpleName();
    private static IBootListener mBootListener;

    public static void setBootListener(IBootListener bootListener) {
        mBootListener = bootListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "action: " + action);
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Log.i(TAG, "接收到开机的广播啦");
            // TODO: 2018/4/23 可以唤醒小驰TV助手

            //启动服务
//            context.startService(new Intent(context, UpgradeService.class));

            //再过2h拉取更新,设置一个闹钟
//            AlarmClockManager.getInstance(context).setPullUpdateAlarm();

            Log.i(TAG, "mBootListener: " + mBootListener);
            if (mBootListener != null) {
                mBootListener.boot();
            }
        }
    }

    public interface IBootListener {
        /**
         * 开机的接口回调
         */
        void boot();
    }
}

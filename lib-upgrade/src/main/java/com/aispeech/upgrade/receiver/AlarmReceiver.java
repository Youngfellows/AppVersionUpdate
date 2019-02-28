package com.aispeech.upgrade.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aispeech.upgrade.alarm.AlarmClockManager;
import com.aispeech.upgrade.conf.Constants;


/**
 * Created by Jie.Chen on 2018/4/23.
 * 接收闹钟的广播
 */

public class AlarmReceiver extends BroadcastReceiver {
    private String TAG = this.getClass().getSimpleName();
    private static IAlarmListener mAlarmListener;

    public static void setAlarmListener(IAlarmListener alarmListener) {
        mAlarmListener = alarmListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "接收到闹钟的广播");
        String action = intent.getAction();
        long alarmTimes = intent.getLongExtra("alarmTimes", -1);
//        AlarmType alarmType = (AlarmType) intent.getSerializableExtra("alarmType");
        Log.i(TAG, "action: " + action + " ,alarmTimes: " + alarmTimes);
        if (Constants.ALARM_ACTION_UPGRADE.equals(action)) {
            //再过2h拉取更新,设置一个闹钟
            AlarmClockManager.getInstance(context).setPullUpdateAlarm();
            if (mAlarmListener != null) {
                mAlarmListener.noticeUpdateAlarm();
            }
        } else if (Constants.ALARM_ACTION_INSTALLER.equals(action)) {
            //再过24安装升级,设置一个闹钟
            AlarmClockManager.getInstance(context).setInstallerAlarm();
            if (mAlarmListener != null) {
                mAlarmListener.noticeInstallerAlarm();
            }
        }
    }

    /**
     * 闹钟接口
     */
    public interface IAlarmListener {
        void noticeUpdateAlarm();//接收到拉取通知的闹钟

        void noticeInstallerAlarm();//接收到安装升级的闹钟
    }
}

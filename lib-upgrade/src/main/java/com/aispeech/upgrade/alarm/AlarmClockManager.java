package com.aispeech.upgrade.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import com.aispeech.upgrade.bean.AlarmDateBean;
import com.aispeech.upgrade.conf.Constants;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jie.Chen on 2018/4/23.
 * 设置闹钟
 */

public class AlarmClockManager {
    private static final String TAG = "AlarmClockManager";
    private final static String DM12 = "E h:mm aa";
    private final static String DM24 = "E k:mm";
    private static AlarmClockManager instance;
    private Context mContext;
    /**
     * 拉取更新的时间设置
     */
    private AlarmDateBean mPullAlarmDate;
    /**
     * 下次安装的时间设置
     */
    private AlarmDateBean mInstallAlarmDate;

    private AlarmClockManager(Context context) {
        mContext = context;
    }

    public static AlarmClockManager getInstance(Context context) {
        if (instance == null) {
            synchronized (AlarmClockManager.class) {
                if (instance == null) {
                    instance = new AlarmClockManager(context);
                }
            }
        }
        return instance;
    }

    public void setPullAlarmDate(AlarmDateBean pullAlarmDate) {
        mPullAlarmDate = pullAlarmDate;
    }

    public void setInstallAlarmDate(AlarmDateBean installAlarmDate) {
        mInstallAlarmDate = installAlarmDate;
    }

    /**
     * 设置闹钟
     *
     * @param context
     * @param timeInMillis
     * @param intent
     */
    public static void setAlarm(Context context, long timeInMillis, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, (int) intent.getLongExtra("id", 0),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int interval = (int) intent.getLongExtra("intervalMillis", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender);
        }
    }

    /**
     * 设置闹钟
     *
     * @param context
     * @param calendar 日期
     * @param id
     */
    public static void setAlarm(Context context, Calendar calendar, int id, AlarmType alarmType) {
        try {
            Log.d(TAG, "设置alarmType:" + alarmType + "类型闹钟,响铃时间是:" + calendar.getTimeInMillis());
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = null;
            if (alarmType == AlarmType.UPGRADE) {
                Log.d(TAG, "2 setAlarm upgrade alarm");
                intent = new Intent(Constants.ALARM_ACTION_UPGRADE);
            } else if (alarmType == AlarmType.INSTALLER) {
                Log.d(TAG, "2 setAlarm installer alarm");
                intent = new Intent(Constants.ALARM_ACTION_INSTALLER);
            }
            intent.putExtra("alarmTimes", calendar.getTimeInMillis());
//            intent.putExtra("alarmType", alarmType);

            //        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent
            //                .FLAG_CANCEL_CURRENT);

            //解决闹钟接收不到广播bug和延时
            PendingIntent sender = PendingIntent.getBroadcast(context, (int) id, intent, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //            am.setWindow(AlarmManager.RTC_WAKEUP, triggerTime(week, calendar.getTimeInMillis()),
                //                    intervalMillis, sender);

                //解决闹钟接收不到广播bug和延时
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            }
            String timeString = formatDayAndTime(context, calendar);
            Log.i(TAG, "timeString = " + timeString);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "设置闹钟错误: " + e.getMessage());
        }
    }


    public static String formatDayAndTime(final Context context, Calendar c) {
        String format = get24HourMode(context) ? DM24 : DM12;
        return (c == null) ? "" : (String) DateFormat.format(format, c);
    }

    public static boolean get24HourMode(final Context context) {
        return DateFormat.is24HourFormat(context);
    }

    /**
     * 设置每隔15分钟获取更新配置的闹钟
     */
    public void setPullUpdateAlarm() {
        Log.d(TAG, "1 setAlarm upgrade alarm: mPullAlarmDate = " + mPullAlarmDate);
        if (mPullAlarmDate != null) {
            Log.d(TAG, "setPullUpdateAlarm year: " + mPullAlarmDate.getYear() + " ,month: " + mPullAlarmDate.getMonth() + " ,day: " + mPullAlarmDate.getDay() + " ,hour: " + mPullAlarmDate.getHour() + " ,minute: " + mPullAlarmDate.getMinute() + " ,second: " + mPullAlarmDate.getSecond());
            Calendar calendar = Calendar.getInstance(Locale.CHINA);//查询出来的时间
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            String timeString = formatDayAndTime(mContext, calendar);
            long millis = calendar.getTimeInMillis();
            Log.i(TAG, "当前日期: " + year + "年" + (month + 1) + "月" + day + "日 " + hour + "时" + minute + "分" + second + "秒");
//        Log.i(TAG, "当前时间: " + timeString);
//        Log.i(TAG, "当前时间毫秒数: " + millis);

            calendar.set(Calendar.YEAR, year + mPullAlarmDate.getYear());
            calendar.set(Calendar.MONTH, month + mPullAlarmDate.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, day + mPullAlarmDate.getDay());
            calendar.set(Calendar.HOUR_OF_DAY, hour + mPullAlarmDate.getHour());
            calendar.set(Calendar.MINUTE, minute + mPullAlarmDate.getMinute());
            calendar.set(Calendar.SECOND, second + mPullAlarmDate.getSecond());
            calendar.setTimeInMillis(calendar.getTimeInMillis());


            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            second = calendar.get(Calendar.SECOND);
            timeString = formatDayAndTime(mContext, calendar);
            millis = calendar.getTimeInMillis();
            Log.i(TAG, "响铃时间: " + year + "年" + (month + 1) + "月" + day + "日 " + hour + "时" + minute + "分" + second + "秒");
//        Log.i(TAG, "响铃时间: " + timeString);
//        Log.i(TAG, "响铃时间毫秒数: " + millis);

            //设置闹钟
            setAlarm(mContext, calendar, 0, AlarmType.UPGRADE);
        } else {
            Log.d(TAG, "mPullAlarmDate is null");
        }
    }

    /**
     * 设置升级更新的闹钟
     */
    public void setInstallerAlarm() {
        Log.d(TAG, "1 setAlarm installer alarm : mInstallAlarmDate = " + mInstallAlarmDate);
        if (mInstallAlarmDate != null) {
            Log.d(TAG, "setInstallerAlarm year: " + mInstallAlarmDate.getYear() + " ,month: " + mInstallAlarmDate.getMonth() + " ,day: " + mInstallAlarmDate.getDay() + " ,hour: " + mInstallAlarmDate.getHour() + " ,minute: " + mInstallAlarmDate.getMinute() + " ,second: " + mInstallAlarmDate.getSecond());

            Calendar calendar = Calendar.getInstance(Locale.CHINA);//查询出来的时间
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            String timeString = formatDayAndTime(mContext, calendar);
            long millis = calendar.getTimeInMillis();
            Log.i(TAG, "当前日期: " + year + "年" + (month + 1) + "月" + day + "日 " + hour + "时" + minute + "分" + second + "秒");
//        Log.i(TAG, "当前时间: " + timeString);
//        Log.i(TAG, "当前时间毫秒数: " + millis);

            calendar.set(Calendar.YEAR, year + mInstallAlarmDate.getYear());
            calendar.set(Calendar.MONTH, month + mInstallAlarmDate.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, day + mInstallAlarmDate.getDay());
            calendar.set(Calendar.HOUR_OF_DAY, hour + mInstallAlarmDate.getHour());
            calendar.set(Calendar.MINUTE, minute + mInstallAlarmDate.getMinute());
            calendar.set(Calendar.SECOND, second + mInstallAlarmDate.getSecond());
            calendar.setTimeInMillis(calendar.getTimeInMillis());


            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            second = calendar.get(Calendar.SECOND);
            timeString = formatDayAndTime(mContext, calendar);
            millis = calendar.getTimeInMillis();
            Log.i(TAG, "响铃时间: " + year + "年" + (month + 1) + "月" + day + "日 " + hour + "时" + minute + "分" + second + "秒");
//        Log.i(TAG, "响铃时间: " + timeString);
//        Log.i(TAG, "响铃时间毫秒数: " + millis);

            //设置闹钟
            setAlarm(mContext, calendar, 0, AlarmType.INSTALLER);
        } else {
            Log.d(TAG, "mInstallAlarmDate is null");
        }
    }
}

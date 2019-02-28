package com.aispeech.tvui.common.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TimeUtils {
    private static String TAG = "TimeUtils";
    private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 判断字符串是否为日期字符串
     *
     * @param datevalue
     * @return
     */
    public static boolean isDateString(String datevalue) {
        if (TextUtils.isEmpty(datevalue)) {
            return false;
        }
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
            Date dd = fmt.parse(datevalue);
            String format = fmt.format(dd);
            Log.i(TAG, "format: " + format);
            if (datevalue.equals(format)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 返回字符串时间的年
     *
     * @param datevalue 字符串时间  "2017-06-03 23:32:31"
     * @return 返回年
     */
    public static int getYear(String datevalue) {
        int year = 0;
        if (isDateString(datevalue)) {
            try {
                Calendar cal = Calendar.getInstance(Locale.CHINA);
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                Date date = format.parse(datevalue);
                cal.setTime(date);
                year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);
                Log.d(TAG, "year: " + year + " ,month: " + month + " ,day: " + day + " ,hour: " + hour + " ,minute: " + minute + " ,second: " + second);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "字符串不是日期字符串");
        }
        return year;
    }

    /**
     * 返回字符串时间的月
     *
     * @param datevalue 字符串时间  "2017-06-03 23:32:31"
     * @return 返回月
     */
    public static int getMonth(String datevalue) {
        int month = 0;
        if (isDateString(datevalue)) {
            try {
                Calendar cal = Calendar.getInstance(Locale.CHINA);
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                Date date = format.parse(datevalue);
                cal.setTime(date);
                month = cal.get(Calendar.MONTH) + 1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "字符串不是日期字符串");
        }
        return month;
    }

    /**
     * 返回字符串时间的日
     *
     * @param datevalue 字符串时间  "2017-06-03 23:32:31"
     * @return 返回日
     */
    public static int getDay(String datevalue) {
        int day = 0;
        if (isDateString(datevalue)) {
            try {
                Calendar cal = Calendar.getInstance(Locale.CHINA);
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                Date date = format.parse(datevalue);
                cal.setTime(date);
                day = cal.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "字符串不是日期字符串");
        }
        return day;
    }

    /**
     * 返回字符串时间的时
     *
     * @param datevalue 字符串时间  "2017-06-03 23:32:31"
     * @return 返回时
     */
    public static int getHour(String datevalue) {
        int hour = 0;
        if (isDateString(datevalue)) {
            try {
                Calendar cal = Calendar.getInstance(Locale.CHINA);
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                Date date = format.parse(datevalue);
                cal.setTime(date);
                hour = cal.get(Calendar.HOUR_OF_DAY);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "字符串不是日期字符串");
        }
        return hour;
    }

    /**
     * 返回字符串时间的分
     *
     * @param datevalue 字符串时间  "2017-06-03 23:32:31"
     * @return 返回分
     */
    public static int getMinute(String datevalue) {
        int minute = 0;
        if (isDateString(datevalue)) {
            try {
                Calendar cal = Calendar.getInstance(Locale.CHINA);
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                Date date = format.parse(datevalue);
                cal.setTime(date);
                minute = cal.get(Calendar.MINUTE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "字符串不是日期字符串");
        }
        return minute;
    }

    /**
     * 返回字符串时间的秒
     *
     * @param datevalue 字符串时间  "2017-06-03 23:32:31"
     * @return 返回秒
     */
    public static int getSecond(String datevalue) {
        int second = 0;
        if (isDateString(datevalue)) {
            try {
                Calendar cal = Calendar.getInstance(Locale.CHINA);
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                Date date = format.parse(datevalue);
                cal.setTime(date);
                second = cal.get(Calendar.SECOND);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "字符串不是日期字符串");
        }
        return second;
    }

    private static String splitYearMonthDay(String dateValue) {
        String yearMonthDay = null;
        if (!TextUtils.isEmpty(dateValue)) {
            String[] dates = dateValue.split(" ");
            if (dates.length > 0) {
                for (int i = 0; i < dates.length; i++) {
                    String date = dates[i];
//                    Log.i(TAG, "dates[" + i + "]" + " = " + date);
                    yearMonthDay = dates[0];
                }
            }
        }
        return yearMonthDay;
    }

    private static String splitHourMinuteSecond(String dateValue) {
        String yearMonthDay = null;
        if (!TextUtils.isEmpty(dateValue)) {
            String[] dates = dateValue.split(" ");
            if (dates.length > 0) {
                for (int i = 0; i < dates.length; i++) {
                    String date = dates[i];
//                    Log.i(TAG, "dates[" + i + "]" + " = " + date);
                    yearMonthDay = dates[1];
                }
            }
        }
        return yearMonthDay;
    }

    public static int getYears(String dateValue) {
        int year = 0;
        try {
            String yearMonthDay = splitYearMonthDay(dateValue);
            String[] dates = yearMonthDay.split("-");
            if (dates.length > 0) {
                for (int i = 0; i < dates.length; i++) {
                    year = Integer.parseInt(dates[0]);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return year;
    }

    public static int getMonths(String dateValue) {
        int month = 0;
        try {
            String yearMonthDay = splitYearMonthDay(dateValue);
            String[] dates = yearMonthDay.split("-");
            if (dates.length > 0) {
                for (int i = 0; i < dates.length; i++) {
                    month = Integer.parseInt(dates[1]);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return month;
    }

    public static int getDays(String dateValue) {
        int day = 0;
        try {
            String yearMonthDay = splitYearMonthDay(dateValue);
            String[] dates = yearMonthDay.split("-");
            if (dates.length > 0) {
                for (int i = 0; i < dates.length; i++) {
                    day = Integer.parseInt(dates[2]);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return day;
    }

    public static int getHours(String dateValue) {
        int hour = 0;
        try {
            String yearMonthDay = splitHourMinuteSecond(dateValue);
            String[] dates = yearMonthDay.split(":");
            if (dates.length > 0) {
                for (int i = 0; i < dates.length; i++) {
                    hour = Integer.parseInt(dates[0]);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return hour;
    }

    public static int getMinutes(String dateValue) {
        int minute = 0;
        try {
            String yearMonthDay = splitHourMinuteSecond(dateValue);
            String[] dates = yearMonthDay.split(":");
            if (dates.length > 0) {
                for (int i = 0; i < dates.length; i++) {
                    minute = Integer.parseInt(dates[1]);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return minute;
    }

    public static int getSeconds(String dateValue) {
        int seconds = 0;
        try {
            String yearMonthDay = splitHourMinuteSecond(dateValue);
            String[] dates = yearMonthDay.split(":");
            if (dates.length > 0) {
                for (int i = 0; i < dates.length; i++) {
                    seconds = Integer.parseInt(dates[2]);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return seconds;
    }
}

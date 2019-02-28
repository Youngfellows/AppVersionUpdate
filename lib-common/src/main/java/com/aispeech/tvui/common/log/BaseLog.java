package com.aispeech.tvui.common.log;

import android.text.TextUtils;
import android.util.Log;



public class BaseLog {

    public static final int V = 1;
    public static final int D = 2;
    public static final int I = 3;
    public static final int W = 4;
    public static final int E = 5;
    protected static final int A = 6;

    public BaseLog() {
    }

    protected static void printLine(String tag, boolean lineType) {
        if(lineType) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }

    }

    protected static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str) || str.equals("\n") || str.equals("\t") || TextUtils.isEmpty(str.trim());
    }

    public static void printDefault(int logLevel, String tag, String msg) {
        switch(logLevel) {
            case V:
                Log.v(tag, msg);
                break;
            case D:
                Log.d(tag, msg);
                break;
            case I:
                Log.i(tag, msg);
                break;
            case W:
                Log.w(tag, msg);
                break;
            case E:
                Log.e(tag, msg);
                break;
            case A:
                Log.wtf(tag, msg);
            default:
        }
    }


}

package com.aispeech.tvui.common.util;

import android.util.Log;

import com.aispeech.tvui.common.log.BaseLog;
import com.aispeech.tvui.common.log.JsonLog;

public class TLog {

    private static int currentLogLevel = 4;
    private static final String LOG_PREFIX = "TVUI";

    public static final int V = 1;
    public static final int D = 2;
    public static final int I = 3;
    public static final int W = 4;
    public static final int E = 5;
    protected static final int A = 6;
    protected static final int JSON = 7;

    public TLog() {
        super();
    }


    /**
     * 封装原生Log.v  使用默认标签
     * @param msg 要打印的内容（Object 类型）
     */
    public static void v(Object msg) {
        if(msg != null) {
            printLog(V, "", msg.toString());
        }
    }

    /**
     * 封装原生Log.v  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（Object 类型）
     */
    public static void v(String tag, Object msg) {
        if(msg != null) {
            printLog(V, tag, msg.toString());
        }
    }

    /**
     * 封装原生Log.v  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     */
    public static void v(String tag, String msg) {
        if(msg != null) {
            printLog(V, tag, msg);
        }
    }

    /**
     * 封装原生Log.v  使用默认标签（频繁调用）
     * @param msg 要打印的内容（Object类型）
     */
    public static void vHighFreq(Object msg) {
        if(msg != null) {
            printLogHighFreq(V, "", msg.toString());
        }
    }

    /**
     * 封装原生Log.v  使用默认标签 + 自定义标签（频繁调用）
     * @param tag 自定义标签
     * @param msg 要打印的内容（Object类型）
     */
    public static void vHighFreq(String tag, Object msg) {
        if(msg != null) {
            printLogHighFreq(V, tag, msg.toString());
        }
    }

    /**
     * 封装原生Log.v  使用默认标签 + 自定义标签（频繁调用）
     * @param tag 自定义标签
     * @param msg 要打印的内容（String 类型）
     */
    public static void vHighFreq(String tag, String msg) {
        if(msg != null) {
            printLogHighFreq(V, tag, msg);
        }
    }

    /**
     * 封装原生Log.d  使用默认标签
     * @param msg 要打印的内容（Object 类型）
     */
    public static void d(Object msg) {
        if(msg != null) {
            printLog(D, "", msg.toString());
        }
    }

    /**
     * 封装原生Log.d  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（Object 类型）
     */
    public static void d(String tag, Object msg) {
        if(msg != null) {
            printLog(D, tag, msg.toString());
        }
    }

    /**
     * 封装原生Log.d  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     */
    public static void d(String tag, String msg) {
        if(msg != null) {
            printLog(D, tag, msg);
        }
    }

    /**
     * 封装原生Log.d  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     * @param throwable 要打印的堆栈信息
     */
    public static void d(String tag, String msg, Throwable throwable) {
        if(msg != null) {
            printLog(D, tag, msg + '\n' + Log.getStackTraceString(throwable));
        }
    }

    /**
     * 封装原生Log.d  使用默认标签（频繁调用 与非频繁调用区别是：前缀不同）
     * @param msg 要打印的内容（Object 类型）
     */
    public static void dHighFreq(Object msg) {
        if(msg != null) {
            printLogHighFreq(D, "", msg.toString());
        }
    }

    /**
     * 封装原生Log.d  使用默认标签+自定义标签（频繁调用）
     * @param tag 标签
     * @param msg 要打印的内容（Object 类型）
     */
    public static void dHighFreq(String tag, Object msg) {
        if(msg != null) {
            printLogHighFreq(D, tag, msg.toString());
        }
    }

    /**
     * 封装原生Log.d  使用默认标签+自定义标签（频繁调用）
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     */
    public static void dHighFreq(String tag, String msg) {
        if(msg != null) {
            printLogHighFreq(D, tag, msg);
        }
    }

    /**
     * 封装原生Log.d  使用默认标签+自定义标签（频繁调用）
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     * @param throwable 要打印的堆栈信息
     */
    public static void dHighFreq(String tag, String msg, Throwable throwable) {
        if(msg != null) {
            printLogHighFreq(D, tag, msg + '\n' + Log.getStackTraceString(throwable));
        }
    }

    /**
     * 封装原生Log.i  使用默认标签
     * @param msg 要打印的内容（Object 类型）
     */
    public static void i(Object msg) {
        if(msg != null) {
            printLog(I, "", msg.toString());
        }
    }

    /**
     * 封装原生Log.i 使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（Object 类型）
     */
    public static void i(String tag, Object msg) {
        if(msg != null) {
            printLog(I, tag, msg.toString());
        }
    }

    /**
     * 封装原生Log.i  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     */
    public static void i(String tag, String msg) {
        if(msg != null) {
            printLog(I, tag, msg);
        }
    }

    /**
     * 封装原生Log.i  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     * @param throwable 要打印的堆栈信息
     */
    public static void i(String tag, String msg, Throwable throwable) {
        if(msg != null) {
            printLog(I, tag, msg + '\n' + Log.getStackTraceString(throwable));
        }
    }

    /**
     * 封装原生Log.i  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     * @param bytes 字节数组
     */
    public static void i(String tag, String msg, byte[]... bytes) {
        for(int i = 0; i < bytes.length; ++i) {
            msg = msg + "\nargs[" + i + "]: " + new String(bytes[i]);
        }
        printLog(I, tag, msg);
    }

    /**
     *  封装原生Log.i  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容
     * @param strings String数组
     */
    public static void i(String tag, String msg, String[] strings) {
        for(int i = 0; i < strings.length; ++i) {
            msg = msg + "\nargs[" + i + "]: " + strings[i];
        }
        printLog(I, tag, msg);
    }

    /**
     * 封装原生Log.w  使用默认标签
     * @param msg 要打印的内容（Object 类型）
     */
    public static void w(Object msg) {
        if(msg != null) {
            printLog(W, "", msg.toString());
        }
    }

    /**
     * 封装原生Log.w  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（Object 类型）
     */
    public static void w(String tag, Object msg) {
        if(msg != null) {
            printLog(W, tag, msg.toString());
        }
    }

    /**
     * 封装原生Log.w  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     */
    public static void w(String tag, String msg) {
        if(msg != null) {
            printLog(W, tag, msg);
        }
    }

    /**
     * 封装原生Log.w  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     * @param throwable 要打印的堆栈信息
     */
    public static void w(String tag, String msg, Throwable throwable) {
        if(msg != null) {
            printLog(W, tag, msg + '\n' + Log.getStackTraceString(throwable));
        }
    }

    /**
     * 封装原生Log.e  使用默认标签
     * @param msg 要打印的内容（Object 类型）
     */
    public static void e(Object msg) {
        if(msg != null) {
            printLog(E, "", msg.toString());
        }
    }

    /**
     * 封装原生Log.e  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（Object 类型）
     */
    public static void e(String tag, Object msg) {
        if(msg != null) {
            printLog(E, tag, msg.toString());
        }
    }

    /**
     * 封装原生Log.e  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     */
    public static void e(String tag, String msg) {
        if(msg != null) {
            printLog(E, tag, msg);
        }
    }

    /**
     * 封装原生Log.e  使用默认标签+自定义标签
     * @param tag 标签
     * @param msg 要打印的内容（String 类型）
     * @param throwable 要打印的堆栈信息
     */
    public static void e(String tag, String msg, Throwable throwable) {
        if(msg != null) {
            printLog(E, tag, msg + '\n' + Log.getStackTraceString(throwable));
        }
    }

    /**
     * 封装原生Log.wtf 使用默认标签
     * @param msg 要打印的信息
     */
    public static void a(Object msg) {
        if(msg != null) {
            printLog(A, "", msg.toString());
        }
    }

    /**
     * 封装原生Log.wtf 使用默认标签
     * @param tag 标签
     * @param msg 要打印的信息（Object类型）
     */
    public static void a(String tag, Object msg) {
        if(msg != null) {
            printLog(A, tag, msg.toString());
        }
    }

    /**
     * 封装原生Log.wtf 使用默认标签 + 自定义标签
     * @param tag 标签
     * @param msg 要打印的信息（String 类型）
     */
    public static void a(String tag, String msg) {
        if(msg != null) {
            printLog(A, tag, msg);
        }
    }

    /**
     * 封装原生Log.wtf 使用默认标签 + 自定义标签（打印不受LogLevel限制，没有开关）
     * @param tag 标签
     * @param msg 要打印的信息
     */
    public static void wtf(String tag, String msg) {
        if(msg != null) {
            Log.wtf(tag, msg);
        }
    }

    /**
     * 封装原生Log.wtf 使用默认标签 + 自定义标签（打印不受LogLevel限制，没有开关）
     * @param tag 标签
     * @param throwable 日志的异常
     */
    public static void wtf(String tag, Throwable throwable) {
        if(throwable != null) {
            Log.wtf(tag, throwable);
        }
    }

    /**
     * 封装原生Log.wtf 使用默认标签 + 自定义标签（打印不受LogLevel限制，没有开关）
     * @param tag 标签
     * @param msg 要打印的信息
     * @param throwable 日志的异常
     */
    public static void wtf(String tag, String msg, Throwable throwable) {
        if(msg != null) {
            Log.wtf(tag, msg, throwable);
        }
    }

    /**
     * 打印JSON 使用默认标签(logLevel <= 3 时候才能打印)
     * @param msg 要打印的内容
     */
    public static void json(String msg) {
        printLog(JSON, "", msg);
    }

    /**
     * 打印JSON 使用默认标签 + 自定义标签(logLevel <= 3 时候才能打印)
     * @param tag 标签
     * @param msg 要打印的信息
     */
    public static void json(String tag, String msg) {
        printLog(JSON, tag, msg);
    }

    /**
     * 分段式打印 （使用 Log.i 级别打印）
     * @param tag 标签
     * @param msg 要打印的信息
     */
    @Deprecated
    public static void snipet(String tag, String msg) {
        short snippetNum = 1000;

        for(int i = 0; i <= msg.length() / snippetNum; ++i) {
            int startPos = i * snippetNum;
            int endPos = (i + 1) * snippetNum;
            endPos = endPos > msg.length()?msg.length():endPos;
            printLog(I, tag, msg.substring(startPos, endPos));
        }
    }

    /**
     * 高频率打印调用方法
     * @param logLevel 打印级别
     * @param tag 标签
     * @param msg 要打印的信息
     */
    private static synchronized void printLogHighFreq(int logLevel, String tag, String msg) {
        printLog(logLevel, tag, msg, true);
    }

    /**
     * 普通打印调用方法
     * @param logLevel 打印级别
     * @param tag 标签
     * @param msg 要打印的信息
     */
    private static synchronized void printLog(int logLevel, String tag, String msg) {
        printLog(logLevel, tag, msg, false);
    }

    /**
     * 具体打印实现方法
     * @param logLevel 打印级别
     * @param tag 标签
     * @param msg 要打印的内容
     * @param isHighFreq 是否是高频率打印
     */
    private static synchronized void printLog(int logLevel, String tag, String msg, boolean isHighFreq) {
        boolean isPrint = (logLevel != JSON || currentLogLevel <= I) && currentLogLevel <= logLevel;
        if(isPrint) {
            String curMsg = msg == null?"(null)":msg;
            String printMsg = (currentLogLevel == 1?Float.valueOf((float)System.nanoTime() / 1.0E9F):"") + "\t" + "" + curMsg;
            switch(logLevel) {
                case V:
                case D:
                case I:
                case W:
                case E:
                case A:
                    BaseLog.printDefault(logLevel, "[" + LOG_PREFIX + "]" + (isHighFreq?"HF-":"N-") + tag, printMsg);
                    break;
                case JSON:
                    if(currentLogLevel == 1) {
                        JsonLog.printJson("[" + LOG_PREFIX + "]HF-" + tag, (float)System.nanoTime() / 1.0E9F + "\t" + curMsg, "");
                    } else {
                        JsonLog.printJson("[" + LOG_PREFIX + "]HF-" + tag, curMsg, "");
                    }
                default:
            }
        }
    }

    /**
     * 获取当前的Log级别
     * @return log级别
     */
    public static int getLogLevel() {
        return currentLogLevel;
    }

    /**
     * 设置Log 级别
     * @param logLevel log级别
     */
    public static void setLogLevel(int logLevel) {
        if(currentLogLevel != logLevel) {
            if(logLevel > 5) {
                printLog(5, "TLog", "Set log level failed . wrong log level");
            }

            w("TLog", "log.level " + currentLogLevel + "->" + logLevel);
            currentLogLevel = logLevel;
        }
    }

}

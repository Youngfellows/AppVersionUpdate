package com.aispeech.tvui.common.prop;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexFile;

/**
 * Created by huijieZ on 2018/9/18.
 * @author huijieZ
 */

public class SystemPropertiesProxy {

    private SystemPropertiesProxy() {
    }


    /**
     * 获取配置文件路径
     * @param context 上下文
     * @param confPath
     * @return TVUI config path
     * @throws IllegalArgumentException 异常
     */
    public static String get(Context context, String confPath) {
        String tvuiConfPath = "";

        try {
            ClassLoader classLoader = context.getClassLoader();
            Class aClass = classLoader.loadClass("android.os.SystemProperties");
            Class[] classes = new Class[]{String.class};
            Method method = aClass.getMethod("get", classes);
            Object[] objects = new Object[]{new String(confPath)};
            tvuiConfPath = (String)method.invoke(aClass, objects);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw illegalArgumentException;
        } catch (Exception exception) {
            tvuiConfPath = "";
        }

        return tvuiConfPath;
    }

    /**
     * 获取配置文件路径
     * @param context 上下文
     * @param confPath
     * @param dirStr
     * @return TVUI config path
     */
    public static String get(Context context, String confPath, String dirStr) {
        String tvuiConfPath;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class aClass = classLoader.loadClass("android.os.SystemProperties");
            Class[] classes = new Class[]{String.class, String.class};
            Method method = aClass.getMethod("get", classes);
            Object[] objects = new Object[]{new String(confPath), new String(dirStr)};
            tvuiConfPath = (String)method.invoke(aClass, objects);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw illegalArgumentException;
        } catch (Exception exception) {
            tvuiConfPath = dirStr;
        }

        return tvuiConfPath;
    }

}

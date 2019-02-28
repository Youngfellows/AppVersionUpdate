package com.aispeech.tvui.common.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class AppVersionUtils {
    public final String TAG = AppVersionUtils.class.getSimpleName();

    /**
     * 判断指定包名的APP是否已经安装
     *
     * @param context
     * @param pckName 包名
     * @return
     */
    public static boolean isInstalledApk(Context context, String pckName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<String> packageNames = new ArrayList<String>();

        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }

        return packageNames.contains(pckName);
    }


    /**
     * 获取指定包信息
     *
     * @param context
     * @return PackageInfo
     */
    private static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取指定包信息
     *
     * @param context
     * @param packageName 应用包名
     * @return
     */
    private static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            return context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo.packageName;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo.versionName;
    }


    /**
     * 获取版本名称
     *
     * @param context
     * @param packageName 应用包名
     * @return 版本名称
     */
    public static String getVersionName(Context context, String packageName) {
        PackageInfo packageInfo = getPackageInfo(context, packageName);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo.versionCode;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @param packageName 应用包名
     * @return 版本号
     */
    public static int getVersionCode(Context context, String packageName) {
        PackageInfo packageInfo = getPackageInfo(context, packageName);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return 0;
    }
}

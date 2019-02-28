package com.aispeech.tvui.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.PrintWriter;



public class InstallUtil {
    private static String TAG = "InstallUtil";

    /**
     * @param context
     * @param apkPath  要安装的APK
     * @param rootMode 是否是Root模式
     */
    public static void install(Context context, String apkPath, boolean rootMode) {
        if (rootMode) {
            installRoot(context, apkPath);
        } else {
            installNormal(context, apkPath);
        }
    }

    /**
     * 通过非Root模式安装
     *
     * @param context
     * @param apkPath 安装的apk路径
     */
    public static void install(Context context, String apkPath) {
        install(context, apkPath, false);
    }


    /**
     * 普通安装
     *
     * @param context 上下午
     * @param apkPath 安装的apk路径
     */
    private static void installNormal(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            File file = (new File(apkPath));
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.aispeech.tvui.adapter", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                    "application/vnd.android.package-archive");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 通过Root方式安装
     *
     * @param context
     * @param apkPath
     * @return true:安装成功 false:安装失败
     */
    public static boolean installRoot(final Context context, final String apkPath) {
        Log.d(TAG, "installRoot 使用静默方式升级");
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("/system/bin/sh", null, new File("/system/bin"));
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 " + apkPath);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r com.aispeech.tvui --user 0 " + apkPath);
//          PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            Log.i(TAG, "静默方式升级: " + (0 == value ? "成功" : "失败"));
            return 0 == value ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 卸载app
     *
     * @param packageName
     * @param context
     * @return
     */
    public static boolean uninstallApp(Context context, String packageName) {
        if (RootUtil.isRooted()) {
            // 有root权限，利用静默卸载实现
            Log.i(TAG, "clientUninstall");
            return clientUninstall(packageName);
        } else {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstallIntent);
            Log.i(TAG, "intentUninstall");
            return true;
        }
    }

    /**
     * 静默卸载
     */
    public static boolean clientUninstall(String packageName) {
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("/system/bin/sh", null, new File("/system/bin"));
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
            PrintWriter.println("pm uninstall " + packageName);
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return 0 == value ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }
}

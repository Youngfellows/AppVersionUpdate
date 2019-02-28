package com.aispeech.upgrade.utils;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bifeng on 2018/4/28.
 */
public class ApkUtil {

    private static final String TAG = "ApkUtil";

    public static final int INSTALL_SUCCESS = 8880000;// 安装成功
    public static final int INSTALL_SUCCESS_24 = 8880000;// 安装成功

    public static final int INSTALL_FILE_NOT_EXIT = 8880021;// 文件不存在
    public static final int INSTALL_NO_PERMISSON = 8880022;// 没有INSTALL权限
    public static final int INSTALL_BAD_APKPATH = 8880023;// apkPath有错误
    public static final int INSTALL_ERROR = 8880024;// 安装时出错

    public static final int INSTALL_THROW_EXCEPTION = 8880025; //安装时抛出异常
    public static final int START_LAUACTIVITY_SUCCESS = 8880026; // 安装后启动service或activity成功


    // 安装失败时反馈的message
    private static final String CONTAINER_ERROR = "[INSTALL_FAILED_CONTAINER_ERROR]";
    private static final String DEXOPT = "[INSTALL_FAILED_DEXOPT]";
    private static final String INSUFFICIENT_STORAGE = "[INSTALL_FAILED_INSUFFICIENT_STORAGE]";
    private static final String INTERNAL_ERROR = "[INSTALL_FAILED_INTERNAL_ERROR]";
    private static final String INVALID_APK = "[INSTALL_FAILED_INVALID_APK]";
    private static final String INVALID_URI = "[INSTALL_FAILED_INVALID_URI]";
    private static final String UID_CHANGED = "[INSTALL_FAILED_UID_CHANGED]";
    private static final String NO_CERTIFICATES = "[INSTALL_PARSE_FAILED_NO_CERTIFICATES]";
    private static final String UNEXPECTED_EXCEPTION = "[INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION]";


    public static final int CONTAINER_ERROR_STATUS = 8880080;
    public static final int DEXOPT_STATUS = 8880081;
    public static final int INSUFFICIENT_STORAGE_STATUS = 8880082;
    public static final int INTERNAL_ERROR_STATUS = 8880083;
    public static final int INVALID_APK_STATUS = 8880084;
    public static final int INVALID_URI_STATUS = 8880085;
    public static final int UID_CHANGED_STATUS = 8880086;
    public static final int NO_CERTIFICATES_STATUS = 8880087;
    public static final int UNEXPECTED_EXCEPTION_STATUS = 8880088;


    public static final int APK_INSTALL_MANUAL = 8880089;// 调用系统手动安装接口
    private static final String APK_INSTALL_DATA_AND_TYPE =
            "application/vnd.android.package-archive";
    private static final String COMMAND_STR_CHMOD = "chmod";
    private static final String COMMAND_STR_FILE_RWD = "777";


    public static final int DELETE_SUCCESS = 8880000;// 卸载成功
    public static final int DELETE_SUCCESS_24 = 8880000;// android N 卸载成功
    public static final int DELETE_LACK_OF_INF = 8880051;// lack of inf
    public static final int DELETE_NO_PERMISSION = 8880052;// 没有uninstall权限
    public static final int DELETE_THROW_EXCEPTION = 8880053;// 卸载时抛出异常
    public static final int DELETE_ERROR = 8880054;// 卸载时出错


    /**
     *
     * @param context
     * @param apkPath
     * @param pkgName
     * @param status 是否静默安装
     * @return
     */
    public static ResultInfo install(Context context, String apkPath, String pkgName, int status){
        ResultInfo resultInfo = new ResultInfo();

        if (TextUtils.isEmpty(apkPath)) {
            Log.d("ContextUtils", "download complete intent has no path param");
            resultInfo.add(INSTALL_BAD_APKPATH, "bad apk path");
            return resultInfo;
        }

        File file = new File(apkPath);
        if (!file.exists()) {
            Log.d("ContextUtils","file %s not exists"+ apkPath);
            resultInfo.add(INSTALL_FILE_NOT_EXIT, "apk file not exit");
            return resultInfo;
        }

        if(status==0){
            resultInfo = installSilent(context,apkPath,pkgName);
        }else {
            installApkManual(context,new File(apkPath));
            resultInfo.add(APK_INSTALL_MANUAL,"apk install manual");
        }
        return resultInfo;

    }



    /**
     * 安装apk文件
     */
    public static void installApkManual(Context context,File apkFile) {
        if (null == apkFile || null == context) {
            Log.e(TAG,"installApkManual apkFile is null");
            return;
        }
        makeFileAccess(apkFile);
        Uri uri = Uri.fromFile(apkFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, APK_INSTALL_DATA_AND_TYPE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 设置文件可读写权限
     *
     * @param apkFile
     */
    public static void makeFileAccess(File apkFile) {
        if (null == apkFile) {
            Log.e(TAG,"makeFileAccess apkFile is null");
            return;
        }
        String[] command = {COMMAND_STR_CHMOD, COMMAND_STR_FILE_RWD, apkFile.getPath()};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置文件可读写权限
     *
     * @param apkFile
     */
    public static void makeFileAccess(File apkFile,String permission) {
        if (null == apkFile) {
            Log.e(TAG,"makeFileAccess apkFile is null");
            return;
        }
        String[] command = {COMMAND_STR_CHMOD, permission, apkFile.getPath()};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置文件可读写权限
     * //如果是系统目录，需要权限，shareuserid 或特定目录
     * @param apkFile
     */
    public static void makeDirByCommad(File apkFile) {
        String[] command = {"mkdir",apkFile.getParentFile().getAbsolutePath()};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            processBuilder.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static ResultInfo installSilent(Context context, String apkPath, String pkgName) {
        ResultInfo resultInfo = new ResultInfo();


        // 没有安装权限
        if (!hasInstallPermission(context)) {
            Log.w(TAG, "install failed: no install permission granted");
            resultInfo.add(INSTALL_NO_PERMISSON, "no install permission granted");
            return resultInfo;
        }

        if (Build.VERSION.SDK_INT < 24) {
            resultInfo = sysInstall(apkPath);
            Log.w(TAG, "install message:  " + resultInfo.message + "   " + apkPath);
            return resultInfo;
        }


        try {
            Uri uri = Uri.fromFile(new File(apkPath));
            BlockPackageInstallObserver observer = new BlockPackageInstallObserver(pkgName);
            PackageManager pm = context.getPackageManager(); // 得到pm对象
            // 通过反射，获取到PackageManager隐藏的方法installPackage
            Method installPackage = pm.getClass().getDeclaredMethod("installPackage", Uri.class,
                    IPackageInstallObserver.class, int.class, String.class);
            installPackage.invoke(pm, uri, observer, 2, pkgName);
            int result = observer.waitResult();
            if (result == 1) {
                resultInfo.add(INSTALL_SUCCESS_24, "install success");
            } else {
                resultInfo.add(result, "the install returnCode:" + result);
            }

        } catch (Exception e) {
            resultInfo.add(INSTALL_THROW_EXCEPTION, e.getClass().getName(), e.getMessage());
        }

        return resultInfo;
    }



    static class BlockPackageInstallObserver implements IPackageInstallObserver {
        private String mPackageName;
        private AtomicInteger mResult = new AtomicInteger(0);
        private volatile boolean mResultGot;

        BlockPackageInstallObserver(String packageName) {
            mPackageName = packageName;
        }

        private void setResult(int result) {
            synchronized (this) {
                mResult.set(result);
                mResultGot = true;
                notifyAll();
            }
        }

        public int waitResult() throws InterruptedException {
            synchronized (this) {
                while (!mResultGot) {
                    wait();
                }
            }

            return mResult.get();
        }

        @Override
        public void packageInstalled(String packageName, int returnCode) throws RemoteException {
            if (mPackageName.equals(packageName)) {
                setResult(returnCode);
            } else {
                setResult(returnCode);
            }

        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    }



    /**
     * 是否拥有安装权限
     */
    public static boolean hasInstallPermission(Context context) {
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();
            if (PackageManager.PERMISSION_GRANTED == packageManager
                    .checkPermission(Manifest.permission.INSTALL_PACKAGES, context.getPackageName())) {
                return true;
            }
        }

        return false;
    }



    /**
     * 系统级自动安装
     */
    private static ResultInfo sysInstall(String filePath) {
        ResultInfo resultInfo = new ResultInfo();
        String[] args = {"pm", "install", "-r", filePath};
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();

        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (IOException e) {
            resultInfo.add(INSTALL_THROW_EXCEPTION, e.getClass().getName(), e.getMessage());
        } catch (Exception e) {
            resultInfo.add(INSTALL_THROW_EXCEPTION, e.getClass().getName(), e.getMessage());
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {}

            if (process != null) {
                process.destroy();
            }
        }

        if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
            resultInfo.add(INSTALL_SUCCESS, "install success");
        } else {
            if (!TextUtils.isEmpty(errorMsg)) {
                if (errorMsg.indexOf("[") != -1 && errorMsg.lastIndexOf("]") != -1){
                    resultInfo = installError(errorMsg);
                } else {
                    resultInfo.add(INSTALL_ERROR, "", errorMsg.toString());
                }
            } else {
                resultInfo.add(INSTALL_ERROR, "", errorMsg.toString());
            }
        }
        return resultInfo;
    }


    private static ResultInfo installError(StringBuilder errorMsg) {
        ResultInfo resultInfo = new ResultInfo();
        String errorType = errorMsg.substring(errorMsg.indexOf("["), errorMsg.lastIndexOf("]") + 1);
        switch (errorType) {
            case CONTAINER_ERROR:
                resultInfo.add(CONTAINER_ERROR_STATUS, errorMsg.toString());
                break;
            case DEXOPT:
                resultInfo.add(DEXOPT_STATUS, errorMsg.toString());
                break;
            case INSUFFICIENT_STORAGE:
                resultInfo.add(INSUFFICIENT_STORAGE_STATUS, errorMsg.toString());
                break;
            case INTERNAL_ERROR:
                resultInfo.add(INTERNAL_ERROR_STATUS, errorMsg.toString());
                break;
            case INVALID_APK:
                resultInfo.add(INVALID_APK_STATUS, errorMsg.toString());
                break;
            case INVALID_URI:
                resultInfo.add(INVALID_URI_STATUS, errorMsg.toString());
                break;
            case UID_CHANGED:
                resultInfo.add(UID_CHANGED_STATUS, errorMsg.toString());
                break;
            case NO_CERTIFICATES:
                resultInfo.add(NO_CERTIFICATES_STATUS, errorMsg.toString());
                break;
            case UNEXPECTED_EXCEPTION:
                resultInfo.add(UNEXPECTED_EXCEPTION_STATUS, errorMsg.toString());
                break;
            default:
                resultInfo.add(INSTALL_ERROR, errorType, errorMsg.toString());
                break;
        }
        return resultInfo;
    }


    /**
     * 静默卸载应用
     *
     * @param pkgName
     * @return
     */
    public static ResultInfo deleteApkSilent(Context context, String pkgName) {
        Log.d(TAG, "start delete apk--pkgName: " + pkgName);

        ResultInfo resultInfo = new ResultInfo();
        if (context == null || TextUtils.isEmpty(pkgName)) {
            resultInfo.add(DELETE_LACK_OF_INF, "lack of inf");
            return resultInfo;
        }

        // apk已卸载
        if (!isApkInstalled(context, pkgName)) {
            resultInfo.add(DELETE_SUCCESS, "apk not exit");//DELETE_SUCCESS
            return resultInfo;
        }

        // 没有卸载权限
        if (!hasUninstallPermission(context)) {
            resultInfo.add(DELETE_NO_PERMISSION, "no uninstall permission granted");
            return resultInfo;
        }

        if (Build.VERSION.SDK_INT < 23) {
            resultInfo = uninstall(pkgName);
            return resultInfo;
        }


        try {
            MyPackageDeleteObserver observer = new MyPackageDeleteObserver();
            PackageManager pm = context.getPackageManager(); // 得到pm对象
            // 通过反射，获取到PackageManager隐藏的方法deletePackage()
            Method deletePackage = pm.getClass().getDeclaredMethod("deletePackage", String.class,
                    IPackageDeleteObserver.class, int.class);
            deletePackage.invoke(pm, pkgName, observer, 2);
            int result = observer.waitResult();
            if (result == DELETE_SUCCESS_24) {
                resultInfo.add(DELETE_SUCCESS_24, "delete success");
            } else {
                resultInfo.add(result, "the uninstall returnCode:" + result);
            }
        } catch (Exception e) {
            resultInfo.add(DELETE_THROW_EXCEPTION, e.getClass().getName(), e.getMessage());
            e.printStackTrace();
        }

        return resultInfo;
    }




    /**
     * 静默卸载回调
     */
    static class MyPackageDeleteObserver extends IPackageDeleteObserver.Stub {

        private AtomicInteger mResult = new AtomicInteger(0);
        private volatile boolean mResultGot;

        private void setResult(int result) {
            synchronized (this) {
                mResult.set(result);
                mResultGot = true;
                notifyAll();
            }
        }

        public int waitResult() throws InterruptedException {
            synchronized (this) {
                while (!mResultGot) {
                    wait();
                }
            }

            return mResult.get();
        }

        @Override
        public void packageDeleted(String packageName, int returnCode) throws RemoteException {
            if (returnCode == 1) {
                setResult(DELETE_SUCCESS_24);
                Log.d(TAG, packageName + "卸载成功...");
            } else {
                setResult(returnCode);
                Log.d(TAG, packageName + "卸载失败...返回码:" + returnCode);
            }
        }
    }


    /**
     * 是否拥有卸载权限
     */
    private static boolean hasUninstallPermission(Context context) {
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();
            if (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission(Manifest.permission.DELETE_PACKAGES,
                    context.getPackageName())) {
                return true;
            }
        }
        return false;
    }



    /**
     * 判断apk是否已安装
     *
     * @param context
     * @param pkgName
     * @return
     */
    private static boolean isApkInstalled(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }

        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    private static ResultInfo uninstall(String pkgName) {
        String[] args = {"pm", "uninstall", pkgName};
        ProcessBuilder processBuilder = new ProcessBuilder(args);

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        ResultInfo resultInfo = new ResultInfo();

        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;

            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }

            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (IOException e) {
           Log.e(TAG, e.getMessage());
            resultInfo.add(DELETE_THROW_EXCEPTION, e.getClass().getName(), e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            resultInfo.add(DELETE_THROW_EXCEPTION, e.getClass().getName(), e.getMessage());
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {}

            if (process != null) {
                process.destroy();
            }
        }

        if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
            resultInfo.add(DELETE_SUCCESS, "delete success");
        } else {
            if (!TextUtils.isEmpty(errorMsg)) {
                if (errorMsg.indexOf("[") != -1 && errorMsg.lastIndexOf("]") != -1){
                    String errorType = errorMsg.substring(errorMsg.indexOf("["), errorMsg.lastIndexOf("]") + 1);
                    resultInfo.add(DELETE_ERROR, errorType, errorMsg.toString());
                } else {
                    resultInfo.add(DELETE_ERROR, "", errorMsg.toString());
                }
            } else {
                resultInfo.add(DELETE_ERROR, "", errorMsg.toString());
            }
        }
        Log.d(TAG, "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
        return resultInfo;
    }





}

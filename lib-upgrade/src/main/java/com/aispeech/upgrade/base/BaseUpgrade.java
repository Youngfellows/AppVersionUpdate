package com.aispeech.upgrade.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.aispeech.tvui.common.interfaces.DoanloadCallback;
import com.aispeech.tvui.common.interfaces.UpgradeRequestCallBack;
import com.aispeech.tvui.common.manager.RetrofitManager;
import com.aispeech.tvui.common.util.AppVersionUtils;
import com.aispeech.tvui.common.util.FileUtil;
import com.aispeech.tvui.common.util.InstallUtil;
import com.aispeech.tvui.common.util.Md5Util;

import java.io.File;
import java.util.Map;

/**
 * @author henrychen
 * @version $Rev$
 * @email henrychen@aispeech.com
 * @time 2018/10/17 16:24
 * @des ${app升级基础模块}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public class BaseUpgrade {
    private static final String TAG = BaseUpgrade.class.getSimpleName();

    /**
     * 初始化接口
     *
     * @param application 上下文对象
     */
    public static void init(Application application) {

    }

    /**
     * GET网络请求
     *
     * @param baseURL  请求URL
     * @param callback 请求回调
     * @param map      参数列表
     */
    public static void upgradeVersion(String baseURL, Map<String, String> map, final UpgradeRequestCallBack callback) {
        //        RequestManager.getInstance().upgradeVersion(context, url, paramMap, callback);
        RetrofitManager.getInstance().upgradeVersion(baseURL, map, callback);
    }

    /**
     * GET网络请求
     *
     * @param baseURL  请求URL
     * @param callback 请求回调
     */
    public static void upgradeConfig(String baseURL, final UpgradeRequestCallBack callback) {
        RetrofitManager.getInstance().upgradeConfig(baseURL, callback);
    }

    /**
     * 下载
     *
     * @param fileUrl  下载地址
     * @param dirPath  文件下载保存路径
     * @param fileName 文件名
     * @param callback 文件名
     */
    public static void requestDownload(String fileUrl, String dirPath, String fileName, DoanloadCallback callback) {
        RetrofitManager.getInstance().download(fileUrl, dirPath, fileName, callback);
    }


    /**
     * 判断文件是否存在
     *
     * @param context  上下文对象
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return true:存在 false:不存在
     */
    public static boolean isFileExist(Context context, String filePath, String fileName) {
        return FileUtil.isApkExist(context, filePath, fileName);
    }

    /**
     * 普通安装app
     *
     * @param context 上下文对象
     * @param file    app文件对象
     */
    public static void installAppByNormal(Context context, File file) {
        InstallUtil.install(context, file.getPath());
    }

    /**
     * 静默安装app
     *
     * @param context 上下文对象
     * @param apkPath app文件路径
     */
    public static boolean installAppBySilent(Context context, String apkPath) {
        return InstallUtil.installRoot(context, apkPath);
    }

    /**
     * apk是否已经安装
     *
     * @param context     上下文对象
     * @param packageName 包名
     * @return true:已经安装 false:未安装
     */
    public static boolean isApkInstalled(Context context, String packageName) {
        return AppVersionUtils.isInstalledApk(context, packageName);
    }

    /**
     * MD5值校验
     *
     * @param md5  md5值
     * @param file 要校验的文件
     * @return true:一致 false:不一致
     */
    public static boolean checkMd5Match(String md5, File file) {
        String fileMd5 = Md5Util.getFileMD5(file);
        Log.d(TAG, "Md5Util.getFileMD5: " + fileMd5 + " ,md5: " + md5);
        return md5.equalsIgnoreCase(fileMd5);
    }

    /**
     * 获取文件的MD5值
     *
     * @param file 文件
     * @return string：文件的md5值，可能为空
     */
    public static String getFileMd5(File file) {
        return Md5Util.getFileMD5(file);
    }
}

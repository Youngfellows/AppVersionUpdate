package com.aispeech.upgrade.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.aispeech.upgrade.conf.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jie.chen on 2018/4/21.
 * 版本更新文件管理
 * 1.获取apk更新包下载路径
 * 2.获取更新配置文件保存路径
 */

public class FileUtils {
    private static String TAG = "FileUtils";

    /**
     * 获取下载APK的保存路径
     *
     * @param context
     * @return
     */
    public static String targetApkPath(Context context) {
        return targetPath(context) + File.separator + Constants.DOWNLOAD_APK_FILE_NAME;
    }

    /**
     * 获取配置文件保存路径
     *
     * @param context
     * @return
     */
    public static String updateConfigPath(Context context) {
        return targetPath(context) + File.separator + Constants.UPDATE_CONFIG_FILE_NAME;
    }

    /**
     * 获取下载APK和升级配置保存路径
     * *@param context
     *
     * @return 载APK和升级配置保存路径
     */
    public static String targetPath(Context context) {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            try {
//                path = context.getExternalCacheDir().getAbsolutePath();
                Log.i(TAG, "path1 = " + path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(path)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                Log.i(TAG, "path2 = " + path);
            }
        } else {
            path = context.getCacheDir().getAbsolutePath();
            Log.i(TAG, "path3 = " + path);
        }
        Log.i(TAG, "path4 = " + path);
        return path;
    }

    /**
     * 按自定编码读取字符创
     *
     * @param context
     * @param file    读取的文件
     * @return 读取到的字符串
     */
    public static String readFile(Context context, File file) {
        if (file == null) {
            return null;
        }
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder();
        try {
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将文本写入到文件中
     *
     * @param filePath 文件路径
     * @param content  内容
     * @param isAppend 是否追加
     */
    public static void saveStringToFile(final String filePath, final String content, final boolean isAppend) {
        if (TextUtils.isEmpty(filePath) || filePath.matches("null\\/.*")) {
            Log.w(TAG, "The file to be saved is null!");
            return;
        }
        FileWriter fw = null;
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            fw = new FileWriter(file, isAppend);
            fw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

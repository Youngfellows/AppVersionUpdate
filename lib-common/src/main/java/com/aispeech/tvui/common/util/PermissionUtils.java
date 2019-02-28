package com.aispeech.tvui.common.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionUtils {

    /**
     * 需要申请的权限列表
     */
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    /**
     * 判断是否授权
     * @param activity Activity对象
     * @param requestCode 自己定义的请求码
     * @return {@code true}:已经授权 {@code false}:otherwise
     */
    public static boolean isGrantExternalRW(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int writeStoragePermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readStoragePermission = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            //检测是否有权限，如果没有权限，就需要申请
            if (writeStoragePermission != PackageManager.PERMISSION_GRANTED ||
                    readStoragePermission != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                activity.requestPermissions(PERMISSIONS_STORAGE, requestCode);
                //返回false。说明没有授权
                return false;
            }
        }
        //说明已经授权
        return true;
    }

}


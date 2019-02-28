package com.aispeech.upgrade.base;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.aispeech.upgrade.conf.Constants;
import com.aispeech.upgrade.service.UpgradeService;

/**
 * Created by Jie.Chen on 2018/7/16.
 */

public class TvuiUpgrade {
    private String TAG = this.getClass().getSimpleName();
    private static volatile TvuiUpgrade mInstance;
    private Application mApplication;

    private TvuiUpgrade(Application application) {
        this.mApplication = application;
        BaseUpgrade.init(application);
    }

    public static TvuiUpgrade getInstance(Application application) {
        if (mInstance == null) {
            synchronized (TvuiUpgrade.class) {
                if (mInstance == null) {
                    mInstance = new TvuiUpgrade(application);
                }
            }
        }
        return mInstance;
    }

    /**
     * @param baseUrl         请求地址
     * @param installerMode   安装模式
     * @param productId       产品ID
     * @param deviceId        设备ID
     * @param pullUpgradeTime 拉取配置的时间
     * @param nextInstallTime 下次安装提示时间
     */
    public void init(String baseUrl, String installerMode, String productId, String deviceId, String pullUpgradeTime, String nextInstallTime) {
        try {
            Log.i(TAG, "TvuiUpgrade init baseUrl: " + baseUrl + " ,installerMode: " + installerMode + " ,productId: " + productId + " ,deviceId: " + deviceId + " ,pullUpgradeTime: " + pullUpgradeTime + " ,nextInstallTime: " + nextInstallTime);
            Intent intent = new Intent();
            intent.setClass(mApplication, UpgradeService.class);
            intent.putExtra(Constants.BASEURL, baseUrl);
            intent.putExtra(Constants.INSTALLER_MODE, installerMode);
            intent.putExtra(Constants.PRODUCT_ID, productId);
            intent.putExtra(Constants.DEVICE_ID, deviceId);
            intent.putExtra(Constants.PULL_UPGRADE_TIME, pullUpgradeTime);
            intent.putExtra(Constants.NEXT_INSTALL_TIME, nextInstallTime);
            mApplication.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "启动UpgradeService服务error: " + e.getMessage());
        }
    }
}

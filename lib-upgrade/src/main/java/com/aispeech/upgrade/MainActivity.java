package com.aispeech.upgrade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.aispeech.tvui.common.util.AppVersionUtils;
import com.aispeech.upgrade.alarm.AlarmClockManager;
import com.aispeech.upgrade.service.UpgradeService;
import com.aispeech.upgrade.utils.ApkUtil;
import com.aispeech.upgrade.utils.FileUtils;
import com.aispeech.upgrade.utils.ResultInfo;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
    private String mProductId;
    private String mDeviceId;
    private String mPullUpgradeTime;
    private String mNextInstallTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        getProductInfo(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getProductInfo(intent);
    }

    public void getProductInfo(Intent intent) {
        mProductId = intent.getStringExtra("productId");
        mDeviceId = intent.getStringExtra("deviceId");
        mPullUpgradeTime = intent.getStringExtra("pullUpgradeTime");
        mNextInstallTime = intent.getStringExtra("nextInstallTime");
        Log.i(TAG, "11 productId: " + mProductId + " ,deviceId: " + mDeviceId + " ,mPullUpgradeTime: " + mPullUpgradeTime + " ,mNextInstallTime: " + mNextInstallTime);
    }

    public void installTest(View view) {
        Log.i(TAG, "installTest: xxxxxxxxxyyyyyyyyyyssssssssssssfdaaaaaaa");
        startDownloadService();
    }

    public void startDownloadService() {
        Intent intent = new Intent();
        intent.setClass(this, UpgradeService.class);
        intent.putExtra("productId", mProductId);
        intent.putExtra("deviceId", mDeviceId);
        intent.putExtra("pullUpgradeTime", mPullUpgradeTime);
        intent.putExtra("nextInstallTime", mNextInstallTime);
        startService(intent);
    }

    public void setUpgradeAlarm(View view) {
        Log.i(TAG, "setUpgradeAlarm xxxxxxxxxxxxxxx1111");
        AlarmClockManager.getInstance(this).setPullUpdateAlarm();
    }

    public void setInstallerAlarm(View view) {
        Log.i(TAG, "setInstallerAlarm xxxxxxxxxxxxx2222");
        AlarmClockManager.getInstance(this).setInstallerAlarm();
    }

    public void installSilent(View view) {
        //静默安装
        final File file = new File(FileUtils.targetApkPath(this));
        if (file.exists()) {
            Log.i(TAG, "installSilent 文件存在,使用静默升级");
//            BaseUpgrade.installAppBySilent(this, file.getPath());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ResultInfo resultInfo = ApkUtil.installSilent(MainActivity.this, file.getPath(), AppVersionUtils.getPackageName(MainActivity.this));
                    Log.d(TAG, resultInfo.toString());
                }
            }).start();

//            sendSilentInstallBroadcast(this, file);
        } else {
            Log.e(TAG, "installSilent 文件不存在");
        }
    }

    /**
     * 发送安装的广播
     *
     * @param context 上下文
     * @param file    apk路径
     */
    private void sendSilentInstallBroadcast(Context context, File file) {
        Intent intent = new Intent();
        String action = "com.aispeech.tvui.UPGRADE_APPLICATION";
        intent.setAction(action);
        intent.putExtra("file", file);
        context.sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        registerInstallReceiver();

        Log.e(TAG, "新版本--->>>"+AppVersionUtils.getPackageName(MainActivity.this)+" ,versioCode: "+AppVersionUtils.getVersionCode(MainActivity.this)+" ,名称: "+AppVersionUtils.getVersionName(MainActivity.this));
    }

    private void registerInstallReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.aispeech.tvui.UPGRADE_APPLICATION");
        this.registerReceiver(mInstallBroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver mInstallBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action: " + action);
            if ("com.aispeech.tvui.UPGRADE_APPLICATION".equals(action)) {
                File file = (File) intent.getSerializableExtra("file");
                Log.i(TAG, "onReceive file path = " + file.getPath());
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mInstallBroadcastReceiver);
    }
}

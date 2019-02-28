package com.aispeech.upgrade.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.aispeech.tvui.common.interfaces.DownloadCallback;
import com.aispeech.tvui.common.interfaces.RequestCallback;
import com.aispeech.tvui.common.util.AppVersionUtils;
import com.aispeech.tvui.common.util.PreferenceUtils;
import com.aispeech.tvui.common.util.TimeUtils;
import com.aispeech.upgrade.R;
import com.aispeech.upgrade.alarm.AlarmClockManager;
import com.aispeech.upgrade.base.BaseUpgrade;
import com.aispeech.upgrade.bean.AlarmDateBean;
import com.aispeech.upgrade.bean.Content;
import com.aispeech.upgrade.bean.Data;
import com.aispeech.upgrade.bean.UpdateAppConfigBean;
import com.aispeech.upgrade.bean.UpgradeVersionBean;
import com.aispeech.upgrade.conf.Constants;
import com.aispeech.upgrade.conf.InstallerType;
import com.aispeech.upgrade.dialog.DialogStateListener;
import com.aispeech.upgrade.dialog.UpgradeDialog;
import com.aispeech.upgrade.receiver.AlarmReceiver;
import com.aispeech.upgrade.utils.ApkUtil;
import com.aispeech.upgrade.utils.FileUtils;
import com.aispeech.upgrade.utils.ResultInfo;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class UpgradeService extends Service {
    private String changlog;
    private static final String TAG = UpgradeService.class.getSimpleName();
    private ScheduledExecutorService mExecutor;
    private String mMd5;
    /**
     * 是否正在下载
     */
    private boolean isDownloading = false;
    private String mProductId;
    private String mDeviceId;
    private String mPackageName;
    private int mVersionCode;
    private String mVersionName;
    /**
     * 已下载APK版本号
     */
    private int mDownVersionCode = -1;
    /**
     * 已下载APK版本名称
     */
    private String mDownVersionName;
    /**
     * 更新包请求地址
     */
    private String mBaseUrl;
    /**
     * 升级类型,默认为普通升级
     */
    private InstallerType mInstallType = InstallerType.COMMON;

    /**
     * 升级对话框
     */
    private UpgradeDialog.Builder mUpgradeDialog = null;
    private boolean isShowDialog = false;//对话框是否显示
    private String mNotification;

    public UpgradeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: UpgradeService-----");
        ThreadFactory threadFactory = new ThreadFactory() {
            AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "tvui-upgrade#" + mCount.getAndIncrement());
            }
        };
        if (null == mExecutor) {
            mExecutor = new ScheduledThreadPoolExecutor(5, threadFactory);
        }
        //        testSlientInstall();
        initDialogStateListener();
    }

    public void initDialogStateListener() {
        UpgradeDialog.setDialogStateListener(mStateListener);
    }

    private DialogStateListener mStateListener = new DialogStateListener() {
        @Override
        public void onShow() {
            Log.i(TAG, "onShow: xxxxxxxxxxx 显示对话框");
            isShowDialog = true;
        }

        @Override
        public void onDismiss() {
            Log.i(TAG, "onDismiss: xxxxxxxxxxx 对话框消失,mUpgradeDialog = " + mUpgradeDialog);
            mUpgradeDialog = null;
            isShowDialog = false;
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: UpgradeService-----");
        //获取产品配置信息
        getProductInfo(intent);
        //注册监听回调
        AlarmReceiver.setAlarmListener(mAlarmListener);
        //设置下次拉取更新闹钟
        AlarmClockManager.getInstance(UpgradeService.this).setPullUpdateAlarm();
        //设置安装APK的闹钟
        AlarmClockManager.getInstance(UpgradeService.this).setInstallerAlarm();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 设置拉取和提示安装的时间配置
     *
     * @param pullUpgradeTime 0000-00-00 00:15:00
     * @param nextInstallTime 0000-00-00 24:00:00
     */
    private void setAlarmDate(String pullUpgradeTime, String nextInstallTime) {
        try {
            int year = TimeUtils.getYears(pullUpgradeTime);
            int month = TimeUtils.getMonths(pullUpgradeTime);
            int day = TimeUtils.getDays(pullUpgradeTime);
            int hour = TimeUtils.getHours(pullUpgradeTime);
            int minute = TimeUtils.getMinutes(pullUpgradeTime);
            int second = TimeUtils.getSeconds(pullUpgradeTime);
            Log.d(TAG, "setAlarmDate year: " + year + " ,month: " + month + " ,day: " + day + " ,hour: " + hour + " ,minute: " + minute + " ,second: " + second);
            AlarmClockManager.getInstance(this).setPullAlarmDate(new AlarmDateBean(year, month, day, hour, minute, second));

            year = TimeUtils.getYears(nextInstallTime);
            month = TimeUtils.getMonths(nextInstallTime);
            day = TimeUtils.getDays(nextInstallTime);
            hour = TimeUtils.getHours(nextInstallTime);
            minute = TimeUtils.getMinutes(nextInstallTime);
            second = TimeUtils.getSeconds(nextInstallTime);
            Log.d(TAG, "setAlarmDate year: " + year + " ,month: " + month + " ,day: " + day + " ,hour: " + hour + " ,minute: " + minute + " ,second: " + second);
            AlarmClockManager.getInstance(this).setInstallAlarmDate(new AlarmDateBean(year, month, day, hour, minute, second));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mExecutor) {
            mExecutor.shutdown();
            mExecutor = null;
        }
        Log.i(TAG, "onDestroy: UpgradeService-----");
    }

    AlarmReceiver.IAlarmListener mAlarmListener = new AlarmReceiver.IAlarmListener() {
        @Override
        public void noticeUpdateAlarm() {
            //设置下次拉取更新闹钟
            AlarmClockManager.getInstance(UpgradeService.this).setPullUpdateAlarm();
            //获取版本更新信息
            pullUpgradeTask();
        }

        @Override
        public void noticeInstallerAlarm() {
            //设置下次安装APK闹钟
            AlarmClockManager.getInstance(UpgradeService.this).setInstallerAlarm();
            //读取配置文件并安装APK
            readConfigFileInstallerTask();
        }
    };

    /**
     * 读取配置文件并安装APK
     */
    private void readConfigFileInstallerTask() {
        if (null != mExecutor) {
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    // TODO: 2018/6/25 读取已下载版本的提示安装次数,小于3次,提示安装
                    readConfigFileInstaller();
                }
            });
        } else {
            Log.i(TAG, "readConfigFileInstallerTask mExecutor is null");
        }
    }

    /**
     * 获取版本更新信息
     */
    private void pullUpgradeTask() {
        if (null != mExecutor) {
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    pullUpgrade(mProductId, mDeviceId, mVersionCode, mPackageName);
                }
            });
        } else {
            Log.i(TAG, "pullUpgradeTask mExecutor is null");
        }
    }

    /**
     * 读取配置文件并安装APK
     */
    private void readConfigFileInstaller() {
        Log.d(TAG, "读取配置文件并安装APK");
        String saveConfigPath = FileUtils.updateConfigPath(this);
        String apkPath = FileUtils.targetApkPath(this);
        Log.i(TAG, "配置文件路径: saveConfigPath = " + saveConfigPath);
        Log.i(TAG, "APP路径: apkPath = " + apkPath);

        File configFile = new File(saveConfigPath);
        File apkFile = new File(apkPath);
        boolean isExistsConfig = configFile.exists();
        boolean isExistsApp = apkFile.exists();
        Log.d(TAG, "配置文件是否存在: isExistsConfig = " + isExistsConfig + " ,APP是否存在: isExistsApp = " + isExistsApp);
        if (isExistsApp && isExistsApp) {
            try {
                String configJson = FileUtils.readFile(this, configFile);
                if (!TextUtils.isEmpty(configJson)) {
                    UpdateAppConfigBean configBean = JSON.parseObject(configJson, UpdateAppConfigBean.class);
                    List<Content> appLists = configBean.getContent();
                    Content tvui = null;
                    Log.i(TAG, "包名: " + mPackageName);
                    for (Content app : appLists) {
                        if (mPackageName.equals(app.getPackagename())) {
                            tvui = app;
                        }
                    }

                    if (tvui != null) {
                        String tvuiMd5 = tvui.getMd5();
                        String versionCode = tvui.getVersionCode();
                        mDownVersionName = tvui.getVersionName();
                        if (!TextUtils.isEmpty(tvuiMd5) && !TextUtils.isEmpty(versionCode)) {
                            boolean isCheckMd5Match = BaseUpgrade.checkMd5Match(tvuiMd5, apkFile);
                            if (isCheckMd5Match) {
                                Log.d(TAG, "APK MD5值校验通过,安装APK,当前TVUI版本号: mVersionCode = " + mVersionCode + " ,配置文件APK版本: versionCode = " + versionCode);
                                mDownVersionCode = Integer.parseInt(versionCode);
                                if (mVersionCode < mDownVersionCode) {
                                    Log.i(TAG, "安装版本比已下载版本小,安装APK");
                                    // TODO: 2018/6/25 版本提示安装次数小于3次,提示安装
                                    String packageNamePromp = PreferenceUtils.getString(this, Constants.PACKAGE_NAME);
                                    String versionNamePromp = PreferenceUtils.getString(this, Constants.VERSION_NAME);
                                    int versionCodePromp = PreferenceUtils.getInt(this, Constants.VERSION_CODE, -1);
                                    int frequency = PreferenceUtils.getInt(this, Constants.FREQUENCY, -1);
                                    Log.d(TAG, "frequency: " + frequency + " ,versionCodePromp: " + versionCodePromp + " ,versionNamePromp: " + versionNamePromp + " ,packageNamePromp: " + packageNamePromp);
                                    if (frequency >= 0 && frequency < Constants.MAX_FREQUENCY && TextUtils.equals(versionNamePromp, mDownVersionName) && versionCodePromp == mDownVersionCode) {
                                        //checkMd5Installer(apkFile, false);
                                        Log.d(TAG, "已经下载完,是否显示安装提示框," + Constants.DISPLAY_INSTALL_DIALOG + " ,是否静默安装 " + (InstallerType.SLIENT == mInstallType));
                                        if (Constants.DISPLAY_INSTALL_DIALOG && (InstallerType.SLIENT != mInstallType)) {
                                            showInstallConfirmDialog(apkFile, false);
                                        } else {
                                            checkMd5Installer(apkFile, false);
                                        }
                                    } else if ((frequency == -1) && (versionCodePromp == -1) && TextUtils.isEmpty(versionNamePromp) && TextUtils.isEmpty(packageNamePromp)) {
                                        Log.d(TAG, "已经下载完,没有安装过,第一次提示安装,是否显示安装提示框," + Constants.DISPLAY_INSTALL_DIALOG + " ,是否静默安装 " + (InstallerType.SLIENT == mInstallType));
                                        //checkMd5Installer(apkFile, true);
                                        if (Constants.DISPLAY_INSTALL_DIALOG && (InstallerType.SLIENT != mInstallType)) {
                                            showInstallConfirmDialog(apkFile, true);
                                        } else {
                                            checkMd5Installer(apkFile, true);
                                        }
                                    } else {
                                        Log.d(TAG, "提示安装次数超过" + Constants.MAX_FREQUENCY + "次");
                                    }
                                } else {
                                    Log.i(TAG, "安装版本大于等于已下载版本小,拉取版本更新");
                                    //获取版本更新信息
                                    deleteFileAndPullUpgrade(configFile, apkFile);
                                }
                            } else {
                                Log.d(TAG, "APK MD5值校验不通过,拉取版本更新");
                                deleteFileAndPullUpgrade(configFile, apkFile);
                            }
                        }
                    }
                } else {
                    Log.i(TAG, "读取的配置文件为空,拉取版本更新");
                    deleteFileAndPullUpgrade(configFile, apkFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "readConfigFileInstaller error " + e.getMessage());
                //获取版本更新信息
                deleteFileAndPullUpgrade(configFile, apkFile);
            }
        } else if (!isExistsApp || !isExistsConfig) {
            Log.d(TAG, "配置文件或者APK不存在,拉取版本更新");
            //获取版本更新信息
            deleteFileAndPullUpgrade(configFile, apkFile);
        }
    }

    /**
     * 删除旧文件，下载新文件
     *
     * @param configFile 旧配置文件
     * @param apkFile    旧APK
     */
    private void deleteFileAndPullUpgrade(File configFile, File apkFile) {
        if (null == configFile || null == apkFile) {
            Log.i(TAG, "deleteFileAndPullUpgrade configFile is null or apkFile is null");
            return;
        }
        boolean isExistsConfig = configFile.exists();
        boolean isExistsApp = apkFile.exists();
        if (isExistsApp) {
            apkFile.delete();
        }
        if (isExistsConfig) {
            configFile.delete();
        }
        //获取版本更新信息
        pullUpgradeTask();
    }

    /**
     * 获取产品配置信息
     */
    private void getProductInfo(Intent intent) {
        if (intent != null) {
            mBaseUrl = intent.getStringExtra(Constants.BASEURL); //更新包请求地址
            mProductId = intent.getStringExtra(Constants.PRODUCT_ID); //产品ID
            mDeviceId = intent.getStringExtra(Constants.DEVICE_ID); //设备ID
            String pullUpgradeTime = intent.getStringExtra(Constants.PULL_UPGRADE_TIME);
            String nextInstallTime = intent.getStringExtra(Constants.NEXT_INSTALL_TIME);
            String installerMode = intent.getStringExtra(Constants.INSTALLER_MODE);

            Log.i(TAG, "22 installerMode: " + installerMode + " ,mBaseUrl: " + mBaseUrl);
            Log.i(TAG, "22 productId: " + mProductId + " ,deviceId: " + mDeviceId + " ,pullUpgradeTime: " + pullUpgradeTime + " ,nextInstallTime: " + nextInstallTime);
            setAlarmDate(pullUpgradeTime, nextInstallTime);
            mPackageName = AppVersionUtils.getPackageName(this); //包名
            mVersionCode = AppVersionUtils.getVersionCode(this);  //版本号
            mVersionName = AppVersionUtils.getVersionName(this);  //版本名称

            mPackageName = "com.aispeech.tvui"; //包名
            mVersionCode = 1005;  //版本号
            Log.i(TAG, "packageName = " + mPackageName + " ,versionCode = " + mVersionCode + " ,versionName = " + mVersionName);

            //设置安装模式
            if (Constants.SLIENT_INSTALLER.equals(installerMode)) {
                mInstallType = InstallerType.SLIENT;//静默安装
            } else {
                mInstallType = InstallerType.COMMON;//普通安装
            }

            //获取版本更新信息
            pullUpgradeTask();
        } else {
            Log.e(TAG, "intent 为空,无法获取产品ID和设备ID");
        }
    }


    /**
     * 获取版本更新信息
     *
     * @param productId   产品ID
     * @param deviceId    设备ID
     * @param versionCode 版本号
     * @param packageName 包名
     */
    public void pullUpgrade(String productId, String deviceId, int versionCode, String packageName) {
        //先拉取是否可以更新
        //        String url = Constants.BASE_URL + "?productId=" + productId + "&versionCode=" + versionCode + "&deviceId=" + deviceId + "&packageName=" + packageName;
        //        url = "http://api.iot.aispeech.com/skyline-iot-api/api/v1/tv/versionUpgrade?productId=278571653&versionCode=1&deviceId=44005190410c383705cc&packageName=com.aispeech.tvui";
        //        url = "http://api.iot.aispeech.com/skyline-iot-api/api/v1/tv/versionUpgrade?productId=2782571653&versionCode=1&deviceId=44005190410c383705cc&packageName=com.aispeech.tvui";

        Log.i(TAG, "触发网络请求,先拉取是否可以更新");
        Log.d(TAG, "mBaseUrl: " + mBaseUrl + " ,productId: " + productId + " ,deviceId: " + deviceId);
        if (!TextUtils.isEmpty(mBaseUrl) && !TextUtils.isEmpty(productId) && !TextUtils.isEmpty(deviceId)) {
            String url = mBaseUrl + "?productId=" + productId + "&versionCode=" + versionCode + "&deviceId=" + deviceId + "&packageName=" + packageName;
            Log.i(TAG, "url = " + url);
            //获取升级信息
            Map<String, String> map = new HashMap<>();
            map.put("productId", productId);
            map.put("versionCode", versionCode + "");
            map.put("deviceId", deviceId);
            map.put("packageName", packageName);
            BaseUpgrade.upgradeVersion(mBaseUrl, map, mUpgradeRequestCallBack);
        } else {
            Log.e(TAG, "请求更新的URL不合法");
        }

    }

    /**
     * 拉取更新配置信息
     */
    private RequestCallback mUpgradeRequestCallBack = new RequestCallback() {
        @Override
        public void onSuccess(String data) {
            Log.i(TAG, "拉取更新配置信息成功");
            Log.i(TAG, "data: " + data);
            processUpgradeJson(data);
        }

        @Override
        public void onFailure(String exception) {
            Log.e(TAG, "拉取更新配置信息错误: " + exception);
        }
    };

    /**
     * 获取配置单
     */
    private RequestCallback mUpgradeConfigRequestCallBack = new RequestCallback() {
        @Override
        public void onSuccess(String data) {
            Log.i(TAG, "获取配置单成功");
            //            Log.i(TAG, data);
            processUpgradeConfigJson(data);
        }

        @Override
        public void onFailure(String exception) {
            Log.e(TAG, "获取配置单错误");
        }
    };

    /**
     * 保存配置单
     *
     * @param data 配置单数据
     */
    private void saveConfigFile(final String data) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String saveConfigPath = FileUtils.updateConfigPath(UpgradeService.this);
                Log.d(TAG, "saveConfigPath: " + saveConfigPath);
                File configFile = new File(saveConfigPath);
                if (configFile.exists()) {
                    Log.i(TAG, "saveConfigFile exists ,delete saveConfigFile");
                    configFile.delete();
                }
                FileUtils.saveStringToFile(saveConfigPath, data, false);
            }
        };

        if (null != mExecutor) {
            mExecutor.execute(task);
        }
    }

    /**
     * 解析并获取升级配置
     *
     * @param jsonData
     */
    private void processUpgradeJson(String jsonData) {
        if (!TextUtils.isEmpty(jsonData)) {
            try {
                UpgradeVersionBean upgradeVersion = JSON.parseObject(jsonData, UpgradeVersionBean.class);
                Integer code = upgradeVersion.getCode();
                if (code == 1) {
                    Data data = upgradeVersion.getData();
                    if (data != null) {
                        String packageName = data.getPackageName();
                        String url = data.getUrl();
                        String versionCode = data.getVersionCode();
                        Integer frequency = data.getFrequency();//下次升级提示时间,单位小时
                        String upgradeWay = data.getUpgradeWay();//升级方式
                        mNotification = data.getNotification();//更新changelog
                        Log.i(TAG, "packageName: " + packageName + " ,versionCode: " + versionCode + " ,upgradeWay: " + upgradeWay + " ,frequency: " + frequency + " ,url: " + url);
                        if (!TextUtils.isEmpty(upgradeWay)) {
                            if (Constants.FORCE_UPGRADE.equals(upgradeWay)) {
                                Log.d(TAG, "set install type force");
                                mInstallType = InstallerType.FORCE;//强制升级
                            } else if (Constants.MANUAL_UPGRADE.equals(upgradeWay)) {
                                Log.d(TAG, "set install type common");
                                mInstallType = InstallerType.COMMON;//普通升级
                            } else if (Constants.QUIET_UPGRADE.equals(upgradeWay)) {
                                Log.d(TAG, "set install type slient");
                                mInstallType = InstallerType.SLIENT;//静默升级
                            }
                        }
                        if (!TextUtils.isEmpty(url) && frequency >= 0) {
                            Log.d(TAG, isDownloading ? "正在下载,不获取配置单" : "没有正在下载,获取配置单");
                            if (!isDownloading) {
                                //请求配置单
                                BaseUpgrade.upgradeConfig(url, mUpgradeConfigRequestCallBack);
                            }
                            //更新设置下次安装时间
                            AlarmClockManager.getInstance(this).setInstallAlarmDate(new AlarmDateBean(frequency, 0, 0));
                            //设置下次安装提示闹钟
                            AlarmClockManager.getInstance(UpgradeService.this).setInstallerAlarm();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "JSON解析错误: " + e.getMessage());
            }
        } else {
            Log.d(TAG, "jsonData数据为空");
        }
    }

    /**
     * 1.解析配置单
     * 2.获取TVUI下载信息
     * 3.保存配置单
     *
     * @param data 配置单
     */
    private void processUpgradeConfigJson(String data) {
        try {
            Log.i(TAG, "处理配置文件,获取下载链接");
            UpdateAppConfigBean configBean = JSON.parseObject(data, UpdateAppConfigBean.class);
            List<Content> appArray = configBean.getContent();
            Log.i(TAG, "下载列表 size = " + appArray.size());
            Content tvuiApp = null;//TVUI下载信息
            for (Content app : appArray) {
                Log.i(TAG, "配置文件包名: " + app.getPackagename());
                //                if (AppVersionUtils.getPackageName(this).equals(app.getPackagename())) {
                if ("com.aispeech.tvui".equals(app.getPackagename())) {
                    String component = app.getComponent();
                    mDownVersionName = app.getVersionName();
                    mDownVersionCode = Integer.parseInt(app.getVersionCode());
                    mMd5 = app.getMd5();//APK的MD5值
                    String changeLog = app.getChangeLog();
                    changlog = changeLog;
                    String appUrl = app.getUrl();

                    Log.d(TAG, "component: " + component + "\nversionCode: " + mDownVersionCode + "\nversionName: " + mDownVersionName + "\nmd5: " + mMd5 + "\nappUrl: " + appUrl + "\nchangeLog: " + changeLog);
                    tvuiApp = app;
                }
            }
            Log.i(TAG, "tvuiApp  = " + tvuiApp);
            if (tvuiApp != null) {
                // TODO: 2018/6/22 判断APK是否已经下载，如未下载则下载APK,同时更新配置文件
                String saveApkPath = FileUtils.targetApkPath(this);
                File oldFile = new File(saveApkPath);
                if (oldFile.exists()) {
                    Log.i(TAG, "TVUI APK已经下载,校验APK的MD5值是否合法");
                    if (BaseUpgrade.checkMd5Match(mMd5, oldFile)) {
                        Log.i(TAG, "TVUI APK 文件已经存在,不用再下载了");
                    } else {
                        Log.i(TAG, "TVUI APK 的MD5值校验不通过,从新下载APK");
                        oldFile.delete();//删除旧包
                        requestDownload(tvuiApp, FileUtils.targetPath(this), Constants.DOWNLOAD_APK_FILE_NAME);//下载APP
                    }
                } else {
                    Log.i(TAG, "TVUI APK没有下载,从新下载");
                    requestDownload(tvuiApp, FileUtils.targetPath(this), Constants.DOWNLOAD_APK_FILE_NAME);//下载APP
                }
            }
            //保存配置单
            saveConfigFile(data);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e(TAG, "类型转换异常: " + e.getMessage());
        }
    }

    /**
     * 1.非静默安装显示升级对话框
     * 2.静默安装不显示升级对话框
     *
     * @param app      将要下载的APP
     * @param dirPath  下载APP的保存路径
     * @param fileName 下载APP的名称
     */
    private void requestDownload(Content app, String dirPath, String fileName) {
        Log.i(TAG, "安装模式: mInstallType = " + mInstallType);
        if (mInstallType == InstallerType.SLIENT) {
            Log.i(TAG, "静默安装,悄悄的下载吧");
            BaseUpgrade.requestDownload(app.getUrl(), dirPath, fileName, mDoanloadCallback);
        } else if (mInstallType == InstallerType.COMMON) {
            Log.i(TAG, "提示安装,显示是否下载提示框: " + Constants.DISPLAY_DOWNLOAD_DIALOG);
            if (Constants.DISPLAY_DOWNLOAD_DIALOG) {
                showDownloadDialog(mNotification, app.getUrl(), dirPath, fileName, mDoanloadCallback);
            } else {
                Log.i(TAG, "不显示下载确认的dialog,直接下载");
                BaseUpgrade.requestDownload(app.getUrl(), dirPath, fileName, mDoanloadCallback);
            }
        } else if (mInstallType == InstallerType.FORCE) {
            Log.i(TAG, "强制安装,显示强制安装的对话框");
        }
    }


    /**
     * 下载回调
     */
    private DownloadCallback mDoanloadCallback = new DownloadCallback() {
        @Override
        public void onSuccess(File file) {
            Log.d(TAG, "downloadSuccess: 是否显示安装提示框" + Constants.DISPLAY_INSTALL_DIALOG + " ,是否静默安装 " + (InstallerType.SLIENT == mInstallType) + " ,apk是否存在: " + file.exists() + " ,path = " + file.getPath());
            isDownloading = false;
            //checkMd5Installer(file, true);//校验MD5,并安装
            if (Constants.DISPLAY_INSTALL_DIALOG && (InstallerType.SLIENT != mInstallType)) {
                showInstallConfirmDialog(file, true);//校验MD5,并安装
            } else {
                checkMd5Installer(file, true);//校验MD5,并安装
            }
        }

        @Override
        public void onFailure(Throwable error) {
            Log.e(TAG, "onFailure: " + error.getMessage());
            isDownloading = false;
        }

        @Override
        public void onLoading(long total, long progress, boolean done) {
            Log.i(TAG, "22 onLoading " + (float) (progress * 1.0 / total) * 100 + "% , " + (done ? "下载完成" : "未下载完成"));
            if (!done) {
                isDownloading = false;
            } else {
                isDownloading = true;
            }
        }
    };

    /**
     * @param file           安装APK的路径
     * @param isFirstInstall 是否首次安装
     */
    private void checkMd5Installer(File file, boolean isFirstInstall) {
        if (file.exists()) {
            if (BaseUpgrade.checkMd5Match(mMd5, file)) {
                //普通安装还是静默安装
                if (InstallerType.SLIENT == mInstallType) {
                    Log.i(TAG, "使用静默安装升级");
                    installSlientSetPromptSendBroadcast(file, isFirstInstall);
                } else if (InstallerType.COMMON == mInstallType) {
                    Log.i(TAG, "使用普通安装升级");
                    installSetPromptSendBroadcast(file, isFirstInstall);
                } else if (InstallerType.FORCE == mInstallType) {
                    Log.i(TAG, "使用强制升级");
                }
            } else {
                Log.e(TAG, "md5校验不通过");
            }
        }
    }

    /**
     * 1.普通安装升级
     * 2.发送安装广播(芒果接收)
     * 3.设置提示次数
     *
     * @param file           安装包路径
     * @param isFirstInstall 是否第一次提示安装
     */
    private void installSetPromptSendBroadcast(File file, boolean isFirstInstall) {
        BaseUpgrade.installAppByNormal(UpgradeService.this, file); //使用普通安装升级
        setPromptFrequency(isFirstInstall);//保存提示安装升级的次数
    }

    private void installSlientSetPromptSendBroadcast(final File file, final boolean isFirstInstall) {
        //        BaseUpgrade.installAppBySilent(this, file.getPath());
        //        sendSilentInstallBroadcast(this, file); //发送安装广播

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "2222 installSlientSetPromptSendBroadcast 静默安装");
                ResultInfo resultInfo = ApkUtil.installSilent(UpgradeService.this, file.getPath(), AppVersionUtils.getPackageName(UpgradeService.this));
                Log.e(TAG, "静默安装结果: " + resultInfo.toString());
                setPromptFrequency(isFirstInstall);//保存提示安装升级的次数
            }
        };

        if (null != mExecutor) {
            mExecutor.execute(task);
        } else {
            Log.i(TAG, "installSlientSetPromptSendBroadcast mExecutor is null");
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
        if (null != context) {
            context.sendBroadcast(intent);
        }
    }

    /**
     * 保存提示安装升级的次数
     *
     * @param isFirstInstall 是否首次安装
     */
    private void setPromptFrequency(boolean isFirstInstall) {
        if (isFirstInstall) {
            Log.i(TAG, "首次安装");
            PreferenceUtils.putInt(this, Constants.FREQUENCY, 1);
        } else {
            int frequency = PreferenceUtils.getInt(this, Constants.FREQUENCY, -1);
            Log.d(TAG, "已经提示安装过: " + frequency + "次");
            if (frequency >= 0) {
                PreferenceUtils.putInt(this, Constants.FREQUENCY, frequency + 1);
            }
        }
        PreferenceUtils.putString(this, Constants.VERSION_NAME, mDownVersionName);
        PreferenceUtils.putInt(this, Constants.VERSION_CODE, mDownVersionCode);
        PreferenceUtils.putString(this, Constants.PACKAGE_NAME, mPackageName);
    }

    /**
     * 获取提示安装升级的次数
     */
    private void getPromptFrequency() {
        String packageName = PreferenceUtils.getString(this, Constants.PACKAGE_NAME);
        String versionName = PreferenceUtils.getString(this, Constants.VERSION_NAME);
        int versionCode = PreferenceUtils.getInt(this, Constants.VERSION_CODE, -1);
        int frequency = PreferenceUtils.getInt(this, Constants.FREQUENCY, -1);
        Log.d(TAG, "frequency: " + frequency + " ,versionCode: " + versionCode + " ,versionName: " + versionName + " ,packageName: " + packageName);
    }

    /**
     * 显示安装确认的对话框
     *
     * @param file           安装APK文件
     * @param isFirstInstall 是否第一次安装
     */
    private void showInstallConfirmDialog(final File file, final boolean isFirstInstall) {
        Log.i(TAG, "showDownloadDialog: 对话框是否显示 isShowDialog = " + isShowDialog + ", mUpgradeDialog = " + mUpgradeDialog);
        if (isShowDialog) {
            Log.i(TAG, "showDownloadDialog: 对话框已经在显示了,先将他消失掉");
            mUpgradeDialog.dismiss();
        }

        if (mUpgradeDialog == null) {
            mUpgradeDialog = new UpgradeDialog.Builder(this)
                    .setBackground(R.drawable.dialog_background)
                    .setTextColor(R.color.white)
                    .setButtonSelector(R.drawable.selector_update_dialog_btn)
                    .setDefaultSelect()//设置默认选中
                    //.setForcedUpdate()//设置强制升级
                    .setTitle("检查到新版本")
                    .setFeature("新版本特性")
                    //.setUpdateInfo("1.xxxxxxx\n2.yyyyyyyy.\n3.hhhhhhhhhh\n4.sssssssssssss\n5.nnnnnnnnnnnnnnn")
                    .setUpdateInfo(changlog)//更新日志
                    .setPositiveButton("立即升级", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG, "点击确认按钮,提示安装");
                            checkMd5Installer(file, isFirstInstall);
                        }
                    })
                    .setNegativeButton("暂不更新", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG, "点击了取消按钮,不安装");
                        }
                    });
        }
        if (mUpgradeDialog != null) {
            mUpgradeDialog.show();
        }
    }


    /**
     * 显示是否下载更新包对话框
     *
     * @param changlog         更新日志
     * @param url              APK下载路径
     * @param dirPath          APK保存路径
     * @param fileName         APK保存名称
     * @param downloadCallback 下载回调
     */
    private void showDownloadDialog(String changlog, final String url, final String dirPath, final String fileName, final DownloadCallback downloadCallback) {
        Log.i(TAG, "showDownloadDialog: 对话框是否显示 isShowDialog = " + isShowDialog + ", mUpgradeDialog = " + mUpgradeDialog);
        if (isShowDialog) {
            Log.i(TAG, "showDownloadDialog: 对话框已经在显示了,先将他消失掉");
            mUpgradeDialog.dismiss();
        }

        if (mUpgradeDialog == null) {
            mUpgradeDialog = new UpgradeDialog.Builder(this)
                    .setBackground(R.drawable.dialog_background)
                    .setTextColor(R.color.white)
                    .setButtonSelector(R.drawable.selector_update_dialog_btn)
                    .setDefaultSelect()//设置默认选中
                    //.setForcedUpdate()//设置强制升级
                    .setTitle("检查到新版本")
                    .setFeature("新版本特性")
                    //.setUpdateInfo("1.xxxxxxx\n2.yyyyyyyy.\n3.hhhhhhhhhh\n4.sssssssssssss\n5.nnnnnnnnnnnnnnn")
                    .setUpdateInfo(changlog)//更新日志
                    .setPositiveButton("立即升级", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG, "点击确认按钮,下载安装包");
                            BaseUpgrade.requestDownload(url, dirPath, fileName, downloadCallback);
                        }
                    })
                    .setNegativeButton("暂不更新", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG, "点击了取消按钮");
                        }
                    });
        }
        if (mUpgradeDialog != null) {
            mUpgradeDialog.show();
        }
    }

}

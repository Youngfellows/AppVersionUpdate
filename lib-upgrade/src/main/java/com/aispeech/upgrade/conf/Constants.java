package com.aispeech.upgrade.conf;

/**
 * Created by Byron on 2018/4/21.
 */

public class Constants {

    /**
     * 是否显示确认下载的对话框
     */
    public static boolean DISPLAY_DOWNLOAD_DIALOG = false;

    /**
     * 是否显示安装提示的对话框
     */
    public static boolean DISPLAY_INSTALL_DIALOG = true;


    /**
     * 闹钟的Action
     */
    public static final String ALARM_ACTION_UPGRADE = "com.aispeech.tvui.ALARM_ACTION_UPGRADE";
    public static final String ALARM_ACTION_INSTALLER = "com.aispeech.tvui.ALARM_ACTION_INSTALLER";

    /**
     * 请求更新包信息v1版本接口
     */
    //public static final String BASE_URL = "http://api.iot.aispeech.com/skyline-iot-api/api/v1/tv/versionUpgrade";

    /**
     * 请求更新包信息v2版本接口
     */
    public static final String BASE_URL = "http://api.iot.aispeech.com/skyline-iot-api/api/v2/tv/versionUpgrade";

    /**
     * 请求更新包信息v2版本开发环境接口
     */
    //    public static final String BASE_URL = "http://172.16.152.200:8089/skyline-iot-api/api/v2/tv/versionUpgrade";

    /**
     * 请求更新包信息v2版本测试环境接口
     */
    //    public static final String BASE_URL = "http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade";

    /**
     * 下载的APK的文件名
     */
    public static String DOWNLOAD_APK_FILE_NAME = "tvui_tv_aispeech.apk";//下载Apk的名字

    /**
     * 更新配置文件文件名
     */
    public static String UPDATE_CONFIG_FILE_NAME = "version_config_v2.json";//更新配置文件的名字

    /**
     * 常量包名
     */
    public static String PACKAGE_NAME = "PackageName";

    /**
     * 常量版本号
     */
    public static String VERSION_CODE = "VersionCode";

    /**
     * 常量版本名称
     */
    public static String VERSION_NAME = "VersionName";

    /**
     * 该版本已提示安装次数
     */
    public static String FREQUENCY = "Frequency";

    /**
     * 最多提示升级次数
     */
    public static int MAX_FREQUENCY = 3;

    /**
     * 客户类型
     */
    //    public static InstallerType CUSTOMER_TYPE = InstallerType.MANGO;
    public static InstallerType CUSTOMER_TYPE = InstallerType.SLIENT;

    /**
     * 常量静默安装
     */
    public static String SLIENT_INSTALLER = "slient_installer";

    /**
     * 常量普通安装
     */
    public static String COMMON_INSTALLER = "common_installer";

    /**
     * mBaseUrl = intent.getStringExtra("baseUrl"); //更新包请求地址
     * mProductId = intent.getStringExtra("productId"); //产品ID
     * mDeviceId = intent.getStringExtra("deviceId"); //设备ID
     * String pullUpgradeTime = intent.getStringExtra("pullUpgradeTime");
     * String nextInstallTime = intent.getStringExtra("nextInstallTime");
     * String installerMode = intent.getStringExtra("installerMode");
     */

    /**
     * 更新包请求地址
     */
    public static String BASEURL = "baseUrl";

    /**
     * 产品ID
     */
    public static String PRODUCT_ID = "productId";

    /**
     * 设备ID
     */
    public static String DEVICE_ID = "deviceId";

    /**
     * 拉取更新配置时间
     */
    public static String PULL_UPGRADE_TIME = "pullUpgradeTime";

    /**
     * 下次提示安装时间
     */
    public static String NEXT_INSTALL_TIME = "nextInstallTime";

    /**
     * 安装模式
     */
    public static String INSTALLER_MODE = "installerMode";

    /**
     * 强制升级
     */
    public static String FORCE_UPGRADE = "force";

    /**
     * 通知升级
     */
    public static String MANUAL_UPGRADE = "manual";

    /**
     * 静默升级
     */
    public static String QUIET_UPGRADE = "quiet";
}

package com.aispeech.upgradeaar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.aispeech.tvui.common.interfaces.DoanloadCallback;
import com.aispeech.tvui.common.interfaces.UpgradeRequestCallBack;
import com.aispeech.tvui.common.manager.RetrofitManager;
import com.aispeech.tvui.common.retrofit.DownloadListener;
import com.aispeech.tvui.common.retrofit.RetrofitClient;
import com.aispeech.tvui.common.util.URLUtils;
import com.aispeech.upgrade.base.TvuiUpgrade;
import com.aispeech.upgrade.bean.Content;
import com.aispeech.upgrade.bean.UpdateAppConfigBean;
import com.aispeech.upgrade.dialog.DialogStateListener;
import com.aispeech.upgrade.dialog.UpgradeDialog;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initDialogStateListener();
    }

    public void initDialogStateListener() {
        UpgradeDialog.setDialogStateListener(mDialogStateListener);
    }

    private DialogStateListener mDialogStateListener = new DialogStateListener() {
        @Override
        public void onShow() {
            isShowDialog = true;
        }

        @Override
        public void onDismiss() {
            isShowDialog = false;
        }
    };


    /**
     * 获取域名
     *
     * @param view
     */
    public void getHost(View view) {
        String path = "http://aispeech-tvui-public.oss-cn-shenzhen.aliyuncs.com/release/dangbei/tvui-tv/tvui-tv-dangbei-1.0.11.180929.2-1011.apk";
        Log.i(TAG, URLUtils.getHost(path));
        Log.i(TAG, URLUtils.getUrl(path));

    }


    /**
     * 下载APK
     *
     * @param view
     */
    public void download(View view) {
        //        final String fileUrl = "http://aispeech-tvui-public.oss-cn-shenzhen.aliyuncs.com/release/dangbei/tvui-tv/tvui-tv-dangbei-1.0.11.180929.2-1011.apk";
        //        final String fileUrl = "http://bsl-cdn.ottboxer.cn/apkmarket_file/app/video/iqiyi_voice/GitvVideo-release-one-weihaokeji-kaV11906-tv8.1.0-r73356-gitv-auto_player.apk";

        //        final String fileUrl = "http://bsl-cdn.ottboxer.cn/apkmarket_file/app/system_recommend/gqzbncb/BA_LIVE[BETA]_ALI_20180831_10022.apk";

        final String fileUrl = "http://ksyun-cdn.ottboxer.cn/apkmarket_file/app/video/CIBN_dangbei/10034989_CIBN_CoolMiao_dangbei12.apk";

        //        final String fileUrl = "http://ksyun-cdn.ottboxer.cn/apkmarket_file/app/video/ystjg/tv_video_3.3.2.2020_android_13090.apk";

        //        final String fileUrl = "http://ksyun-cdn.ottboxer.cn/apkmarket_file/app/online_music/QQmusic_TV/qqyy_3.2.0.7_dangbei.apk";

        RetrofitManager.getInstance().download(fileUrl, "sdcard/Download/", "download222.apk", new DoanloadCallback() {
            @Override
            public void onSuccess(File file) {
                Log.i(TAG, "onSuccess path: " + file.getPath());
            }

            @Override
            public void onFailure(Throwable error) {
                Log.e(TAG, "onFailure " + error.getMessage());
            }

            @Override
            public void onLoading(long total, long progress, boolean done) {
                Log.i(TAG, "22 onLoading " + (float) (progress * 1.0 / total) * 100 + "% , " + (done ? "下载完成" : "未下载完成"));
            }
        });
    }

    /**
     * 下载APK
     *
     * @param view
     */
    public void download2(View view) {
        Log.i(TAG, "使用simin方式下载");

        //        final String fileUrl = "http://aispeech-tvui-public.oss-cn-shenzhen.aliyuncs.com/release/dangbei/tvui-tv/tvui-tv-dangbei-1.0.11.180929.2-1011.apk";
        //        final String fileUrl = "http://bsl-cdn.ottboxer.cn/apkmarket_file/app/video/iqiyi_voice/GitvVideo-release-one-weihaokeji-kaV11906-tv8.1.0-r73356-gitv-auto_player.apk";

        //        final String fileUrl = "http://bsl-cdn.ottboxer.cn/apkmarket_file/app/system_recommend/gqzbncb/BA_LIVE[BETA]_ALI_20180831_10022.apk";

        final String fileUrl = "http://ksyun-cdn.ottboxer.cn/apkmarket_file/app/video/CIBN_dangbei/10034989_CIBN_CoolMiao_dangbei12.apk";

        //        final String fileUrl = "http://ksyun-cdn.ottboxer.cn/apkmarket_file/app/video/ystjg/tv_video_3.3.2.2020_android_13090.apk";

        //        final String fileUrl = "http://ksyun-cdn.ottboxer.cn/apkmarket_file/app/online_music/QQmusic_TV/qqyy_3.2.0.7_dangbei.apk";

        //RetrofitManager.getInstance().download(fileUrl, "sdcard/Download/", "download222.apk", new DoanloadCallback() {

        RetrofitClient.getRetrofitClient().download(fileUrl, "sdcard/Download/", "simin_download333.apk", new DownloadListener() {
            @Override
            public void onSuccess(File file) {
                Log.i(TAG, "simin onSuccess path: " + file.getPath());
            }

            @Override
            public void onFailure(Throwable error) {
                Log.e(TAG, "simin onFailure " + error.getMessage());
            }

            @Override
            public void onLoading(long total, long progress, boolean done) {
                Log.i(TAG, "simin onLoading " + (float) (progress * 1.0 / total) * 100 + "% , " + (done ? "下载完成" : "未下载完成"));
            }
        });
    }


    /**
     * 更新配置单
     *
     * @param view
     */

    public void upgradeConfig(View view) {
        //        String configUrl = "http://aispeech-tvui-public.oss-cn-shenzhen.aliyuncs.com/release/dangbei/version-guide-1.1.json";
        String configUrl = "http://aispeech-tvui-public.oss-cn-shenzhen.aliyuncs.com/release/tvuipublic/version-guide-1.1.json";

        RetrofitManager.getInstance().upgradeConfig(configUrl, new UpgradeRequestCallBack() {
            @Override
            public void requestSuccess(String data) {

                UpdateAppConfigBean configBean = JSON.parseObject(data, UpdateAppConfigBean.class);
                List<Content> appArray = configBean.getContent();
                Content tvuiApp = null;//TVUI下载信息
                for (Content app : appArray) {
                    String component = app.getComponent();
                    String mDownVersionName = app.getVersionName();
                    int mDownVersionCode = Integer.parseInt(app.getVersionCode());
                    String mMd5 = app.getMd5();//APK的MD5值
                    String changeLog = app.getChangeLog();
                    String changlog = changeLog;
                    String appUrl = app.getUrl();

                    Log.d(TAG, "component: " + component + "\nversionCode: " + mDownVersionCode + "\nversionName: " + mDownVersionName + "\nmd5: " + mMd5 + "\nappUrl: " + appUrl + "\nchangeLog: " + changeLog);

                }
            }

            @Override
            public void requestError(String exception) {
                Log.e(TAG, "requestError " + exception);
            }
        });
    }

    /**
     * 获取更新配置
     *
     * @param view
     */
    public void upgradeConfig2(View view) {
        //        String configUrl = "http://aispeech-tvui-public.oss-cn-shenzhen.aliyuncs.com/release/dangbei/version-guide-1.1.json";
        String configUrl = "http://aispeech-tvui-public.oss-cn-shenzhen.aliyuncs.com/release/tvuipublic/version-guide-1.1.json";

        RetrofitClient.getRetrofitClient().upgradeConfig(configUrl, new com.aispeech.tvui.common.retrofit.UpgradeRequestCallBack() {
            @Override
            public void requestSuccess(String data) {
                UpdateAppConfigBean configBean = JSON.parseObject(data, UpdateAppConfigBean.class);
                List<Content> appArray = configBean.getContent();
                Content tvuiApp = null;//TVUI下载信息
                for (Content app : appArray) {
                    String component = app.getComponent();
                    String mDownVersionName = app.getVersionName();
                    int mDownVersionCode = Integer.parseInt(app.getVersionCode());
                    String mMd5 = app.getMd5();//APK的MD5值
                    String changeLog = app.getChangeLog();
                    String changlog = changeLog;
                    String appUrl = app.getUrl();

                    Log.d(TAG, "si.min component: " + component + "\nversionCode: " + mDownVersionCode + "\nversionName: " + mDownVersionName + "\nmd5: " + mMd5 + "\nappUrl: " + appUrl + "\nchangeLog: " + changeLog);

                }
            }

            @Override
            public void requestError(String exception) {
                Log.e(TAG, "requestError " + exception);
            }
        });
    }


    /**
     * 获取版本更新信息
     *
     * @param view
     */
    public void upgradeVersion(View view) {

        /**
         * 触发网络请求,先拉取是否可以更新
         * mBaseUrl: http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade ,productId: 278572232 ,deviceId: 4d07e4be9184e15a8b483c97077e171b
         * url = http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade?productId=278572232&versionCode=1005&deviceId=4d07e4be9184e15a8b483c97077e171b&packageName=com.aispeech.tvui
         */
        //        String url = "http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade";//测试环境
        //        String url = "http://api.iot.aispeech.com/skyline-iot-api/api/v2/tv/versionUpgrade";//正式环境
        //        String productId = "278572232";
        //        String versionCode = "1005";
        //        String deviceId = "4d07e4be9184e15a8b483c97077e171b";
        //        String packageName = "com.aispeech.tvui";

        //        mBaseUrl: http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade ,productId: 278572232 ,deviceId: 4d07e4be9184e15a8b483c97077e171b
        //        url = http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade?productId=278572232&versionCode=1005&deviceId=4d07e4be9184e15a8b483c97077e171b&packageName=com.aispeech.tvui


        //当贝
        /**
         *  mBaseUrl: http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade ,productId: 278574056 ,deviceId: 985664e014ef446db706de665035949d
         *  url = http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade?productId=278574056&versionCode=1010&deviceId=985664e014ef446db706de665035949d&packageName=com.aispeech.tvui
         */

        String url = "http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade";//测试环境
        //        String url = "http://api.iot.aispeech.com/skyline-iot-api/api/v2/tv/versionUpgrade";//正式环境
        String productId = "278574056";
        String versionCode = "1005";
        String deviceId = "985664e014ef446db706de665035949d";
        String packageName = "com.aispeech.tvui";

        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);
        map.put("versionCode", versionCode);
        map.put("deviceId", deviceId);
        map.put("packageName", packageName);

        RetrofitManager.getInstance().upgradeVersion(url, map, new UpgradeRequestCallBack() {
            @Override
            public void requestSuccess(String data) {
                Log.i(TAG, "requestSuccess 版本更新信息==>> " + data);
            }

            @Override
            public void requestError(String exception) {
                Log.e(TAG, "requestError " + exception);
            }
        });
    }

    /**
     * 获取版本信息
     *
     * @param view
     */
    public void upgradeVersion2(View view) {
        /**
         * 触发网络请求,先拉取是否可以更新
         * mBaseUrl: http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade ,productId: 278572232 ,deviceId: 4d07e4be9184e15a8b483c97077e171b
         * url = http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade?productId=278572232&versionCode=1005&deviceId=4d07e4be9184e15a8b483c97077e171b&packageName=com.aispeech.tvui
         */
        //        String url = "http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade";//测试环境
        //        String url = "http://api.iot.aispeech.com/skyline-iot-api/api/v2/tv/versionUpgrade";//正式环境
        //        String productId = "278572232";
        //        String versionCode = "1005";
        //        String deviceId = "4d07e4be9184e15a8b483c97077e171b";
        //        String packageName = "com.aispeech.tvui";

        //        mBaseUrl: http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade ,productId: 278572232 ,deviceId: 4d07e4be9184e15a8b483c97077e171b
        //        url = http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade?productId=278572232&versionCode=1005&deviceId=4d07e4be9184e15a8b483c97077e171b&packageName=com.aispeech.tvui


        //当贝
        /**
         *  mBaseUrl: http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade ,productId: 278574056 ,deviceId: 985664e014ef446db706de665035949d
         *  url = http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade?productId=278574056&versionCode=1010&deviceId=985664e014ef446db706de665035949d&packageName=com.aispeech.tvui
         */

        String url = "http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade";//测试环境
        //        String url = "http://api.iot.aispeech.com/skyline-iot-api/api/v2/tv/versionUpgrade";//正式环境
        String productId = "278574056";
        String versionCode = "1005";
        String deviceId = "985664e014ef446db706de665035949d";
        String packageName = "com.aispeech.tvui";

        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);
        map.put("versionCode", versionCode);
        map.put("deviceId", deviceId);
        map.put("packageName", packageName);

       RetrofitClient.getRetrofitClient().upgradeVersion(url, map, new com.aispeech.tvui.common.retrofit.UpgradeRequestCallBack() {
           @Override
           public void requestSuccess(String data) {
               Log.i(TAG, "si.min requestSuccess 版本更新信息==>> " + data);
           }

           @Override
           public void requestError(String exception) {
               Log.e(TAG, "requestError " + exception);
           }
       });
    }

    /**
     * 测试升级安装
     *
     * @param view
     */
    public void upgradeInstall(View view) {
        //        String url = "http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade";//测试环境
        //        String url = "http://api.iot.aispeech.com/skyline-iot-api/api/v2/tv/versionUpgrade";//正式环境
        //        String productId = "278572232";
        //        String versionCode = "1005";
        //        String deviceId = "4d07e4be9184e15a8b483c97077e171b";
        //        String packageName = "com.aispeech.tvui";

        //当贝
        /**
         *  mBaseUrl: http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade ,productId: 278574056 ,deviceId: 985664e014ef446db706de665035949d
         *  url = http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade?productId=278574056&versionCode=1010&deviceId=985664e014ef446db706de665035949d&packageName=com.aispeech.tvui
         */

        String url = "http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade";//测试环境
        //String url = "http://api.iot.aispeech.com/skyline-iot-api/api/v2/tv/versionUpgrade";//正式环境
        String productId = "278574056";
        String versionCode = "1005";
        String deviceId = "985664e014ef446db706de665035949d";
        String packageName = "com.aispeech.tvui";


        String baseUrl = url;
        String installerMode = "common_installer";
        String pullUpgradeTime = "0000-00-00 00:02:00";
        String nextInstallTime = "0000-00-00 24:00:00";

        TvuiUpgrade.getInstance(App.getApplication()).init(baseUrl, installerMode, productId, deviceId, pullUpgradeTime, nextInstallTime);
    }

    private UpgradeDialog.Builder mUpgradeDialog = null;
    private boolean isShowDialog = false;

    /**
     * 显示对话框
     *
     * @param view
     */
    public void showDialog(View view) {
        Log.i(TAG, "showDownloadDialog: 对话框是否显示 isShowDialog = " + isShowDialog + ", mUpgradeDialog = " + mUpgradeDialog);
        if (isShowDialog && mUpgradeDialog != null) {
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
                    .setUpdateInfo("1.xxxxxxx\n2.yyyyyyyy.\n3.hhhhhhhhhh\n4.sssssssssssss\n5.nnnnnnnnnnnnnnn")
                    //                    .setUpdateInfo(changlog)//更新日志
                    .setPositiveButton("立即升级", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG, "点击确认按钮,提示安装");
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
}

package com.aispeech.tvui.common.manager;

import android.util.Log;

import com.aispeech.tvui.common.entity.FileDownloadEntity;
import com.aispeech.tvui.common.interfaces.DoanloadCallback;
import com.aispeech.tvui.common.interfaces.RequestCallback;
import com.aispeech.tvui.common.net.IApiService;
import com.aispeech.tvui.common.net.RetrofitCallback;
import com.aispeech.tvui.common.net.RetrofitClient;
import com.aispeech.tvui.common.net.RetrofitFileUtils;
import com.aispeech.tvui.common.util.URLUtils;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RetrofitManager {
    private String TAG = "RetrofitManager";
    private static RetrofitManager instance;

    private RetrofitManager() {

    }

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    /**
     * 下载文件
     *
     * @param fileUrl  下载文件url
     * @param dirPath  保存的目录名称
     * @param fileName 保存的文件名
     * @param callback 下载回调
     */
    public void download(String fileUrl, final String dirPath, final String fileName, final DoanloadCallback callback) {

        //        String path = "http://aispeech-tvui-public.oss-cn-shenzhen.aliyuncs.com/release/dangbei/tvui-tv/tvui-tv-dangbei-1.0.11.180929.2-1011.apk";
        //        fileUrl = "http://ksyun-cdn.ottboxer.cn/apkmarket_file/app/video/ystjg/tv_video_3.3.2.2020_android_13090.apk";

        String url = URLUtils.getUrl(fileUrl);
        String baseUrl = URLUtils.getHost(fileUrl);
        Log.i(TAG, baseUrl);
        Log.i(TAG, url);
        try {
            FileDownloadEntity downloadEntity = new FileDownloadEntity(url);
            RetrofitFileUtils.downloadFile(baseUrl, downloadEntity, new RetrofitCallback<ResponseBody>() {
                @Override
                public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i(TAG, "onSuccess: code = " + response.code());
                    InputStream is = null;
                    FileOutputStream fos = null;
                    BufferedInputStream bis = null;
                    try {
                        is = response.body().byteStream();
                        //Log.i("TAG", "is = " + is.toString());

                        // String path = "/sdcard/Download";
                        // File f = new File(path);

                        File f = new File(dirPath);
                        if (!f.exists()) {
                            f.mkdirs();
                        }

                        //File file = new File(f, "download.apk");
                        File file = new File(f, fileName);
                        if (file.exists()) {
                            Log.i(TAG, "文件已经纯在了，删除");
                            file.delete();
                        }

                        fos = new FileOutputStream(file);
                        bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        fos.flush();
                        Log.i(TAG, "文件保存成功");
                        if (callback != null) {
                            callback.onSuccess(file);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        closeStream(is);
                        closeStream(fos);
                        closeStream(bis);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i(TAG, "onFailure " + t.getMessage());
                    if (callback != null) {
                        callback.onFailure(t);
                    }
                }

                @Override
                public void onLoading(long total, long progress, boolean done) {
                    //super.onLoading(total, progress, done);
                    Log.i(TAG, "onLoading " + (float) (progress * 1.0 / total) * 100 + "% , " + (done ? "下载完成" : "未下载完成"));
                    if (callback != null) {
                        callback.onLoading(total, progress, done);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(new Throwable("baseUrl参数异常: " + baseUrl));
            }
        }
    }

    /**
     * 关闭IO流
     */
    private void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取更新配置单
     *
     * @param configUrl 配置文件url
     * @param callback  回调
     */
    public void upgradeConfig(String configUrl, final RequestCallback callback) {
        String url = URLUtils.getUrl(configUrl);
        String baseUrl = URLUtils.getHost(configUrl);
        Log.i(TAG, baseUrl);
        Log.i(TAG, url);

        try {
            IApiService iApiService = RetrofitClient.getInstance(baseUrl).getiApiService();
            Call<ResponseBody> call = iApiService.upgradeConfig(url);
            call.enqueue(new RetrofitCallback<ResponseBody>() {
                @Override
                public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String data = response.body().string();
                            Log.i(TAG, "data -->>>  " + data);
                            if (callback != null) {
                                callback.onSuccess(data);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            if (callback != null) {
                                callback.onFailure(e.getMessage());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "onFailure " + t.getMessage());
                    if (callback != null) {
                        callback.onFailure(t.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure("baseUrl参数异常: " + baseUrl);
            }
        }
    }

    /**
     * 请求版本更新
     *
     * @param baseURL  升级环境
     *                 productId   产品ID
     *                 versionCode 版本号
     *                 deviceId    设备号
     *                 packageName 包名
     * @param callback 回调
     */
    //    public void upgradeVersion(String baseURL, String productId, String versionCode, String deviceId, String packageName, final RequestCallback callback) {
    public void upgradeVersion(String baseURL, Map<String, String> map, final RequestCallback callback) {

        /**
         * 触发网络请求,先拉取是否可以更新
         * mBaseUrl: http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade ,productId: 278572232 ,deviceId: 4d07e4be9184e15a8b483c97077e171b
         * url = http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade?productId=278572232&versionCode=1005&deviceId=4d07e4be9184e15a8b483c97077e171b&packageName=com.aispeech.tvui
         */

        /**
         *  不正常
         *  http://test.iot.aispeech.com/:8089/skyline-iot-api/api/v2/tv/versionUpgrade?productId=278572232&versionCode=1005&deviceId=4d07e4be9184e15a8b483c97077e171b&packageName=com.aispeech.tvui http/1.1
         *  正常
         *  http://test.iot.aispeech.com:8089/skyline-iot-api/api/v2/tv/versionUpgrade?productId=278572232&versionCode=1005&deviceId=4d07e4be9184e15a8b483c97077e171b&packageName=com.aispeech.tvui
         */

        //        Map<String, String> map = new HashMap<>();
        //        map.put("productId", productId);
        //        map.put("versionCode", versionCode);
        //        map.put("deviceId", deviceId);
        //        map.put("packageName", packageName);

        String url = URLUtils.getUrl(baseURL);
        String baseUrl = URLUtils.getHost(baseURL);
        Log.i(TAG, "baseUrl == " + baseUrl);
        Log.i(TAG, "url == " + url);

        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            Log.i(TAG, entry.getKey() + " --->> " + entry.getValue());
        }

        try {
            Log.e(TAG, "1111 --->>> 2222 ");
            IApiService iApiService = RetrofitClient.getInstance(baseUrl).getiApiService();
            Log.e(TAG, "333 --->>> 444 ");

            //Call<ResponseBody> call = iApiService.upgradeVersion(url, productId, versionCode, deviceId, packageName);//带参数
            Call<ResponseBody> call = iApiService.upgradeVersion(url, map);//参数列表
            call.enqueue(new RetrofitCallback<ResponseBody>() {
                @Override
                public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String data = response.body().string();
                            Log.i(TAG, "data -->>>  " + data);
                            if (callback != null) {
                                callback.onSuccess(data);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            if (callback != null) {
                                callback.onFailure(e.getMessage());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "onFailure " + t.getMessage());
                    if (callback != null) {
                        callback.onFailure(t.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure("baseUrl参数异常: " + baseUrl);
            }
        }
    }

}

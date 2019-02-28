package com.aispeech.tvui.common.manager;


import com.aispeech.tvui.common.interfaces.DoanloadCallback;
import com.aispeech.tvui.common.interfaces.RequestCallback;
import com.aispeech.tvui.common.retrofit.RetrofitClient;

import java.util.Map;

/**
 * HTTP网络请求入口
 */
public class HttpManager {
    private String TAG = "HttpManager";
    private static volatile HttpManager instance;

    /**
     * 是否保存下载的文件
     */
    private boolean isSave = true;

    private HttpManager() {

    }

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }


    /**
     * 普通Get请求
     *
     * @param url
     */
    public void get(String url, final RequestCallback callback) {
        RetrofitClient.getRetrofitClient().asyncGet(url, callback);
    }

    /**
     * 带参数get请求
     *
     * @param url
     * @param map
     */
    public void get(String url, Map<String, String> map, final RequestCallback callback) {
        RetrofitClient.getRetrofitClient().asyncGet(url, map, callback);
    }

    /**
     * 文件下载
     *
     * @param fileUrl
     * @param dirPath
     * @param fileName
     * @param callback
     */
    public void download(String fileUrl, final String dirPath, final String fileName, final DoanloadCallback callback) {

    }

}

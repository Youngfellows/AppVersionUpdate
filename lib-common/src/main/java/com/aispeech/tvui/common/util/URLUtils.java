package com.aispeech.tvui.common.util;

import android.text.TextUtils;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtils {
    private static String TAG = "URLUtils";

    /**
     * 或者指定url的域名
     *
     * @param path
     * @return
     */
    public static String getHost(String path) {
        String host = null;
        String key = null;
        try {
            //            URL url = new URL(path);
            //            host = "http://" + url.getHost();

            URL url = new URL(path);
            host = url.getHost();
            if (!TextUtils.isEmpty(host)) {
                String[] splits = path.split(host);
                if (splits != null && splits.length == 2) {
                    key = splits[1];
                }
                host = "http://" + host;
                if (key.startsWith(":")) {
                    int index = key.indexOf("/");
                    String port = key.substring(0, index);
                    host += port;
                    Log.i(TAG, "该url以:开头 port = " + port);
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "host = " + host);
        return host;
    }


    /**
     * 获取指定域名后的参数串
     *
     * @param path
     * @return
     */
    public static String getUrl(String path) {
        String key = null;
        try {
            URL url = new URL(path);
            String host = url.getHost();
            if (!TextUtils.isEmpty(host)) {
                String[] splits = path.split(host);
                if (splits != null && splits.length == 2) {
                    key = splits[1];
                }

                if (key.startsWith(":")) {
                    Log.i(TAG, "该url以:开头");
                    int index = key.indexOf("/");
                    key = key.substring(index);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "key = " + key);
        return key;
    }
}

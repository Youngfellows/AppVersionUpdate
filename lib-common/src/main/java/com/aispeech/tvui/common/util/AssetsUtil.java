package com.aispeech.tvui.common.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.aispeech.tvui.common.prop.SystemPropertiesProxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



public class AssetsUtil {
    private static String TAG = "AssetsUtil";

    /**
     * dds log 级别
     */
    public static final String log_level_dds = "log_level_dds";
    /**
     * TLog log 级别
     */
    public static final String log_level_tlog = "log_level_tlog";
    /**
     * SDK类型
     */
    public static final String sdk_type = "sdk_type";


    public static List<String> getJsonAssets(Context context, String path) throws IOException {
        String[] assetList = context.getAssets().list(path);
        List<String> files = new ArrayList<String>();
        for (String asset : assetList) {
            if (asset.toLowerCase().endsWith(".json")) {
                files.add(asset);
            }
        }
        return files;
    }

    /**
     * 根据key 获取配置文件中对应的值
     * @param context 上下文
     * @param key key值
     * @return 返回对应的value
     */
    public static String readProp(Context context, String key) {
        return readProp(context,key, "");
    }

    /**
     * 根据key 获取配置文件中对应的值
     * @param context 上下文
     * @param key key值
     * @param defaultValue 默认值
     * @return 返回对应的值
     */
    public static String readProp(Context context, String key, String defaultValue) {
        String propFilePath = getTvuiConfPath(context) + "/config.properties";

        Resources resources = context.getResources();
        AssetManager assetManager = resources.getAssets();
        InputStream inputStream = null;
        String value = null;
        try {
            File propFile = new File(propFilePath);
            // external properties
            if (propFile.isFile() && propFile.canRead()) {
                Properties properties=new Properties();
                InputStream stream = new FileInputStream(propFile);
                InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
                properties.load(reader);
                value = properties.getProperty(key);
                reader.close();
                stream.close();

                // allow empty string to overwrite inner one
                if (value != null) {
                    Log.i(TAG, "property@etc[" + key + "]: " + value);
                    return value;
                }
            }
            // inner properties
            inputStream = assetManager.open("config.properties");
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            Properties properties = new Properties();
            properties.load(reader);
            value = properties.getProperty(key);
            inputStream.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // default value will be used for the keys which not in inner and external properties
        if (value == null) {
            value = defaultValue;
        }
        TLog.i(TAG, "property@asset[" + key + "]: " + value);

        return value;
    }

    /**
     * 获取配置文件路径
     * @param context 上下文
     * @return 返回配置文件路径
     */
    public static String getTvuiConfPath(Context context) {

        String debug_path = SystemPropertiesProxy.get(context,
                "debug.aispeech.tvui.conf_path", "");

        String conf_path = SystemPropertiesProxy.get(context,
                "ro.aispeech.tvui.conf_path", "/system/etc/aispeech");
        return TextUtils.isEmpty(debug_path)?conf_path:debug_path;
    }


}

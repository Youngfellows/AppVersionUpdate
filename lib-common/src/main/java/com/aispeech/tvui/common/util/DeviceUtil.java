package com.aispeech.tvui.common.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;

/**
 * Created by Hongtao.Wan on 2018/9/18/0018.
 */

public class DeviceUtil {
    private static final String TAG = DeviceUtil.class.getSimpleName();

    /**
     * MAC地址最大长度
     * */
    private static final int MAC_MAX_LENGTH = 12;

    /**
     * 字符冒号
     * */
    private static final String CHAR_COLON = ":";

    private static String aispeechDeviceId;


    /**
     * 生成aispeech设备号
     *
     * @param factoryId 厂家编号
     * @param goodsId   产品编号
     * deviceId规则：保留(5位数)+厂家编号(4位最大值65536)+产品编号（3位最大值4096）+生产日期（yyyyMMdd）+mac地址(12位)
     */
    public static String createAispeechDeviceId(Context context,String factoryId, String goodsId) {
        String deviceId = "";
        if (!TextUtils.isEmpty(factoryId) && !TextUtils.isEmpty(goodsId)) {
            //预留字段，默认为00000
            String reservedStr = "00000";
            //生产日期,android客户端暂无法获取，默认为00000000
            String timestamp = "00000000";
            //mac地址，优先获取wifi的mac地址，如果获取不到，则获取有线的mac地址
            String mac = "000000000000";
            try {
                mac = getMacAddress(context);
                if (TextUtils.isEmpty(mac)) {
                    mac = getDeviceId(context);
                }

                if (TextUtils.isEmpty(mac)) {
                    mac = getSerialNumber();
                }

                if (!TextUtils.isEmpty(mac)) {
                    if (mac.contains(CHAR_COLON)) {
                        mac = mac.replaceAll(CHAR_COLON, "").trim();
                    }
                    if (mac.length() > MAC_MAX_LENGTH) {
                        mac = mac.substring(0, MAC_MAX_LENGTH);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            deviceId = reservedStr + factoryId + goodsId + timestamp + mac;
        } else {
            TLog.i(TAG,"factoryid or goodsId is null:");
        }
        TLog.i(TAG,"aispeechDeviceId:"+deviceId);
        aispeechDeviceId = deviceId;
        return deviceId;
    }

    /**
     * 获取aispeechDeviceId
     * @return
     */
    public static String getAispeechDeviceId() {
        return aispeechDeviceId;
    }

    /**
     * 序列号
     *
     * @return
     */
    public static String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return serial;
    }

    public static String tryGetDeviceId(Context context) {
        if (context == null) {
            return "";
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = null;
        if (tm != null) {
            imei = tm.getDeviceId();
        }
        return TextUtils.isEmpty(imei) ? "" : imei.trim();
    }

    /**
     * 获取系统deviceId
     * */
    public static String getDeviceId(Context context){
        return tryGetDeviceId(context);
    }

    /**
     * 获取设备mac地址
     * 优先wifi mac，然后eth mac
     */
    public static String getMacAddress(Context context) {
        String mac = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacByWifiManage(context);
        } else {
            mac = getMacByFile("cat /sys/class/net/wlan0/address");
            if (TextUtils.isEmpty(mac)) {
                mac = getMacByFile("cat /sys/class/net/eth0/address");
            }
        }
        return mac;
    }

    /**
     * @param context
     * @return mac
     * @throws AndroidRuntimeException
     */
    public static String getMacByWifiManage(Context context){
        String mac = null;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mac = tryGetMAC(wifiManager);
        if (TextUtils.isEmpty(mac)) {
            int state = wifiManager.getWifiState();
            if (state != WifiManager.WIFI_STATE_ENABLED && state != WifiManager.WIFI_STATE_ENABLING) {
                wifiManager.setWifiEnabled(true);
            }
            mac = tryGetMAC(wifiManager);
        }
        return mac;
    }

    /**
     * @param file mac file
     * @return mac
     * @throws IOException
     */
    private static String getMacByFile(String file) {
        String mac = "";
        try {
            String str = "";
            Process pp = Runtime.getRuntime().exec(file);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    mac = str.trim();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mac;
    }

    /**
     * @param manager
     * @return null or trimed mac address.
     */
    private static String tryGetMAC(WifiManager manager) {
        String mac = null;
        WifiInfo wifiInfo = manager.getConnectionInfo();
        if (wifiInfo != null && !TextUtils.isEmpty(wifiInfo.getMacAddress())) {
            mac = wifiInfo.getMacAddress().trim();
        }
        return mac;
    }

}

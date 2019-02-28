package com.aispeech.tvui.common.log;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class JsonLog extends BaseLog {

    /**
     * 换行符
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public JsonLog() {
    }

    /**
     * 打印json
     * @param tag 标签
     * @param msg 要打印的内容
     * @param str ""
     */
    public static void printJson(String tag, String msg, String str) {
        //拼接打印信息
        String spliceMsg;
        try {
            if(msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                spliceMsg = jsonObject.toString(4);
            } else if(msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                spliceMsg = jsonArray.toString(4);
            } else {
                spliceMsg = msg;
            }
        } catch (JSONException exception) {
            spliceMsg = msg;
        }

        printLine(tag, true);
        spliceMsg = str + LINE_SEPARATOR + spliceMsg;
        //以换行符分割字符串
        String[] strings = spliceMsg.split(LINE_SEPARATOR);
        int length = strings.length;

        for(int i = 0; i < length; ++i) {
            String snippetStr = strings[i];
            Log.d(tag, "║ " + snippetStr);
        }

        printLine(tag, false);
    }

}

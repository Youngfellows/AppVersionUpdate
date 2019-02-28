package com.aispeech.upgrade.utils;

/**
 * Created by bifeng on 2018/4/28.
 *
 */
public class ResultInfo {

    public int status; // 状态码
    public String message = ""; // 信息
    public String messageType = ""; //信息种类，非必需（try..catch是需要上报一个异常类型）

    public void add(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void add(int status, String messageType, String message) {
        this.status = status;
        this.messageType = messageType;
        this.message = message;
    }

    public ResultInfo(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResultInfo() {}

    @Override
    public String toString() {
        return "ResultInfo{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", messageType='" + messageType + '\'' +
                '}';
    }
}

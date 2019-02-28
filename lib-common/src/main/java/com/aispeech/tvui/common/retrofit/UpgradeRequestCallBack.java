package com.aispeech.tvui.common.retrofit;

public interface UpgradeRequestCallBack {
    void requestSuccess(String data);

    void requestError(String exception);
}

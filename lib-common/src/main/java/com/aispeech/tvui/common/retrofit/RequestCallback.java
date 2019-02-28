package com.aispeech.tvui.common.retrofit;

public interface RequestCallback {
    void onSuccess(String response);
    void onFaliue(String throwable);
}

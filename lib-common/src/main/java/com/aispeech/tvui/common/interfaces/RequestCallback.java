package com.aispeech.tvui.common.interfaces;

/**
 * Http请求回调
 */
public interface RequestCallback {

    void onSuccess(String data);

    void onFailure(String exception);

}

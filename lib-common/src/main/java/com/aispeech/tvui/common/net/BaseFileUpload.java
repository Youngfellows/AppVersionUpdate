package com.aispeech.tvui.common.net;

import retrofit2.Call;

/**
 * 文件上传基类
 */
public abstract class BaseFileUpload {

    public abstract Call getFileUploadCall(IApiService IApiService);
}

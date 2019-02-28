package com.aispeech.tvui.common.entity;

import com.aispeech.tvui.common.net.BaseFileDownload;
import com.aispeech.tvui.common.net.IApiService;

import retrofit2.Call;

/**
 * 测试下载文件请求实体类(具体更具需求变更)
 * 必须继承BaseFileDownload
 */
public class FileDownloadEntity extends BaseFileDownload {

    private String url;

    public FileDownloadEntity(String url) {
        this.url = url;
    }

    @Override
    public Call getFileDownloadCall(IApiService IApiService) {
        return IApiService.download(url);
    }
}

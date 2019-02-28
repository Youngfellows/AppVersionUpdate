package com.aispeech.tvui.common.retrofit;

import java.util.Map;

public interface ApiFuction {
    void upgradeVersion(String baseURL, Map<String, String> map, final UpgradeRequestCallBack callback);
    void upgradeConfig(String configUrl, final UpgradeRequestCallBack callback);
    void download(final String fileUrl, final String dirPath, final String fileName, DownloadListener callback);
}

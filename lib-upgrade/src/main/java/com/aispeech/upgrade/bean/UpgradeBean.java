
package com.aispeech.upgrade.bean;

import java.util.HashMap;
import java.util.Map;

public class UpgradeBean {

    /**
     * 是否可以更新
     */
    private Boolean upgrade;
    /**
     * 更新配置单拉取路径
     */
    private String url;
    /**
     * 云端版本号
     */
    private String versionCode;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 响应码
     */
    private Integer code;

    /**
     * 提示
     */
    private String message;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Boolean getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(Boolean upgrade) {
        this.upgrade = upgrade;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

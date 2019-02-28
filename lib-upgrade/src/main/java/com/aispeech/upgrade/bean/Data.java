
package com.aispeech.upgrade.bean;

import java.util.HashMap;
import java.util.Map;

public class Data {

    private String notification;
    private String upgradeWay;
    private String packageName;
    private String url;
    private String versionCode;
    private Integer frequency;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getUpgradeWay() {
        return upgradeWay;
    }

    public void setUpgradeWay(String upgradeWay) {
        this.upgradeWay = upgradeWay;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

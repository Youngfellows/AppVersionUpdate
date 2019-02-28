
package com.aispeech.upgrade.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateAppConfigBean {

    private String branch;
    private List<Content> content = null;
    private String contentType;
    private String scenario;
    private List<String> tags = null;
    private String title;
    private String versionCode;
    private String version_info;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionInfo() {
        return version_info;
    }

    public void setVersionInfo(String versionInfo) {
        this.version_info = versionInfo;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

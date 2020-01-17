package com.intl.init.model;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class InitModel {
    private String appId;
    private String appIdFacebook;
    private String appIdAppsflyer;
    private String clientName;
    private String clientCode;

    public InitModel() {
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppIdFacebook(String appIdFacebook) {
        this.appIdFacebook = appIdFacebook;
    }

    public void setAppIdAppsflyer(String appIdAppsflyer) {
        this.appIdAppsflyer = appIdAppsflyer;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getAppId() {
        return this.appId;
    }

    public String getAppIdFacebook() {
        return this.appIdFacebook;
    }

    public String getClientCode() {
        return this.clientCode;
    }

    public String getClientName() {
        return this.clientName;
    }

    public String getAppIdAppsflyer() {
        return this.appIdAppsflyer;
    }
}


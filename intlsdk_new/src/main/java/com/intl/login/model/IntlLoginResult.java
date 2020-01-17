package com.intl.login.model;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class IntlLoginResult {
    String accessToken;
    String id;
    String puid;
    String username;
    String email;
    String type_user;
    String new_user;
    String avatar;

    public IntlLoginResult() {
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getUserId() {
        return this.id;
    }

    public String getPuid() {
        return this.puid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getType_user() {
        return this.type_user;
    }

    public String getNew_user() {
        return this.new_user;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}


package com.intl.login.model;

import com.google.gson.annotations.SerializedName;
import com.intl.base.BaseResponse;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class UserSdkInfo extends BaseResponse {
    @SerializedName("user_info")
    IntlLoginResult loginResult;
    @SerializedName("update")
    UserSdkInfo.Update update;
    String retry;
    String logout;

    public UserSdkInfo() {
    }

    public String getLogout() {
        return this.logout;
    }

    public String getRetry() {
        return this.retry;
    }

    public IntlLoginResult getLoginResult() {
        return this.loginResult;
    }

    public UserSdkInfo.Update getUpdate() {
        return this.update;
    }

    public static class Update {
        String status;
        String force;
        String link;

        public Update() {
        }

        public String getStatus() {
            return this.status;
        }

        public String getForce() {
            return this.force;
        }

        public String getLink() {
            return this.link;
        }
    }
}
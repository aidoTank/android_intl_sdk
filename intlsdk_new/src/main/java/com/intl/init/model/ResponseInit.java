package com.intl.init.model;

import com.google.gson.annotations.SerializedName;
import com.intl.base.BaseResponse;

import java.util.ArrayList;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class ResponseInit extends BaseResponse {
    @SerializedName("data")
    private ResponseInit.Data data;

    public ResponseInit() {
    }

    public ResponseInit.Data getData() {
        return this.data;
    }

    public static class BannersObject {
        @SerializedName("img_url")
        private String imgUrl;
        @SerializedName("type")
        private int type;
        @SerializedName("sort")
        private int sort;
        @SerializedName("backup_url")
        private String backupUrl;
        @SerializedName("target_url")
        private String targetUrl;

        public BannersObject() {
        }

        public String getImgUrl() {
            return this.imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getType() {
            return this.type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getSort() {
            return this.sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getBackupUrl() {
            return this.backupUrl;
        }

        public void setBackupUrl(String backupUrl) {
            this.backupUrl = backupUrl;
        }

        public String getTargetUrl() {
            return this.targetUrl;
        }

        public void setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
        }
    }

    public static class Data {
        String status;
        String image_age;
        String warning_time_message;
        int show_warning_ingame;
        String e_name;
        String device_token;
        String size_image_age_width;
        String size_image_age_height;
        String limit_reconnect_mqtt;
        String icon_db;
        int hidden_dashboard;
        int warning_time_connect;
        String active_mqtt;
        String domain_mqtt;
        String port_mqtt;
        String url_warning;
        String logo;
        @SerializedName("banners")
        ArrayList<BannersObject> arrbanners;

        public Data() {
        }

        public String getLogo() {
            return this.logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getDevice_token() {
            return this.device_token;
        }

        public String getE_name() {
            return this.e_name;
        }

        public String getStatus() {
            return this.status;
        }

        public String getImage_age() {
            return this.image_age;
        }

        public String getWarning_time_message() {
            return this.warning_time_message;
        }

        public int getShow_warning_ingame() {
            return this.show_warning_ingame;
        }

        public String getSize_image_age_width() {
            return this.size_image_age_width;
        }

        public String getSize_image_age_height() {
            return this.size_image_age_height;
        }

        public String getLimit_reconnect_mqtt() {
            return this.limit_reconnect_mqtt;
        }

        public String getIcon_db() {
            return this.icon_db;
        }

        public int getHidden_dashboard() {
            return this.hidden_dashboard;
        }

        public int getWarning_time_connect() {
            return this.warning_time_connect;
        }

        public String getActive_mqtt() {
            return this.active_mqtt;
        }

        public String getDomain_mqtt() {
            return this.domain_mqtt;
        }

        public String getPort_mqtt() {
            return this.port_mqtt;
        }

        public String getUrl_warning() {
            return this.url_warning;
        }

        public ArrayList<ResponseInit.BannersObject> getArrbanners() {
            return this.arrbanners;
        }
    }
}


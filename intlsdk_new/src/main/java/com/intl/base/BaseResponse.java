package com.intl.base;

import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

/**
 * @Author: yujingliang
 * @Date: 2020/1/13
 */
public class BaseResponse {
    String status;
    String message;
    int error_code;
    @SerializedName("signed_request")
    String signedRequest;
    @SerializedName("detail_url")
    String detailUrl;

    public BaseResponse() {
    }

    public <T> T decodeResponse(Class<T> tClass) {
        if (TextUtils.isEmpty(this.signedRequest)) {
            return null;
        } else {
            String sDecode = new String(Base64.decode(this.signedRequest, 0));
            Gson gson = new Gson();

            try {
                return gson.fromJson(sDecode, tClass);
            } catch (JsonSyntaxException var5) {
                return null;
            }
        }
    }

    public String getDetailUrl() {
        return this.detailUrl;
    }

    public String getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public int getError_code() {
        return this.error_code;
    }
}

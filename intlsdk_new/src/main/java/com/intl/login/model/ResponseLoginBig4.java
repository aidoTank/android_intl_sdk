package com.intl.login.model;

import com.intl.base.BaseResponse;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class ResponseLoginBig4 extends BaseResponse {
    ResponseLoginBig4.Data data;
    String otp_token;
    String syntax;
    String phone_number;

    public ResponseLoginBig4() {
    }

    public ResponseLoginBig4.Data getData() {
        return this.data;
    }

    public String getOtp_token() {
        return this.otp_token;
    }

    public String getSyntax() {
        return this.syntax;
    }

    public String getPhone_number() {
        return this.phone_number;
    }

    public static class Data {
        String access_token;

        public Data() {
        }

        public String getAccess_token() {
            return this.access_token;
        }
    }
}


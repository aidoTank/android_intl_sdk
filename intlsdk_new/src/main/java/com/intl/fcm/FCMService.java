package com.intl.fcm;

import com.intl.base.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public interface FCMService {
    @FormUrlEncoded
    @POST("api/POST/Push/RegisterDevice")
    Call<BaseResponse> registerDevice(@Field("signed_request") String var1);

    @FormUrlEncoded
    @POST("api/POST/Push/RegisterDeviceFCM")
    Call<BaseResponse> registerDeviceFCM(@Field("signed_request") String var1);
}

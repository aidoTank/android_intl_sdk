package com.intl.login.presenter;

import com.intl.base.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public interface LoginService {
    @FormUrlEncoded
    @POST("api/GET/Me/Userinfo")
    Call<BaseResponse> getUserInfo(@Field("signed_request") String var1, @Field("lang") String var2);

    @FormUrlEncoded
    @POST("api/GET/Auth/LoginBig4?android_debug=1")
    Call<BaseResponse> loginBig4(@Field("signed_request") String var1, @Field("lang") String var2);

    @FormUrlEncoded
    @POST("api/GET/Mobile/LogPlayUser")
    Call<BaseResponse> mapUserGame(@Field("signed_request") String var1, @Field("lang") String var2);
}


package com.intl.init.presenter;

import com.intl.base.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public interface InitService {
    @FormUrlEncoded
    @POST("api/GET/App/Oinfo110")
    Call<BaseResponse> getAppInfo(@Field("signed_request") String var1, @Field("lang") String var2);
}

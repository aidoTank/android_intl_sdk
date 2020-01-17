package com.intl.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author: yujingliang
 * @Date: 2020/1/13
 */
public class RetrofitService {
    private static Retrofit retrofit;
    public static <T> T create(Class<T> tClass) {
        if (retrofit == null) {
            retrofit = (new Retrofit.Builder()).baseUrl("https://gather-auth.ycgame.com").addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit.create(tClass);
    }
}

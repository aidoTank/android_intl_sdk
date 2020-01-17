package com.intl.fcm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.intl.base.BaseResponse;
import com.intl.init.model.InitModel;
import com.intl.network.RetrofitService;
import com.intl.utils.PrefUtils;
import com.intl.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class FCMRequest {
    public static void sendRegistrationToServer(final Context context, String token) {
        subscribeTopic();
        FCMService fcmService = (FCMService) RetrofitService.create(FCMService.class);
        JSONObject jsonObject = Utils.createDefaultParams(context);

        try {
            jsonObject.put("redirect_uri", "uri_login");
            jsonObject.put("device_token", token);
            jsonObject.put("type", "android");
        } catch (JSONException var6) {
            var6.printStackTrace();
        }

        Call<BaseResponse> callRegisterDevice = fcmService.registerDeviceFCM("");
        callRegisterDevice.enqueue(new Callback<BaseResponse>() {
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                BaseResponse res = (BaseResponse)response.body();
                if (res != null) {
                    BaseResponse respon = (BaseResponse)res.decodeResponse(BaseResponse.class);
                    if (respon != null && respon.getStatus().equals("success")) {
                        PrefUtils.putBoolean(context, "PREF_IS_SEND_PUSH_NOTIFY_SUCCESS", true);
                    }
                }

            }

            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
            }
        });
        Call<BaseResponse> callRegisterDeviceGCM = fcmService.registerDevice("");
        callRegisterDeviceGCM.enqueue(new Callback<BaseResponse>() {
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
            }

            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
            }
        });
    }

    private static void subscribeTopic() {
        InitModel initModel = (InitModel)PrefUtils.getObject("PREF_INIT_MODEL", InitModel.class);
        if (initModel != null) {
            FirebaseMessaging.getInstance().subscribeToTopic(initModel.getAppId()).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                }
            });
        }
    }
}

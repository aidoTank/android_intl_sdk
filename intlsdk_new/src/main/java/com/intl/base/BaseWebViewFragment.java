package com.intl.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.intl.R;
import com.intl.utils.PrefUtils;
import com.intl.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;

/**
 * @Author: yujingliang
 * @Date: 2020/1/13
 */
public abstract class BaseWebViewFragment extends BaseFragment {
    private static final String TAG = "BaseWebViewFragment";
    protected WebView webView;
    protected ProgressBar progressLoading;

    public BaseWebViewFragment() {
    }

    protected abstract void onActivityCreated();

    protected abstract String getURLRequest();

    protected abstract boolean onShouldOverrideUrlLoading(String var1);

    protected abstract void onReceivedError(int var1, String var2, String var3);

    protected abstract void onPageStarted(String var1);

    protected abstract void onPageFinished(String var1);

    protected abstract void onJavaScriptInteract(String var1, String var2);

    @LayoutRes
    protected abstract int getLayoutRes();

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(this.getLayoutRes(), container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.onActivityCreated();
        this.webView = (WebView)this.getView().findViewById(R.id.webView);
        _symbolView = (RelativeLayout)this.getView().findViewById(IntlContext.getApplicationContext().getResources().getIdentifier("symbol_view", "id", IntlContext.getApplicationContext().getPackageName()));//R.id.symbol_view);
        _containerLayout = this.getView().findViewById(IntlContext.getApplicationContext().getApplicationContext().getResources().getIdentifier("container_layout", "id", IntlContext.getApplicationContext().getPackageName()));//R.id.symbol_view);
        this.progressLoading = (ProgressBar)this.getView().findViewById(R.id.progressLoading);
        String device_id = PrefUtils.getString("PREF_ADS_ID_GG");
        if (TextUtils.isEmpty(device_id)) {
            this.getAdvertisingIdClient();
        } else {
            this.setUpWebView(this.getURLRequest(),414,315);
        }

    }

    private void getAdvertisingIdClient() {
        final String deviceIdVcc = Utils.getDeviceIDVCC(IntlContext.getApplicationContext());
        (new Thread(new Runnable() {
            public void run() {
                AdvertisingIdClient.Info adInfo = null;

                try {
                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(IntlContext.getApplicationContext());
                } catch (GooglePlayServicesNotAvailableException var3) {
                    var3.printStackTrace();
                } catch (GooglePlayServicesRepairableException var4) {
                    var4.printStackTrace();
                } catch (IOException var5) {
                    var5.printStackTrace();
                }

                if (adInfo != null) {
                    String idAds = adInfo.getId();
                    PrefUtils.putString("PREF_ADS_ID_GG", idAds);
                } else {
                    PrefUtils.putString("PREF_ADS_ID_GG", deviceIdVcc);
                }

                BaseWebViewFragment.this.setUpWebView(BaseWebViewFragment.this.getURLRequest(),414,315);
            }
        })).start();
    }
    private int hightPixels;
    private int widthPixels;
    private RelativeLayout _symbolView = null;
    private LinearLayout _containerLayout  = null;
    public void setSize(int width, int height) {
        hightPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, this.getContext().getResources().getDisplayMetrics());
        widthPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, this.getContext().getResources().getDisplayMetrics());

        ViewGroup.LayoutParams layoutParams = _symbolView.getLayoutParams();
        layoutParams.width = widthPixels;
        layoutParams.height = hightPixels;
        _symbolView.setLayoutParams(layoutParams);
    }
    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void setUpWebView(String url,int width, int height) {
        this.webView.setVerticalScrollBarEnabled(false);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.setWebViewClient(new BaseWebViewFragment.DialogWebViewClient());
        this.webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        setSize(width,height);
        _containerLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                webView.layout(left, top, right,bottom);
            }
        });
        this.webView.addJavascriptInterface(new BaseWebViewFragment.JavaScriptInterface(), "JavaScriptInterface");
        this.webView.loadUrl(url);
        this.webView.setVisibility(View.INVISIBLE);
        this.webView.getSettings().setSavePassword(false);
        this.webView.getSettings().setSaveFormData(false);
        this.webView.setFocusable(true);
        this.webView.setFocusableInTouchMode(true);
        if (url != null) {
            Log.e("URL_WEBVIEW", url + " ");
        }

    }

    protected JSONObject createObjectRequest() {
        return Utils.createDefaultParams(this.getActivity());
    }

    private class JavaScriptInterface {

        @JavascriptInterface
        public void javaScriptInteract(final String method, final String value) {
            Log.e(TAG,"LoginJavaScriptInterface: " + method + "//" + value);
            BaseWebViewFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    BaseWebViewFragment.this.onJavaScriptInteract(method, value);
                }
            });
        }
    }

    private class DialogWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(TAG,"shouldOverrideUrlLoading Redirect URL: " + url);
            return BaseWebViewFragment.this.onShouldOverrideUrlLoading(url);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.e("mytag", "onReceivedError: " + failingUrl);
            Utils.showToastError(BaseWebViewFragment.this.getActivity());
            BaseWebViewFragment.this.onReceivedError(errorCode, description, failingUrl);
            BaseWebViewFragment.this.webView.loadUrl("");
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.e(TAG,"Webview onPageStarted: " + url);
            super.onPageStarted(view, url, favicon);
            BaseWebViewFragment.this.onPageStarted(url);
        }

        @SuppressLint({"SetJavaScriptEnabled"})
        public void onPageFinished(WebView view, String url) {
            Log.e(TAG,"Webview onPageFinished: " + url);
            super.onPageFinished(view, url);
            BaseWebViewFragment.this.webView.setVisibility(View.VISIBLE);
            //BaseWebViewFragment.this.progressLoading.setVisibility(View.GONE);
            BaseWebViewFragment.this.webView.loadUrl("javascript:function AppSDKexecute(method,value) {JavaScriptInterface.javaScriptInteract(method,value);}");
            BaseWebViewFragment.this.onPageFinished(url);
        }
    }
}
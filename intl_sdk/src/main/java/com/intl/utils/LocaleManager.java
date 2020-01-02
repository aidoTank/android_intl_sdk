package com.intl.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

/**
 * @Author: yujingliang
 * @Date: 2020/1/2
 */
public class LocaleManager {
    private static final String TAG = LocaleManager.class.getSimpleName();
    private static LocaleManager localeManager;
    public static final String PREF_LANGUAGE = "PREF_LANGUAGE";

    private SharedPreferences prefs;
    public static synchronized LocaleManager getInstance() {
        if (localeManager == null) {
            localeManager = new LocaleManager();
        }

        return localeManager;
    }

    public String getLanguage(Context context) {
        if (this.prefs == null) {
            this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        String languageCode = this.prefs.getString("PREF_LANGUAGE", (String)null);
        if (TextUtils.isEmpty(languageCode)) {
            String languageDevice = Locale.getDefault().getLanguage();
            if (!languageDevice.equals("vi") && !languageDevice.equals("zh") && !languageDevice.equals("en")) {
                languageCode = "en";
            } else {
                languageCode = languageDevice;
            }

            this.persistLanguage(context, languageCode);
        }

        Log.e(TAG, "languageCode: " + languageCode);
        return languageCode;
    }

    @SuppressLint({"ApplySharedPref"})
    private void persistLanguage(Context context, String language) {
        if (this.prefs == null) {
            this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        this.prefs.edit().putString("PREF_LANGUAGE", language).commit();
    }

    public Context setLocale(Context c) {
        setupLanguage(c, getLanguage(c));
        return c;
    }
    public Context setLocale(Context context, String language) {
        this.persistLanguage(context, language);
        setupLanguage(context, language);
        return context;
    }

    private void setupLanguage(Context context, String languageToLoad) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(languageToLoad);
        res.updateConfiguration(conf, dm);
    }

}

package com.intl.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;
/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGameUtil {
    private static final String TAG = "IntlGameUtil";
    public static boolean ENABLE_LOG = true;
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm") ;
    public static void getLocalGoogleAdID(final Context context, final IGgetLocalGoogleAdIdListener iGgetLocalGoogleAdIdListener){
        (new Thread(){
            public void run(){
                try{
                    Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context.getApplicationContext());
                    String googleAdID = adInfo.getId();
                    iGgetLocalGoogleAdIdListener.onComplete(0, googleAdID);
                }catch (Exception e)
                {
                    iGgetLocalGoogleAdIdListener.onComplete(-1, UUID.randomUUID().toString());
                }
            }
        }).start();
    }
    public static String getLocalAndroidId(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), "android_id");
        return androidID;
    }
    public interface IGgetLocalGoogleAdIdListener{
        void onComplete(int code, String ID);
    }

    /**
     * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm"<br />
     * 如果获取失败，返回null
     * @return
     */
    public static long getUTCTimeStr() {
        //StringBuffer UTCTimeBuffer = new StringBuffer();
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance() ;
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return (cal.getTime().getTime()/1000);
    }

    public static String md5(String source) {
        StringBuffer sb = new StringBuffer(32);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                array = md.digest(source.getBytes(StandardCharsets.UTF_8));
            }

            for(int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString(array[i] & 255 | 256).toUpperCase().substring(1, 3));
            }
        } catch (Exception var5) {
            return null;
        }

        return sb.toString().toLowerCase();
    }

    public static boolean checkNetwork(Activity act) {
        ConnectivityManager manager = (ConnectivityManager)act.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        } else {
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            return networkinfo != null && networkinfo.isAvailable();
        }
    }

    public static String getLocalSimOperator(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String simOperator = tm.getSimOperator();
        return simOperator;
    }

    public static String getLocalNetwordTypeName(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        String typeName = "";
        if (info != null) {
            typeName = info.getTypeName();
        } else {
            typeName = "NONE";
        }

        return typeName;
    }

    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    @SuppressLint("LongLogTag")
    public static String getLocalIpAddress(Context context) {
        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();

            while(en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface)en.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();

                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("WifiPreference IpAddress", e.toString());
        }

        return null;
    }


    public static JSONObject parseJson(String jsonstring) throws JSONException {
        JSONTokener jsonParser = new JSONTokener(jsonstring);
        return (JSONObject)jsonParser.nextValue();
    }

    public static void logd(String tag, String msg) {
        if (ENABLE_LOG && msg != null && !msg.equals("")) {
            Log.d(tag, msg);
        }

    }


    public static String objectToJson(Object object) {
        StringBuilder json = new StringBuilder();
        if (object == null) {
            json.append("\"\"");
        } else if (object instanceof String || object instanceof Integer) {
            json.append("\"").append(object.toString()).append("\"");
        }

        return json.toString();
    }

    public static String listToJson(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (list != null && list.size() > 0) {

            for (Object obj : list) {
                json.append(objectToJson(obj));
                json.append(",");
            }

            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }

        return json.toString();
    }

    public static boolean isAllNumber(String str) {
        for(int i = 0; i < str.length(); ++i) {
            if (!Character.isDigit(str.charAt(0))) {
                return false;
            }
        }

        return true;
    }

    public static String getNowTime() {
        SimpleDateFormat fomat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curdate = new Date(System.currentTimeMillis());
        return fomat.format(curdate);
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email.trim());
        return m.matches();
    }

}

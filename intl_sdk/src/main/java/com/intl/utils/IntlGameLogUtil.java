//package com.intl.utils;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.intl.ad.IntlGameAdStrack;
//
//import java.io.File;
//
///**
// * @Author: yujingliang
// * @Date: 2019/11/21
// */
//public class IntlGameLogUtil {
//    private static String TAG = "IntlGameLogUtil";
//    private static final int noPrint = 0;
//    private static final int Print = 1;
//
//    public IntlGameLogUtil() {
//    }
//
//    public static void i(String tag, String msg) {
//        if (IntlGameAdStrack.LemonLogMode != 0) {
//            Log.i(tag, msg);
//        }
//    }
//
//    public static void i(String tag, boolean msg) {
//        i(tag, String.valueOf(msg));
//    }
//
//    public static void i(String tag, int msg) {
//        i(tag, String.valueOf(msg));
//    }
//
//    public static void i(String tag, long msg) {
//        i(tag, String.valueOf(msg));
//    }
//
//    public static void WriteToFileLog(Context context, String msg) {
//        File newfile = new File("sdcard/lemon_docs/pay_log");
//        if (!newfile.exists()) {
//            newfile.mkdirs();
//            Log.i(TAG, "1:" + newfile.getAbsolutePath());
//        } else {
//            Log.i(TAG, "2:" + newfile.getAbsolutePath());
//        }
//
//        File[] allFiles = (new File("sdcard/lemon_docs/pay_log")).listFiles();
//        Log.i(TAG, "----------");
//        String var14;
//        LemonGamePurchaseLogSaveToFile var10000;
//        StringBuilder var10001;
//        LemonGamePurchaseLogSaveToFile var10002;
//        LemonGamePurchaseLogSaveToFile var10003;
//        if (allFiles.length == 0) {
//            Log.i(TAG, "all files is null");
//            var10003 = LemonGame.Log_instace;
//            File file2 = new File("sdcard/lemon_docs/pay_log", LemonGamePurchaseLogSaveToFile.createFileName(context).toString());
//            var10000 = LemonGame.Log_instace;
//            boolean flag = LemonGamePurchaseLogSaveToFile.saveToFile(context, msg, file2);
//            Log.i(TAG, "保存内容到文件：" + flag);
//            if (flag) {
//                var14 = TAG;
//                var10001 = new StringBuilder("读取：");
//                var10002 = LemonGame.Log_instace;
//                Log.i(var14, var10001.append(LemonGamePurchaseLogSaveToFile.getInfoFromFile(context, file2)).toString());
//            }
//        } else if (allFiles.length > 0) {
//            Log.i(TAG, "all files is not null");
//            Log.i(TAG, "all files length:" + allFiles.length);
//
//            int i;
//            File files;
//            for(i = 0; i < allFiles.length; ++i) {
//                Log.i(TAG, "file begin list");
//                files = allFiles[i];
//                Log.i(TAG, files.getName());
//                StringBuilder sb = new StringBuilder(files.getName());
//                String[] a = sb.toString().split("_", 2);
//                Log.i(TAG, a[0]);
//                Log.i(TAG, a[1]);
//                var10000 = LemonGame.Log_instace;
//                String fileNameToday = LemonGamePurchaseLogSaveToFile.createFileName(context).toString();
//                if (fileNameToday.equals(files.getName())) {
//                    Log.i(TAG, "文件夹中有今天的文件");
//                    var10000 = LemonGame.Log_instace;
//                    boolean flag = LemonGamePurchaseLogSaveToFile.saveToFile(context, msg, files);
//                    Log.i(TAG, "保存内容到文件：" + flag);
//                    if (flag) {
//                        var14 = TAG;
//                        var10001 = new StringBuilder("读取：");
//                        var10002 = LemonGame.Log_instace;
//                        Log.i(var14, var10001.append(LemonGamePurchaseLogSaveToFile.getInfoFromFile(context, files)).toString());
//                    }
//                }
//            }
//
//            for(i = 0; i < allFiles.length; ++i) {
//                Log.i(TAG, "file begin list");
//                files = allFiles[i];
//                var14 = files.getName();
//                LemonGamePurchaseLogSaveToFile var15 = LemonGame.Log_instace;
//                if (!var14.equals(LemonGamePurchaseLogSaveToFile.createFileName(context).toString())) {
//                    Log.i(TAG, "文件夹中没有今天的文件");
//                    var10003 = LemonGame.Log_instace;
//                    File file3 = new File("sdcard/lemon_docs/pay_log", LemonGamePurchaseLogSaveToFile.createFileName(context).toString());
//                    var10000 = LemonGame.Log_instace;
//                    boolean flag = LemonGamePurchaseLogSaveToFile.saveToFile(context, msg, file3);
//                    Log.i(TAG, "保存内容到文件：" + flag);
//                }
//            }
//        }
//
//    }
//
//    public static void testLogUseTime() {
//        long startTime = 0L;
//        long endTime = 0L;
//        startTime = System.currentTimeMillis();
//        int sum = 0;
//
//        for(int i = 0; i < 1000000; ++i) {
//            sum += i;
//        }
//
//        endTime = System.currentTimeMillis();
//        i("info", "花费时间" + (endTime - startTime));
//    }
//
//}

package com.intl.utils;

/**
 * @Author: yujingliang
 * @Date: 2019/11/21
 */
import java.io.PrintWriter;
import java.io.StringWriter;

public class IntlGameExceptionUtil {
    private static final String TAG = "IntlGameExceptionUtil";

    public static void handle(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String string = e.toString();
            IntlGameLogUtil.i(TAG, "Exception:" + string);
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }
}
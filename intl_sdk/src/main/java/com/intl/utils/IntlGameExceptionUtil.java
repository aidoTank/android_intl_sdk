package com.intl.utils;

/**
 * @Author: yujingliang
 * @Date: 2019/11/21
 */
import java.io.PrintWriter;
import java.io.StringWriter;

public class IntlGameExceptionUtil {
    private static final String TAG = "IntlGameExceptionUtil";
    private static boolean show = true;

    public IntlGameExceptionUtil() {
    }

    public static void handle(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String string = e.toString();
            IntlGameLogUtil.i(TAG, "Exception:" + string);
            show = true;
            if (true) {
                e.printStackTrace();
            }
        } catch (Exception var4) {
        }

    }
}
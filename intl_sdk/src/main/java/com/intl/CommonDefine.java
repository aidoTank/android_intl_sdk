package com.intl;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */

public class CommonDefine {

    public static final int YC_SDK_CODE_LEVEL_MASK = 0xF;

    public static final int YC_SDK_CODE_LEVEL_0 = 0x0;  //Low Level   Message
    public static final int YC_SDK_CODE_LEVEL_1 = 0x1;  // |
    public static final int YC_SDK_CODE_LEVEL_2 = 0x2;  // |
    public static final int YC_SDK_CODE_LEVEL_3 = 0x3;  // |
    public static final int YC_SDK_CODE_LEVEL_4 = 0x4;  // |
    public static final int YC_SDK_CODE_LEVEL_5 = 0x5;  // |
    public static final int YC_SDK_CODE_LEVEL_6 = 0x6;  // |
    public static final int YC_SDK_CODE_LEVEL_7 = 0x7;  // |
    public static final int YC_SDK_CODE_LEVEL_8 = 0x8;  // |          Warning
    public static final int YC_SDK_CODE_LEVEL_9 = 0x9;  // |
    public static final int YC_SDK_CODE_LEVEL_10 = 0xA;  // |
    public static final int YC_SDK_CODE_LEVEL_11 = 0xB;  // |
    public static final int YC_SDK_CODE_LEVEL_12 = 0xC;  // |
    public static final int YC_SDK_CODE_LEVEL_13 = 0xD;  // |
    public static final int YC_SDK_CODE_LEVEL_14 = 0xE;  // |
    public static final int YC_SDK_CODE_LEVEL_15 = 0xF; //Hight Level Error
    public static final int YC_SDK_MSG = YC_SDK_CODE_LEVEL_0;
    public static final int YC_SDK_WARNING = YC_SDK_CODE_LEVEL_8;
    public static final int YC_SDK_ERROR = YC_SDK_CODE_LEVEL_15;


    //===========================================================
    //Domain Mask
    public static final int YC_SDK_DOMAIN_MASK = 0xF0;
    // API
    public static final int YC_SDK_SDK_DOMAIN = 0xF0;
    // 通知功能
    public static final int YC_SDK_NOTIFICATION_DOMAIN = 0x20;
    // 应用内购
    public static final int YC_SDK_IAP_DOMAIN = 0x30;
    // Media Library
    public static final int YC_SDK_MEDIA_LIBRARY_DOMAIN = 0x40;
    // Share
    public static final int YC_SDK_SHARE_DOMAIN = 0x50;
    // UserCenter
    public static final int YC_SDK_USER_CENTER_DOMAIN = 0x60;
    // WebPage
    public static final int YC_SDK_WEB_VIEW_DOMAIN = 0x70;


    //===========================================================
    //Module Mask
    public static final int YC_SDK_MODULE_MASK = 0xFF00;
    //StoreKit(Only iOS)
    public static final int YC_SDK_SK_MODULE = 0x0100;
    //APNs(Only iOS)
    public static final int YC_SDK_APNS_MODULE = 0x0200;
    //Library(OS)
    public static final int YC_SDK_LIBR_MODULE = 0x0300;

    //Notification
    public static final int YC_SDK_NOTIFICATION_MODULE = 0x1100;
    //IAP (Only iOS)
    public static final int YC_SDK_IAP_MODULE = 0x1200;
    //Verify IAP (Only iOS)
    public static final int YC_SDK_IAP_VERIFY_MODULE = 0x1300;
    //Library
    public static final int YC_SDK_MEDIA_LIBR_MODULE = 0x1400;
    //UserCenter
    public static final int YC_SDK_USER_CENTER_MODULE = 0x1500;
    //UserCenterPresistent
    public static final int YC_SDK_USER_CENTER_PRESISTENT_MODULE = 0x1600;
    //General WEB Command
    public static final int YC_SDK_GENERAL_WEB_COMMAND_MODULE = 0x1700;
    //General User Center Command
    public static final int YC_SDK_USER_CENTER_WEB_COMMAND_MODULE = 0x1800;

    //REPORT_NOTIFICATION_ID_API
    public static final int YC_SDK_REPORT_NOTIFICATION_ID_API_MODULE = 0x3100;
    //YC_IAP_VERIFY_API
    public static final int YC_SDK_IAP_VERIFY_API_MODULE = 0x3200;
    //Auto LoginAPI
    public static final int YC_SDK_AUTO_LOGIN_API_MODULE = 0x3300;

//===========================================================

    public static final int YC_SDK_BUSY = YC_SDK_WARNING | (0x01 << 16);

    //HTTP ERROR
    public static final int YC_SDK_HTTP_RESPONSE_INCORRECT = YC_SDK_WARNING | (0x02 << 16);
    public static final int YC_SDK_HTTP_FAIL = YC_SDK_ERROR | (0x03 << 16);

    // JSON
    public static final int YC_SDK_JSON_INCORRECT = YC_SDK_ERROR | (0x04 << 16);

    //Encodeing
    public static final int YC_SDK_ENCODING_ERROR = YC_SDK_ERROR | (0x05 << 16);

    public static final int YC_SDK_INVALID_OPERATION = (0x0F << 16);
    public static final int YC_SDK_INVALID_ARGUMENT = (0x0E << 16);

    //===========================================================
    //User Center
    public static final int YC_USER_CENTER_AUTO_LOGIN_UID_NOT_MATCH = YC_SDK_USER_CENTER_DOMAIN | YC_SDK_USER_CENTER_DOMAIN | YC_SDK_ERROR | (0x12 << 16);
    
    //Web Page
    public static final int YC_WEB_PAGE_LOAD_FAILED = YC_SDK_WEB_VIEW_DOMAIN | YC_SDK_GENERAL_WEB_COMMAND_MODULE | YC_SDK_ERROR | (0x13 << 16);

}

package com.intl.entity;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */

public class IntlDefine {

//    public static final int INIT_SUCCESS = 0x40;
//    public static final int INIT_FAILED = 0x50;
//
//    public static final int LOGIN_SUCCESS = 0x70;
//    public static final int LOGIN_CANCEL = 0x80;
//    public static final int LOGIN_FAILED = 0x90;
//
//    public static final int LOGOUT_SUCCESS = 0;
//    public static final int LOGOUT_FAILED = 1;
//
//
//
//    public static final int BIND_SUCCESS = 0x100;
//    public static final int BIND_CANCEL = 0x110;
//    public static final int BIND_FAILED = 0x120;
//
//    public static final int SWITCH = 0x130;
    public static final int BIND_MSG = 0x100;
    public static final int LOGIN_MSG = 0x200;
    public static final int SWITCH_MSG = 0x300;

    public static final int SUCCESS = 0;
    public static final int CANCEL = -1;
    public static final int FAILED = 1;

    public static final int SWITCH_SUCCESS = 0x500;
    public static final int SWITCH_CANCEL = 0x600;
    public static final int SWITCH_FAILED = 0x700;

    public static final int HAVE_BIND=10010;
    public static final int BIND_SUCCESS = SUCCESS;
    public static final int BIND_CANCEL = CANCEL;
    public static final int BIND_FAILED = FAILED;

}

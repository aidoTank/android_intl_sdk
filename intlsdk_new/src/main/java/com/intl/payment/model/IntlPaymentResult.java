package com.intl.payment.model;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class IntlPaymentResult {
    String orderId;

    public IntlPaymentResult(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return this.orderId;
    }
}

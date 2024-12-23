package com.xixi.user.entity;

import lombok.Data;

/**
 * @author 永
 */
@Data
public class AliPay {
    //订单编号
    private String traceNo;
    //商品金额
    private double totalAmount;
    //商品名称
    private String subject;
    //支付宝流水号，退款时使用
    private String alipayTraceNo;
}
package com.xixi.user.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author 永
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {
    //支付宝AppId
    private String appId;
    //应用私钥
    private String appPrivateKey;
    //支付宝公钥
    private String alipayPublicKey;
    //支付宝通知本地接口地址
    private String notifyUrl;
}


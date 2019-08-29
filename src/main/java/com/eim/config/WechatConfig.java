package com.eim.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@PropertySource("classpath:config.properties")
public class WechatConfig {
    // 小程序唯一标识 (在微信小程序管理后台获取)
    @Value("${wechat.wxspAppid}")
    public String wxspAppid;

    // 小程序的 app secret (在微信小程序管理后台获取)
    @Value("${wechat.wxspSecret}")
    public String wxspSecret;

    // 授权类型（必填）
    @Value("${wechat.grantType}")
    public String grantType;

    @Value("${wechat.code2Session}")
    public String code2Session;
}

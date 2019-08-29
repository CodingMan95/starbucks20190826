package com.eim.service;

import com.eim.entity.DecodeInfo;

public interface WechatService {
    /**
     * 获取微信用户信息
     */
    String decodeInfo(DecodeInfo info);
}

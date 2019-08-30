package com.eim.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eim.config.WechatConfig;
import com.eim.entity.DecodeInfo;
import com.eim.entity.WechatUser;
import com.eim.mapper.WechatMapper;
import com.eim.service.WechatService;
import com.eim.util.AesCbcUtil;
import com.eim.util.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
@Transactional
public class WechatServiceImpl implements WechatService {
    @Autowired
    private WechatMapper wechatMapper;
    @Autowired
    private WechatConfig wxConfig;

    @Override
    public String decodeInfo(DecodeInfo info) {
        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        //////////////// ////////////////
        // 请求参数
        String params = "appid=" + wxConfig.wxspAppid + "&secret=" + wxConfig.wxspSecret + "&js_code=" + info.getCode() + "&grant_type="
                + wxConfig.grantType;
        // 发送请求
        String sr = HttpRequest.sendGet(wxConfig.code2Session, params);
        // 解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(sr);

        if (null != json) {
            // 获取会话密钥（session_key）
            //String session_key = json.get("session_key").toString();
            String openId = json.get("openid").toString();

            int userCount = wechatMapper.selectCount(new QueryWrapper<WechatUser>().eq("open_id", openId));
            if (userCount == 0) {
                WechatUser user = new WechatUser();
                user.setOpenId(openId);
                user.setNickName(info.getNickName());
                user.setGender(info.getGender());
                user.setAvatarUrl(info.getAvatarUrl());
                user.setInTime(new Date());
                wechatMapper.insert(user);
            }

            return openId;

            //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
            /*try {
                String result = AesCbcUtil.decrypt(info.getEncryptedData(), session_key, info.getIv(), "UTF-8");
                if (null != result && result.length() > 0) {

                    JSONObject userInfoJSON = JSONObject.parseObject(result);



                    int userCount = wechatMapper.selectCount(new QueryWrapper<WechatUser>().eq("open_id", openId));
                    if (userCount == 0) {
                        WechatUser user = new WechatUser();
                        user.setOpenId(openId);
                        user.setNickName(userInfoJSON.get("nickName").toString());
                        user.setGender(Integer.valueOf(userInfoJSON.get("gender").toString()));
                        user.setAvatarUrl(userInfoJSON.get("avatarUrl").toString());
                        user.setInTime(new Date());
                        wechatMapper.insert(user);
                    }
                    return openId;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
        return null;
    }
}

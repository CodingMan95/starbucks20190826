package com.eim.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("wechat_info")
public class WechatUser {

    private String openId;
    private String nickName;
    private int gender;
    private String avatarUrl;
    private Date inTime;

}

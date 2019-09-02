package com.eim.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityOrder {
    private int orderId;

    @ApiModelProperty("活动id")
    private String activityId;

    //微信相关信息
    private String openId;
    @ApiModelProperty(hidden = true)
    private String nickName;
    @ApiModelProperty(hidden = true)
    private String avatarUrl;

    //活动相关信息
    @ApiModelProperty(hidden = true)
    private String coverUrl;
    @ApiModelProperty(hidden = true)
    private String bannerUrl;
    @ApiModelProperty(hidden = true)
    private String title;
    @ApiModelProperty(hidden = true)
    private String introduce;

    @ApiModelProperty(value = "参与时间")
    private String activityTime;

    @ApiModelProperty(value = "预约的门店")
    private String storeName;

    @ApiModelProperty(value = "预约的门店id")
    private int storeId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话")
    private String mobile;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "套餐id")
    private int comboId;
    private String comboName;
    private String comboPic;

    @ApiModelProperty(value = "预约人数")
    private int peopleNum;

    @ApiModelProperty(value = "共计花费")
    private double totalCost;

    @ApiModelProperty(value = "报名时间", hidden = true)
    private Date addTime;

    @ApiModelProperty(value = "预约状态 为1时预约成功，为2时店员签到成功，为0时活动结束", hidden = true)
    private int status;
}

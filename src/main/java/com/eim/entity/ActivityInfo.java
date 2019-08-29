package com.eim.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "ActivityInfo")
public class ActivityInfo {
    @ApiModelProperty(value = "活动id(唯一)")
    private int activeId;
    @ApiModelProperty(value = "活动列表缩略图")
    private String coverUrl;
    @ApiModelProperty(value = "活动内容头图")
    private String bannerUrl;
    @ApiModelProperty(value = "活动名称")
    private String title;
    @ApiModelProperty(value = "活动说明")
    private String introduce;
    @ApiModelProperty(value = "参与活动的店铺id集合(用,隔开)")
    private String storeId;
    @ApiModelProperty(value = "活动可选时间")
    private String activeTime;
    @ApiModelProperty(value = "报名填写信息(用,隔开)")
    private String applyInfo;
    @ApiModelProperty(value = "套餐id集合(用,隔开)")
    private String comboId;
    @ApiModelProperty(value = "套餐列表", hidden = true)
    private List<Combo> comboList;
    @ApiModelProperty(value = "套餐价格")
    private double comboPrice;
    @ApiModelProperty(value = "报名填写信息(用,隔开)")
    private Date createTime;
    @ApiModelProperty(value = "活动状态：开启为true/关闭为false")
    private boolean status;
    @ApiModelProperty(value = "涉及活动的城市集合(用,隔开)")
    private String city;
}

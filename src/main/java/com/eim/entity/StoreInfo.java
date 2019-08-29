package com.eim.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("shop_info")
public class StoreInfo {
    @ApiModelProperty("门店id(添加时不传，更新时传)")
    private int id;

    @ApiModelProperty(value = "门店id(如：S00001)")
    private String storeId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店地址")
    private String address;

    @ApiModelProperty(value = "所在省")
    private String province;

    @ApiModelProperty(value = "所在市")
    private String city;

    @ApiModelProperty(value = "所在区")
    private String area;
}

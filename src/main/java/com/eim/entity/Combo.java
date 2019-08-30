package com.eim.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("activity_combo")
public class Combo {
    @ApiModelProperty(hidden = true)
    private int id;
    private String name;
    private String pic;

    @ApiModelProperty(hidden = true)
    private Date createTime;
}

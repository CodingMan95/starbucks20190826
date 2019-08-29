package com.eim.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("activity_combo")
public class Combo {
    private Integer id;
    private String name;
    private String pic;
    private Date createTime;
}

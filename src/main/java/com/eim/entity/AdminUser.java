package com.eim.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("sys_user")
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {
    private String storeId;
    private String password;
    private int roleId;
    private boolean status;
    private Date createTime;
    private Date updateTime;

    public AdminUser(String storeId, String password, int roleId, boolean status, Date createTime) {
        this.storeId = storeId;
        this.password = password;
        this.roleId = roleId;
        this.status = status;
        this.createTime = createTime;
    }
}

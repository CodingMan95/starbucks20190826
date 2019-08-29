package com.eim.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eim.entity.AdminUser;

import java.util.Map;

public interface AdminUserService extends IService<AdminUser> {
    /**
     * 用户登录
     */
    Map<String, Object> userLogin(String storeId, String password);

    /**
     * 修改密码
     */
    boolean changePwd(String storeId, String oldPassword, String newPassword);
}

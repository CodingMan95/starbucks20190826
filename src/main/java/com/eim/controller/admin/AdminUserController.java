package com.eim.controller.admin;

import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.model.ResultTemplate;
import com.eim.service.AdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "后台-用户管理")
@RestController
@RequestMapping("/admin-user/")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    @ApiOperation("修改登录密码")
    @PostMapping("changePwd.do")
    public ResultTemplate changePwd(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String storeId) {
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty("storeId")) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        boolean changePwd;
        try {
            changePwd = adminUserService.changePwd(storeId, oldPassword, newPassword);
        } catch (BusinessException e) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.CHANGE_ERROR);
        }

        if (changePwd) {
            return ResultTemplate.success();
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.CHANGE_ERROR);
    }

}

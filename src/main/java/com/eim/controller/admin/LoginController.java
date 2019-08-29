package com.eim.controller.admin;

import com.eim.exception.BusinessException;
import com.eim.model.ResultTemplate;
import com.eim.service.AdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "后台-用户登录管理")
@RestController
public class LoginController {
    @Autowired
    private AdminUserService adminUserService;

    @ApiOperation("用户登录")
    @PostMapping("login.do")
    public ResultTemplate login(@RequestParam String storeId, @RequestParam String password) {
        Map<String, Object> map;
        try {
            map = adminUserService.userLogin(storeId, password);
        } catch (BusinessException e) {
            throw new BusinessException(e.getMessage());
        }
        return ResultTemplate.success(map);
    }
}

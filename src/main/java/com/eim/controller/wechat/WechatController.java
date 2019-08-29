package com.eim.controller.wechat;

import com.eim.entity.DecodeInfo;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.model.ResultTemplate;
import com.eim.service.WechatService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api("微信小程序授权管理")
@RestController
public class WechatController {
    @Autowired
    private WechatService wechatService;

    @ApiOperation("解密用户敏感数据，获取用户信息")
    @PostMapping("/decodeUserInfo.do")
    public ResultTemplate decodeUserInfo(@RequestBody DecodeInfo info) {

        // 登录凭证不能为空
        if (StringUtils.isEmpty(info.getCode()) || StringUtils.isEmpty(info.getNickName())) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        String userInfo = wechatService.decodeInfo(info);
        if (null == userInfo) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.ERROR_CODE);
        }
        return ResultTemplate.success(userInfo);
    }

}

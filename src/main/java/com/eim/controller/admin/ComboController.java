package com.eim.controller.admin;

import com.eim.entity.Combo;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.model.ResultTemplate;
import com.eim.service.ComboService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(tags = "后台-套餐管理")
@RestController
@RequestMapping("/combo/")
public class ComboController {
    @Autowired
    private ComboService comboService;

    @ApiOperation("添加套餐")
    @PostMapping("add.do")
    public ResultTemplate addCombo(@RequestBody Combo combo) {
        if (StringUtils.isEmpty(combo.getName()) || StringUtils.isEmpty(combo.getPic())) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        int comboId = comboService.add(combo);
        if (comboId != 0) {
            return ResultTemplate.success(comboId);
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
    }
}

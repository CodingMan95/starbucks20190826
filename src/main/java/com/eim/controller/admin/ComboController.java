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
    public ResultTemplate addCombo(@RequestParam String name, @RequestParam String pic, @RequestParam int comboId) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(pic)) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        int id = comboService.add(name, pic, comboId);
        if (id != 0) {
            return ResultTemplate.success(id);
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
    }

    @ApiOperation("删除套餐")
    @GetMapping("delete.do")
    public ResultTemplate delete(@RequestParam Integer comboId) {
        if (null == comboId || comboId <= 0) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }

        try {
            boolean delete = comboService.delete(comboId);
            if (delete) {
                return ResultTemplate.success();
            }
            return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
        } catch (BusinessException e) {
            e.printStackTrace();
            return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.COMBO_ORDERED);
        }

    }

}

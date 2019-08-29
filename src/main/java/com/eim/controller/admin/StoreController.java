package com.eim.controller.admin;

import com.eim.entity.StoreInfo;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.model.ResultTemplate;
import com.eim.service.StoreInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "后台-门店管理")
@RestController
@RequestMapping("/store-admin/")
public class StoreController {
    @Autowired
    private StoreInfoService storeInfoService;

    @ApiOperation("新增、修改门店信息")
    @PostMapping("add.do")
    public ResultTemplate add(@RequestBody StoreInfo storeInfo) {
        if (StringUtils.isEmpty(storeInfo.getStoreId()) || StringUtils.isEmpty(storeInfo.getStoreName()) || StringUtils.isEmpty(storeInfo.getAddress())) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        boolean addStore = storeInfoService.addStore(storeInfo);
        if (addStore) {
            return ResultTemplate.success();
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
    }

    @ApiOperation("删除门店信息")
    @PostMapping("delete.do")
    public ResultTemplate delete(@RequestParam int id) {
        if (0 == id) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        boolean delete = storeInfoService.deleteStore(id);
        if (delete) {
            return ResultTemplate.success();
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
    }

    @ApiOperation("分页查询门店信息")
    @GetMapping("all.do")
    public ResultTemplate all(@RequestParam int page) {
        if (0 == page) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        Map<String, Object> map = storeInfoService.getByPage(page);
        return ResultTemplate.success(map);
    }

}

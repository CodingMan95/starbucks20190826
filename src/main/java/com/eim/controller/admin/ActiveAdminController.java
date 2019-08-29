package com.eim.controller.admin;

import com.eim.entity.ActivityInfo;
import com.eim.entity.ActivityOrder;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.model.ResultTemplate;
import com.eim.service.ActivityInfoService;
import com.eim.service.ActivityOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "后台-活动管理")
@RestController
@RequestMapping("/active-admin/")
public class ActiveAdminController {
    @Autowired
    private ActivityInfoService activityInfoService;
    @Autowired
    private ActivityOrderService activityOrderService;

    @ApiOperation("创建活动、修改活动详情")
    @PostMapping("add.do")
    public ResultTemplate add(@RequestBody ActivityInfo info) {
        if (null == info.getCoverUrl() || null == info.getBannerUrl() || null == info.getTitle() || null == info.getIntroduce()) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        boolean add = activityInfoService.addActivityInfo(info);
        if (add) {
            return ResultTemplate.success();
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
    }

    @ApiOperation("获取所有已创建活动")
    @GetMapping("list.do")
    public ResultTemplate list() {
        List<Map> list = activityInfoService.allActivity();

        return ResultTemplate.success(list);
    }

    @ApiOperation("修改活动状态")
    @PostMapping("updateStatus.do")
    public ResultTemplate updateStatus(@RequestParam Integer activeId, @RequestParam Boolean status) {
        if (activeId == 0 || null == status) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        boolean updateStatus = activityInfoService.updateStatus(activeId, status);
        if (updateStatus) {
            return ResultTemplate.success();
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
    }

    @ApiOperation("删除活动")
    @PostMapping("delete.do")
    public ResultTemplate delete(@RequestParam Integer activeId) {
        if (activeId == 0) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        boolean updateStatus = activityInfoService.delete(activeId);
        if (updateStatus) {
            return ResultTemplate.success();
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
    }

    @ApiOperation("查看活动数据")
    @GetMapping("getData.do")
    public ResultTemplate getData(@RequestParam(required = false) String province, @RequestParam(required = false) String city, @RequestParam(required = false) String area, @RequestParam(required = false) String storeName, @RequestParam Integer activeId) {
        List<ActivityOrder> activityOrders = activityOrderService.selectData(province, city, area, storeName, activeId);
        return ResultTemplate.success(activityOrders);
    }

    @ApiOperation("查看活动详情")
    @GetMapping("getDetail.do")
    public ResultTemplate getDetail(@RequestParam Integer activeId) {
        return ResultTemplate.success();
    }

}

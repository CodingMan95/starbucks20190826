package com.eim.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import springfox.documentation.annotations.ApiIgnore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags = "后台-活动管理")
@RestController
@RequestMapping("/active-admin/")
public class ActiveAdminController {
    @Autowired
    private ActivityInfoService activityInfoService;
    @Autowired
    private ActivityOrderService activityOrderService;

    @ApiOperation("超级管理员-创建活动、修改活动详情")
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

    @ApiOperation("超级管理员-获取所有已创建活动")
    @GetMapping("list.do")
    public ResultTemplate list() {
        List<Map> list = activityInfoService.allActivity();

        return ResultTemplate.success(list);
    }

    @ApiOperation("超级管理员-修改活动状态")
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

    @ApiOperation("超级管理员-删除活动")
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

    @ApiOperation("超级管理员-查看活动数据")
    @GetMapping("getData.do")
    public ResultTemplate getData(@RequestParam(required = false) String province, @RequestParam(required = false) String city, @RequestParam(required = false) String area, @RequestParam(required = false) String storeName, @RequestParam Integer activeId) {
        List<ActivityOrder> activityOrders = activityOrderService.selectData(province, city, area, storeName, activeId);
        return ResultTemplate.success(activityOrders);
    }

    @ApiIgnore
    @ApiOperation("超级管理员-查看活动详情")
    @GetMapping("getDetail.do")
    public ResultTemplate getDetail(@RequestParam Integer activeId) {
        return ResultTemplate.success();
    }

    @ApiIgnore
    @ApiOperation("测试接口")
    @GetMapping("test.do")
    public ResultTemplate test() throws ParseException {
        long time = new Date().getTime();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        List<ActivityInfo> list = activityInfoService.list(new QueryWrapper<ActivityInfo>().select("active_id", "active_time"));
        for (ActivityInfo activityInfo : list) {
            //取出时间把列表中的最大时间
            String[] split = activityInfo.getActiveTime().split(",");
            String de_time = split[split.length - 1];

            //计算活动结束时间离现在时间差
            Date d2 = df1.parse(de_time);
            Calendar c = Calendar.getInstance();
            c.setTime(d2);
            c.add(Calendar.DAY_OF_MONTH, 1);

            long diff1 = c.getTime().getTime() - time;
            if (diff1 <= 0) {
                activityInfoService.updateStatus(activityInfo.getActiveId(), false);
            }

        }

        return ResultTemplate.success();
    }

    @ApiOperation("店铺管理员-查看本店数据")
    @GetMapping("getDataOfStore.do")
    public ResultTemplate getDataOfStore(@RequestParam Integer activeId, @RequestParam String storeId) {
        if (activeId == 0 || null == storeId) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        List<ActivityOrder> activityOrders = activityOrderService.selectDataOfStore(activeId, storeId);
        return ResultTemplate.success(activityOrders);
    }

    @ApiOperation("店铺管理员-查看本店参与的活动")
    @GetMapping("getActiveOfStore.do")
    public ResultTemplate getActiveOfStore(@RequestParam String storeId) {

        if (null == storeId) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        List<ActivityInfo> activeOfStore = activityInfoService.getActiveOfStore(storeId);
        return ResultTemplate.success(activeOfStore);
    }

}

package com.eim.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eim.entity.ActivityInfo;
import com.eim.entity.ActivityOrder;
import com.eim.entity.StoreInfo;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.model.ResultTemplate;
import com.eim.service.ActivityInfoService;
import com.eim.service.ActivityOrderService;
import com.eim.util.HttpRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
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
    public ResultTemplate add(@RequestParam String info) {
        JSONObject jsStr = JSONObject.parseObject(info);
        /**
         * json对象转换成java对象
         */
        ActivityInfo store = JSONObject.toJavaObject(jsStr, ActivityInfo.class);
        if (null == store.getCoverUrl() || null == store.getBannerUrl() || null == store.getTitle() || null == store.getIntroduce()) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        boolean add = activityInfoService.addActivityInfo(store);
        if (add) {
            return ResultTemplate.success();
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
    }

    @ApiOperation("超级管理员-获取所有已创建活动")
    @GetMapping("list.do")
    public ResultTemplate list(@RequestParam Integer page) {
        Map<String, Object> stringObjectMap = activityInfoService.allActivity(page);

        return ResultTemplate.success(stringObjectMap);
    }

    @ApiOperation("超级管理员-修改活动状态")
    @PostMapping("updateStatus.do")
    public ResultTemplate updateStatus(@RequestParam Integer activeId, @RequestParam Boolean status) {
        if (activeId == 0 || null == status) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        String updateStatus = activityInfoService.updateStatus(activeId, status);
        if (updateStatus.equals(ConstantKit.SUCCESS)) {
            return ResultTemplate.success();
        } else if (updateStatus.equals(ConstantKit.ACTIVITY_OVER)) {
            return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.ACTIVITY_OVER);
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
    public ResultTemplate getData(@RequestParam(required = false) String province, @RequestParam(required = false) String city, @RequestParam(required = false) String area, @RequestParam(required = false) String storeName, @RequestParam Integer activeId, @RequestParam int page, @RequestParam int limit) {
        Map<String, Object> activityOrders = activityOrderService.selectData(province, city, area, storeName, activeId, page, limit);
        return ResultTemplate.success(activityOrders);
    }

    @ApiOperation("超级管理员-查看活动详情")
    @GetMapping("getDetail.do")
    public ResultTemplate getDetail(@RequestParam Integer activeId) {
        if (activeId == 0 || null == activeId) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        ActivityInfo info = activityInfoService.ActivityDetail(activeId);
        return ResultTemplate.success(info);
    }

    @ApiOperation("店铺管理员-查看本店数据")
    @GetMapping("getDataOfStore.do")
    public ResultTemplate getDataOfStore(@RequestParam Integer activeId, @RequestParam String storeId, @RequestParam int page, @RequestParam int limit) {
        if (activeId == 0 || null == storeId) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        Map<String, Object> activityOrders = activityOrderService.selectDataOfStore(activeId, storeId, page, limit);
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

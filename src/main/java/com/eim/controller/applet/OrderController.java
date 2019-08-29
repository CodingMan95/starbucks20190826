package com.eim.controller.applet;

import com.eim.entity.ActivityOrder;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.model.ResultTemplate;
import com.eim.service.ActivityOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "小程序-预约管理")
@RestController
@RequestMapping("/order/")
public class OrderController {
    @Autowired
    private ActivityOrderService orderService;

    @ApiOperation("报名预约")
    @PostMapping("add.do")
    public ResultTemplate addOrder(@RequestBody ActivityOrder order) {
        boolean add;
        try {
            add = orderService.addActivity(order);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(ConstantKit.BAD_REQUEST, e.getMessage());
        }

        if (add) {
            return ResultTemplate.success();
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
    }

    @ApiOperation("我的预约列表")
    @GetMapping("list.do")
    public ResultTemplate list(@RequestParam String openId) {
        if (StringUtils.isEmpty(openId)) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        List<ActivityOrder> list = orderService.list(openId);
        if (null == list) {
            return ResultTemplate.success("[]");
        }
        return ResultTemplate.success(list);
    }

    @ApiOperation("预约详情")
    @GetMapping("detail.do")
    public ResultTemplate detail(@RequestParam Integer orderId) {
        if (orderId == 0 || null == orderId) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        ActivityOrder order = orderService.orderDetail(orderId);
        if (null == order) {
            return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.USERINFO_FAIL);
        }
        return ResultTemplate.success(order);
    }

    @ApiOperation("取消预约")
    @PostMapping("cancel.do")
    public ResultTemplate cancelOrder(@RequestParam Integer orderId) {
        if (orderId == 0) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }

        boolean add = orderService.cancelOrder(orderId);
        if (add) {
            return ResultTemplate.success();
        }
        return ResultTemplate.error(ConstantKit.BAD_REQUEST, ConstantKit.FAIL);
    }
}

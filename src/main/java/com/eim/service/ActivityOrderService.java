package com.eim.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eim.entity.ActivityOrder;

import java.util.List;

public interface ActivityOrderService extends IService<ActivityOrder> {
    /**
     * 用户预约活动
     */
    boolean addActivity(ActivityOrder order);

    /**
     * 获取用户的预约列表
     */
    List<ActivityOrder> list(String openId);

    boolean cancelOrder(int orderId);

    /**
     * 获取预约详情
     */
    ActivityOrder orderDetail(int orderId);

    List<ActivityOrder> selectData(String province, String city, String area, String storeName, Integer activeId);
}

package com.eim.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eim.entity.ActivityInfo;
import com.eim.entity.ActivityOrder;

import java.util.List;
import java.util.Map;

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

    Map<String, Object> selectData(String province, String city, String area, String storeName, Integer activeId, int start, int num);

    /**
     * 查看店铺的数据
     */
    Map<String, Object> selectDataOfStore(Integer activeId, String storeId, int page, int num);

    /**
     * 店员确认签到
     */
    boolean sign(int orderId);

}

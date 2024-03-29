package com.eim.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eim.entity.ActivityInfo;

import java.util.List;
import java.util.Map;

public interface ActivityInfoService extends IService<ActivityInfo> {
    /**
     * 根据城市获取相关活动
     */
    List<ActivityInfo> getActivityByCity(String city);

    /**
     * 根据活动id获取活动详情
     */
    ActivityInfo getActivityDetail(int activeId);

    /**
     * 创建活动
     */
    boolean addActivityInfo(ActivityInfo activityInfo);

    /**
     * 分页查看所有活动
     */
    Map<String, Object> allActivity(int page);

    /**
     * 更新活动状态
     */
    String updateStatus(int activeId, boolean status);

    /**
     * 删除活动
     */
    boolean delete(int activeId);

    /**
     * 获取某一店铺参与的活动列表
     */
    List<ActivityInfo> getActiveOfStore(String storeId);

    ActivityInfo ActivityDetail(int activeId);

}

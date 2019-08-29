package com.eim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eim.entity.ActivityInfo;
import com.eim.entity.ActivityOrder;
import com.eim.entity.Combo;
import com.eim.entity.StoreInfo;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.mapper.ActivityOrderMapper;
import com.eim.service.ActivityInfoService;
import com.eim.service.ActivityOrderService;
import com.eim.service.ComboService;
import com.eim.service.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ActivityOrderServiceImpl extends ServiceImpl<ActivityOrderMapper, ActivityOrder> implements ActivityOrderService {
    @Autowired
    private ActivityOrderMapper activityOrderMapper;
    @Autowired
    private ActivityInfoService activityInfoService;
    @Autowired
    private ComboService comboService;

    @Override
    public boolean addActivity(ActivityOrder order) {
        if (null == order.getActivityTime() || null == order.getStoreName() || null == order.getOpenId()) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        order.setStatus(ConstantKit.ORDER_SUCCESS);
        order.setAddTime(new Date());
        activityOrderMapper.insert(order);
        return true;
    }

    @Override
    public List<ActivityOrder> list(String openId) {
        List<ActivityOrder> activityOrders = activityOrderMapper.selectList(new QueryWrapper<ActivityOrder>().select("order_id", "activity_id", "activity_time", "store_name", "status").eq("open_id", openId));
        if (null == activityOrders) {
            return null;
        }
        for (ActivityOrder order : activityOrders) {
            ActivityInfo activityInfo = activityInfoService.getOne(new QueryWrapper<ActivityInfo>()
                    .select("cover_url", "title").eq("active_id", order.getActivityId()));

            order.setCoverUrl(activityInfo.getCoverUrl());
            order.setTitle(activityInfo.getTitle());

        }
        return activityOrders;
    }

    @Override
    public boolean cancelOrder(int orderId) {
        int delete = activityOrderMapper.delete(new QueryWrapper<ActivityOrder>().eq("order_id", orderId));
        if (delete == 1) {
            return true;
        }
        return false;
    }

    @Override
    public ActivityOrder orderDetail(int orderId) {
        ActivityOrder activityOrder = activityOrderMapper.selectOne(new QueryWrapper<ActivityOrder>()
                .select("order_id", "activity_id", "activity_time", "store_name", "combo_id", "people_num", "total_cost").eq("order_id", orderId));

        ActivityInfo activityInfo = activityInfoService.getOne(new QueryWrapper<ActivityInfo>()
                .select("banner_url", "title", "introduce").eq("active_id", activityOrder.getActivityId()));
        activityOrder.setBannerUrl(activityInfo.getBannerUrl());
        activityOrder.setTitle(activityInfo.getTitle());
        activityOrder.setIntroduce(activityInfo.getIntroduce());

        Combo combo = comboService.getOne(new QueryWrapper<Combo>().select("pic", "name").eq("id", activityOrder.getComboId()));
        activityOrder.setComboName(combo.getName());
        activityOrder.setComboPic(combo.getPic());

        return activityOrder;
    }

    @Override
    public List<ActivityOrder> selectData(String province, String city, String area, String storeName, Integer activeId) {
        List<ActivityOrder> orders = activityOrderMapper.selectData(province, city, area, storeName, activeId);
        return orders;
    }
}

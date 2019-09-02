package com.eim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ActivityOrderServiceImpl extends ServiceImpl<ActivityOrderMapper, ActivityOrder> implements ActivityOrderService {
    @Autowired
    private ActivityOrderMapper activityOrderMapper;
    @Autowired
    private ActivityInfoService activityInfoService;
    @Autowired
    private ComboService comboService;
    @Autowired
    private StoreInfoService storeInfoService;

    @Override
    public boolean addActivity(ActivityOrder order) {
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
                .select("order_id", "activity_id", "activity_time", "store_name", "combo_id", "people_num", "total_cost", "status").eq("order_id", orderId));

        if (null == activityOrder) {
            return null;
        }

        ActivityInfo activityInfo = activityInfoService.getOne(new QueryWrapper<ActivityInfo>()
                .select("banner_url", "title", "introduce").eq("active_id", activityOrder.getActivityId()));
        activityOrder.setBannerUrl(activityInfo.getBannerUrl());
        activityOrder.setTitle(activityInfo.getTitle());
        activityOrder.setIntroduce(activityInfo.getIntroduce());

        if (activityOrder.getComboId() != 0) {
            Combo combo = comboService.getOne(new QueryWrapper<Combo>().select("pic", "name").eq("id", activityOrder.getComboId()));
            activityOrder.setComboName(combo.getName());
            activityOrder.setComboPic(combo.getPic());
        }
        return activityOrder;
    }

    @Override
    public Map<String, Object> selectData(String province, String city, String area, String storeName, Integer activeId, int page, int num) {
        Map<String, Object> map = new HashMap<>();
        int start;
        if (page == 1) {
            start = 0;
        } else {
            start = (page - 1) * num;
        }

        List<ActivityOrder> orders = activityOrderMapper.selectData(province, city, area, storeName, activeId, start, num);
        int count = activityOrderMapper.selectTotal(province, city, area, storeName, activeId);
        int signNum = activityOrderMapper.selectSignNum(province, city, area, storeName, activeId);
        map.put("orderList", orders);
        map.put("total", count);
        map.put("signNum", signNum);
        return map;
    }

    @Override
    public Map<String, Object> selectDataOfStore(Integer activeId, String storeId, int page, int num) {
        Map<String, Object> map = new HashMap<>();

        int start;
        if (page == 1) {
            start = 0;
        } else {
            start = (page - 1) * num;
        }

        StoreInfo storeInfo = storeInfoService.getOne(new QueryWrapper<StoreInfo>().select("id").eq("store_id", storeId));

        List<ActivityOrder> orders = activityOrderMapper.selectDataOfStore(activeId, storeInfo.getId(), start, num);
        map.put("orderList", orders);

        StoreInfo one = storeInfoService.getOne(new QueryWrapper<StoreInfo>().eq("store_id", storeId).select("id"));
        int count = activityOrderMapper.selectCount(new QueryWrapper<ActivityOrder>().eq("activity_id", activeId).eq("store_id", one.getId()));
        map.put("total", count);
        return map;
    }

    @Override
    public boolean sign(int orderId) {
        boolean update = update(new UpdateWrapper<ActivityOrder>().eq("order_id", orderId).set("status", 2));
        return update;
    }
}

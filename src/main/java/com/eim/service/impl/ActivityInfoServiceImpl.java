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
import com.eim.mapper.ActivityInfoMapper;
import com.eim.mapper.ActivityOrderMapper;
import com.eim.service.ActivityInfoService;
import com.eim.service.ComboService;
import com.eim.service.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {
    @Autowired
    private ActivityInfoMapper activityInfoMapper;
    @Autowired
    private ComboService comboService;
    @Autowired
    private ActivityOrderMapper orderMapper;
    @Autowired
    private StoreInfoService storeInfoService;

    @Override
    public List<ActivityInfo> getActivityByCity(String city) {
        if (StringUtils.isEmpty(city)) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_PARAMETER);
        }
        List<ActivityInfo> activityByCity = activityInfoMapper.getActivityByCity(city);
        return activityByCity;
    }

    @Override
    public ActivityInfo getActivityDetail(int activeId) {
        ActivityInfo info = activityInfoMapper.selectOne(new QueryWrapper<ActivityInfo>()
                .eq("active_id", activeId)
                .select("active_id", "banner_url", "title", "introduce", "active_time", "apply_info", "combo_id", "combo_price"));

        String[] ids = info.getComboId().split(",");
        int array[] = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            array[i] = Integer.parseInt(ids[i]);
        }
        List<Combo> combos = comboService.selectByIdSet(array);
        info.setComboList(combos);

        return info;
    }

    @Override
    public boolean addActivityInfo(ActivityInfo activityInfo) {
        activityInfo.setStatus(true);
        activityInfo.setCreateTime(new Date());
        //创建活动
        if (activityInfo.getActiveId() == 0) {
            activityInfoMapper.insert(activityInfo);
        } else {
            //更新活动
            activityInfoMapper.update(activityInfo, new UpdateWrapper<ActivityInfo>().eq("active_id", activityInfo.getActiveId()));
        }
        return true;
    }

    @Override
    public Map<String, Object> allActivity(int page) {
        List list = new ArrayList<>();
        Map<String, Object> allMap = new HashMap<>();

        /*List<ActivityInfo> activityInfoList = activityInfoMapper.selectList(new QueryWrapper<ActivityInfo>()
                .select("active_id", "title", "store_id", "status"));*/
        int start;
        if (page == 1) {
            start = 0;
        } else {
            start = (page - 1) * ConstantKit.PAGE_LIMIT;
        }
        List<ActivityInfo> activityInfoList = activityInfoMapper.getActivityByPage(start, ConstantKit.PAGE_LIMIT);
        if (null != activityInfoList) {
            for (ActivityInfo info : activityInfoList) {
                Map<String, Object> map = new HashMap<>();
                //活动名称
                map.put("title", info.getTitle());
                //活动状态
                map.put("status", info.isStatus());
                //参与活动的门店数
                int length = info.getStoreId().split(",").length;
                map.put("storeNum", length);
                //参与活动总人数
                //int peopleNum = orderMapper.selectCount(new QueryWrapper<ActivityOrder>().eq("activity_id", info.getActiveId()));
                int peopleNum = orderMapper.getNumOfActive(info.getActiveId());
                map.put("peopleNum", peopleNum);
                //活动id
                map.put("activeId", info.getActiveId());
                list.add(map);
            }
        }
        Integer count = activityInfoMapper.selectCount(new QueryWrapper<>());
        allMap.put("activity", list);
        allMap.put("activeNum", count);
        return allMap;
    }

    @Override
    public String updateStatus(int activeId, boolean status) {
        ActivityInfo activityInfo = activityInfoMapper.selectOne(new QueryWrapper<ActivityInfo>().select("active_time").eq("active_id", activeId));
        //取出时间列表中的最大时间

        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String[] split = activityInfo.getActiveTime().split(",");
        String de_time = split[split.length - 1];

        //计算活动结束时间离现在时间差
        Date d2 = null;
        try {
            d2 = df1.parse(de_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d2);
        c.add(Calendar.DAY_OF_MONTH, 1);

        long diff1 = c.getTime().getTime() - (new Date().getTime());
        if (diff1 >= 0) {
            boolean update = update(new UpdateWrapper<ActivityInfo>().set("status", status).eq("active_id", activeId));
            if (update) {
                return ConstantKit.SUCCESS;
            }
            return ConstantKit.FAIL;
        }
        return ConstantKit.ACTIVITY_OVER;
    }

    @Override
    public boolean delete(int activeId) {
        int remove = activityInfoMapper.delete(new QueryWrapper<ActivityInfo>().eq("active_id", activeId));
        if (remove == 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<ActivityInfo> getActiveOfStore(String storeId) {
        StoreInfo storeInfo = storeInfoService.getOne(new QueryWrapper<StoreInfo>().select("id").eq("store_id", storeId));

        List<ActivityInfo> infoList = activityInfoMapper.selectList(new QueryWrapper<ActivityInfo>().select("store_id", "active_id", "title", "active_time", "status", "cover_url"));
        List<ActivityInfo> myActivity = new ArrayList<>();
        for (ActivityInfo info : infoList) {
            String[] ids = info.getStoreId().split(",");

            int array[] = new int[ids.length];
            for (int i = 0; i < ids.length; i++) {
                array[i] = Integer.parseInt(ids[i]);
            }

            for (int id : array) {
                if (id == storeInfo.getId()) {
                    myActivity.add(info);
                }
            }

        }
        return myActivity;
    }

    @Override
    public ActivityInfo ActivityDetail(int activeId) {
        ActivityInfo info = activityInfoMapper.selectOne(new QueryWrapper<ActivityInfo>()
                .eq("active_id", activeId)
                .select("active_id", "banner_url", "title", "introduce", "active_time", "apply_info", "combo_id", "combo_price", "store_id", "cover_url", "city"));

        //获取套餐列表
        String[] ids = info.getComboId().split(",");
        int array[] = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            array[i] = Integer.parseInt(ids[i]);
        }
        List<Combo> combos = comboService.selectByIdSet(array);
        info.setComboList(combos);

        //获取门店列表
        String[] strings = info.getStoreId().split(",");
        int storeIds[] = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            storeIds[i] = Integer.parseInt(strings[i]);
        }
        List<StoreInfo> storeInfos = storeInfoService.selectStoreByIdSet(storeIds);
        info.setStoreInfoList(storeInfos);
        return info;
    }

}
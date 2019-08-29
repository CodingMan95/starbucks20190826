package com.eim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eim.entity.ActivityInfo;
import com.eim.entity.Combo;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.mapper.ActivityInfoMapper;
import com.eim.mapper.ActivityOrderMapper;
import com.eim.service.ActivityInfoService;
import com.eim.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {
    @Autowired
    private ActivityInfoMapper activityInfoMapper;
    @Autowired
    private ComboService comboService;
    @Autowired
    private ActivityOrderMapper orderMapper;

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
        //创建活动
        if (activityInfo.getActiveId() != 0) {
            activityInfoMapper.insert(activityInfo);
        } else {
            //更新活动
            activityInfoMapper.updateById(activityInfo);
        }
        return true;
    }

    @Override
    public List<Map> allActivity() {
        List<Map> list = new ArrayList<>();

        List<ActivityInfo> activityInfoList = activityInfoMapper.selectList(new QueryWrapper<ActivityInfo>()
                .select("active_id", "title", "store_id", "status"));
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
                int peopleNum = orderMapper.getNumOfActive(info.getActiveId());
                map.put("peopleNum", peopleNum);

                list.add(map);
            }
        }
        return list;
    }

    @Override
    public boolean updateStatus(int activeId, boolean status) {
        boolean update = update(new UpdateWrapper<ActivityInfo>().set("status", status).eq("active_id", activeId));
        return update;
    }

    @Override
    public boolean delete(int activeId) {
        int remove = activityInfoMapper.delete(new QueryWrapper<ActivityInfo>().eq("active_id", activeId));
        if (remove == 1) {
            return true;
        }
        return false;
    }

}
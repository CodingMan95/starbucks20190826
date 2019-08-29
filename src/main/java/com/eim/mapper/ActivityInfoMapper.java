package com.eim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eim.entity.ActivityInfo;

import java.util.List;

public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {
    List<ActivityInfo> getActivityByCity(String city);
}

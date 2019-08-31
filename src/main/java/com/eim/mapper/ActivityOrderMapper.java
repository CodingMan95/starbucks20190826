package com.eim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eim.entity.ActivityOrder;
import com.sun.xml.internal.rngom.parse.host.Base;

import java.util.List;

public interface ActivityOrderMapper extends BaseMapper<ActivityOrder> {
    int getNumOfActive(int activeId);

    List<ActivityOrder> selectData(String province, String city, String area, String storeName, Integer activeId, int start, int num);

    List<ActivityOrder> selectDataOfStore(Integer activeId, Integer storeId, int page, int num);
}

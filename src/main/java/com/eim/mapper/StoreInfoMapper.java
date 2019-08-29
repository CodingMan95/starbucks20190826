package com.eim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eim.entity.StoreInfo;

import java.util.List;

public interface StoreInfoMapper extends BaseMapper<StoreInfo> {
    List<StoreInfo> selectByIdSet(String city, int[] ids);

    List<StoreInfo> getByPage(int start, int num);

    List<String> getCityByProvince(String province, int[] ids);

    List<String> getAreaByCity(String city, int[] ids);

    List<StoreInfo> getStoreByArea(String area, int[] ids);

    List<String> getProvince(int[] ids);

    List<String> getAllCityByProvince(String province);

    List<String> getAllAreaByCity(String city);

    List<StoreInfo> getAllStoreByArea(String area);

    List<String> getAllProvince();
}

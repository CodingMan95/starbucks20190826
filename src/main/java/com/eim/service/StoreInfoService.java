package com.eim.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eim.entity.StoreInfo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface StoreInfoService extends IService<StoreInfo> {
    /**
     * 获取门店列表
     */
    List<StoreInfo> selectByIdSet(String city, int activeId);

    /**
     * 新增门店
     */
    boolean addStore(StoreInfo storeInfo);

    /**
     * 删除门店信息
     */
    boolean deleteStore(int id);

    /**
     * 分页查询门店信息
     */
    Map<String, Object> getByPage(int page, int limit);

    Map<String, Object> getStoreLimit(int type, String parm, String cityName, int activeId);

    Map<String, Object> getAllStore(int type, String parm, String cityName);

    List<StoreInfo> selectStoreByIdSet(int[] ids);

    List<StoreInfo> getStoreByType(int type, String parm);

    List<StoreInfo> getStoreList(String[] ids);
}

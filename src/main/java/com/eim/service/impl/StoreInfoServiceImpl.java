package com.eim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eim.entity.ActivityInfo;
import com.eim.entity.AdminUser;
import com.eim.entity.StoreInfo;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.mapper.StoreInfoMapper;
import com.eim.service.ActivityInfoService;
import com.eim.service.AdminUserService;
import com.eim.service.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class StoreInfoServiceImpl extends ServiceImpl<StoreInfoMapper, StoreInfo> implements StoreInfoService {
    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Autowired
    private ActivityInfoService activityInfoService;
    @Autowired
    private AdminUserService adminUserService;

    @Override
    public List<StoreInfo> selectByIdSet(String city, int activeId) {
        ActivityInfo activityInfo = activityInfoService.getOne(new QueryWrapper<ActivityInfo>().eq("active_id", activeId).select("store_id"));
        String[] ids = activityInfo.getStoreId().split(",");
        int array[] = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            array[i] = Integer.parseInt(ids[i]);
        }
        List<StoreInfo> storeInfos = storeInfoMapper.selectByIdSet(city, array);
        return storeInfos;
    }

    @Override
    public boolean addStore(StoreInfo storeInfo) {
        //如果没有id则为添加门店
        if (0 == storeInfo.getId()) {
            int count = storeInfoMapper.selectCount(new QueryWrapper<StoreInfo>().eq("store_id", storeInfo.getStoreId()));
            if (count != 0) {
                throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.HAVE_STORE);
            }

            int insert = storeInfoMapper.insert(storeInfo);
            if (insert == 1) {
                //添加对应门店的账号信息
                AdminUser user = new AdminUser(storeInfo.getStoreId(), storeInfo.getStoreId(), ConstantKit.STORE_ROLE, true, new Date());
                boolean save = adminUserService.save(user);
                return save;
            }
            return false;
        }
        //如果有id则为更新门店
        boolean update = update(new UpdateWrapper<StoreInfo>().setEntity(storeInfo));
        return update;

    }

    @Override
    public boolean deleteStore(int id) {

        StoreInfo storeInfo = storeInfoMapper.selectOne(new QueryWrapper<StoreInfo>().eq("id", id).select("store_id"));
        int delete = storeInfoMapper.delete(new QueryWrapper<StoreInfo>().eq("id", id));
        if (delete == 1) {
            //删除门店对应的账号信息
            boolean remove = adminUserService.remove(new QueryWrapper<AdminUser>().eq("store_id", storeInfo.getStoreId()));
            return remove;
        }
        return false;
    }

    @Override
    public Map<String, Object> getByPage(int page, int limit) {
        Map<String, Object> map = new HashMap<>();

        int start;
        if (page == 1) {
            start = 0;
        } else {
            start = (page - 1) * limit;
        }

        List<StoreInfo> list = storeInfoMapper.getByPage(start, limit);
        map.put("storeList", list);

        int totalStore = storeInfoMapper.selectCount(new QueryWrapper<>());

        map.put("totalStore", totalStore);
        return map;
    }

    @Override
    public Map<String, Object> getStoreLimit(int type, String parm, String cityName, int activeId) {
        ActivityInfo activityInfo = activityInfoService.getOne(new QueryWrapper<ActivityInfo>().select("store_id").eq("active_id", activeId));
        String[] ids = activityInfo.getStoreId().split(",");
        int array[] = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            array[i] = Integer.parseInt(ids[i]);
        }

        Map<String, Object> map = new HashMap<>();
        //通过省查询市
        if (type == 0) {
            List<String> province = storeInfoMapper.getProvince(array);
            map.put("province", province);
        } else if (type == 1) {
            List<String> city = storeInfoMapper.getCityByProvince(parm, array);
            map.put("city", city);
        } else if (type == 2) {
            List<String> area = storeInfoMapper.getAreaByCity(parm, array);
            map.put("area", area);
        } else if (type == 3) {
            List<StoreInfo> store = storeInfoMapper.getStoreByArea(parm, cityName, array);
            map.put("store", store);
        }
        return map;
    }

    @Override
    public Map<String, Object> getAllStore(int type, String parm, String cityName) {
        Map<String, Object> map = new HashMap<>();
        //通过省查询市
        if (type == 0) {
            List<String> province = storeInfoMapper.getAllProvince();
            map.put("province", province);
        } else if (type == 1) {
            List<String> city = storeInfoMapper.getAllCityByProvince(parm);
            map.put("city", city);
        } else if (type == 2) {
            List<String> area = storeInfoMapper.getAllAreaByCity(parm);
            map.put("area", area);
        } else if (type == 3) {
            List<StoreInfo> store = storeInfoMapper.getAllStoreByArea(parm, cityName);
            map.put("store", store);
        }
        return map;
    }

    @Override
    public List<StoreInfo> selectStoreByIdSet(int[] ids) {
        List<StoreInfo> storeInfoList = storeInfoMapper.selectStoreByIdSet(ids);
        return storeInfoList;
    }

    @Override
    public List<StoreInfo> getStoreByType(int type, String parm) {
        List<StoreInfo> infos = new ArrayList<>();
        //通过省查询市
        if (type == 0) {
            infos = storeInfoMapper.selectList(new QueryWrapper<StoreInfo>().eq("province", parm).select("id", "store_name"));
        } else if (type == 1) {
            infos = storeInfoMapper.selectList(new QueryWrapper<StoreInfo>().eq("city", parm).select("id", "store_name"));
        }
        return infos;
    }
}

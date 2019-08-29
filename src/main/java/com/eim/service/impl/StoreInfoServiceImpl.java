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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        int delete = storeInfoMapper.delete(new QueryWrapper<StoreInfo>().eq("id", id));
        if (delete == 1) {
            //删除门店对应的账号信息
            StoreInfo storeInfo = storeInfoMapper.selectOne(new QueryWrapper<StoreInfo>().eq("id", id).select("store_id"));
            boolean remove = adminUserService.remove(new QueryWrapper<AdminUser>().eq("store_id", storeInfo.getStoreId()));
            return remove;
        }
        return false;
    }

    @Override
    public Map<String, Object> getByPage(int page) {
        Map<String, Object> map = new HashMap<>();

        int start;
        if (page == 1) {
            start = 0;
        } else {
            start = (page - 1) * ConstantKit.PAGE_LIMIT;
        }

        List<StoreInfo> list = storeInfoMapper.getByPage(start, ConstantKit.PAGE_LIMIT);
        map.put("storeList", list);
        int count = storeInfoMapper.selectCount(new QueryWrapper<>());
        int totalPage;
        if (count <= ConstantKit.PAGE_LIMIT) {
            totalPage = 1;
        } else if (count % ConstantKit.PAGE_LIMIT == 0) {
            totalPage = count / ConstantKit.PAGE_LIMIT;
        } else {
            totalPage = count / ConstantKit.PAGE_LIMIT + 1;
        }

        map.put("totalPage", totalPage);
        return map;
    }

    @Override
    public Map<String, Object> getStoreLimit(int type, String parm, int activeId) {
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
            List<StoreInfo> store = storeInfoMapper.getStoreByArea(parm, array);
            map.put("store", store);
        }
        return map;
    }

    @Override
    public Map<String, Object> getAllStore(int type, String parm) {
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
            List<StoreInfo> store = storeInfoMapper.getAllStoreByArea(parm);
            map.put("store", store);
        }
        return map;
    }
}

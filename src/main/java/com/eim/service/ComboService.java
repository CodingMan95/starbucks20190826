package com.eim.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eim.entity.Combo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ComboService extends IService<Combo> {
    /**
     * 获取套餐列表
     */
    List<Combo> selectByIdSet(@Param("ids") int[] ids);

    int add(String name, String pic, int id);

    boolean delete(int id);
}

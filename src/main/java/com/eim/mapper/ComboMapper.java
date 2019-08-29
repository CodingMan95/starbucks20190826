package com.eim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eim.entity.Combo;

import java.util.List;

public interface ComboMapper extends BaseMapper<Combo> {
    List<Combo> selectByIdSet(int[] ids);
}

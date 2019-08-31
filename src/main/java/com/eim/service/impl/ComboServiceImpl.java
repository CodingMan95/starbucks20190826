package com.eim.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eim.entity.Combo;
import com.eim.mapper.ComboMapper;
import com.eim.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ComboServiceImpl extends ServiceImpl<ComboMapper, Combo> implements ComboService {
    @Autowired
    private ComboMapper comboMapper;

    @Override
    public List<Combo> selectByIdSet(int[] ids) {
        return comboMapper.selectByIdSet(ids);
    }

    @Override
    public int add(String name, String pic) {
        Combo combo = new Combo();
        combo.setName(name);
        combo.setPic(pic);
        combo.setCreateTime(new Date());
        boolean insert = comboMapper.addCombo(combo);
        if (insert) {
            return combo.getId();
        }
        return 0;
    }

}

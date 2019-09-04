package com.eim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eim.entity.ActivityOrder;
import com.eim.entity.Combo;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.mapper.ComboMapper;
import com.eim.service.ActivityOrderService;
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
    @Autowired
    private ActivityOrderService orderService;

    @Override
    public List<Combo> selectByIdSet(int[] ids) {
        return comboMapper.selectByIdSet(ids);
    }

    @Override
    public int add(String name, String pic, int id) {
        Combo combo = new Combo();
        combo.setName(name);
        combo.setPic(pic);

        boolean status;
        if (id > 0) {
            combo.setId(id);
            //更新
            status = update(combo, new UpdateWrapper<Combo>().eq("id", id));
        } else {
            //添加
            combo.setCreateTime(new Date());
            status = comboMapper.addCombo(combo);
        }

        if (status) {
            return combo.getId();
        }
        return 0;
    }

    @Override
    public boolean delete(int id) {
        //如果该套餐已有人预约，则不允许删除
        int count = orderService.count(new QueryWrapper<ActivityOrder>().eq("combo_id", id));
        if (count > 0) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.COMBO_ORDERED);
        }

        int delete = comboMapper.delete(new QueryWrapper<Combo>().eq("id", id));
        if (delete == 1) {
            return true;
        }
        return false;
    }

}

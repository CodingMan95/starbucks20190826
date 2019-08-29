package com.eim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eim.config.RedisConfig;
import com.eim.exception.BusinessException;
import com.eim.kit.ConstantKit;
import com.eim.mapper.AdminUserMapper;
import com.eim.entity.AdminUser;
import com.eim.service.AdminUserService;
import com.eim.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {
    @Autowired
    private AdminUserMapper userMapper;
    @Autowired
    private RedisConfig redisConfig;

    @Override
    public Map<String, Object> userLogin(String storeId, String password) {

        int userNum = userMapper.selectCount(new QueryWrapper<AdminUser>().eq("store_id", storeId));
        if (userNum == 0) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.NO_USER);
        }

        //对密码进行加密
        //String md5Password = Md5Util.getMD5String(password);
        AdminUser user = userMapper.selectOne(new QueryWrapper<AdminUser>().eq("store_id", storeId).eq("password", password));
        if (null == user) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.ERROR_PASSWORD);
        }

        Jedis jedis = new Jedis(redisConfig.REDIS_HOST, redisConfig.REDIS_PORT);
        jedis.auth(redisConfig.REDIS_PASSWORD);
        //生成token
        String token = Md5Util.generate(storeId, password);
        //存入redis
        jedis.set(storeId, token);
        jedis.expire(storeId, ConstantKit.TOKEN_EXPIRE_TIME);
        jedis.set(token, storeId);
        jedis.expire(token, ConstantKit.TOKEN_EXPIRE_TIME);
        Long currentTime = System.currentTimeMillis();
        jedis.set(token + storeId, currentTime.toString());
        jedis.expire(token + storeId, ConstantKit.TOKEN_EXPIRE_TIME);
        //用完关闭
        jedis.close();

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("role", user.getRoleId());

        return map;
    }

    @Override
    public boolean changePwd(String storeId, String oldPassword, String newPassword) {

        AdminUser adminUser = userMapper.selectOne(new QueryWrapper<AdminUser>().eq("store_id", storeId).eq("password", oldPassword));
        if (null == adminUser) {
            throw new BusinessException(ConstantKit.BAD_REQUEST, ConstantKit.ERROR_PASSWORD);
        }
        userMapper.update(adminUser, new UpdateWrapper<AdminUser>().set("password", newPassword).set("update_time", new Date()).eq("store_id", storeId));
        return true;
    }
}

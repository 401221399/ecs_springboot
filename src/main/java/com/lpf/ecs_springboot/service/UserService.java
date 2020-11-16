package com.lpf.ecs_springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lpf.ecs_springboot.common.ShiroUtils;
import com.lpf.ecs_springboot.entity.User;
import com.lpf.ecs_springboot.dao.*;
import com.lpf.ecs_springboot.entity.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserDao, User> {

    //添加用户
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user) {
        if(this.getBaseMapper().selectOne(new QueryWrapper<User>().eq("username", user.getUsername())) == null)
        {
            //sha256加密，产生随机盐
            String salt = RandomStringUtils.randomAlphanumeric(20);

            user.setSalt(salt);

            //ShiroUtils.sha256加密       //明码+盐+迭代次数=密文
            user.setRepassword(user.getPassword());
            user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
            this.save(user);
        }

    }


}

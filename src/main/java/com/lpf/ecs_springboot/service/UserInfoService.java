package com.lpf.ecs_springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lpf.ecs_springboot.common.ShiroUtils;
import com.lpf.ecs_springboot.dao.UserDao;
import com.lpf.ecs_springboot.dao.UserInfoDao;
import com.lpf.ecs_springboot.entity.User;
import com.lpf.ecs_springboot.entity.UserInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInfoService extends ServiceImpl<UserInfoDao, UserInfo> {

}

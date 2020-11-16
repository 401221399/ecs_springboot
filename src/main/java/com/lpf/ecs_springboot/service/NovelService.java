package com.lpf.ecs_springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lpf.ecs_springboot.dao.NovelDao;
import com.lpf.ecs_springboot.dao.UserInfoDao;
import com.lpf.ecs_springboot.entity.Novel;
import com.lpf.ecs_springboot.entity.UserInfo;
import org.springframework.stereotype.Service;

@Service
public class NovelService extends ServiceImpl<NovelDao, Novel> {

}

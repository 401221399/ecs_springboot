package com.lpf.ecs_springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lpf.ecs_springboot.entity.Novel;
import com.lpf.ecs_springboot.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NovelDao extends BaseMapper<Novel> {
}

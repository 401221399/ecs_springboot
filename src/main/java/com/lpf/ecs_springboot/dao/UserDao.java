package com.lpf.ecs_springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lpf.ecs_springboot.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao extends BaseMapper<User> {
}

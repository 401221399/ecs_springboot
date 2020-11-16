package com.lpf.ecs_springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lpf.ecs_springboot.entity.Cartoon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartoonDao extends BaseMapper<Cartoon> {
}

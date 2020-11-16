package com.lpf.ecs_springboot.dao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//用于进行数据库操作，建表，查表等
@Mapper
public interface MysqlOperaterDao{
    int existTable(@Param("tableName")String tableName);

    int dropTable(@Param("tableName")String tableName);

    int createNewTable(@Param("tableName")String tableName);

    List<String> getTableListById(@Param("id")String id);
}

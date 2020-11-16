package com.lpf.ecs_springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lpf.ecs_springboot.common.ShiroUtils;
import com.lpf.ecs_springboot.dao.MysqlOperaterDao;
import com.lpf.ecs_springboot.dao.UserDao;
import com.lpf.ecs_springboot.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MysqlOpraterService{
    @Resource
    MysqlOperaterDao MysqlOperaterDao;

    public int existTable(String table_name) {
       return MysqlOperaterDao.existTable(table_name);
    }
    public int createNewTable(String table_name) {
        return MysqlOperaterDao.createNewTable(table_name);
    }
    public List<String> getTableListById(int id) {
        return MysqlOperaterDao.getTableListById(id+"");
    }


}

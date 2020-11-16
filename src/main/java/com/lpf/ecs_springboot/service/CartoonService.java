package com.lpf.ecs_springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lpf.ecs_springboot.dao.CartoonDao;
import com.lpf.ecs_springboot.entity.Cartoon;
import org.springframework.stereotype.Service;

@Service
public class CartoonService extends ServiceImpl<CartoonDao, Cartoon> {

}

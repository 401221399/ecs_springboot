package com.lpf.ecs_springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lpf.ecs_springboot.common.R;
import com.lpf.ecs_springboot.entity.*;
import com.lpf.ecs_springboot.service.NovelService;
import com.lpf.ecs_springboot.service.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/Touzi")
public class TouziController {
    @Resource
    com.lpf.ecs_springboot.service.TouziService TouziService;

    @ResponseBody
    @RequestMapping(value = "/getTouziRoom",method = RequestMethod.GET)
    @CrossOrigin
    public R getTouziRoom(){
        Map<String,Object> resultMap=new HashMap<>();
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();
            QueryWrapper qw = new QueryWrapper<Touzi>();
            qw.eq("uid",uid);
            List<Touzi> resList = TouziService.list(qw);
            if (resList.size()>0)
            {
                resultMap.put("data",resList);
                return R.ok(resultMap);
            }
            else {
                return R.error("该用户未在游戏房间内");
            }
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/enterTouziRoom")
    public R enterTouziRoom(@RequestBody Map param) {
        int roomid = (int) param.get("roomid");
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            QueryWrapper qw = new QueryWrapper<Touzi>();
            qw.eq("roomid",roomid);
            Touzi touzi = TouziService.getOne(qw);
            if (touzi==null)
            {
                return R.error("未找到该房间信息");
            }
            else{
                if (touzi.getRoomstatus() == 1)
                {
                    return R.error("该房间已开始");
                }
                else {
                    User user = (User) principal;
                    int uid = user.getId();
                    Touzi newTouzi = new Touzi();
                    newTouzi.setUid(uid);
                    newTouzi.setRoomid(touzi.getRoomid());
                    newTouzi.setRoomstatus(touzi.getRoomstatus());
                    newTouzi.setDate(touzi.getDate());
                    boolean result = TouziService.save(newTouzi);
                    if(result) {
                        return R.ok();
                    }
                    else {
                        return R.error("保存失败");
                    }
                }
            }
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/newTouziRoom")
    public R newTouziRoom(@RequestBody Map param) {
        int roomid = (int) param.get("roomid");
        Map<String,Object> resultMap=new HashMap<>();
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();
            QueryWrapper qw = new QueryWrapper<Touzi>();
            qw.eq("uid",uid);
            List<Touzi> resList = TouziService.list(qw);
            if (resList.size()>0)
            {
                return R.error("您已在游戏房间内");
            }
            else {
                Touzi touzi = new Touzi();
                touzi.setUid(uid);
                touzi.setRoomstatus(0);
                touzi.setDate(System.currentTimeMillis()+"");
                boolean result = TouziService.save(touzi);
                if(result) {
                    return R.ok("新建房间成功");
                }
                else {
                    return R.error("新建房间失败");
                }
            }
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/palyGame")
    public R palyGame(@RequestBody Map param) {
        int roomid = (int) param.get("roomid");
        Map<String,Object> resultMap=new HashMap<>();
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            QueryWrapper qw = new QueryWrapper<Touzi>();
            qw.eq("roomid",roomid);
            List<Touzi> touziList = TouziService.list(qw);
            if (touziList.size()>0)
            {
                for (Touzi touzi :touziList)
                {
                    JSONArray data = new JSONArray();
                    for (int i=0;i<4;i++)
                    {
                        data.add(new Random().nextInt(5)+1);
                    }
                    touzi.setDate(JSONArray.toJSONString(data));
                }
                TouziService.saveBatch(touziList);
                return R.ok();
            }
            else {
                return R.error("错误:未找到该房间信息");
            }
        }
        else {
            return R.error("未登录");
        }
    }




}

package com.lpf.ecs_springboot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lpf.ecs_springboot.common.R;
import com.lpf.ecs_springboot.dao.TouziDao;
import com.lpf.ecs_springboot.dao.UserDao;
import com.lpf.ecs_springboot.entity.Touzi;
import com.lpf.ecs_springboot.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import java.util.*;


@Service(value = "TouziService")
public class TouziService extends ServiceImpl<TouziDao, Touzi> {
    @Resource
    UserDao userDao;
    //获取当前用户的房间信息
    public JSONObject getTouziRoom(String parameter){
        JSONObject resultMap = new JSONObject();
        JSONObject requestMap = JSON.parseObject(parameter);
        String uid = requestMap.get("uid").toString();
        QueryWrapper qw = new QueryWrapper<Touzi>();
        qw.eq("uid",uid);
        List<Touzi> resList = this.list(qw);
        if (resList!=null && resList.size()>0)
        {
            resultMap.put("data",resList.get(0));
            resultMap.put("flag","ok");
        }
        else {
            resultMap.put("msg","该用户未在游戏房间内");
            resultMap.put("flag","error");
        }
        return resultMap;
    }

    //获取当前房间的玩家
    public JSONObject getPalyer(String parameter){
        JSONObject resultMap = new JSONObject();
        JSONObject requestMap = JSON.parseObject(parameter);
        String roomid = requestMap.get("roomid").toString();
        QueryWrapper qw = new QueryWrapper<Touzi>();
        qw.eq("roomid",roomid);
        List<Touzi> resList = this.list(qw);
        List palyerList = new ArrayList();
        if (resList!=null && resList.size()>0)
        {
            for (Touzi t : resList)
            {
                palyerList.add(t.getUsername());
            }
            resultMap.put("data",palyerList);
            resultMap.put("flag","ok");
        }
        else {
            resultMap.put("msg","未找到该房间玩家信息");
            resultMap.put("flag","error");
        }

        return resultMap;
    }

    public JSONObject enterTouziRoom(String parameter) {
        JSONObject resultMap = new JSONObject();
        JSONObject requestMap = JSON.parseObject(parameter);
        String uid = requestMap.get("uid").toString();
        String roomid = requestMap.get("roomid").toString();
        QueryWrapper qw = new QueryWrapper<Touzi>();
        qw.eq("roomid",roomid);
        Touzi touzi = this.getOne(qw);

        if (touzi==null)
        {
            resultMap.put("msg","未找到该房间信息");
            resultMap.put("flag","error");
        }
        else
        {
            if (touzi.getRoomstatus() == 1)
            {
                resultMap.put("msg","该房间已开始");
                resultMap.put("flag","error");
            }
            else {

                Touzi newTouzi = new Touzi();
                newTouzi.setUid(Integer.parseInt(uid));
                newTouzi.setRoomid(touzi.getRoomid());
                newTouzi.setRoomstatus(touzi.getRoomstatus());
                newTouzi.setDate(touzi.getDate());
                User u = userDao.selectById(uid);
                newTouzi.setUsername(u.getName());
                touzi.setIshost(0);
                boolean result = this.save(newTouzi);
                if(result) {
                    resultMap.put("msg","保存成功");
                    resultMap.put("data",touzi);
                    resultMap.put("flag","ok");
                }
                else {
                    resultMap.put("msg","保存失败");
                    resultMap.put("flag","error");
                }
            }
        }

        return resultMap;
    }

    public JSONObject newTouziRoom(String parameter) {
        JSONObject resultMap = new JSONObject();
        JSONObject requestMap = JSON.parseObject(parameter);
        String uid = requestMap.get("uid").toString();
        QueryWrapper qw = new QueryWrapper<Touzi>();
        qw.eq("uid",uid);
        List<Touzi> resList = this.list(qw);
        if (resList.size()>0)
        {
            resultMap.put("msg","您已在游戏房间内");
            resultMap.put("flag","error");
        }
        else {
            int roomid = new Random().nextInt(9999);
            while (true)
            {
                QueryWrapper whileqw = new QueryWrapper<Touzi>();
                whileqw.eq("roomid",roomid);
                Touzi whileqT = this.getOne(qw);
                if(whileqT != null){
                    roomid = new Random().nextInt(9999);
                }
                else {
                    break;
                }
            }
            Touzi touzi = new Touzi();
            touzi.setUid(Integer.parseInt(uid));
            touzi.setRoomid(roomid);
            touzi.setRoomstatus(0);
            touzi.setDate(System.currentTimeMillis()+"");
            touzi.setIshost(1);
            User u = userDao.selectById(uid);
            touzi.setUsername(u.getName());
            boolean result = this.save(touzi);
            if(result) {
                resultMap.put("msg","新建房间成功");
                resultMap.put("data",touzi);
                resultMap.put("flag","ok");
            }
            else {
                resultMap.put("msg","新建房间失败");
                resultMap.put("flag","error");
            }
        }
        return resultMap;
    }

    public JSONObject palyGame(String parameter) {
        JSONObject resultMap = new JSONObject();
        JSONObject requestMap = JSON.parseObject(parameter);
        String uid = requestMap.get("uid").toString();
        String roomid = requestMap.get("roomid").toString();
        QueryWrapper qw = new QueryWrapper<Touzi>();
        qw.eq("roomid",roomid);
        List<Touzi> touziList = this.list(qw);
        qw.eq("uid",uid);
        Touzi nowT = this.getOne(qw);
        if (nowT.getIshost()==1)
        {
            if (touziList.size()>1)
            {
                for (Touzi touzi :touziList)
                {
                    boolean flag = true;
                    JSONArray data = new JSONArray();
                    while (flag)
                    {
                        data = new JSONArray();
                        int[] arr = new int[6];
                        for (int i=0;i<5;i++)
                        {
                            int x = new Random().nextInt(6);
                            arr[x]++;
                            data.add(x+1);
                        }
                        for (int x : arr)
                        {
                            if (x>=2)
                            {
                                flag = false;
                                break;
                            }
                        }
                        if (flag)
                        {
                            System.out.println("顺骰，重摇");;
                        }
                    }
                    touzi.setData(JSONArray.toJSONString(data));
                    touzi.setRoomstatus(1);
                }
                this.updateBatchById(touziList);
                resultMap.put("msg","游戏开始");
                resultMap.put("flag","ok");
            }
            else {
                resultMap.put("msg","游戏人数不够");
                resultMap.put("flag","error");
            }
        }
        else {
            QueryWrapper hostqw = new QueryWrapper<Touzi>();
            hostqw.eq("roomid",roomid);
            hostqw.eq("ishost",1);
            Touzi host = this.getOne(hostqw);
            resultMap.put("msg","请让房主("+host.getUsername()+")开始游戏");
            resultMap.put("flag","error");
        }
        return resultMap;
    }

    public JSONObject overGame(String parameter) {
        JSONObject resultMap = new JSONObject();
        JSONObject requestMap = JSON.parseObject(parameter);
        String uid = requestMap.get("uid").toString();
        String roomid = requestMap.get("roomid").toString();
        QueryWrapper qw = new QueryWrapper<Touzi>();
        qw.eq("roomid",roomid);
        List<Touzi> touziList = this.list(qw);
        qw.eq("uid",uid);
        Touzi nowT = this.getOne(qw);
        if (nowT.getIshost()==1)
        {
            if (touziList.size()>0)
            {
                List delList = new ArrayList();
                for (Touzi touzi :touziList)
                {
                    delList.add(touzi.getId());
                }
                this.getBaseMapper().deleteBatchIds(delList);
                resultMap.put("msg","游戏结束");
                resultMap.put("flag","ok");
            }
            else {
                resultMap.put("msg","未找到该游戏房间信息");
                resultMap.put("flag","error");
            }
        }
        else {
            QueryWrapper hostqw = new QueryWrapper<Touzi>();
            hostqw.eq("roomid",roomid);
            hostqw.eq("ishost",1);
            Touzi host = this.getOne(hostqw);
            resultMap.put("msg","请让房主("+host.getUsername()+")结束游戏");
            resultMap.put("flag","error");
        }
        return resultMap;
    }

    public JSONObject leaveRoom(String parameter) {
        JSONObject resultMap = new JSONObject();
        JSONObject requestMap = JSON.parseObject(parameter);
        String uid = requestMap.get("uid").toString();
        String roomid = requestMap.get("roomid").toString();
        QueryWrapper qw = new QueryWrapper<Touzi>();
        qw.eq("roomid",roomid);
        qw.eq("uid",uid);
        Touzi t = this.getOne(qw);
        if (t!=null)
        {
            this.baseMapper.deleteById(t.getId());
            resultMap.put("msg","退出成功");
            resultMap.put("flag","ok");
        }
        else {
            resultMap.put("msg","未找到该房间信息");
            resultMap.put("flag","error");
        }
        return resultMap;
    }

    public JSONObject openGame(String parameter) {
        JSONObject resultMap = new JSONObject();
        JSONObject requestMap = JSON.parseObject(parameter);
        String roomid = requestMap.get("roomid").toString();
        QueryWrapper qw = new QueryWrapper<Touzi>();
        qw.eq("roomid",roomid);
        List<Touzi> touziList = this.list(qw);
        List resultTextList = new ArrayList();
        if (touziList!=null && touziList.size()>0)
        {
            for (Touzi t:touziList)
            {
                resultTextList.add(t.getUsername()+":"+t.getData());
            }
            resultMap.put("data",resultTextList);
            resultMap.put("flag","ok");
        }
        else {
            resultMap.put("msg","未找到该房间玩家信息");
            resultMap.put("flag","error");
        }
        return resultMap;
    }

    public JSONObject demo(String parameter) {
        JSONObject resultMap = new JSONObject();
        JSONObject requestMap = JSON.parseObject(parameter);
        String uid = requestMap.get("uid").toString();

        return resultMap;
    }

}

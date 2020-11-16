package com.lpf.ecs_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "userinfo")
public class UserInfo {
    @TableId(value = "id")
    private int id;
    @TableField("uid")
    private int uid;
    @TableField("weixin")
    private String weixin;
    @TableField("zhifubao")
    private String zhifubao;
    @TableField("time")
    private String time;

    public void setId(int id) {
        this.id = id;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public void setZhifubao(String zhifubao) {
        this.zhifubao = zhifubao;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public int getUid() {
        return uid;
    }

    public String getWeixin() {
        return weixin;
    }

    public String getZhifubao() {
        return zhifubao;
    }

    public String getTime() {
        return time;
    }
}

package com.lpf.ecs_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
//大话骰子
@TableName(value = "touzi")
public class Touzi {
    @TableId(value = "id")
    private int id;
    @TableField("uid")
    private int uid;
    @TableField("roomid")
    private int roomid;
    @TableField("data")
    private String data;
    @TableField("roomstatus")
    //0结束，1进行中
    private int roomstatus;
    @TableField("date")
    private String date;
    @TableField("ishost")
    //是否为房主1为是，0为否
    private int ishost;

    @TableField("username")
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setIshost(int ishost) {
        this.ishost = ishost;
    }

    public int getIshost() {
        return ishost;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setRoomstatus(int roomstatus) {
        this.roomstatus = roomstatus;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getUid() {
        return uid;
    }

    public int getRoomid() {
        return roomid;
    }

    public String getData() {
        return data;
    }

    public int getRoomstatus() {
        return roomstatus;
    }

    public String getDate() {
        return date;
    }
}

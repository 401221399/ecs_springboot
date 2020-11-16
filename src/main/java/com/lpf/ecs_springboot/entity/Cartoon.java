package com.lpf.ecs_springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "cartoon")
public class Cartoon {
    @TableId(value = "id")
    private int id;
    @TableField("uid")
    private int uid;
    @TableField("name")
    private String name;
    @TableField("url")
    private String url;
    @TableField("catalogurl")
    private String catalogurl;
    @TableField("catalogindex")
    private String catalogindex;
    @TableField("img")
    private String img;

    public void setId(int id) {
        this.id = id;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCatalogurl(String catalogurl) {
        this.catalogurl = catalogurl;
    }

    public void setCatalogindex(String catalogindex) {
        this.catalogindex = catalogindex;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getCatalogurl() {
        return catalogurl;
    }

    public String getCatalogindex() {
        return catalogindex;
    }

    public String getImg() {
        return img;
    }
}

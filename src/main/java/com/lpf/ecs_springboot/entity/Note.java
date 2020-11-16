package com.lpf.ecs_springboot.entity;
public class Note {
    private int id;
    private String money;
    private String time;
    private String profile;
    private String classname;
    private String channel;
    private String profitandloss;

    public void setId(int id) {
        this.id = id;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


    public int getId() {
        return id;
    }

    public String getMoney() {
        return money;
    }

    public String getTime() {
        return time;
    }

    public String getProfile() {
        return profile;
    }

    public String getClassname() {
        return classname;
    }

    public String getChannel() {
        return channel;
    }

    public String getProfitandloss() {
        return profitandloss;
    }

    public void setProfitandloss(String profitandloss) {
        this.profitandloss = profitandloss;
    }
}

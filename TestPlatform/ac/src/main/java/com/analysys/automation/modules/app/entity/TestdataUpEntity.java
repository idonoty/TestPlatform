package com.analysys.automation.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;

@TableName("t_testdata_up")
public class TestdataUpEntity {
    @TableId  //通过这个注释来指定主键，这样列名不一致也可以对应上主键
    private int id;

    @TableField(value = "loopNum")
    private int loopNum ;
    @TableField(value = "userNum")
    private int userNum ;
    @TableField(value = "eventMin")
    private int eventMin ;
    @TableField(value = "eventMax")
    private int eventMax ;
    @TableField(value = "timeMin")
    private String timeMin ;
    @TableField(value = "timeMax")
    private String timeMax ;

    private String alias ;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    private String appkey ;

    private String url ;

    private Date create_time;

    private Date end_time;

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoopNum() {
        return loopNum;
    }

    public void setLoopNum(int loopNum) {
        this.loopNum = loopNum;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public int getEventMin() {
        return eventMin;
    }

    public void setEventMin(int eventMin) {
        this.eventMin = eventMin;
    }

    public int getEventMax() {
        return eventMax;
    }

    public void setEventMax(int eventMax) {
        this.eventMax = eventMax;
    }

    public String getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(String timeMin) {
        this.timeMin = timeMin;
    }

    public String getTimeMax() {
        return timeMax;
    }

    public void setTimeMax(String timeMax) {
        this.timeMax = timeMax;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
}

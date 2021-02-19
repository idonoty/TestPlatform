package com.analysys.automation.modules.app.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2019-03-20 09:32:20
 */
@TableName("t_profile_log")
public class ProfileLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer profileLogId;
	/**
	 * 
	 */
	private String groupName;
	/**
	 * 
	 */
	private String profileName;
	/**
	 * 
	 */
	private String eventName;
	/**
	 * 
	 */
	private Integer createUserId;
	/**
	 * 
	 */
	private Date createTime;

	@JSONField(serialize=false)
	private String profileDir;

    @JSONField(serialize=false)
	private String eventDir;

    private int status;

	/**
	 * 设置：
	 */
	public void setProfileLogId(Integer profileLogId) {
		this.profileLogId = profileLogId;
	}
	/**
	 * 获取：
	 */
	public Integer getProfileLogId() {
		return profileLogId;
	}
	/**
	 * 设置：
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * 获取：
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * 设置：
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	/**
	 * 获取：
	 */
	public String getProfileName() {
		return profileName;
	}
	/**
	 * 设置：
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	/**
	 * 获取：
	 */
	public String getEventName() {
		return eventName;
	}
	/**
	 * 设置：
	 */
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * 获取：
	 */
	public Integer getCreateUserId() {
		return createUserId;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}

    public String getProfileDir() {
        return profileDir;
    }

    public void setProfileDir(String profileDir) {
        this.profileDir = profileDir;
    }

    public String getEventDir() {
        return eventDir;
    }

    public void setEventDir(String eventDir) {
        this.eventDir = eventDir;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

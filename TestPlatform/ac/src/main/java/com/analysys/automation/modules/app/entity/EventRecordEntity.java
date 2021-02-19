package com.analysys.automation.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2019-03-18 15:40:27
 */
@TableName("t_event_record")
public class EventRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer eventRecordId;
	/**
	 * 
	 */
	private String content;
	/**
	 * 
	 */
	private String xwho;
	/**
	 * 
	 */
	private Long xwhen;
	/**
	 * 
	 */
	private Date tfXwhen;
	/**
	 * 
	 */
	private String xwhat;

	private String groupName;
	/**
	 * 
	 */
	private Integer createUserId;
	/**
	 * 
	 */
	private Date createTime;

	/**
	 * 设置：
	 */
	public void setEventRecordId(Integer eventRecordId) {
		this.eventRecordId = eventRecordId;
	}
	/**
	 * 获取：
	 */
	public Integer getEventRecordId() {
		return eventRecordId;
	}
	/**
	 * 设置：
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：
	 */
	public void setXwho(String xwho) {
		this.xwho = xwho;
	}
	/**
	 * 获取：
	 */
	public String getXwho() {
		return xwho;
	}
	/**
	 * 设置：
	 */
	public void setXwhen(Long xwhen) {
		this.xwhen = xwhen;
	}
	/**
	 * 获取：
	 */
	public Long getXwhen() {
		return xwhen;
	}
	/**
	 * 设置：
	 */
	public void setTfXwhen(Date tfXwhen) {
		this.tfXwhen = tfXwhen;
	}
	/**
	 * 获取：
	 */
	public Date getTfXwhen() {
		return tfXwhen;
	}
	/**
	 * 设置：
	 */
	public void setXwhat(String xwhat) {
		this.xwhat = xwhat;
	}
	/**
	 * 获取：
	 */
	public String getXwhat() {
		return xwhat;
	}

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
}

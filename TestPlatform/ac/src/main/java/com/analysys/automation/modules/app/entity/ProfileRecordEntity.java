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
 * @date 2019-03-18 15:40:27
 */
@TableName("t_profile_record")
public class ProfileRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer profileRecordId;
	/**
	 * 
	 */
	private String content;
	/**
	 * 
	 */
	private String orginalId;
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
	private String phone;
	/**
	 * 
	 */
	private String email;
	/**
	 * 
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date firstVisitTime;
	/**
	 * 分组名
	 */
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
	public void setProfileRecordId(Integer profileRecordId) {
		this.profileRecordId = profileRecordId;
	}
	/**
	 * 获取：
	 */
	public Integer getProfileRecordId() {
		return profileRecordId;
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
	public void setOrginalId(String orginalId) {
		this.orginalId = orginalId;
	}
	/**
	 * 获取：
	 */
	public String getOrginalId() {
		return orginalId;
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
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：
	 */
	public String getEmail() {
		return email;
	}

    public Date getFirstVisitTime() {
        return firstVisitTime;
    }

    public void setFirstVisitTime(Date firstVisitTime) {
        this.firstVisitTime = firstVisitTime;
    }

    /**
	 * 设置：分组名
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * 获取：分组名
	 */
	public String getGroupName() {
		return groupName;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getOrginalId() + "|" + this.getXwho() + "|" + this.getPhone() + "|" + this.getEmail());
		return sb.toString();
	}
}

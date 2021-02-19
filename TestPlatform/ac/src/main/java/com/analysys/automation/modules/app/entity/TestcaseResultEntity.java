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
 * @date 2018-10-16 15:09:51
 */
@TableName("t_testcase_result")
public class TestcaseResultEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer testcaseResultId;
	/**
	 * 
	 */
	private Integer testcaseId;

	private String testcaseCode;

	private String testplanName;

	/**
	 * 
	 */
	private String projectName;
	/**
	 * 
	 */
	private String moduleName;
	/**
	 * 
	 */
	private String uri;
	/**
	 * 
	 */
	private String method;
	/**
	 * 
	 */
	private String data;
	/**
	 * 
	 */
	private String expectedResult;

	private String actualResult;
	/**
	 * 
	 */
	private String description;
	/**
	 * 
	 */
	private String result;

	/**
	 * 
	 */
	private Integer creatorId;
	/**
	 * 
	 */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	public TestcaseResultEntity() {

    }

    public TestcaseResultEntity(TestcaseEntity testcaseEntity) {
	    this.testcaseId = testcaseEntity.getTestcaseId();
	    this.projectName = testcaseEntity.getProjectName();
	    this.moduleName = testcaseEntity.getModuleName();
	    this.uri = testcaseEntity.getUri();
	    this.method = testcaseEntity.getMethod();
	    this.data = testcaseEntity.getData();
	    this.expectedResult = testcaseEntity.getExpectedResult();
	    this.description = testcaseEntity.getDescription();
    }
	/**
	 * 设置：
	 */
	public void setTestcaseResultId(Integer testcaseResultId) {
		this.testcaseResultId = testcaseResultId;
	}
	/**
	 * 获取：
	 */
	public Integer getTestcaseResultId() {
		return testcaseResultId;
	}
	/**
	 * 设置：
	 */
	public void setTestcaseId(Integer testcaseId) {
		this.testcaseId = testcaseId;
	}
	/**
	 * 获取：
	 */
	public Integer getTestcaseId() {
		return testcaseId;
	}

	public String getTestplanName() {
		return testplanName;
	}

	public void setTestplanName(String testplanName) {
		this.testplanName = testplanName;
	}

	/**
	 * 设置：
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * 获取：
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * 设置：
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	/**
	 * 获取：
	 */
	public String getModuleName() {
		return moduleName;
	}
	/**
	 * 设置：
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	/**
	 * 获取：
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * 设置：
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * 获取：
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * 设置：
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * 获取：
	 */
	public String getData() {
		return data;
	}
	/**
	 * 设置：
	 */
	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}
	/**
	 * 获取：
	 */
	public String getExpectedResult() {
		return expectedResult;
	}

    public String getActualResult() {
        return actualResult;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }

    /**
	 * 设置：
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：
	 */
	public String getDescription() {
		return description;
	}

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    /**
	 * 设置：
	 */
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * 获取：
	 */
	public Integer getCreatorId() {
		return creatorId;
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

    public String getTestcaseCode() {
        return testcaseCode;
    }

    public void setTestcaseCode(String testcaseCode) {
        this.testcaseCode = testcaseCode;
    }
}

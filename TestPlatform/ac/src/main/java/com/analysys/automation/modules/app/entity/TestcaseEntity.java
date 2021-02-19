package com.analysys.automation.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-10 14:36:34
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_testcase")
public class TestcaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Integer testcaseId;

	private String testcaseCode;

	private String projectName;

	private String moduleName;

	private String version;

	private String uri;

	private String method;

	private String data;

	private Integer creatorId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Date createTime;

	private Integer updatorId;

	@TableField(fill = FieldFill.UPDATE)
	private Date updateTime;

	private String expectedResult;

	private String description;

	private int status;

}

package com.analysys.automation.modules.app.entity;

import com.analysys.automation.common.validator.group.AddGroup;
import com.analysys.automation.common.validator.group.UpdateGroup;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-17 15:52:18
 */
@Data
@TableName("t_testplan")
public class TestplanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer testplanId;
	/**
	 * 测试计划名称
	 */
    @NotBlank(message = "测试计划名称不能为空", groups= {AddGroup.class, UpdateGroup.class})
	private String testplanName;

    @NotBlank(message = "endpoint不能为空", groups= {AddGroup.class, UpdateGroup.class})
	private String endpoint;

	private String cookie;

	private String progress;

	//测试用例ID，执行单个或批量时使用
	private String testcaseId;

	/**
	 * 项目名称
	 */
    //@NotBlank(message = "项目名称不能为空", groups= {AddGroup.class, UpdateGroup.class})
	private String projectName;
	/**
	 * 模块名称
	 */
	//@NotBlank(message = "模块名称不能为空", groups= {AddGroup.class, UpdateGroup.class})
	private String moduleName;

    /**
     * 测试用例版本
     */
    private String version;


	/**
	 * 状态
     * 10 正常
     * 20 已删除
     * 21 执行中
     * 31 执行完成
     * 32 执行失败
	 */
	private Integer status;


	private Integer creatorId;
	/**
	 * 
	 */
	private Date createTime;

}

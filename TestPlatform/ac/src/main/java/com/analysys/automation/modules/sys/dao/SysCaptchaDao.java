package com.analysys.automation.modules.sys.dao;

import com.analysys.automation.modules.sys.entity.SysCaptchaEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统验证码
 * 
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {
	
}

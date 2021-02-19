package com.analysys.automation.modules.sys.dao;

import com.analysys.automation.modules.sys.entity.SysUserTokenEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Token
 * 
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
@Mapper
public interface SysUserTokenDao extends BaseMapper<SysUserTokenEntity> {
    SysUserTokenEntity queryByToken(String token);

}

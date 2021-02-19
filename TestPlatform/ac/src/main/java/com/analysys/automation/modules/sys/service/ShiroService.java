package com.analysys.automation.modules.sys.service;


import com.analysys.automation.modules.sys.entity.SysUserEntity;
import com.analysys.automation.modules.sys.entity.SysUserTokenEntity;

import java.util.Set;

public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    SysUserTokenEntity queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUserEntity queryUser(Long userId);
}

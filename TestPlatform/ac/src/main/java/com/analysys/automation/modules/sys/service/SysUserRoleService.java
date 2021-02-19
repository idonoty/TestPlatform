package com.analysys.automation.modules.sys.service;

import com.analysys.automation.modules.sys.entity.SysUserRoleEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity> {

    void saveOrUpdate(Long userId, List<Long> roleIdList);

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}


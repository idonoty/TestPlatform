package com.analysys.automation.modules.sys.service;

import com.analysys.automation.modules.sys.entity.SysRoleMenuEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity> {

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}


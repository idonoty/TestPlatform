package com.analysys.automation.modules.sys.service;

import com.analysys.automation.modules.sys.entity.SysMenuEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * 菜单管理
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
public interface SysMenuService extends IService<SysMenuEntity> {

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @param menuIdList  用户菜单ID
     */
    List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    List<SysMenuEntity> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<SysMenuEntity> queryNotButtonList();

    /**
     * 获取用户菜单列表
     */
    List<SysMenuEntity> getUserMenuList(Long userId);

    /**
     * 删除
     */
    void delete(Long menuId);
}


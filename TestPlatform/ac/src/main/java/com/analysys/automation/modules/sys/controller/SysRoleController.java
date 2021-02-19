package com.analysys.automation.modules.sys.controller;

import com.analysys.automation.common.annotation.SysLog;
import com.analysys.automation.common.utils.Constant;
import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.common.validator.ValidatorUtils;
import com.analysys.automation.modules.sys.entity.SysRoleEntity;
import com.analysys.automation.modules.sys.service.SysRoleMenuService;
import com.analysys.automation.modules.sys.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 角色
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
@RestController
@RequestMapping("sys/role")
public class SysRoleController extends AbstractController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 角色列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    public Ret list(@RequestParam Map<String, Object> params){
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
            params.put("create_user_id", getUserId());
        }

        PageUtils page = sysRoleService.queryPage(params);

        return Ret.ok().put("page", page);
    }

    /**
     * 角色列表
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:role:select")
    public Ret select(){
        Map<String, Object> map = new HashMap<>();

        //如果不是超级管理员，则只查询自己所拥有的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
            map.put("create_user_id", getUserId());
        }
        List<SysRoleEntity> list = sysRoleService.selectByMap(map);

        return Ret.ok().put("list", list);
    }

    /**
     * 角色信息
     */
    @GetMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public Ret info(@PathVariable("roleId") Long roleId){
        SysRoleEntity role = sysRoleService.selectById(roleId);

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);

        return Ret.ok().put("role", role);
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    public Ret save(@RequestBody SysRoleEntity role){
        ValidatorUtils.validateEntity(role);

        role.setCreateUserId(getUserId());
        sysRoleService.save(role);

        return Ret.ok();
    }

    /**
     * 修改角色
     */
    @SysLog("修改角色")
    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    public Ret update(@RequestBody SysRoleEntity role){
        ValidatorUtils.validateEntity(role);

        role.setCreateUserId(getUserId());
        sysRoleService.update(role);

        return Ret.ok();
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @PostMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public Ret delete(@RequestBody Long[] roleIds){
        sysRoleService.deleteBatch(roleIds);

        return Ret.ok();
    }
}

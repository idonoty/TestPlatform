package com.analysys.automation.modules.sys.controller;

import com.analysys.automation.common.annotation.SysLog;
import com.analysys.automation.common.utils.Constant;
import com.analysys.automation.common.utils.CryptUtils;
import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.common.validator.Assert;
import com.analysys.automation.common.validator.ValidatorUtils;
import com.analysys.automation.common.validator.group.AddGroup;
import com.analysys.automation.common.validator.group.UpdateGroup;
import com.analysys.automation.modules.sys.entity.SysUserEntity;
import com.analysys.automation.modules.sys.form.PasswordForm;
import com.analysys.automation.modules.sys.service.SysUserRoleService;
import com.analysys.automation.modules.sys.service.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



/**
 * 系统用户
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
@RestController
@RequestMapping("sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;


    /**
     * 所有用户列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    public Ret list(@RequestParam Map<String, Object> params){
        //只有超级管理员，才能查看所有管理员列表
        if(getUserId() != Constant.SUPER_ADMIN){
            params.put("createUserId", getUserId());
        }
        PageUtils page = sysUserService.queryPage(params);

        return Ret.ok().put("page", page);
    }

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    public Ret info(){
        return Ret.ok().put("user", getUser());
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @PostMapping("/password")
    public Ret password(@RequestBody PasswordForm form) throws Exception {
        Assert.isBlank(form.getNewPassword(), "新密码不为能空");

        String initPassword = CryptUtils.aesDecrypt(form.getPassword(), form.getUuid().replaceAll("-", ""));
        String initNewPassword = CryptUtils.aesDecrypt(form.getNewPassword(), form.getUuid().replaceAll("-", ""));

        //sha256加密
        String password = new Sha256Hash(initPassword, getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(initNewPassword, getUser().getSalt()).toHex();


        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if(!flag){
            return Ret.error("原密码不正确");
        }

        return Ret.ok();
    }

    /**
     * 用户信息
     */
    @GetMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public Ret info(@PathVariable("userId") Long userId){
        SysUserEntity user = sysUserService.selectById(userId);

        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return Ret.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    public Ret save(@RequestBody SysUserEntity user){
        ValidatorUtils.validateEntity(user, AddGroup.class);

        user.setCreateUserId(getUserId());
        sysUserService.save(user);

        return Ret.ok();
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    public Ret update(@RequestBody SysUserEntity user){
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        user.setCreateUserId(getUserId());

        sysUserService.update(user);

        return Ret.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @PostMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public Ret delete(@RequestBody Long[] userIds){
        if(ArrayUtils.contains(userIds, 1L)){
            return Ret.error("系统管理员不能删除");
        }

        if(ArrayUtils.contains(userIds, getUserId())){
            return Ret.error("当前用户不能删除");
        }

        sysUserService.deleteBatch(userIds);

        return Ret.ok();
    }
}

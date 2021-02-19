package com.analysys.automation.modules.sys.controller;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.sys.entity.SysOssEntity;
import com.analysys.automation.modules.sys.service.SysOssService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 文件上传
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
@RestController
@RequestMapping("app/oss")
public class SysOssController {
    @Autowired
    private SysOssService sysOssService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("app:sysoss:list")
    public Ret list(@RequestParam Map<String, Object> params){
        PageUtils page = sysOssService.queryPage(params);

        return Ret.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("app:sysoss:info")
    public Ret info(@PathVariable("id") Long id){
			SysOssEntity sysOss = sysOssService.selectById(id);

        return Ret.ok().put("sysOss", sysOss);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("app:sysoss:save")
    public Ret save(@RequestBody SysOssEntity sysOss){
			sysOssService.insert(sysOss);

        return Ret.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("app:sysoss:update")
    public Ret update(@RequestBody SysOssEntity sysOss){
			sysOssService.updateById(sysOss);

        return Ret.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("app:sysoss:delete")
    public Ret delete(@RequestBody Long[] ids){
			sysOssService.deleteBatchIds(Arrays.asList(ids));

        return Ret.ok();
    }

}

package com.analysys.automation.modules.sys.controller;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.sys.service.SysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



/**
 * 系统日志
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
@RestController
@RequestMapping("sys/log")
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:log:list")
    public Ret list(@RequestParam Map<String, Object> params){
        PageUtils page = sysLogService.queryPage(params);

        return Ret.ok().put("page", page);
    }

}

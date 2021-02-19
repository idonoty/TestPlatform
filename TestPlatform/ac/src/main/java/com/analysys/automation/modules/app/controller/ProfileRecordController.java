package com.analysys.automation.modules.app.controller;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.app.entity.ProfileRecordEntity;
import com.analysys.automation.modules.app.service.ProfileRecordService;
import com.analysys.automation.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2019-03-18 15:40:27
 */
@RestController
@RequestMapping("app/profilerecord")
public class ProfileRecordController  extends AbstractController {
    @Autowired
    private ProfileRecordService profileRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("app:profilerecord:list")
    public Ret list(@RequestParam Map<String, Object> params){
        PageUtils page = profileRecordService.queryPage(params);

        return Ret.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{profileRecordId}")
    @RequiresPermissions("app:profilerecord:info")
    public Ret info(@PathVariable("profileRecordId") Integer profileRecordId){
			ProfileRecordEntity profileRecord = profileRecordService.selectById(profileRecordId);

        return Ret.ok().put("profileRecord", profileRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("app:profilerecord:save")
    public Ret save(@RequestBody ProfileRecordEntity profileRecord){
			profileRecordService.insert(profileRecord);

        return Ret.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("app:profilerecord:update")
    public Ret update(@RequestBody ProfileRecordEntity profileRecord){
			profileRecordService.updateById(profileRecord);

        return Ret.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("app:profilerecord:delete")
    public Ret delete(@RequestBody Integer[] profileRecordIds){
			profileRecordService.deleteBatchIds(Arrays.asList(profileRecordIds));

        return Ret.ok();
    }

}

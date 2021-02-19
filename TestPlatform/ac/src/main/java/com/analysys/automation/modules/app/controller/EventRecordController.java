package com.analysys.automation.modules.app.controller;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.app.entity.EventRecordEntity;
import com.analysys.automation.modules.app.service.EventRecordService;
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
@RequestMapping("app/evenrecord")
public class EventRecordController extends AbstractController {
    @Autowired
    private EventRecordService evenrecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("app:evenrecord:list")
    public Ret list(@RequestParam Map<String, Object> params){
        PageUtils page = evenrecordService.queryPage(params);

        return Ret.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{eventRecordId}")
    @RequiresPermissions("app:evenrecord:info")
    public Ret info(@PathVariable("eventRecordId") Integer eventRecordId){
			EventRecordEntity evenrecord = evenrecordService.selectById(eventRecordId);

        return Ret.ok().put("evenrecord", evenrecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("app:evenrecord:save")
    public Ret save(@RequestBody EventRecordEntity evenrecord){
			evenrecordService.insert(evenrecord);

        return Ret.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("app:evenrecord:update")
    public Ret update(@RequestBody EventRecordEntity evenrecord){
			evenrecordService.updateById(evenrecord);

        return Ret.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("app:evenrecord:delete")
    public Ret delete(@RequestBody Integer[] eventRecordIds){
			evenrecordService.deleteBatchIds(Arrays.asList(eventRecordIds));

        return Ret.ok();
    }

}

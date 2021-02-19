package com.analysys.automation.modules.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.analysys.automation.common.annotation.SysLog;
import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.common.validator.ValidatorUtils;
import com.analysys.automation.common.validator.group.AddGroup;
import com.analysys.automation.modules.app.entity.TestplanEntity;
import com.analysys.automation.modules.app.service.TestplanService;
import com.analysys.automation.modules.sys.controller.AbstractController;
import com.analysys.automation.test.Select;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-17 15:52:18
 */
@RestController
@RequestMapping("app/testplan")
public class TestplanController extends AbstractController {
    @Autowired
    private TestplanService testplanService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("app:testplan:list")
    public Ret list(@RequestParam Map<String, Object> params) {
        PageUtils page = testplanService.queryPage(params);

        return Ret.ok().put("page", page);
    }

    @RequestMapping("/sonlist")
    @RequiresPermissions("app:testplan:sonlist")
    public Ret sonlist(@RequestParam Map<String, Object> params) {
        PageUtils page = Select.selectTest(params);
        return Ret.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{testplanId}")
    @RequiresPermissions("app:testplan:info")
    public Ret info(@PathVariable("testplanId") Integer testplanId) {
        TestplanEntity testplan = testplanService.selectById(testplanId);
        return Ret.ok().put("testplan", testplan);
    }

    /**
     * 保存
     */
    @SysLog("创建测试计划")
    @RequestMapping("/save")
    @RequiresPermissions("app:testplan:save")
    public Ret save(@RequestBody TestplanEntity testplan) {
        testplanService.insert(testplan);
        return Ret.ok();
    }

    /**
     * 保存
     */
    @SysLog("创建多个测试计划")
    @RequestMapping("/savelist")
    @RequiresPermissions("app:testplan:save")
    public Ret save(@RequestBody String jsonString) {
        JSONObject object = JSON.parseObject(jsonString);
        List<TestplanEntity> list = JSONObject.parseArray(object.get("dataList").toString(), TestplanEntity.class);

        list.forEach(testplanEntity -> ValidatorUtils.validateEntity(testplanEntity, AddGroup.class));

        testplanService.insertBatch(list);

        return Ret.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("app:testplan:update")
    public Ret update(@RequestBody String jsonString) {
        JSONObject object = JSON.parseObject(jsonString);
        List<TestplanEntity> list = JSONObject.parseArray(object.get("dataList").toString(), TestplanEntity.class);
        TestplanEntity testplan = list.get(0);
        testplanService.updateById(testplan);
        return Ret.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("app:testplan:delete")
    public Ret delete(@RequestBody Integer[] testplanIds) {
        testplanService.deleteBatchIds(Arrays.asList(testplanIds));

        return Ret.ok();
    }

}

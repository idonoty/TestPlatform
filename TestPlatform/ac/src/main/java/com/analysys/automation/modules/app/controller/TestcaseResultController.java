package com.analysys.automation.modules.app.controller;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.app.entity.TestcaseResultEntity;
import com.analysys.automation.modules.app.service.TestcaseResultService;
import com.analysys.automation.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-16 15:09:51
 */
@RestController
@RequestMapping("app/testcaseresult")
public class TestcaseResultController extends AbstractController {
    @Autowired
    private TestcaseResultService testcaseResultService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("app:testcaseresult:list")
    public Ret list(@RequestParam Map<String, Object> params) {
        PageUtils page = testcaseResultService.queryPage(params);

        return Ret.ok().put("page", page);
    }
//
//
//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{testcaseResultId}")
//    @RequiresPermissions("app:testcaseresult:info")
//    public Ret info(@PathVariable("testcaseResultId") Integer testcaseResultId) {
//        TestcaseResultEntity testcaseResult = testcaseResultService.selectById(testcaseResultId);
//
//        return Ret.ok().put("testcaseResult", testcaseResult);
//    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("app:testcaseresult:save")
    public Ret save(@RequestBody TestcaseResultEntity testcaseResult) {
        testcaseResult.setCreateTime(new Date());
        testcaseResult.setCreatorId(-1);
        testcaseResultService.insert(testcaseResult);

        return Ret.ok();
    }

//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    @RequiresPermissions("app:testcaseresult:update")
//    public Ret update(@RequestBody TestcaseResultEntity testcaseResult) {
//        testcaseResultService.updateById(testcaseResult);
//
//        return Ret.ok();
//    }
//
    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("app:testcaseresult:delete")
    public Ret delete(@RequestBody Integer[] testcaseResultIds) {
        testcaseResultService.deleteBatchIds(Arrays.asList(testcaseResultIds));

        return Ret.ok();
    }

}

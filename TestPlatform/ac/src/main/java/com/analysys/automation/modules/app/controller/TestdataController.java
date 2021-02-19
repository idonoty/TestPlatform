package com.analysys.automation.modules.app.controller;

import com.analysys.automation.common.annotation.SysLog;
import com.analysys.automation.common.utils.Constant;
import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.app.entity.TestdataEntity;
import com.analysys.automation.modules.app.entity.TestdataUpEntity;
import com.analysys.automation.modules.app.service.TestdataService;
import com.analysys.automation.modules.app.service.TestdataUpService;
import com.analysys.automation.modules.sys.controller.AbstractController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.SneakyThrows;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.analysys.automation.modules.app.utils.DataUp.dataup;
import static com.analysys.automation.modules.app.utils.DataUp.dataupAlias;


/**
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-10 14:36:34
 */
@RestController
@RequestMapping("app/testdata")
public class TestdataController extends AbstractController {
    @Autowired
    private TestdataService testdataService;

    @Autowired
    private TestdataUpService testdataUpService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("app:testdata:list")
    public Ret list(@RequestParam Map<String, Object> params) {
        PageUtils page = testdataService.queryPage(params);
        return Ret.ok().put("page", page);
    }

    @RequestMapping("/up/list")
    @RequiresPermissions("app:testdata:list")
    public Ret uplist(@RequestParam Map<String, Object> params) {
        PageUtils page = testdataUpService.queryPage(params);
        return Ret.ok().put("page", page);
    }

    @RequestMapping("/delete")
    @RequiresPermissions("app:testdata:delete")
    public Ret delete(@RequestBody Integer[] testdataupIds) {
        testdataUpService.deleteBatchIds(Arrays.asList(testdataupIds));
        return Ret.ok();
    }
    //造数
    @RequestMapping("/updata")
    @RequiresPermissions("app:testdata:updata")
    public Ret postUpData(@RequestBody TestdataUpEntity testdataup) {
        List<TestdataUpEntity> runningList = testdataUpService.selectList(new EntityWrapper<TestdataUpEntity>().eq("status",
                21));
        //如果已经有running状态的job，不再运行新的任务。
        if (runningList.size() >= 2) {
            return Ret.error("当前已有在执行中的上数任务,请等待当前任务执行结束");
        } else {
            try{
                testdataup.setCreate_time(new Date());
                testdataup.setStatus(21);
                testdataUpService.insert(testdataup);
                logger.info("开始造数");
                int loopNum = testdataup.getLoopNum();
                System.out.println(testdataup.getAlias());
                if (testdataup.getAlias()!=null&&testdataup.getAlias().equals("false")){
                    for (int i = 0; i < loopNum; i++) {
                        new Thread("" + i) {
                            @SneakyThrows
                            public void run() {
                                dataup(testdataup);
                                //执行上数结束后，将状态修改
                                testdataup.setStatus(31);
                                testdataup.setEnd_time(new Date());
                                testdataUpService.updateById(testdataup);
                                logger.info("修改造数状态成功");
                            }
                        }.start();
                    }
                }else{
                    for (int i = 0; i < loopNum; i++) {
                        new Thread("" + i) {
                            @SneakyThrows
                            public void run() {
                                dataupAlias(testdataup);
                                //执行上数结束后，将状态修改
                                testdataup.setStatus(31);
                                testdataup.setEnd_time(new Date());
                                testdataUpService.updateById(testdataup);
                                logger.info("修改造数状态成功");
                            }
                        }.start();
                    }
                }
                return Ret.ok("成功");
            }catch (Exception e){
                e.printStackTrace();
                return Ret.error(e.toString());
            }
        }
    }

    /**
     * 保存
     */
    @SysLog("保存测试数据场景")
    @RequestMapping("/save")
    @RequiresPermissions("app:testdata:save")
    public Ret save(@RequestBody TestdataEntity testdata) {
        testdata.setStatus(Constant.Status.NORMAL.getValue());
        testdata.setCreateUserId(getUserId().intValue());
        testdata.setCreateTime(new Date());
        testdataService.insert(testdata);
        return Ret.ok();
    }

    @RequestMapping("/grouplist")
    @RequiresPermissions("app:testdata:grouplist")
    public Ret getGroupList() {
        return Ret.ok().put("list", testdataService.queryGroupNameList());
    }

}

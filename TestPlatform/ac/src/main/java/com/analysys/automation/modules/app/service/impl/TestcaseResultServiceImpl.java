package com.analysys.automation.modules.app.service.impl;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Query;
import com.analysys.automation.modules.app.dao.TestcaseResultDao;
import com.analysys.automation.modules.app.entity.TestcaseResultEntity;
import com.analysys.automation.modules.app.service.TestcaseResultService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("testcaseResultService")
public class TestcaseResultServiceImpl extends ServiceImpl<TestcaseResultDao, TestcaseResultEntity> implements TestcaseResultService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Wrapper<TestcaseResultEntity> wrapper = new EntityWrapper();
        wrapper = requestMapToWrapper(params, wrapper);
        //wrapper.orderBy("testplan_name, testcase_id, project_name, module_name");
        wrapper.orderBy("create_time", false);
        Page<TestcaseResultEntity> page = this.selectPage(
                new Query<TestcaseResultEntity>(params).getPage(),
                wrapper
        );

        return new PageUtils(page);
    }

    private Wrapper requestMapToWrapper(Map<String, Object> params, Wrapper wrapper) {
        if(params.containsKey("projectName") && StringUtils.isNotBlank(params.get("projectName").toString())) {
            wrapper.eq("project_name", params.get("projectName").toString());
        }
        if(params.containsKey("moduleName") && StringUtils.isNotBlank(params.get("moduleName").toString())) {
            wrapper.eq("module_name", params.get("moduleName").toString());
        }
        if(params.containsKey("testplanName") && StringUtils.isNotBlank(params.get("testplanName").toString())) {
            wrapper.eq("testplan_name", params.get("testplanName").toString());
        }
        if(params.containsKey("result") && StringUtils.isNotBlank(params.get("result").toString())) {
            wrapper.eq("result", params.get("result").toString());
        }
        return wrapper;
    }
}

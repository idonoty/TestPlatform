package com.analysys.automation.modules.app.service.impl;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Query;
import com.analysys.automation.modules.app.dao.TestplanDao;
import com.analysys.automation.modules.app.entity.TestplanEntity;
import com.analysys.automation.modules.app.service.TestplanService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("testplanService")
public class TestplanServiceImpl extends ServiceImpl<TestplanDao, TestplanEntity> implements TestplanService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Wrapper<TestplanEntity> wrapper =  new EntityWrapper<>();
        wrapper = requestMapToWrapper(params, wrapper);
        wrapper.orderBy("create_time", false);
        Page<TestplanEntity> page = this.selectPage(
                new Query<TestplanEntity>(params).getPage(),
                wrapper
        );

        return new PageUtils(page);
    }

    private Wrapper requestMapToWrapper(Map<String, Object> params, Wrapper wrapper) {
        if(params.containsKey("project_name") && StringUtils.isNotBlank(params.get("project_name").toString())) {
            wrapper.eq("project_name", params.get("project_name").toString());
        }
        if(params.containsKey("module_name") && StringUtils.isNotBlank(params.get("module_name").toString())) {
            wrapper.eq("module_name", params.get("module_name").toString());
        }
        if(params.containsKey("version") && StringUtils.isNotBlank(params.get("version").toString())) {
            wrapper.eq("version", params.get("version").toString());
        }
        if(params.containsKey("status") && StringUtils.isNotBlank(params.get("status").toString())) {
            wrapper.eq("status", Integer.parseInt(params.get("status").toString()));
        }
        if(params.containsKey("testplan_name") && StringUtils.isNotBlank(params.get("testplan_name").toString())) {
            wrapper.eq("testplan_name", params.get("testplan_name").toString());
        }
        return wrapper;
    }

}

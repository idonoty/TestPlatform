package com.analysys.automation.modules.app.service.impl;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Query;
import com.analysys.automation.modules.app.dao.TestcaseDao;
import com.analysys.automation.modules.app.entity.TestcaseEntity;
import com.analysys.automation.modules.app.service.TestcaseService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("testcaseService")
public class TestcaseServiceImpl extends ServiceImpl<TestcaseDao, TestcaseEntity> implements TestcaseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Wrapper<TestcaseEntity> wrapper = new EntityWrapper();
        wrapper = requestMapToWrapper(params, wrapper);
        wrapper.orderBy("version, project_name, module_name, testcase_code", false);
        Page<TestcaseEntity> page = this.selectPage(
                new Query<TestcaseEntity>(params).getPage(),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<TestcaseEntity> queryList(Map<String, Object> params) {
        Wrapper<TestcaseEntity> wrapper = new EntityWrapper();
        wrapper = requestMapToWrapper(params, wrapper);
        wrapper.orderBy("version,project_name,module_name", false);

        return selectList(wrapper);
    }

    @Override
    public List<String> queryAllProjects() {
        Wrapper<TestcaseEntity> wrapper = new EntityWrapper();
        wrapper.setSqlSelect("distinct project_name");
        List<String> projectNames = new ArrayList<>();
        selectList(wrapper).forEach(e -> projectNames.add(e.getProjectName()));
        return projectNames;
    }

    @Override
    public List<String> queryModuleNames(String projectName) {
        Wrapper<TestcaseEntity> wrapper = new EntityWrapper();
        if(projectName != null && StringUtils.isNotBlank(projectName))
            wrapper.eq("project_name", projectName);
        wrapper.setSqlSelect("distinct module_name");
        List<String> moduleNames = new ArrayList<>();
        selectList(wrapper).forEach(e -> moduleNames.add(e.getModuleName()));
        return moduleNames;
    }

    @Override
    public List<String> queryVersions(String projectName) {
        Wrapper<TestcaseEntity> wrapper = new EntityWrapper();
        if(projectName != null && StringUtils.isNotBlank(projectName))
            wrapper.eq("project_name", projectName);
        wrapper.setSqlSelect("distinct version");
        List<String> versions = new ArrayList<>();
        selectList(wrapper).forEach(e -> versions.add(e.getVersion()));
        return versions;
    }

    private Wrapper requestMapToWrapper(Map<String, Object> params, Wrapper wrapper) {
        if(params.containsKey("projectName") && StringUtils.isNotBlank(params.get("projectName").toString())) {
            wrapper.eq("project_name", params.get("projectName").toString());
        }
        if(params.containsKey("moduleName") && StringUtils.isNotBlank(params.get("moduleName").toString())) {
            wrapper.eq("module_name", params.get("moduleName").toString());
        }
        if(params.containsKey("version") && StringUtils.isNotBlank(params.get("version").toString())) {
            wrapper.eq("version", params.get("version").toString());
        }
        return wrapper;
    }

}

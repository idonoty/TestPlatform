package com.analysys.automation.modules.app.service;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.modules.app.entity.TestcaseEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-10 14:36:34
 */
public interface TestcaseService extends IService<TestcaseEntity> {
    PageUtils queryPage(Map<String, Object> params);

    List<TestcaseEntity> queryList(Map<String, Object> params);

    List<String> queryAllProjects();

    List<String> queryModuleNames(String projectName);

    List<String> queryVersions(String projectName);
}


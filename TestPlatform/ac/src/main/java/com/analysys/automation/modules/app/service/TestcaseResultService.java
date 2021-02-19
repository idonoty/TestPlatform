package com.analysys.automation.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.modules.app.entity.TestcaseResultEntity;

import java.util.Map;

/**
 * 
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-16 15:09:51
 */
public interface TestcaseResultService extends IService<TestcaseResultEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


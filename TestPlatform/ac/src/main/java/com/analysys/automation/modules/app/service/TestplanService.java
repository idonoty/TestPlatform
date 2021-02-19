package com.analysys.automation.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.modules.app.entity.TestplanEntity;

import java.util.Map;

/**
 * 
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-17 15:52:18
 */
public interface TestplanService extends IService<TestplanEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


package com.analysys.automation.modules.app.service;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.modules.app.entity.TestdataEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-10 14:36:34
 */
public interface TestdataService extends IService<TestdataEntity> {
    PageUtils queryPage(Map<String, Object> params);

    List<String> queryGroupNameList();

    List<Date> queryDateList(String groupName);
}


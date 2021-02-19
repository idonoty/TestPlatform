package com.analysys.automation.modules.app.service;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.modules.app.entity.TestdataUpEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestdataUpService extends IService<TestdataUpEntity> {
    PageUtils queryPage(Map<String, Object> params);

    List<String> queryGroupNameList();

    List<Date> queryDateList(String groupName);
}

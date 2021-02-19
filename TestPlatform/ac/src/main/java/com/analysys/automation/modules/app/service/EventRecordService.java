package com.analysys.automation.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.modules.app.entity.EventRecordEntity;

import java.util.Map;

/**
 * 
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2019-03-18 15:40:27
 */
public interface EventRecordService extends IService<EventRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void sendRequests(String groupName, String url, String appKey, int offset, int threadNumber) throws InterruptedException;
}


package com.analysys.automation.modules.app.service;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.modules.app.entity.ProfileLogEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2019-03-20 09:32:20
 */
public interface ProfileLogService extends IService<ProfileLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ProfileLogEntity> queryProfileLogByStatus(int status);
}


package com.analysys.automation.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.modules.sys.entity.SysOssEntity;

import java.util.Map;

/**
 * 文件上传
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
public interface SysOssService extends IService<SysOssEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


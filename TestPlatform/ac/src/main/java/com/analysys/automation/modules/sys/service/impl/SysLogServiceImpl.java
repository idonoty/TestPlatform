package com.analysys.automation.modules.sys.service.impl;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Query;
import com.analysys.automation.modules.sys.dao.SysLogDao;
import com.analysys.automation.modules.sys.entity.SysLogEntity;
import com.analysys.automation.modules.sys.service.SysLogService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String)params.get("key");

        Page<SysLogEntity> page = this.selectPage(
                new Query<SysLogEntity>(params).getPage(),
                new EntityWrapper<SysLogEntity>().like(StringUtils.isNotBlank(key),"username", key)
        );

        return new PageUtils(page);
    }

}

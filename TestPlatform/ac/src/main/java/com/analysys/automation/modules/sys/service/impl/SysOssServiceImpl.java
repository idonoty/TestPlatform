package com.analysys.automation.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Query;

import com.analysys.automation.modules.sys.dao.SysOssDao;
import com.analysys.automation.modules.sys.entity.SysOssEntity;
import com.analysys.automation.modules.sys.service.SysOssService;


@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SysOssEntity> page = this.selectPage(
                new Query<SysOssEntity>(params).getPage(),
                new EntityWrapper<SysOssEntity>()
        );

        return new PageUtils(page);
    }

}

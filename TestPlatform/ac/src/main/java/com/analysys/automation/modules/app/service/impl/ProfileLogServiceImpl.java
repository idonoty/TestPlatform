package com.analysys.automation.modules.app.service.impl;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Query;
import com.analysys.automation.modules.app.dao.ProfileLogDao;
import com.analysys.automation.modules.app.entity.ProfileLogEntity;
import com.analysys.automation.modules.app.service.ProfileLogService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("profileLogService")
public class ProfileLogServiceImpl extends ServiceImpl<ProfileLogDao, ProfileLogEntity> implements ProfileLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ProfileLogEntity> page = this.selectPage(
                new Query<ProfileLogEntity>(params).getPage(),
                new EntityWrapper<ProfileLogEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ProfileLogEntity> queryProfileLogByStatus(int status) {
        return selectList(new EntityWrapper<ProfileLogEntity>().eq("status", status));
    }

}

package com.analysys.automation.modules.app.service.impl;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Query;
import com.analysys.automation.modules.app.dao.TestdataUpDao;
import com.analysys.automation.modules.app.entity.TestdataUpEntity;
import com.analysys.automation.modules.app.service.TestdataUpService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("testdataUpService")
public class TestdataUpServiceImpl extends ServiceImpl<TestdataUpDao, TestdataUpEntity> implements TestdataUpService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Wrapper<TestdataUpEntity> wrapper = new EntityWrapper();
        wrapper.orderBy("create_time", false);

        Page<TestdataUpEntity> page = this.selectPage(
                new Query<TestdataUpEntity>(params).getPage(),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public List<String> queryGroupNameList() {
        return baseMapper.queryGroupNameList();
    }

    @Override
    public List<Date> queryDateList(String groupName) {
        return baseMapper.queryDateList(groupName);
    }
}

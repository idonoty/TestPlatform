package com.analysys.automation.modules.app.dao;

import com.analysys.automation.modules.app.entity.TestdataUpEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface TestdataUpDao extends BaseMapper<TestdataUpEntity> {
        List<String> queryGroupNameList();

        List<Date> queryDateList(String groupName);
        }

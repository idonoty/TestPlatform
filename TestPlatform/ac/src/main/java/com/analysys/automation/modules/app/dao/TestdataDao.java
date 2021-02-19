package com.analysys.automation.modules.app.dao;

import com.analysys.automation.modules.app.entity.TestdataEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-10 14:36:34
 */
@Mapper
public interface TestdataDao extends BaseMapper<TestdataEntity> {
    List<String> queryGroupNameList();

    List<Date> queryDateList(String groupName);
}

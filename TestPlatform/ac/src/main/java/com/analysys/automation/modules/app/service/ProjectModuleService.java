package com.analysys.automation.modules.app.service;

import com.analysys.automation.modules.app.entity.ProjectModuleEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

public interface ProjectModuleService extends IService<ProjectModuleEntity> {
    List<ProjectModuleEntity> queryProjects();
    List<ProjectModuleEntity> queryModules(String projectName);
}

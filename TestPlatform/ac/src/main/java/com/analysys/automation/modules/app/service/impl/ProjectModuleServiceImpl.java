package com.analysys.automation.modules.app.service.impl;

import com.analysys.automation.modules.app.dao.ProjectModuleDao;
import com.analysys.automation.modules.app.entity.ProjectModuleEntity;
import com.analysys.automation.modules.app.service.ProjectModuleService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("projectModuleService")
public class ProjectModuleServiceImpl extends ServiceImpl<ProjectModuleDao, ProjectModuleEntity> implements ProjectModuleService {

    @Override
    public List<ProjectModuleEntity> queryProjects() {
        List<ProjectModuleEntity> list = selectList(null);
        List<String> projectNameList = new ArrayList<>();
        List<ProjectModuleEntity> projects = new ArrayList<>();

        for (ProjectModuleEntity projectModuleEntity : list) {
            if(projectNameList.size() == 0 || !projectNameList.contains(projectModuleEntity.getProjectName())) {
                projectNameList.add(projectModuleEntity.getProjectName());
            }
        }
        projectNameList.forEach(name -> {
            ProjectModuleEntity  projectModuleEntity = new ProjectModuleEntity();
            projectModuleEntity.setProjectName(name);
            projects.add(projectModuleEntity);
        });
        return projects;
    }

    @Override
    public List<ProjectModuleEntity> queryModules(String projectName) {
        return selectList(new EntityWrapper<ProjectModuleEntity>().eq("project_name", projectName));
    }
}

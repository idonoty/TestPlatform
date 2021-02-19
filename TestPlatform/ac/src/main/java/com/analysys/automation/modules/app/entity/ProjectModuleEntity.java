package com.analysys.automation.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("t_project_module")
public class ProjectModuleEntity {
    @TableId
    private int projectModuleId;
    private String projectName;
    private String moduleName;
    private String projectNameEn;
    private String moduleNameEn;

    public int getProjectModuleId() {
        return projectModuleId;
    }

    public void setProjectModuleId(int projectModuleId) {
        this.projectModuleId = projectModuleId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getProjectNameEn() {
        return projectNameEn;
    }

    public void setProjectNameEn(String projectNameEn) {
        this.projectNameEn = projectNameEn;
    }

    public String getModuleNameEn() {
        return moduleNameEn;
    }

    public void setModuleNameEn(String moduleNameEn) {
        this.moduleNameEn = moduleNameEn;
    }
}

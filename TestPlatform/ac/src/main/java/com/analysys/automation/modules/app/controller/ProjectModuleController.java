package com.analysys.automation.modules.app.controller;

import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.app.service.ProjectModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app/pm")
public class ProjectModuleController {

    @Autowired
    private ProjectModuleService projectModuleService;

    @RequestMapping("/queryprojects")
    public Ret queryProjects() {
        return Ret.ok().put("list", projectModuleService.queryProjects());
    }

    @RequestMapping("/querymodules")
    public Ret queryModules(String projectName) {
        return Ret.ok().put("list", projectModuleService.queryModules(projectName));
    }
}

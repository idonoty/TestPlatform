package com.analysys.automation.modules.app.controller;

import com.analysys.automation.common.exception.AutomationException;
import com.analysys.automation.common.utils.Constant;
import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.app.entity.ProfileLogEntity;
import com.analysys.automation.modules.app.service.ProfileLogService;
import com.analysys.automation.modules.sys.controller.AbstractController;
import com.analysys.automation.modules.sys.service.FileService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


/**
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2019-03-20 09:32:20
 */
@RestController
@RequestMapping("app/profilelog")
public class ProfileLogController extends AbstractController {
    @Autowired
    private ProfileLogService profileLogService;

    @Autowired
    private FileService fileService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("app:profilelog:list")
    public Ret list(@RequestParam Map<String, Object> params) {
        PageUtils page = profileLogService.queryPage(params);

        return Ret.ok().put("page", page);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("app:profilelog:save")
    public Ret save(HttpServletRequest request) {
        String groupName = request.getParameter("groupName");
        String profileName = request.getParameter("profileName");
        String eventName = request.getParameter("eventName");
        if(StringUtils.isBlank(groupName) || StringUtils.isBlank(profileName) || StringUtils.isBlank(eventName)) {
            throw new AutomationException("上传参数不能为空");
        }
        ProfileLogEntity profileLogEntity = profileLogService.selectOne(new EntityWrapper<ProfileLogEntity>().eq("group_name", groupName));
        if(profileLogEntity != null) {
            throw new AutomationException("分组名称已经存在");
        }

        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
        MultipartFile profileFile = multipartRequest.getFile("profileFile");
        if(profileFile == null || profileFile.getSize() <= 0) {
            throw new AutomationException("上传的文件不能为空");
        }
        String absoluteProfileName = fileService.upload(profileFile);

        MultipartFile eventFile = multipartRequest.getFile("eventFile");
        if(eventFile == null || eventFile.getSize() <= 0) {
            throw new AutomationException("上传的文件不能为空");
        }
        String absoluteEventName = fileService.upload(eventFile);

        ProfileLogEntity profileLog = new ProfileLogEntity();
        profileLog.setGroupName(groupName);
        profileLog.setProfileName(profileName);
        profileLog.setEventName(eventName);
        profileLog.setProfileDir(absoluteProfileName);
        profileLog.setEventDir(absoluteEventName);
        profileLog.setStatus(Constant.Status.NORMAL.getValue());
        profileLog.setCreateUserId(getUserId().intValue());
        profileLog.setCreateTime(new Date());
        profileLogService.insert(profileLog);
        return Ret.ok();
    }



}

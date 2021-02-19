package com.analysys.automation.modules.sys.service;

import com.analysys.automation.modules.sys.entity.SysCaptchaEntity;
import com.baomidou.mybatisplus.service.IService;

import java.awt.image.BufferedImage;

/**
 * 系统验证码
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
public interface SysCaptchaService extends IService<SysCaptchaEntity> {
    /**
     * 获取图片验证码
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * 验证码效验
     * @param uuid  uuid
     * @param code  验证码
     * @return  true：成功  false：失败
     */
    boolean validate(String uuid, String code);
}


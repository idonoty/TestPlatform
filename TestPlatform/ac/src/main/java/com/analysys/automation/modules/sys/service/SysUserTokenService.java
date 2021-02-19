package com.analysys.automation.modules.sys.service;

import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.sys.entity.SysUserTokenEntity;
import com.baomidou.mybatisplus.service.IService;

/**
 * 系统用户Token
 *
 * @author analysys QA Team
 * @email qa@analysys.com.cn
 * @date 2018-10-09 14:54:51
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

    /**
     * 生成token
     * @param userId  用户ID
     */
    Ret createToken(long userId);

    /**
     * 退出，修改token值
     * @param userId  用户ID
     */
    void logout(long userId);
}


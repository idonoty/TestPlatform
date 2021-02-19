package com.analysys.automation.modules.sys.controller;


import cn.com.analysys.javasdk.AnalysysJavaSdk;
import cn.com.analysys.javasdk.DEBUG;
import cn.com.analysys.javasdk.SyncCollecter;
import com.analysys.automation.common.utils.Constant;
import com.analysys.automation.common.utils.CryptUtils;
import com.analysys.automation.common.utils.Ret;
import com.analysys.automation.modules.sys.entity.SysUserEntity;
import com.analysys.automation.modules.sys.form.SysLoginForm;
import com.analysys.automation.modules.sys.service.SysCaptchaService;
import com.analysys.automation.modules.sys.service.SysUserService;
import com.analysys.automation.modules.sys.service.SysUserTokenService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录相关
 */
@RestController
public class SysLoginController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysCaptchaService sysCaptchaService;

	/**
	 * 验证码
	 */
	@GetMapping("captcha.jpg")
	public void captcha(HttpServletResponse response, String uuid)throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		//获取图片验证码
		BufferedImage image = sysCaptchaService.getCaptcha(uuid);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@PostMapping("/sys/login")
	public Map<String, Object> login(@RequestBody SysLoginForm form) throws Exception {
		boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
		if(!captcha){
			return Ret.error("验证码不正确");
		}

		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(form.getUsername());
        String initPassword = CryptUtils.aesDecrypt(form.getPassword(), form.getUuid().replaceAll("-", ""));
		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(initPassword, user.getSalt()).toHex())) {
			return Ret.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return Ret.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库
		Ret ret = sysUserTokenService.createToken(user.getUserId());
		ret.put("user", user);

		//增加login事件在Java客户端上报
        AnalysysJavaSdk analysys = new AnalysysJavaSdk(new SyncCollecter(Constant.ANALYSYS_SERVICE_URL), Constant.APP_KEY);
		analysys.setDebugMode(DEBUG.OPENANDSAVE);
        boolean isLogin = true;
        String eventName = "login";
        String platform = "Java";
        Map<String, Object> trackPropertie = new HashMap<String, Object>();
        trackPropertie.put("$ip", "112.112.112.112");
        List<String> bookList = new ArrayList<String>();
        trackPropertie.put("userName", user.getUsername());
        trackPropertie.put("level", user.getLevel());
        if(StringUtils.isNotBlank(user.getHobbies())) {
            trackPropertie.put("hobbies", user.getHobbies().split(","));
        }
        analysys.track(String.valueOf(user.getUserId()), isLogin, eventName, trackPropertie, platform);

		return ret;
	}


	/**
	 * 退出
	 */
	@PostMapping("/sys/logout")
	public Ret logout() {
		sysUserTokenService.logout(getUserId());
		return Ret.ok();
	}
	
}

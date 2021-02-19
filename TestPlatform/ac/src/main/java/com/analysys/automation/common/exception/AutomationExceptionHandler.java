package com.analysys.automation.common.exception;

import com.analysys.automation.common.utils.Ret;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class AutomationExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(AutomationException.class)
	public Ret handleRRException(AutomationException e){
		Ret ret = new Ret();
		ret.put("code", e.getCode());
		ret.put("msg", e.getMessage());

		return ret;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public Ret handlerNoFoundException(Exception e) {
		logger.error(e.getMessage(), e);
		return Ret.error(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Ret handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return Ret.error("数据库中已存在该记录");
	}

	@ExceptionHandler(AuthorizationException.class)
	public Ret handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		return Ret.error("没有权限，请联系管理员授权");
	}

	@ExceptionHandler(Exception.class)
	public Ret handleException(Exception e){
		logger.error(e.getMessage(), e);
		return Ret.error();
	}
}

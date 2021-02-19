package com.analysys.automation.common.utils;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class Ret extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public Ret() {
		put("code", 0);
		put("msg", "success");
	}
	
	public static Ret error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static Ret error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}
	
	public static Ret error(int code, String msg) {
		Ret ret = new Ret();
		ret.put("code", code);
		ret.put("msg", msg);
		return ret;
	}

	public static Ret ok(String msg) {
		Ret ret = new Ret();
		ret.put("msg", msg);
		return ret;
	}
	
	public static Ret ok(Map<String, Object> map) {
		Ret ret = new Ret();
		ret.putAll(map);
		return ret;
	}
	
	public static Ret ok() {
		return new Ret();
	}

	public Ret put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}

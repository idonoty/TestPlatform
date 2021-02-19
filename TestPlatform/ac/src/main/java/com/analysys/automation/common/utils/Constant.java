package com.analysys.automation.common.utils;


public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;

    public static final String APP_KEY = "ef48123af739643e";

    public static final String ANALYSYS_SERVICE_URL = "http://192.168.220.102:8089/up";

	/**
	 * 菜单类型
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 状态
     */
    public enum Status {
        NORMAL(10),
        DELETE(20),
        RUNNING(21),
        FINISHED(31),
        OVERWITHERROR(32)
        ;

        private int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}

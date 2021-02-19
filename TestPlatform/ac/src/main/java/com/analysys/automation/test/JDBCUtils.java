package com.analysys.automation.test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCUtils {

	// 获得c3p0连接池对象，不传值用默认，传的话用值里的（和配置文件xml对应）
	private static ComboPooledDataSource ds = new ComboPooledDataSource();

	/**
	 * 获得数据库连接对象
	 *
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	/**
	 * 获得c3p0连接池对象
	 * 
	 * @return
	 */
	public static DataSource getDataSource() {
		return ds;
	}

	public static void main(String[] args) {
		System.out.println("----------" + JDBCUtils.getDataSource());

	}
}

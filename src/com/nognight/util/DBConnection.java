package com.nognight.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	public static Connection getConnection() {
		// 使用的驱动程序类，用于查找驱动程序
		String driver = "oracle.jdbc.driver.OracleDriver"; // 驱动程序类，由厂商提供
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl"; // 数据库URL，格式由厂商规定
		String username = "ycc";
		String password = "123456";

		Connection con = null;
		try {
			Class.forName(driver); // 加载数据库驱动类，但是并不创建对象实例
		} catch (ClassNotFoundException e) {
			System.err.println(e.toString());
		}
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}
}

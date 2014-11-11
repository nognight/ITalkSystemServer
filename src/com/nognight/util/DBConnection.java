package com.nognight.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	public static Connection getConnection() {
		// ʹ�õ����������࣬���ڲ�����������
		String driver = "oracle.jdbc.driver.OracleDriver"; // ���������࣬�ɳ����ṩ
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl"; // ���ݿ�URL����ʽ�ɳ��̹涨
		String username = "ycc";
		String password = "123456";

		Connection con = null;
		try {
			Class.forName(driver); // �������ݿ������࣬���ǲ�����������ʵ��
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

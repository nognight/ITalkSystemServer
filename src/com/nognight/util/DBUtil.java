package com.nognight.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

	//关闭数据库连接
	public static void close(Connection con) {
		if(con != null) {
			try{
				con.close();
			} catch(SQLException e) {
				//
			}
		}
	}

	public static void close(Statement stmt) {
		if(stmt != null) {
			try{
				stmt.close();
			} catch(SQLException e) {
				//
			}
		}
	}

	public static void close(ResultSet rs) {
		if(rs != null) {
			try{
				rs.close();
			} catch(SQLException e) {
				//
			}
		}
	}
}
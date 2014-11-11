package com.nognight.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nognight.entity.Account;
import com.nognight.util.DBConnection;
import com.nognight.util.DBUtil;

public class AccountDAO {
	public int insert(Account account) {
		String sql = "insert into account (username,password) values (?,?)";

		int total = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnection.getConnection();
			// 创建语句和Statement不同
			ps = con.prepareStatement(sql);
			// 赋参数，下标从1开始
			ps.setString(1, account.getUsername());
			ps.setString(2, account.getPassword());
			// 和Statement调用不同，这里没有参数
			total = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 注意关闭顺序
			DBUtil.close(ps);
			DBUtil.close(con);
		}
		return total;
	}

	public int update(Account account) {
		String sql = "update account set password=? where username=?";

		int total = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, account.getPassword());
			ps.setString(2, account.getUsername());

			total = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(ps);
			DBUtil.close(con);
		}
		return total;
	}

	public Account findByUserName(String username) {
		String sql = "select acc_id,username,password from Account where username=?";

		Account account = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, username);

			// 查询执行executeQuery，返回ResultSet
			rs = ps.executeQuery();
			// 获得ResultSet后，默认游标在第一行之前，在此要求先调用next滚动游标到下一行
			if (rs.next()) {
				account = new Account();
				account.setId(rs.getInt("acc_id"));
				account.setUsername(rs.getString("username"));
				account.setPassword(rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(ps);
			DBUtil.close(con);
		}
		return account;
	}
}

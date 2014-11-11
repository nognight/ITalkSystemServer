package com.nognight.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nognight.entity.Friend;
import com.nognight.entity.Message;
import com.nognight.util.DBConnection;
import com.nognight.util.DBUtil;

public class FriendDao {
	// 插入好友列表
	public int insert(Friend friend) {
		int total = 0;
		Connection con = null;
		PreparedStatement ps = null;
		String sql = "insert into friend (acc_id,friendname)value(?,?)";
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, friend.getAcc_id());
			ps.setString(2, friend.getFriendname());
			total = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(ps);
			DBUtil.close(con);
		}

		return total;
	}

	// 升级好友信息（功能未完善暂时无效）
	public int update(Friend friend) {
		int total = 0;
		Connection con = null;
		PreparedStatement ps = null;
		String sql = "update";
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, friend.getAcc_id());
			total = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(ps);
			DBUtil.close(con);
		}

		return total;
	}

	// 删除好友
	public int delect(Friend friend) {
		int total = 0;
		Connection con = null;
		PreparedStatement ps = null;
		String sql = "delect from friend where friendname=?";
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, friend.getFriendname());
			total = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(ps);
			DBUtil.close(con);
		}
		return total;
	}

	// 查看好友列表
	// arraylist+泛型
	public ArrayList<Friend> findFriendList(int acc_id) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Friend> friends = new ArrayList<Friend>();
		String sql = "select friendname from friend where acc_id=?";
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, acc_id);
			rs = ps.executeQuery();
			// 执行循环输出
			while (rs.next()) {
				Friend friend = new Friend();
				friend.setFriendname(rs.getString("friendname"));
				friends.add(friend);

			}
			//测试输出（正确）
			//System.out.print(friends.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(ps);
			DBUtil.close(con);
		}
		return friends;
	}
}

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
	// ��������б�
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

	// ����������Ϣ������δ������ʱ��Ч��
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

	// ɾ������
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

	// �鿴�����б�
	// arraylist+����
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
			// ִ��ѭ�����
			while (rs.next()) {
				Friend friend = new Friend();
				friend.setFriendname(rs.getString("friendname"));
				friends.add(friend);

			}
			//�����������ȷ��
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

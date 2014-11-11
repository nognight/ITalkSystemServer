package com.nognight.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.nognight.entity.Message;
import com.nognight.util.DBConnection;
import com.nognight.util.DBUtil;

public class MessageDAO {
	// ������Ϣ��¼
	public int insert(Message message) {
		String sql = "insert into message (acc_id,messageStr,date,receiver) values (?,?,?,?)";
		int total = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, message.getAcc_id());
			ps.setString(2, message.getMessageStr());
			ps.setDate(3, (Date) message.getDate());// ���ݿ����ʱ��
			ps.setString(4, message.getReceiver());
			// ��Statement���ò�ͬ������û�в���
			total = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBUtil.close(ps);
			DBUtil.close(con);
		}

		return total;

	}

	// ���û�ɾ����Ϣ��¼
	public int deleteByUsername(int acc_id) {
		String sql = "delete from message where acc_id=?";
		int total = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, acc_id);

			total = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBUtil.close(ps);
			DBUtil.close(con);
		}

		return total;
	}

	// ��ʱ��ɾ����¼
	public int deleteByDate(Date date) {
		String sql = "delete from message where date=?";
		int total = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setDate(1, date);

			total = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBUtil.close(ps);
			DBUtil.close(con);
		}

		return total;

	}

	// �û����Ҽ�¼�б�
	public ArrayList<Message> findMessage(int acc_id, String receiver) {
		String sql = "select messageStr,date,receiver from message where acc_id=? and receiver=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Message> messagelList = new ArrayList<Message>();
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, acc_id);
			ps.setString(2, receiver);
			rs = ps.executeQuery();
			// ѭ������
			while (rs.next()) {
				Message message = new Message();
				message.setMessageStr(rs.getString("messageStr"));
				message.setDate(rs.getDate("date"));
				message.setReceiver(rs.getString("receiver"));
				messagelList.add(message);
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBUtil.close(rs);
			DBUtil.close(ps);
			DBUtil.close(con);
		}
		return messagelList;

	}
}

package com.nognight.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nognight.entity.FilePath;
import com.nognight.entity.Message;
import com.nognight.util.DBConnection;
import com.nognight.util.DBUtil;

public class FilePathDAO {
	public int insert(FilePath filePath) {
		String sql = "insert into filepath (acc_id,path) values (?,?)";
		int total = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, filePath.getAcc_id());
			ps.setString(2, filePath.getPath());
			// ps.setString(3, filePath.getReceiver());

			// 和Statement调用不同，这里没有参数
			total = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBUtil.close(ps);
			DBUtil.close(con);
		}

		return total;
	}

	public ArrayList<FilePath> findFiles(int acc_id) {
		String sql = "select path from filepath where acc_id=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<FilePath> filePaths = new ArrayList<FilePath>();
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, acc_id);
			rs = ps.executeQuery();
			// 循环查找
			while (rs.next()) {
				FilePath filePath = new FilePath();
				filePath.setPath(rs.getString("path"));
				// filePath.setReceiver(rs.getString("receiver"));
				filePaths.add(filePath);
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBUtil.close(rs);
			DBUtil.close(ps);
			DBUtil.close(con);
		}
		return filePaths;

	}

}

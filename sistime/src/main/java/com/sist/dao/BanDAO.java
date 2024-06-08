package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class BanDAO {
	private Connection conn = DBConn.getConnection();

	public void insertBan(int ban_date, String ban_reason, String email) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (ban_date == 1) {
				sql = "insert into ban(ban_num , ban_state, ban_date , ban_reason , email ) values"
						+ " ( ban_seq.nextval , 1 , sysdate+1 , ? , ?)";
			} else if (ban_date == 2) {
				sql = "insert into ban(ban_num , ban_state, ban_date , ban_reason , email ) values"
						+ " ( ban_seq.nextval , 1 , sysdate+7 , ? , ?)";
			} else if (ban_date == 3) {
				sql = "insert into ban(ban_num , ban_state, ban_date , ban_reason , email ) values"
						+ " ( ban_seq.nextval , 1 , sysdate+30 , ? , ?)";
			} else {
				sql = "insert into ban(ban_num , ban_state, ban_date , ban_reason , email ) values"
						+ " ( ban_seq.nextval , 1 , '2099-12-31' , ? , ?)";
			}

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, ban_reason);
			pstmt.setString(2, email);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public int findByID(String email) throws SQLException{
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		int banState = 0;
		
		try {
			sql = "select ban_state , ban_date from ban where email = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				banState = rs.getInt("ban_state");
				Timestamp OraBanDate = rs.getTimestamp("ban_date");
				LocalDateTime bandate = OraBanDate.toLocalDateTime();
				if(banState == 1 && bandate.isBefore(LocalDateTime.now())) {
					System.out.println(bandate);
					updateBanState(email);
					banState = 0;
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		return banState;
	}
	
	public void updateBanState(String email) throws SQLException{
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			sql = "update ban set ban_state = 0 where email = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(pstmt);
		}
	}
}

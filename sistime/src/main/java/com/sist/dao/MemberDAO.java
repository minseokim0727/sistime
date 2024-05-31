package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sist.domain.MemberDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();

	public MemberDTO loginMember(String email, String userPwd) {

		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(" SELECT email,user_pwd,user_name,enabled,register_date,nickname,univ_num ");
			sb.append(" FROM member1 ");
			sb.append("WHERE email = ? AND user_pwd = ? AND enabled = 1");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, email);
			pstmt.setString(2, userPwd);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();

				dto.setEmail(rs.getString("email"));
				dto.setUserPwd(rs.getString("user_pwd"));
				dto.setUserName(rs.getString("user_name"));
				dto.setRegister_date(rs.getString("register_date"));
				dto.setNickname(rs.getString("nickname"));
				dto.setUniv_num(Integer.parseInt(rs.getString("univ_num")));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	public void insertMember(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO member1(email"
					+ ", user_Pwd, user_Name, enabled, register_date, nickname,univ_num) VALUES (?, ?, ?, 1,SYSDATE, ?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getEmail());
			pstmt.setString(2, dto.getUserPwd());
			pstmt.setString(3, dto.getUserName());
			pstmt.setString(4, dto.getNickname());
			pstmt.setInt(5, dto.getUniv_num());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO member2(email, birth, tel, zip, addr1, addr2) VALUES (?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, ?, ?)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getEmail());
			pstmt.setString(2, dto.getBirth());
			pstmt.setString(3, dto.getTel());
			pstmt.setString(4, dto.getZip());
			pstmt.setString(5, dto.getAddr1());
			pstmt.setString(6, dto.getAddr2());
			
			pstmt.executeUpdate();
		
			conn.commit();

		} catch (SQLException e) {
			DBUtil.rollback(conn);
			
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e2) {
			}
		}
		
	}
	
	public MemberDTO findByEmail(String email) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.email, user_Name, user_Pwd,");
			sb.append("      enabled, register_date,nickname,univ_num, ");
			sb.append("      birth, ");
			sb.append("       tel,");
			sb.append("      zip, addr1, addr2 ");
			sb.append("  FROM member1 m1");
			sb.append("  JOIN member2 m2 ON m1.email=m2.email ");
			sb.append("  WHERE m1.email = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, email);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setEmail(rs.getString("email"));
				dto.setUserPwd(rs.getString("user_Pwd"));
				dto.setUserName(rs.getString("user_Name"));
				dto.setEnabled(rs.getInt("enabled"));
				dto.setRegister_date(rs.getString("register_date"));
				dto.setNickname(rs.getString("nickname"));
				dto.setUniv_num(rs.getInt("univ_num"));
				dto.setBirth(rs.getString("birth"));
				dto.setTel(rs.getString("tel"));
				dto.setZip(rs.getString("zip"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}	
	
	public MemberDTO findByNickname(String nickname) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.email, user_Name, user_Pwd,");
			sb.append("      enabled, register_date,nickname,univ_num, ");
			sb.append("      birth, ");
			sb.append("       tel,");
			sb.append("      zip, addr1, addr2 ");
			sb.append("  FROM member1 m1");
			sb.append("  JOIN member2 m2 ON m1.email=m2.email ");
			sb.append("  WHERE nickname = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, nickname);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setEmail(rs.getString("email"));
				dto.setUserPwd(rs.getString("user_Pwd"));
				dto.setUserName(rs.getString("user_Name"));
				dto.setEnabled(rs.getInt("enabled"));
				dto.setRegister_date(rs.getString("register_date"));
				dto.setNickname(rs.getString("nickname"));
				dto.setUniv_num(rs.getInt("univ_num"));
				dto.setBirth(rs.getString("birth"));
				dto.setTel(rs.getString("tel"));
				dto.setZip(rs.getString("zip"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}	
	
	
}

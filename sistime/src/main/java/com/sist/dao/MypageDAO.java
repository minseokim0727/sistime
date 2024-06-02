package com.sist.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sist.domain.MemberDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;


//memerDTO를 가져다 씀
public class MypageDAO {
	private Connection conn = DBConn.getConnection();
	
	
	// 이메일 찾기 
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
	// 닉네임 찾기
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
	// 비밀번호 수정
	public void updatePwd(MemberDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update member1 set user_pwd=? where email = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserPwd());
			pstmt.setString(2, dto.getEmail());
			
			pstmt.executeUpdate();			

			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 이름 수정
	public void updateName(MemberDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update member1 set user_name=? where email = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserName());
			pstmt.setString(2, dto.getEmail());
			
			pstmt.executeUpdate();			

			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	// 닉네임 수정
	public void updateNickname(MemberDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update member1 set nickname=? where email = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getNickname());
			pstmt.setString(2, dto.getEmail());
			
			pstmt.executeUpdate();			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	
	// 상세 정보 수정(member2에 대한 정보)
	public void updateMember(MemberDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE member2 SET birth=TO_DATE(?,'YYYY-MM-DD'), tel=?, zip=?, addr1=?, addr2=? WHERE email=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getBirth());
			pstmt.setString(2, dto.getTel());
			pstmt.setString(3, dto.getZip());
			pstmt.setString(4, dto.getAddr1());
			pstmt.setString(5, dto.getAddr2());
			pstmt.setString(6, dto.getEmail());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	// 회원 탈퇴
	public void deleteMember(String email) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE member1 SET enabled=0 WHERE email=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql = "DELETE FROM member2 WHERE email=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
}

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

}

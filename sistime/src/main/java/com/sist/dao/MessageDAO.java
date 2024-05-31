package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.MessageDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class MessageDAO {
	private Connection conn = DBConn.getConnection();

	public List<MessageDTO> listMessage(int offset, int size,String email) {
		List<MessageDTO> list = new ArrayList<MessageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		// email은 nickname으로 바꿔야함 테이블도 전부
		try {
			System.out.println(email);
			//session에서 나의 정보 찾아오기

			sb.append(" SELECT  read_email, content, msg_num, send_date "
			        + "FROM ("
			        + "    SELECT DISTINCT send_email, read_email, content, msg_num, send_date, "
			        + "           ROW_NUMBER() OVER (PARTITION BY read_email ORDER BY send_date DESC) AS rn "
			        + "    FROM message  WHERE send_email = ?  "
			        + ") t "
			        + "WHERE rn = 1 "
			        + "OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, email); // 이 부분에서 read_email로 변경
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
			    MessageDTO dto = new MessageDTO();

			    dto.setContent(rs.getString("content"));
			    dto.setSend_email(rs.getString("read_email")); 
			    dto.setMsg_num(rs.getInt("msg_num"));
			    dto.setSend_date(rs.getString("send_date"));

			    list.add(dto);
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	public List<MessageDTO> listMessage(int offset, int size, String schType, String kwd,String email) {
		List<MessageDTO> list = new ArrayList<MessageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT read_email, content, msg_num, send_date "
					+ "FROM ("
					+ "    SELECT DISTINCT send_email,read_email, content, msg_num, send_date, "
					+ "           ROW_NUMBER() OVER (PARTITION BY read_email ORDER BY send_date DESC) AS rn "
					+ "    FROM message where send_email=?"
					+ ") t "
					+ "WHERE rn = 1; ");
			
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
			}
			sb.append(" ORDER BY msg_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			if (schType.equals("all")) {
				pstmt.setString(1, email);
				pstmt.setString(2, kwd);
				pstmt.setString(3, kwd);
				pstmt.setInt(4, offset);
				pstmt.setInt(5, size);
			} else {
				pstmt.setString(1, email);
				pstmt.setString(2, kwd);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, size);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MessageDTO dto = new MessageDTO();

				dto.setContent(rs.getString("content"));
				dto.setSend_email(rs.getString("read_email"));
				dto.setMsg_num(rs.getInt("msg_num"));
				dto.setSend_date(rs.getString("send_date"));

				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM message ";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;
	}

	// 검색에서의 데이터 개수
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) "
					+ " FROM message m "
					+ " JOIN member1 m1 ON m.send_email = m1.email "
					+ " JOIN member1 m1 ON m.read_email = m1.email ";
			if (schType.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sql += "  WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += "  WHERE INSTR(" + schType + ", ?) >= 1 ";
			}

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, kwd);
			if (schType.equals("all")) {
				pstmt.setString(2, kwd);
			}

			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;
	}

}
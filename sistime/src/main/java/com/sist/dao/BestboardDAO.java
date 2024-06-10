package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.BoardDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class BestboardDAO {
	private Connection conn = DBConn.getConnection();
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM board"
					+ " where hitcount>10 ";
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
	
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) "
					+ " FROM board b "
					+ " JOIN member1 m ON b.email = m.email ";
			if (schType.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 and hitcount>10 ";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sql += "  WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? and hitcount>10 ";
			} else {
				sql += "  WHERE INSTR(" + schType + ", ?) >= 1 and hitcount>10 ";
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
	
	public List<BoardDTO> listbestBoard(int offset, int size) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT b.board_num, user_Name, b.email, title, hitCount, ");
			sb.append("      TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append("      NVL(replyCount, 0) replyCount ");
			sb.append(" FROM board b ");
			sb.append(" JOIN member1 m ON b.email = m.email ");
			sb.append(" LEFT OUTER JOIN ( ");
			sb.append("     SELECT board_num, COUNT(*) replyCount ");
			sb.append("     FROM Board_Reply ");
		//	sb.append("     WHERE answer = 0 ");
			sb.append("     GROUP BY board_num");
			sb.append(" ) c ON b.board_num = c.board_num");
			sb.append(" where hitcount>10 ");
			sb.append(" ORDER BY b.board_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BoardDTO dto = new BoardDTO();

				dto.setBoard_num(rs.getLong("Board_num"));
				dto.setEmail(rs.getString("email"));
				dto.setTitle(rs.getString("title"));
				dto.setHitcount(rs.getInt("hitcount"));
				dto.setReg_date(rs.getString("reg_date"));

				// dto.setReplyCount(rs.getInt("replyCount"));
				
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
	public List<BoardDTO> listbestBoard(int offset, int size, String schType, String kwd) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT b.board_num, user_Name, b.email, title, hitCount, ");
			sb.append("      TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append("      NVL(replyCount, 0) replyCount ");
			sb.append(" FROM board b ");
			sb.append(" JOIN member1 m ON b.email = m.email ");
			sb.append(" LEFT OUTER JOIN ( ");
			sb.append("     SELECT board_num, COUNT(*) replyCount ");
			sb.append("     FROM board_Reply ");
		//	sb.append("     WHERE answer=0 ");
			sb.append("     GROUP BY board_num");
			sb.append(" ) c ON b.board_num = c.board_num");
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(thitle, ?) >= 1 OR INSTR(content, ?) >= 1 and hitcount>10 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? and hitcount>10 ");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1  and hitcount>10 ");
			}
			sb.append(" ORDER BY b.board_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			if (schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setString(2, kwd);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, size);
			} else {
				pstmt.setString(1, kwd);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}

			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BoardDTO dto = new BoardDTO();

				dto.setBoard_num(rs.getLong("Board_num"));
				dto.setEmail(rs.getString("email"));
				dto.setTitle(rs.getString("title"));
				dto.setHitcount(rs.getInt("hitcount"));
				dto.setReg_date(rs.getString("reg_date"));
				
				// dto.setReplyCount(rs.getInt("replyCount"));
				
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
	
	
	
	
	
	
	
}

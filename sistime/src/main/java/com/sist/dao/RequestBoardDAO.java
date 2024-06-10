package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.RequestBoardDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class RequestBoardDAO {
	private Connection conn = DBConn.getConnection();
	
	
	public void insertRequestBoard(RequestBoardDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO RequestBoard(RB_num, title, content,reason, secret, reg_date, email,answer_content,answer_reg_date) "
					+ " VALUES (RequestBoard_seq.NEXTVAL, ?, ?,?, ?,SYSDATE, ?, ?,?)";
			
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getReason());
			
			pstmt.setInt(4, dto.getSecret());
			pstmt.setString(5, dto.getEmail());
			pstmt.setString(6, dto.getAnswer_content());
			pstmt.setString(7, dto.getAnswer_reg_date());
			
			pstmt.executeUpdate();
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;

		}finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM RequestBoard";
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
	public int dataCount(String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) "
					+ " FROM RequestBoard "
					+ " WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 OR INSTR(answer_content, ?) >= 1 ";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, kwd);
			pstmt.setString(2, kwd);
			pstmt.setString(3, kwd);

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
	
	
	// 게시물 리스트
	public List<RequestBoardDTO> listRequestBoard(int offset, int size) {
		List<RequestBoardDTO> list = new ArrayList<RequestBoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT RB_num, secret, nickname, title, answer_content, m.email, ");
			sb.append("       TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM RequestBoard q ");
			sb.append(" JOIN member1 m ON q.email = m.email ");
			sb.append(" ORDER BY RB_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
					
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();
					
			while (rs.next()) {
				RequestBoardDTO dto = new RequestBoardDTO();
						
				dto.setRB_num(rs.getLong("RB_num"));
				dto.setSecret(rs.getInt("secret"));
				dto.setEmail(rs.getString("email"));
				dto.setNickname(rs.getString("nickname"));        
		        dto.setTitle(rs.getString("title"));    
		        dto.setReg_date(rs.getString("reg_date"));
		        dto.setAnswer_content(rs.getString("answer_content"));

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
	// 글보기
	public RequestBoardDTO findById(long RB_num) {
		RequestBoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql= "select RB_num, secret, nickname,title,content, reg_date, answer_content , ANSWER_REG_DATE, m.email, reason "
					+ " from RequestBoard q"
					+ " JOIN member1 m ON q.email = m.email "
					+ " where RB_num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, RB_num);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new RequestBoardDTO();
				
				
				dto.setRB_num(rs.getLong("RB_num"));
				dto.setEmail(rs.getString("email"));
				dto.setSecret(rs.getInt("secret"));
				dto.setNickname(rs.getString("nickname"));        
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setAnswer_content(rs.getString("answer_content"));  
                dto.setAnswer_reg_date(rs.getString("answer_reg_date"));
                dto.setReason(rs.getString("reason"));
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	// 글 수정
	public void updateRequestBoard(RequestBoardDTO dto) throws SQLException { 
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE RequestBoard SET title=?, secret=?, content=? WHERE RB_num=? AND email=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setInt(2, dto.getSecret());
			pstmt.setString(3, dto.getContent());
			pstmt.setLong(4, dto.getRB_num());
			pstmt.setString(5, dto.getEmail());
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(pstmt);
		}
	}
	// 답글 달기
	public void updateAnswer(RequestBoardDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE RequestBoard SET answer_content=?,  ";
			if(dto.getAnswer_content().length() == 0) {
				sql += " answer_reg_date=NULL ";
			} else {
				sql += " answer_reg_date=SYSDATE ";
			}
			
			sql += " WHERE RB_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getAnswer_content());
			
			pstmt.setLong(2, dto.getRB_num());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(pstmt);
		}
	}
	// 게시물 삭제
	public void deleteRequestBoard(long num, String email) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		try {
			if (email.equals("admin")) {
				sql = "DELETE FROM RequestBoard WHERE RB_num=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				
				pstmt.executeUpdate();
			} else {
				sql = "DELETE FROM RequestBoard WHERE RB_num=? AND email=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				pstmt.setString(2, email);
				
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	// 이전글
	public RequestBoardDTO findByPrev(long num, String kwd) {
		RequestBoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT RB_num, secret, title, email ");
				sb.append(" FROM RequestBoard ");
				sb.append(" WHERE ( RB_num > ? ) ");
				sb.append("     AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 OR INSTR(answer_content, ?) >= 1) ");
				sb.append(" ORDER BY RB_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				pstmt.setString(3, kwd);
				pstmt.setString(4, kwd);
			} else {
				sb.append(" SELECT RB_num, secret, title, email ");
				sb.append(" FROM RequestBoard ");
				sb.append(" WHERE RB_num > ? ");
				sb.append(" ORDER BY RB_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new RequestBoardDTO();
				
				dto.setRB_num(rs.getLong("RB_num"));
				dto.setSecret(rs.getInt("secret"));
				dto.setEmail(rs.getString("email"));
				dto.setTitle(rs.getString("title"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	// 다음글
	public RequestBoardDTO findByNext(long num, String kwd) {
		RequestBoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT RB_num, secret, title, email ");
				sb.append(" FROM RequestBoard ");
				sb.append(" WHERE ( RB_num < ? ) ");
				sb.append("     AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 OR INSTR(answer_content, ?) >= 1) ");
				sb.append(" ORDER BY RB_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				pstmt.setString(3, kwd);
				pstmt.setString(4, kwd);
			} else {
				sb.append(" SELECT RB_num, secret, title, email ");
				sb.append(" FROM RequestBoard ");
				sb.append(" WHERE RB_num < ? ");
				sb.append(" ORDER BY RB_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
			}
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				dto = new RequestBoardDTO();
				
				dto.setRB_num(rs.getLong("RB_num"));
				dto.setSecret(rs.getInt("secret"));
				dto.setEmail(rs.getString("email"));
				dto.setTitle(rs.getString("title"));
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

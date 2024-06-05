package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.QnaDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class QnaDAO {
	private Connection conn = DBConn.getConnection();
	
	// 데이터 추가
	public void insertQuestion(QnaDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO qna(qna_num, title, content, reg_date, secret, email,answer_content,answer_reg_date) "
					+ " VALUES (qna_seq.NEXTVAL, ?, ?, SYSDATE, ?, ?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getSecret());
			pstmt.setString(4, dto.getEmail());
			pstmt.setString(5, dto.getAnswer_content());
			pstmt.setString(6, dto.getAnswer_reg_date());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	// 데이터 개수
		public int dataCount() {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = "SELECT NVL(COUNT(*), 0) FROM qna";
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
						+ " FROM qna "
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
		public List<QnaDTO> listQuestion(int offset, int size) {
			List<QnaDTO> list = new ArrayList<QnaDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				sb.append(" SELECT qna_num, secret, user_Name, title, answer_content, m.email, ");
				sb.append("       TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
				sb.append(" FROM qna q ");
				sb.append(" JOIN member1 m ON q.email = m.email ");
				sb.append(" ORDER BY qna_num DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, offset);
				pstmt.setInt(2, size);

				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					QnaDTO dto = new QnaDTO();
					
					dto.setQna_num(rs.getLong("qna_num"));
					dto.setSecret(rs.getInt("secret"));
					dto.setEmail(rs.getString("email"));
	                dto.setUserName(rs.getString("user_name"));
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
		// 글 보기
		public QnaDTO findById(long qna_num) {
			QnaDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = "SELECT qna_num, secret, user_Name, title, content, reg_date,  "
						+ " answer_content , ANSWER_REG_DATE, m.email  "
						+ " FROM qna q "
						+ " JOIN member1 m ON q.email = m.email  "
						+ " WHERE qna_num = ? ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, qna_num);

				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new QnaDTO();
					
					
					dto.setQna_num(rs.getLong("qna_num"));
					dto.setEmail(rs.getString("email"));
					dto.setSecret(rs.getInt("secret"));
					dto.setUserName(rs.getString("user_Name"));
					dto.setTitle(rs.getString("title"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("reg_date"));
					dto.setAnswer_content(rs.getString("answer_content"));  
	                dto.setAnswer_reg_date(rs.getString("answer_reg_date"));
				}
				System.out.println();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return dto;
	}
	
	// 게시물 수정
		public void updateQuestion(QnaDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;

			try {
				sql = "UPDATE qna SET title=?, secret=?, content=? WHERE qna_num=? AND email=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getTitle());
				pstmt.setInt(2, dto.getSecret());
				pstmt.setString(3, dto.getContent());
				pstmt.setLong(4, dto.getQna_num());
				pstmt.setString(5, dto.getEmail());
				
				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		
		}
		
		// 답글 달기
		public void updateAnswer(QnaDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;

			try {
				sql = "UPDATE qna SET answer_content=?,  ";
				if(dto.getAnswer_content().length() == 0) {
					sql += " answer_reg_date=NULL ";
				} else {
					sql += " answer_reg_date=SYSDATE ";
				}
				sql += " WHERE qna_num = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getAnswer_content());
				
				pstmt.setLong(2, dto.getQna_num());
				
				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		}
		
		// 게시물 삭제
		public void deleteQuestion(long num, String email) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;

			try {
				if (email.equals("admin")) {
					sql = "DELETE FROM qna WHERE qna_num=?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, num);
					
					pstmt.executeUpdate();
				} else {
					sql = "DELETE FROM qna WHERE qna_num=? AND email=?";
					
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
		public QnaDTO findByPrev(long num, String kwd) {
			QnaDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				if (kwd != null && kwd.length() != 0) {
					sb.append(" SELECT qna_num, secret, title, email ");
					sb.append(" FROM qna ");
					sb.append(" WHERE ( qna_num > ? ) ");
					sb.append("     AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 OR INSTR(answer_content, ?) >= 1) ");
					sb.append(" ORDER BY qna_num ASC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");

					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
					pstmt.setString(2, kwd);
					pstmt.setString(3, kwd);
					pstmt.setString(4, kwd);
				} else {
					sb.append(" SELECT qna_num, secret, title, email ");
					sb.append(" FROM qna ");
					sb.append(" WHERE qna_num > ? ");
					sb.append(" ORDER BY qna_num ASC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");

					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
				}

				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new QnaDTO();
					
					dto.setQna_num(rs.getLong("qna_num"));
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
		public QnaDTO findByNext(long num, String kwd) {
			QnaDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				if (kwd != null && kwd.length() != 0) {
					sb.append(" SELECT qna_num, secret, title, email ");
					sb.append(" FROM qna ");
					sb.append(" WHERE ( qna_num < ? ) ");
					sb.append("     AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 OR INSTR(answer_content, ?) >= 1) ");
					sb.append(" ORDER BY qna_num ASC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");
					
					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
					pstmt.setString(2, kwd);
					pstmt.setString(3, kwd);
					pstmt.setString(4, kwd);
				} else {
					sb.append(" SELECT qna_num, secret, title, email ");
					sb.append(" FROM qna ");
					sb.append(" WHERE qna_num < ? ");
					sb.append(" ORDER BY qna_num ASC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");
					
					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
				}
				
				rs = pstmt.executeQuery();
				
				if (rs.next()) {
					dto = new QnaDTO();
					
					dto.setQna_num(rs.getLong("qna_num"));
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

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
					+ " VALUES (qna_seq.NEXTVAL, ?, ?, SYSDATE, default, ?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getAnswer_content());
			pstmt.setString(5, dto.getAnswer_reg_date());
			
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
		// 게시물 리스트
		public List<QnaDTO> listQuestion(int offset, int size) {
			List<QnaDTO> list = new ArrayList<QnaDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				sb.append(" SELECT qna_num, secret, q.userId, userName, title, answerId, ");
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

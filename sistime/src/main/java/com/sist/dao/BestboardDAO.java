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
	
	
	// best게시판
	public List<BoardDTO> listBoard(int offset, int size) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT b.board_num, b.email, user_Name, title, content, reg_date, hitCount, "
					+ "    NVL(boardLikeCount, 0) boardLikeCount "
					+ " FROM board b "
					+ " JOIN member1 m ON b.email = m.email "
					+ " LEFT OUTER JOIN ("
					+ "      SELECT board_num, COUNT(*) boardLikeCount FROM board_Like "
					+ "      GROUP BY board_num"
					+ " ) bc ON b.board_num = bc.board_num "
					+ " WHERE b.board_num = ? and boardLikeCount>= 10 ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, offset);
			pstmt.setLong(1, size);
			

			rs = pstmt.executeQuery();

			while (rs.next()) {
				dto = new BoardDTO();
				
				dto.setBoard_num(rs.getLong("board_num"));
				dto.setEmail(rs.getString("email"));
				dto.setUserName(rs.getString("user_Name"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setHitcount(rs.getInt("hitcount"));
				dto.setReg_date(rs.getString("reg_date"));
				
				dto.setBoardLikeCount(rs.getInt("boardLikeCount"));				
				
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

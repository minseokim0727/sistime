package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.BoardDTO;
import com.sist.domain.Board_ReplyDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class BoardDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertBoard(BoardDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO board(board_num, title, content, email, hitcount, reg_date) "
					+ " VALUES (board_seq.nextval, ?, ?, ?, 0, SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getEmail());
			
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
			sql = "SELECT NVL(COUNT(*), 0) FROM board";
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
	
	public List<BoardDTO> listBoard(int offset, int size) {
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
	
	public List<BoardDTO> listBoard(int offset, int size, String schType, String kwd) {
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
				sb.append(" WHERE INSTR(thitle, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
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
	
	// 조회수 증가하기
	public void updateHitCount(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE board SET hitCount=hitCount+1 WHERE board_num=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 해당 게시물 보기
		public BoardDTO findById(long num) {
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
						+ " ) bc ON b.board_num = bc.board_num"
						+ " WHERE b.board_num = ? ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);

				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new BoardDTO();
					
					dto.setBoard_num(rs.getLong("board_num"));
					dto.setEmail(rs.getString("email"));
					// dto.setUserName(rs.getString("userName")
					dto.setTitle(rs.getString("title"));
					dto.setContent(rs.getString("content"));
					dto.setHitcount(rs.getInt("hitcount"));
					dto.setReg_date(rs.getString("reg_date"));
					
					// dto.setBoardLikeCount(rs.getInt("boardLikeCount"));				
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}

			return dto;
		}
		
		// 이전글
		public BoardDTO findByPrev(long num, String schType, String kwd) {
			BoardDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				if (kwd != null && kwd.length() != 0) {
					sb.append(" SELECT b.board_num, title ");
					sb.append(" FROM board b ");
					sb.append(" JOIN member1 m ON b.email = m.email ");
					sb.append(" WHERE ( board_num > ? ) ");
					if (schType.equals("all")) {
						sb.append("   AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
					} else if (schType.equals("reg_date")) {
						kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
						sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
					} else {
						sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
					}
					sb.append(" ORDER BY board_num ASC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");

					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
					pstmt.setString(2, kwd);
					if (schType.equals("all")) {
						pstmt.setString(3, kwd);
					}
				} else {
					sb.append(" SELECT board_num, title ");
					sb.append(" FROM board ");
					sb.append(" WHERE board_num > ? ");
					sb.append(" ORDER BY board_num ASC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");

					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
				}

				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new BoardDTO();
					
					dto.setBoard_num(rs.getLong("board_num"));
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
		public BoardDTO findByNext(long num, String schType, String kwd) {
			BoardDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				if (kwd != null && kwd.length() != 0) {
					sb.append(" SELECT b.board_num, title ");
					sb.append(" FROM board b ");
					sb.append(" JOIN member1 m ON b.email = m.email ");
					sb.append(" WHERE ( board_num < ? ) ");
					if (schType.equals("all")) {
						sb.append("   AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
					} else if (schType.equals("reg_date")) {
						kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
						sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
					} else {
						sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
					}
					sb.append(" ORDER BY board_num DESC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");

					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
					pstmt.setString(2, kwd);
					if (schType.equals("all")) {
						pstmt.setString(3, kwd);
					}
				} else {
					sb.append(" SELECT board_num, title ");
					sb.append(" FROM board ");
					sb.append(" WHERE board_num < ? ");
					sb.append(" ORDER BY board_num DESC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");

					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
				}

				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new BoardDTO();
					
					dto.setBoard_num(rs.getLong("board_num"));
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
		
		// 게시물 수정
		public void updateBoard(BoardDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;

			try {
				sql = "UPDATE board SET title=?, content=? WHERE board_num=? AND email=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getTitle());
				pstmt.setString(2, dto.getContent());
				pstmt.setLong(3, dto.getBoard_num());
				pstmt.setString(4, dto.getEmail());
				
				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}

		}
		
		// 게시물 삭제
		public void deleteBoard(long num, String userId) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;

			try {
				if (userId.equals("admin")) {
					sql = "DELETE FROM board WHERE board_num=?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, num);
					
					pstmt.executeUpdate();
				} else {
					sql = "DELETE FROM board WHERE board_num=? AND email=?";
					
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, num);
					pstmt.setString(2, userId);
					
					pstmt.executeUpdate();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		}
			
			
			/*
			// 로그인 유저의 게시글 공감 유무
			public boolean isUserBoardLike(long num, String userId) {
				boolean result = false;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql;
				
				try {
					sql = "SELECT num, userId FROM bbsLike WHERE num = ? AND userId = ?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, num);
					pstmt.setString(2, userId);
					
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
						result = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					DBUtil.close(rs);
					DBUtil.close(pstmt);
				}
				
				return result;
			}
			*/
			
			/*
			// 게시물의 공감 추가
			public void insertBoardLike(long num, String userId) throws SQLException {
				PreparedStatement pstmt = null;
				String sql;
				
				try {
					sql = "INSERT INTO bbsLike(num, userId) VALUES (?, ?)";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, num);
					pstmt.setString(2, userId);
					
					pstmt.executeUpdate();
					
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				} finally {
					DBUtil.close(pstmt);
				}
			}
			*/
			
			/*
			// 게시글 공감 삭제
			public void deleteBoardLike(long num, String userId) throws SQLException {
				PreparedStatement pstmt = null;
				String sql;
				
				try {
					sql = "DELETE FROM bbsLike WHERE num = ? AND userId = ?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, num);
					pstmt.setString(2, userId);
					
					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				} finally {
					DBUtil.close(pstmt);
				}
			}
			*/
			
			/*
			// 게시물의 공감 개수
			public int countBoardLike(long num) {
				int result = 0;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql;
				
				try {
					sql = "SELECT NVL(COUNT(*), 0) FROM bbsLike WHERE num=?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, num);
					
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
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
			*/
			
			
			
			// 게시물의 댓글 및 답글 추가
			public void insertReply(Board_ReplyDTO dto) throws SQLException {
				PreparedStatement pstmt = null;
				String sql;
				
				try {
					sql = "INSERT INTO  Board_Reply(replyNum, board_num, email, replycontent, B_replydate) "
							+ " VALUES (Board_Reply_seq.NEXTVAL, ?, ?, ?, SYSDATE)";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, dto.getBoard_num());
					pstmt.setString(2, dto.getEmail());
					pstmt.setString(3, dto.getReplycontent());

					pstmt.executeUpdate();
					
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				} finally {
					DBUtil.close(pstmt);
				}
			}
			
			
			/*
			// 게시물의 댓글 개수
			public int dataCountReply(long num) {
				int result = 0;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql;
				
				try {
					sql = "SELECT NVL(COUNT(*), 0) "
							+ " FROM bbsReply "
							+ " WHERE num = ? AND answer = 0";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, num);
					
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
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
			*/
			
			
			// 게시물 댓글 리스트
			public List<Board_ReplyDTO> listReply(long num, int offset, int size) {
				List<Board_ReplyDTO> list = new ArrayList<>();
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				StringBuilder sb = new StringBuilder();
				
				try {
					sb.append(" SELECT r.replyNum, r.userId, userName, num, content, r.reg_date, ");
					sb.append("     NVL(answerCount, 0) answerCount, ");
					sb.append("     NVL(likeCount, 0) likeCount, ");
					sb.append("     NVL(disLikeCount, 0) disLikeCount ");
					sb.append(" FROM board_Reply r ");
					sb.append(" JOIN member1 m ON r.userId = m.userId ");
					sb.append(" LEFT OUTER  JOIN (");
					sb.append("	    SELECT answer, COUNT(*) answerCount ");
					sb.append("     FROM boardReply ");
					sb.append("     WHERE answer != 0 ");
					sb.append("     GROUP BY answer ");
					sb.append(" ) a ON r.replyNum = a.answer ");
					sb.append(" LEFT OUTER  JOIN ( ");
					sb.append("	    SELECT replyNum, ");
					sb.append("         COUNT(DECODE(replyLike, 1, 1)) likeCount, ");
					sb.append("         COUNT(DECODE(replyLike, 0, 1)) disLikeCount ");
					sb.append("     FROM boardReplyLike ");
					sb.append("     GROUP BY replyNum ");
					sb.append(" ) b ON r.replyNum = b.replyNum  ");
					sb.append(" WHERE num = ? AND r.answer=0 ");
					sb.append(" ORDER BY r.replyNum DESC ");
					sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
					
					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, num);
					pstmt.setInt(2, offset);
					pstmt.setInt(3, size);

					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						Board_ReplyDTO dto = new Board_ReplyDTO();
						
						dto.setReplynum(rs.getLong("replynum"));
						dto.setEmail(rs.getNString("email"));
						dto.setReplycontent(rs.getNString("replycontent"));
						dto.setB_replydate(rs.getNString("B_replydate"));
						
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
			
			
			/*
			public ReplyDTO findByReplyId(long replyNum) {
				ReplyDTO dto = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql;
				
				try {
					sql = "SELECT replyNum, num, r.userId, userName, content, r.reg_date "
							+ " FROM bbsReply r  "
							+ " JOIN member1 m ON r.userId = m.userId  "
							+ " WHERE replyNum = ? ";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, replyNum);

					rs=pstmt.executeQuery();
					
					if(rs.next()) {
						dto=new ReplyDTO();
						
						dto.setReplyNum(rs.getLong("replyNum"));
						dto.setNum(rs.getLong("num"));
						dto.setUserId(rs.getString("userId"));
						dto.setUserName(rs.getString("userName"));
						dto.setContent(rs.getString("content"));
						dto.setReg_date(rs.getString("reg_date"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBUtil.close(rs);
					DBUtil.close(pstmt);
				}
				
				return dto;
			}
			*/
			
			/*
			// 게시물의 댓글 삭제
			public void deleteReply(long replyNum, String userId) throws SQLException {
				PreparedStatement pstmt = null;
				String sql;
				
				if(! userId.equals("admin")) {
					ReplyDTO dto = findByReplyId(replyNum);
					if(dto == null || (! userId.equals(dto.getUserId()))) {
						return;
					}
				}
				
				try {
					sql = "DELETE FROM bbsReply "
							+ " WHERE replyNum IN  "
							+ " (SELECT replyNum FROM bbsReply START WITH replyNum = ?"
							+ "     CONNECT BY PRIOR replyNum = answer)";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, replyNum);
					
					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				} finally {
					DBUtil.close(pstmt);
				}		
			}
			*/

			/*
			// 댓글의 답글 리스트
			public List<ReplyDTO> listReplyAnswer(long answer) {
				List<ReplyDTO> list = new ArrayList<>();
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				StringBuilder sb=new StringBuilder();
				
				try {
					sb.append(" SELECT replyNum, num, r.userId, userName, content, reg_date, answer ");
					sb.append(" FROM bbsReply r ");
					sb.append(" JOIN member1 m ON r.userId = m.userId ");
					sb.append(" WHERE answer = ? ");
					sb.append(" ORDER BY replyNum DESC ");
					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, answer);

					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						ReplyDTO dto=new ReplyDTO();
						
						dto.setReplyNum(rs.getLong("replyNum"));
						dto.setNum(rs.getLong("num"));
						dto.setUserId(rs.getString("userId"));
						dto.setUserName(rs.getString("userName"));
						dto.setContent(rs.getString("content"));
						dto.setReg_date(rs.getString("reg_date"));
						dto.setAnswer(rs.getLong("answer"));
						
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
			*/
			
			/*
			// 댓글의 답글 개수
			public int dataCountReplyAnswer(long answer) {
				int result = 0;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql;
				
				try {
					sql = "SELECT NVL(COUNT(*), 0) "
							+ " FROM bbsReply WHERE answer = ?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, answer);
					
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
						result=rs.getInt(1);
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBUtil.close(rs);
					DBUtil.close(pstmt);
				}
				
				return result;
			}
			*/
			
			/*
			// 댓글의 좋아요 / 싫어요 추가
			public void insertReplyLike(ReplyDTO dto) throws SQLException {
				PreparedStatement pstmt = null;
				String sql;
				
				try {
					sql = "INSERT INTO bbsReplyLike(replyNum, userId, replyLike) VALUES (?, ?, ?)";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, dto.getReplyNum());
					pstmt.setString(2, dto.getUserId());
					pstmt.setInt(3, dto.getReplyLike());
					
					pstmt.executeUpdate();
					
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				} finally {
					DBUtil.close(pstmt);
				}		

			}
			*/
			
			/*
			// 댓글의 좋아요 / 싫어요 개수
			public Map<String, Integer> countReplyLike(long replyNum) {
				Map<String, Integer> map = new HashMap<>();
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql;
				
				try {
					sql = " SELECT COUNT(DECODE(replyLike, 1, 1)) likeCount,  "
						+ "     COUNT(DECODE(replyLike, 0, 1)) disLikeCount  "
						+ " FROM bbsReplyLike WHERE replyNum = ? ";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, replyNum);
					
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
						map.put("likeCount", rs.getInt("likeCount"));
						map.put("disLikeCount", rs.getInt("disLikeCount"));
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DBUtil.close(rs);
					DBUtil.close(pstmt);
				}
				
				return map;
				*/
		}
		


package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.BoardDTO;
import com.sist.domain.Board_ReplyDTO;
import com.sist.domain.ComplainDTO;
import com.sist.domain.CreateDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class CreateDAO {
	private Connection conn = DBConn.getConnection();

	public void createBoard(String name) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "CREATE TABLE " + name + " (" +
				      "    BOARD_NUM NUMBER primary key," +
				      "    TITLE VARCHAR2(50) NOT NULL," +
				      "    CONTENT VARCHAR2(4000)," +
				      "    REG_DATE DATE," +
				      "    HITCOUNT NUMBER," +
				      "    EMAIL VARCHAR2(50)," +
				      "    BOARD_ID NUMBER," +
				      "    BOARD_NAME VARCHAR2(30)," +
				      "    FOREIGN KEY (EMAIL) REFERENCES member1(EMAIL)," +
				      "    FOREIGN KEY (BOARD_ID) REFERENCES board_names(BOARD_ID)" +
				      ")";


			pstmt = conn.prepareStatement(sql);
			pstmt.execute();

			pstmt = null;

			sql = "CREATE SEQUENCE " + name+ "_seq " + " START WITH 1" + " INCREMENT BY 1" + " NOCACHE" + " NOCYCLE";

			pstmt = conn.prepareStatement(sql);
			pstmt.execute();

			pstmt = null;

			sql = "CREATE TABLE " + name + "_reply (" + "    REPLYNUM NUMBER NOT NULL, "
					+ "    REPLYCONTENT VARCHAR2(500) NOT NULL, " + "    B_REPLYDATE DATE, " + "    BOARD_NUM NUMBER, "
					+ "    EMAIL VARCHAR2(50), " + "    PRIMARY KEY (REPLYNUM), "
					+ "    FOREIGN KEY (BOARD_NUM) REFERENCES " + name + "(BOARD_NUM), "
					+ "    FOREIGN KEY (EMAIL) REFERENCES member1(EMAIL)" + ")";
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();

			pstmt = null;

			sql = "CREATE SEQUENCE " + name + "_reply_seq " + " START WITH 1" + " INCREMENT BY 1" + " NOCACHE"
					+ " NOCYCLE";
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();

			pstmt = null;

			sql = "create table " + name+ "_like (" + " board_num number primary key," + " email varchar2(50),"
					+ "  FOREIGN KEY (BOARD_NUM) REFERENCES " + name + "(BOARD_NUM), "
					+ "    FOREIGN KEY (EMAIL) REFERENCES member1(EMAIL)" + ")";
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();

			pstmt = null;
			
			

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	public void insertBoardname(String name, String description) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO board_names(BOARD_ID,BOARD_NAME,BOARD_DESCRIPTION) values(board_names_seq.nextval,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, name);
			pstmt.setString(2, description);

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	public List<CreateDTO> selectBoardname() throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		List<CreateDTO> list = new ArrayList<CreateDTO>();
		try {
			sql = "select board_id,board_name,board_description from board_names";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				CreateDTO dto = new CreateDTO();

				dto.setBOARD_ID(rs.getInt("board_id"));
				dto.setBOARD_NAME(rs.getString("board_name"));
				dto.setBOARD_DESCRIPTION(rs.getString("board_description"));

				list.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		return list;
	}

	public int dataCount(String name) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM " + name;
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
	public int dataCount(String schType, String kwd, String name) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) " + " FROM" + name + "b " + " JOIN member1 m ON b.userId = m.userId ";
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

	public List<BoardDTO> listBoard(int offset, int size, String name) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT b.board_num, nickname, title, hitCount, board_name, ");
			sb.append("      TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append("      NVL(replyCount, 0) replyCount ");
			sb.append(" FROM ");
			sb.append(name + " b ");
			sb.append(" JOIN member1 m ON b.email = m.email ");
			sb.append(" LEFT OUTER JOIN ( ");
			sb.append("     SELECT board_num, COUNT(*) replyCount ");
			sb.append("     FROM " + name + "_Reply ");
			sb.append("     GROUP BY board_num ");
			sb.append(" ) c ON b.board_num = c.board_num ");
			sb.append("     WHERE board_name = '" + name + "' ");
			sb.append(" ORDER BY b.board_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");



			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardDTO dto = new BoardDTO();

				dto.setBoard_num(rs.getLong("Board_num"));
				dto.setEmail(rs.getString("nickname"));
				dto.setTitle(rs.getString("title"));
				dto.setHitcount(rs.getInt("hitcount"));
				dto.setReg_date(rs.getString("reg_date"));

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

	public List<BoardDTO> listBoard(int offset, int size, String schType, String kwd,String name) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT b.board_num, nickname, title, hitCount, board_name, ");
			sb.append("      TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append("      NVL(replyCount, 0) replyCount ");
			sb.append(" FROM ");
			sb.append(name + " b ");
			sb.append(" JOIN member1 m ON b.email = m.email ");
			sb.append(" LEFT OUTER JOIN ( ");
			sb.append("     SELECT board_num, COUNT(*) replyCount ");
			sb.append("     FROM " + name + "_Reply ");
			sb.append("     GROUP BY board_num ");
			sb.append(" ) c ON b.board_num = c.board_num ");
			sb.append("     WHERE board_name = '" + name + "' ");
			if (schType.equals("all")) {
				sb.append(" AND INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" AND TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append(" AND INSTR(" + schType + ", ?) >= 1 ");
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
				dto.setEmail(rs.getString("nickname"));
				dto.setTitle(rs.getString("title"));
				dto.setHitcount(rs.getInt("hitcount"));
				dto.setReg_date(rs.getString("reg_date"));

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
	
	public void insertBoard(BoardDTO dto,String name,int id) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO " + name + " (board_num, title, content, email, hitcount, reg_date, board_id, board_name) "
			        + "VALUES (" + name + "_seq.nextval, ?, ?, ?, 0, SYSDATE, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getEmail());
			pstmt.setInt(4, id);
			pstmt.setString(5, name);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(pstmt);
		}
	}
	
	public int findbyBoardId(String name) {
	    int result = 0;
	    
	    PreparedStatement pstmt = null;
	    String sql;
	    ResultSet rs = null;
	    try {
	        sql = "SELECT board_id FROM board_names WHERE board_name = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, name);
	        rs = pstmt.executeQuery();
	        
	        // 결과가 있는 경우에만 처리
	        if (rs.next()) {
	            
	            if (rs.getObject("board_id") != null) {
	                result = rs.getInt("board_id");
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(pstmt);
	        DBUtil.close(rs);
	    }
	    
	    return result;
	}
	
	public void updateHitCount(long num,String name) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE "+name+ " SET hitCount=hitCount+1 WHERE board_num=?";
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
	
	public BoardDTO findById(long num,String name) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT b.board_num, b.email, nickname, title, content, reg_date, hitCount, "
					+ "    NVL(boardLikeCount, 0) boardLikeCount "
					+ " FROM "+name+ " b "
					+ " JOIN member1 m ON b.email = m.email "
					+ " LEFT OUTER JOIN ("
					+ "      SELECT board_num, COUNT(*) boardLikeCount FROM "+name+"_Like "
					+ "      GROUP BY board_num"
					+ " ) bc ON b.board_num = bc.board_num"
					+ " WHERE b.board_num = ? and board_name = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			pstmt.setString(2,name);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BoardDTO();
				
				dto.setBoard_num(rs.getLong("board_num"));
				dto.setEmail(rs.getString("email"));
				dto.setUserName(rs.getString("nickname"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setHitcount(rs.getInt("hitcount"));
				dto.setReg_date(rs.getString("reg_date"));
				
				dto.setBoardLikeCount(rs.getInt("boardLikeCount"));				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	public BoardDTO findByPrev(long num, String schType, String kwd,String name) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT b.board_num, title ");
				sb.append(" FROM " + name +" b ");
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
				sb.append(" FROM "+name);
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
	public BoardDTO findByNext(long num, String schType, String kwd,String name) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT b.board_num, title ");
				sb.append(" FROM " +name+ " b ");
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
				sb.append(" FROM  "+name);
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
	public void updateBoard(BoardDTO dto,String name) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE " + name + " SET title = ?, content = ? WHERE board_num = ? AND email = ?";

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
	
	public void deleteBoard(long num, String userId,String name) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (userId.equals("admin")) {
				sql = "DELETE FROM " +name+ " WHERE board_num=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				
				pstmt.executeUpdate();
			} else {
				sql = "DELETE FROM " +name+ " WHERE board_num=? AND email=?";
				
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
	
	public void insertReply(Board_ReplyDTO dto,String name) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO  "+name+"_Reply(replyNum, board_num, email, replycontent, B_replydate) "
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
	
	public int dataCountReply(long num,String name) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) "
					+ " FROM "+name+"_Reply "
					+ " WHERE board_num = ? ";
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
	
	public List<Board_ReplyDTO> listReply(long num, int offset, int size,String name) {
		List<Board_ReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT replyNum, r.email, nickname, board_num, replycontent, to_char(b_replydate, 'yyyy-mm-dd') b_replydate ");
			sb.append(" FROM " +name+"_Reply r ");
			sb.append(" JOIN member1 m ON r.email = m.email ");
			sb.append(" WHERE board_num = ? ");
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
				dto.setUserName(rs.getString("nickname"));
				dto.setReplycontent(rs.getNString("replycontent"));
				dto.setB_replydate(rs.getNString("b_replydate"));
				
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
	
	public void deleteReply(long replyNum, String userId,String name) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		if(! userId.equals("admin")) {
			Board_ReplyDTO dto = findByReplyId(replyNum,name);
			if(dto == null || (! userId.equals(dto.getEmail()))) {
				return;
			}
		}
		
		try {
			sql = "DELETE FROM "+name+"_Reply "
					+ " WHERE replynum  = ?";
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
	
	public Board_ReplyDTO findByReplyId(long replyNum,String name) {
		Board_ReplyDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT replyNum, r.email, user_name, board_num, replycontent, b_replydate "
					+ " FROM " +name+"_Reply r  "
					+ " JOIN member1 m ON r.email = m.email  "
					+ " WHERE replyNum = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, replyNum);

			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new Board_ReplyDTO();
				
				dto.setReplynum(rs.getLong("replynum"));
				dto.setEmail(rs.getString("email"));
				dto.setUserName(rs.getString("user_name"));
				dto.setReplycontent(rs.getString("replycontent"));
				dto.setB_replydate(rs.getString("B_replydate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}	
	
	// 게시물의 공감 추가
	public void insertBoardLike(long num, String userId,String name) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO " +name+"_Like(board_num, email) VALUES (?, ?)";
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
	
	public void deleteBoardLike(long num, String userId,String name) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM "+name+"_Like WHERE board_num = ? AND email = ?";
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
	
	public int countBoardLike(long num,String name) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM  "+name+"_Like WHERE board_num=?";
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

	public String findDescription(String board_name) {
		String result=null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT board_description FROM  board_names WHERE board_name=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, board_name);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getString("board_description");
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

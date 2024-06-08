package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.EventDTO;
import com.sist.domain.EventReplyDTO;

import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class EventDAO {
	private Connection conn = DBConn.getConnection();

	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM eventpage";
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
			sql = "SELECT NVL(COUNT(*), 0) " + " FROM eventpage e " + " JOIN member1 m ON e.email = m.email ";
			if (schType.equals("all")) {
				sql += "  WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ";
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
	public String findByNickname(String email) {
		String nickname = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT e.email,m.nickname from eventpage e ");
			sb.append(" join member1 m on e.email = m.email ");
			sb.append(" where e.email = ? ");
			
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, email);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				nickname = rs.getString("nickname");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return nickname;
	}

	public List<EventDTO> listEvent(int offset, int size) {
		List<EventDTO> list = new ArrayList<EventDTO>();
		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		String nickname;
		try {
			sb.append(" SELECT eventpage_num, m.email, title, content, ");
			sb.append(" TO_CHAR(reg_date, 'yyyy-MM-dd') reg_date ");
			sb.append(" FROM eventpage e ");
			sb.append(" JOIN member1 m ON e.email = m.email ");
			sb.append(" ORDER BY eventpage_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
		
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				EventDTO dto = new EventDTO();

				dto.setEventpage_num(rs.getLong("eventpage_num"));
				dto.setEmail(rs.getString("email"));
				dto.setEvent_title(rs.getString("title"));
				dto.setEvent_content(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				
				
				nickname = findByNickname(dto.getEmail());
				dto.setNickname(nickname);
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

	public List<EventDTO> listEvent(int offset, int size, String schType, String kwd) {
		List<EventDTO> list = new ArrayList<EventDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		String nickname;

		try {
			sb.append(" SELECT eventpage_num, e.email, title, content, ");
			sb.append("      reg_date ");
			sb.append(" FROM eventpage e ");
			sb.append(" JOIN member1 m ON e.email = m.email ");
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
			}
			sb.append(" ORDER BY notice_num DESC ");
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
				EventDTO dto = new EventDTO();

				dto.setEventpage_num(rs.getLong("eventpage_num"));
				dto.setEmail(rs.getString("email"));
				dto.setEvent_title(rs.getString("title"));
				dto.setEvent_content(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				
				nickname = findByNickname(dto.getEmail());
				dto.setNickname(nickname);

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

	public List<EventDTO> listEvent() {
		List<EventDTO> list = new ArrayList<EventDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		String nickname;

		try {
			sb.append(" SELECT eventpage_num, m.email, title, content, ");
			sb.append(" TO_CHAR(reg_date, 'yyyy-MM-dd') reg_date ");
			sb.append(" FROM eventpage e ");
			sb.append(" JOIN member1 m ON e.email = m.email ");
			sb.append(" WHERE notice = 1 ");
			sb.append(" ORDER BY eventpage_num DESC ");

			pstmt = conn.prepareStatement(sb.toString());

			rs = pstmt.executeQuery();

			while (rs.next()) {
				EventDTO dto = new EventDTO();

				dto.setEventpage_num(rs.getLong("eventpage_num"));
				dto.setEmail(rs.getString("email"));
				dto.setEvent_title(rs.getString("title"));
				dto.setEvent_content(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				
				nickname = findByNickname(dto.getEmail());
				dto.setNickname(nickname);

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

	public void insertEvent(EventDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO eventpage(eventpage_num, title, content, start_date, hitcount, savefilename, originalfilename, filesize, reg_date, board_name, notice, email)"
				    + "VALUES (eventpage_seq.nextval, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), 0, ?, ?, ?, SYSDATE, 'event', ?, ?)";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, dto.getEvent_title());
				pstmt.setString(2, dto.getEvent_content());
				pstmt.setString(3, dto.getStart_date());  // start_date
				pstmt.setString(4, dto.getSaveFilename());
				pstmt.setString(5, dto.getOriginalFilename());
				pstmt.setLong(6, dto.getFilesize());
				pstmt.setLong(7, dto.getNotice());
				pstmt.setString(8, dto.getEmail());

				pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	public void updateHitCount(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE eventpage SET hitCount=hitCount+1 WHERE eventpage_num=?";

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
	
	public EventDTO findById(long num) {
		EventDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		String nickname;

		try {
			sql = "SELECT e.eventpage_num, m1.email, user_Name, title, content, "
					+ " saveFilename, originalFilename, filesize, reg_date, hitCount, "
					+ " NVL(likeCount, 0) likeCount "
					+ " FROM eventpage e "
					+ " JOIN member1 m1 ON e.email=m1.email "
					+ " LEFT OUTER JOIN ("
					+ "      SELECT eventpage_num, COUNT(*) likeCount FROM eventpage_like"
					+ "      GROUP BY eventpage_num"
					+ " )bc ON e.eventpage_num = bc.eventpage_num"
					+ " WHERE e.eventpage_num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new EventDTO();
				
				dto.setEventpage_num(rs.getLong("eventpage_num"));
				dto.setEmail(rs.getString("email"));
				
				dto.setEvent_title(rs.getString("title"));
				dto.setEvent_content(rs.getString("content"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setFilesize(rs.getLong("filesize"));
				dto.setHitccount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				
				dto.setLikeCount(rs.getInt("likeCount"));
				
				nickname = findByNickname(dto.getEmail());
				dto.setNickname(nickname);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	public EventDTO findByPrev(long num, String schType, String kwd) {
		EventDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT eventpage_num, title ");
				sb.append(" FROM eventpage e ");
				sb.append(" JOIN member1 m ON e.userId = m.userId ");
				sb.append(" WHERE eventpage_num > ? ");
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
				} else {
					sb.append("   AND INSTR(" + schType + ", ?) >= 1 ");
				}
				sb.append(" ORDER BY num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append(" SELECT eventpage_num, title FROM eventpage ");
				sb.append(" WHERE eventpage_num > ? ");
				sb.append(" ORDER BY eventpage_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new EventDTO();
				
				dto.setEventpage_num(rs.getLong("eventpage_num"));
				dto.setEvent_title(rs.getString("title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return dto;
	}

	// 다음글
	public EventDTO findByNext(long num, String schType, String kwd) {
		EventDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT eventpage_num, title ");
				sb.append(" FROM eventpage e ");
				sb.append(" JOIN member1 m ON b.email = m.email ");
				sb.append(" WHERE eventpage_num < ? ");
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
				} else {
					sb.append("   AND INSTR(" + schType + ", ?) >= 1 ");
				}
				sb.append(" ORDER BY eventpage_num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append(" SELECT eventpage_num, title FROM eventpage ");
				sb.append(" WHERE eventpage_num < ? ");
				sb.append(" ORDER BY eventpage_num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new EventDTO();
				
				dto.setEventpage_num(rs.getLong("eventpage_num"));
				dto.setEvent_title(rs.getString("title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	public boolean isUserEventLike(long num, String email) {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT eventpage_num, email "
					+ " FROM eventpage_like "
					+ " WHERE eventpage_num = ? AND email = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			pstmt.setString(2, email);
			
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
	
	public void updateEvent(EventDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE eventpage SET  title=?, content=? ,saveFilename=?, originalFilename=?, filesize=? "
				+ " WHERE eventpage_num=? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getEvent_title());
			pstmt.setString(2, dto.getEvent_content());
			
			pstmt.setString(3, dto.getSaveFilename());
			pstmt.setString(4, dto.getOriginalFilename());
			pstmt.setLong(5, dto.getFilesize());
			pstmt.setLong(6, dto.getEventpage_num());
			pstmt.executeUpdate();
	
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public void deleteEvent(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM eventpage WHERE eventpage_num = ?";
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
	
	public void deleteEvent(long[] nums) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM eventpage WHERE eventpage_num = ?";
			pstmt = conn.prepareStatement(sql);
			
			for(long num : nums) {
				pstmt.setLong(1, num);
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public void insertEventLike(long num, String email) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO eventpage_like(eventpage_num, email) VALUES (?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			pstmt.setString(2, email);
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	public void deleteEventLike(long num, String email) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM eventpage_like WHERE eventpage_num = ? AND email = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			pstmt.setString(2, email);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	public int countEventLike(long num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM eventpage_like WHERE eventpage_num=?";
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
	
	public int dataCountReply(long num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) "
					+ " FROM event_reply "
					+ " WHERE eventpage_num = ? AND answer = 0";
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
	
	public List<EventReplyDTO> listReply(long num, int offset, int size) {
		List<EventReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT reply_num, r.email, nickname, up_reply, content, r.reg_date, eventpage_num, ");
			sb.append("       NVL(a.answer_count, 0) AS answerCount ");
			sb.append("FROM event_reply r ");
			sb.append("JOIN member1 m ON r.email = m.email ");
			sb.append("LEFT OUTER JOIN ( ");
			sb.append("    SELECT answer, COUNT(*) AS answer_count ");
			sb.append("    FROM event_reply ");
			sb.append("    WHERE answer != 0 ");
			sb.append("    GROUP BY answer ");
			sb.append(") a ON r.reply_num = a.answer ");
			sb.append("WHERE eventpage_num = ? AND r.answer = 0 ");
			sb.append("ORDER BY r.reply_num DESC ");
			sb.append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, num);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EventReplyDTO dto = new EventReplyDTO();
				
				dto.setReply_num(rs.getLong("reply_num"));
				dto.setUp_reply(rs.getLong("up_reply"));
				dto.setNickname(rs.getString("nickname"));
				dto.setEmail(rs.getString("email"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				 dto.setAnswerCount(rs.getInt("answerCount"));
				
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
	
	public void insertReply(EventReplyDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO event_reply(reply_num, up_reply, email, content, answer, reg_date,eventpage_num) "
					+ " VALUES (event_reply_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, dto.getUp_reply());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getContent());
			pstmt.setLong(4, dto.getAnswer());
			pstmt.setLong(5, dto.getEVENTPAGE_NUM());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public EventReplyDTO readReply(long reply_num) {
		EventReplyDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT reply_num, eventpage_num, r.email, nickname, content, r.reg_date "
					+ "  FROM event_reply r "
					+ "  JOIN member1 m ON r.email = m.email  "
					+ "  WHERE reply_num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, reply_num);

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new EventReplyDTO();
				
				dto.setReply_num(rs.getLong("reply_num"));
				dto.setEVENTPAGE_NUM(rs.getLong("eventpage_num"));
				dto.setEmail(rs.getString("email"));
				dto.setNickname(rs.getString("nickname"));
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
	
	public void deleteReply(long reply_num, String email) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		if(! email.equals("admin")) {
			EventReplyDTO dto = readReply(reply_num);
			if(dto == null || (! email.equals(dto.getEmail()))) {
				return;
			}
		}
		
		try {
			sql = "DELETE FROM event_reply "
					+ "  WHERE reply_num IN  "
					+ "  (SELECT reply_num FROM event_Reply START WITH reply_num = ?"
					+ "     CONNECT BY PRIOR reply_num = answer)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, reply_num);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}		
		
	}
	
	public List<EventReplyDTO> listReplyAnswer(long answer) {
		List<EventReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT reply_num, up_reply, r.email, nickname, content, reg_date, answer "
					+ " FROM event_reply r "
					+ " JOIN member1 m ON r.email = m.email "
					+ " WHERE answer = ? "
					+ " ORDER BY reply_num DESC ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, answer);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EventReplyDTO dto = new EventReplyDTO();
				
				dto.setReply_num(rs.getLong("reply_num"));
				dto.setUp_reply(rs.getLong("up_reply"));
				dto.setEmail(rs.getString("email"));
				dto.setNickname(rs.getString("nickname"));
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
	
	public int dataCountReplyAnswer(long answer) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM event_reply WHERE answer = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, answer);
			
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


}

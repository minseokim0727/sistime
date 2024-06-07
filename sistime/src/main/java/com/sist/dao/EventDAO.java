package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.EventDTO;
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

	public List<EventDTO> listEvent(int offset, int size) {
		List<EventDTO> list = new ArrayList<EventDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

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
			sql = "INSERT INTO eventpage(eventpage_num, title, content, start_date, end_date, hitcount, savefilename, originalfilename, filesize, reg_date, board_name, notice, email)"
				    + "VALUES (eventpage_seq.nextval, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), 0, ?, ?, ?, SYSDATE, 'event', ?, ?)";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, dto.getEvent_title());
				pstmt.setString(2, dto.getEvent_content());
				pstmt.setString(3, "2024-06-07");  // start_date
				pstmt.setString(4, "2024-06-07");  // end_date
				pstmt.setString(5, dto.getSaveFilename());
				pstmt.setString(6, dto.getOriginalFilename());
				pstmt.setLong(7, dto.getFilesize());
				pstmt.setLong(8, dto.getGap());
				pstmt.setString(9, dto.getEmail());

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

		try {
			sql = "SELECT m.message_num, m1.email, user_Name, title, content, "
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
				sb.append(" ORDER BY num ASC ");
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
				sb.append(" JOIN member1 m ON b.userId = m.userId ");
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
				sb.append(" WHERE title = ? AND eventpage_num < ? ");
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
					+ " WHERE num = ? AND email = ?";
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


}

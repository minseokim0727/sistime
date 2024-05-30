package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hs.domain.NoticeDTO;
import com.hs.util.DBConn;
import com.hs.util.DBUtil;
import com.hs.util.MyMultipartFile;

public class NoticeDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertNotice(NoticeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO notice(num, notice, userId, subject, content, hitCount, reg_date) "
					+ " VALUES(notice_seq.NEXTVAL, ?, ?, ?, ?, 0, SYSDATE)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNotice());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			
			pstmt.executeUpdate();
			DBUtil.close(pstmt);
			pstmt = null;
			
			sql = "INSERT INTO noticeFile(fileNum, num, saveFilename, originalFilename) "
					+ " VALUES (noticeFile_seq.NEXTVAL, notice_seq.CURRVAL, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			for(MyMultipartFile mf : dto.getListFile()) {
				pstmt.setString(1, mf.getSaveFilename());
				pstmt.setString(2, mf.getOriginalFilename());
				pstmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM notice";
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
					+ " FROM notice n "
					+ " JOIN member1 m ON n.userId = m.userId ";
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

	public List<NoticeDTO> listNotice() {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT num, userName, subject, hitCount, ");
			sb.append("       TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM notice n ");
			sb.append(" JOIN member1 m ON n.userId = m.userId ");
			sb.append(" WHERE notice = 1 ");
			sb.append(" ORDER BY num DESC ");

			pstmt = conn.prepareStatement(sb.toString());
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();

				dto.setNum(rs.getLong("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
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
	
	public List<NoticeDTO> listNotice(int offset, int size) {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT num, userName, subject, hitCount, ");
			sb.append("      reg_date ");
			sb.append(" FROM notice n ");
			sb.append(" JOIN member1 m ON n.userId = m.userId ");
			sb.append(" ORDER BY num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();

				dto.setNum(rs.getLong("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
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
	
	public List<NoticeDTO> listNotice(int offset, int size, String schType, String kwd) {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT num, userName, subject, hitCount, ");
			sb.append("      reg_date ");
			sb.append(" FROM notice n ");
			sb.append(" JOIN member1 m ON n.userId = m.userId ");
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
			}
			sb.append(" ORDER BY num DESC ");
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
				NoticeDTO dto = new NoticeDTO();

				dto.setNum(rs.getLong("num"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
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
	
	public NoticeDTO findById(long num) {
		NoticeDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT num, n.userId, notice, userName, subject, content, reg_date, hitCount "
					+ " FROM notice n "
					+ " JOIN member1 m ON n.userId=m.userId "
					+ " WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setNotice(rs.getInt("notice"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
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
	
	public void updateHitCount(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE notice SET hitCount=hitCount+1 WHERE num=?";
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
	
	public NoticeDTO findByPrev(long num, String schType, String kwd) {
		NoticeDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT num, subject ");
				sb.append(" FROM notice n ");
				sb.append(" JOIN member1 m ON n.userId = m.userId ");
				sb.append(" WHERE ( num > ? ) ");
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
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
				sb.append(" SELECT num, subject ");
				sb.append(" FROM notice ");
				sb.append(" WHERE num > ? ");
				sb.append(" ORDER BY num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	public NoticeDTO findByNext(long num, String schType, String kwd) {
		NoticeDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT num, subject ");
				sb.append(" FROM notice n ");
				sb.append(" JOIN member1 m ON n.userId = m.userId ");
				sb.append(" WHERE ( num < ? ) ");
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
				}
				sb.append(" ORDER BY num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append(" SELECT num, subject ");
				sb.append(" FROM notice ");
				sb.append(" WHERE num < ? ");
				sb.append(" ORDER BY num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	public List<NoticeDTO> listNoticeFile(long num) {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT fileNum, saveFilename, originalFileName "
					+ " FROM noticeFile "
					+ " WHERE num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				dto.setFileNum(rs.getLong("fileNum"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFileName"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public NoticeDTO findByFileId(long fileNum) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT num, fileNum, saveFilename, originalFileName "
					+ " FROM noticeFile "
					+ " WHERE fileNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, fileNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new NoticeDTO();
				dto.setNum(rs.getLong("num"));
				dto.setFileNum(rs.getLong("fileNum"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFileName"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dto;
	}
	
	public void updateNotice(NoticeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE notice SET notice=?, subject=?, content=? "
				+ " WHERE num=? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNotice());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setLong(4, dto.getNum());
			
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO noticeFile(fileNum, num, saveFilename, originalFilename) "
					+ " VALUES (noticeFile_seq.NEXTVAL, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			for(MyMultipartFile mf : dto.getListFile()) {
				pstmt.setLong(1, dto.getNum());
				pstmt.setString(2, mf.getSaveFilename());
				pstmt.setString(3, mf.getOriginalFilename());
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public void deleteNotice(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM notice WHERE num = ?";
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
	
	public void deleteNotice(long[] nums) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM notice WHERE num = ?";
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
	
	public void deleteNoticeFile(String mode, long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if(mode.equals("all")) {
				sql = "DELETE FROM noticeFile WHERE num = ?";
			} else {
				sql = "DELETE FROM noticeFile WHERE fileNum = ?";
			}
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
	
}

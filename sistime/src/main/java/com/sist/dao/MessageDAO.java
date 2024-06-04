package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.MessageDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class MessageDAO {
	private Connection conn = DBConn.getConnection();

	public List<MessageDTO> listMessage(int offset, int size, String email) {
		List<MessageDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {

			sb.append("SELECT email, content, msg_num, send_date, nickname ");
			sb.append("FROM ( ");
			sb.append("    SELECT email, content, msg_num, send_date, nickname, ");
			sb.append("           ROW_NUMBER() OVER (PARTITION BY email ORDER BY send_date DESC) AS rn ");
			sb.append("    FROM ( ");
			sb.append("        SELECT send_email AS email, content, msg_num, send_date, m1.nickname ");
			sb.append("        FROM message m ");
			sb.append("        JOIN member1 m1 ON m.send_email = m1.email ");
			sb.append("        WHERE read_email = ? ");
			sb.append("        UNION ");
			sb.append("        SELECT read_email AS email, content, msg_num, send_date, m1.nickname ");
			sb.append("        FROM message m ");
			sb.append("        JOIN member1 m1 ON m.read_email = m1.email ");
			sb.append("        WHERE send_email = ? ");
			sb.append("    ) t ");
			sb.append(") t2 ");
			sb.append("WHERE rn = 1");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, email); // 읽는 사람의 이메일 설정 (상대방)
			pstmt.setString(2, email); // 보내는 사람의 이메일 설정 (나)
			pstmt.setInt(3, offset); // 오프셋 설정
			pstmt.setInt(4, size); // 페치 크기 설정

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MessageDTO dto = new MessageDTO();
				dto.setContent(rs.getString("content"));
				dto.setSend_email(rs.getString("nickname"));
				dto.setMsg_num(rs.getInt("msg_num"));
				dto.setSend_date(rs.getString("send_date"));
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

	public List<MessageDTO> listMessage(int offset, int size, String schType, String kwd, String email) {
		List<MessageDTO> list = new ArrayList<MessageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT email, content, msg_num, send_date " + "FROM ("
					+ "    SELECT send_email AS email, content, msg_num, send_date, "
					+ "           ROW_NUMBER() OVER (PARTITION BY send_email ORDER BY send_date DESC) AS rn "
					+ "    FROM message WHERE read_email = ? " + "    UNION "
					+ "    SELECT read_email AS email, content, msg_num, send_date, "
					+ "           ROW_NUMBER() OVER (PARTITION BY read_email ORDER BY send_date DESC) AS rn "
					+ "    FROM message WHERE send_email = ? " + ") t " + "WHERE rn = 1 ");

			if (schType.equals("all")) {
				sb.append(" AND (INSTR(content, ?) >= 1) ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" AND TO_CHAR(send_date, 'YYYYMMDD') = ?");
			} else {
				sb.append(" AND INSTR(" + schType + ", ?) >= 1 ");
			}

			sb.append(" ORDER BY send_date DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			if (schType.equals("all")) {
				pstmt.setString(1, email);
				pstmt.setString(2, email);
				pstmt.setString(3, kwd);
				pstmt.setString(4, kwd);
				pstmt.setInt(5, offset);
				pstmt.setInt(6, size);
			} else {
				pstmt.setString(1, email);
				pstmt.setString(2, email);
				pstmt.setString(3, kwd);
				pstmt.setInt(4, offset);
				pstmt.setInt(5, size);
			}
			// 이거 고쳐야함
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MessageDTO dto = new MessageDTO();

				dto.setContent(rs.getString("content"));
				dto.setSend_email(rs.getString("read_email"));
				dto.setMsg_num(rs.getInt("msg_num"));
				dto.setSend_date(rs.getString("send_date"));

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

	public int dataCount(String email) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM message where send_email = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
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
	
	public int dataCount(String receiver, String send) {
	    int result = 0;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;

	    try {
	        sql = "SELECT NVL(SUM(cnt), 0) " +
	              "FROM (" +
	              "    SELECT COUNT(*) AS cnt " +
	              "    FROM message " +
	              "    WHERE (read_email = ? AND send_email = ?) " +
	              "        OR (read_email = ? AND send_email = ?) " +
	              ") total";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, receiver);
	        pstmt.setString(2, send);
	        pstmt.setString(3, send);
	        pstmt.setString(4, receiver);
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
	public int dataCount(String schType, String kwd, String email) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) " + " FROM message m " + " JOIN member1 m1 ON m.send_email = m1.email "
					+ " JOIN member1 m1 ON m.read_email = m1.email ";
			if (schType.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sql += "  WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += "  WHERE INSTR(" + schType + ", ?) >= 1 ";
			}
			sql += " AND send_email = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, kwd);
			pstmt.setString(2, email);
			if (schType.equals("all")) {
				pstmt.setString(2, kwd);
				pstmt.setString(3, email);
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

	public int insertMessage(MessageDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		int msg_num = 0;
		try {
			sql = "INSERT INTO message(msg_num,content,send_email,read_email,send_date)"
					+ "VALUES (message_seq.nextval,?,?,?,SYSDATE)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getContent());
			pstmt.setString(2, dto.getSend_email());
			pstmt.setString(3, dto.getRead_email());

			pstmt.executeUpdate();
			
			pstmt = null;
			
			sql = "select max(msg_num)msg_num from message order by send_date desc";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				msg_num = rs.getInt("msg_num");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		return msg_num;
	}

	// 파라미터 넘기기용
	public MessageDTO findbyNum(int msg_num) {
		MessageDTO dto = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT msg_num, send_email, read_email, content, send_date " + " FROM message m "
					+ " JOIN member1 m1 ON m.read_email=m1.email " + " WHERE msg_num = ? ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, msg_num);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				dto = new MessageDTO();

				dto.setMsg_num(rs.getInt("msg_num"));
				dto.setSend_email(rs.getString("send_email"));
				dto.setRead_email(rs.getString("read_email"));
				dto.setContent(rs.getString("content"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 메세지를 보내는 사람과 받는 사람간의 모든 메세지를 출력
	public List<MessageDTO> findMessage(String send_email, String read_email) {
		List<MessageDTO> list = new ArrayList<MessageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT content, msg_num, send_date, nickname ");
			sb.append("FROM ( ");
			sb.append("    SELECT send_email, content, msg_num, send_date, m1.nickname ");
			sb.append("    FROM message m ");
			sb.append("    JOIN member1 m1 ON m1.email = m.read_email ");
			sb.append("    WHERE read_email = ? AND send_email = ? ");
			sb.append("    UNION ");
			sb.append("    SELECT read_email, content, msg_num, send_date, m1.nickname ");
			sb.append("    FROM message m ");
			sb.append("    JOIN member1 m1 ON m1.email = m.read_email ");
			sb.append("    WHERE send_email = ? AND read_email = ? ");
			sb.append(") t ");
			sb.append("ORDER BY send_date DESC");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, send_email);
			pstmt.setString(2, read_email);
			pstmt.setString(3, send_email);
			pstmt.setString(4, read_email);
			
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				MessageDTO dto = new MessageDTO();
				
				dto.setContent(rs.getString("content"));
				dto.setSend_email(rs.getString("nickname"));
				dto.setMsg_num(rs.getInt("msg_num"));
				dto.setSend_date(rs.getString("send_date"));
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}

		return list;
	}
	
	public String findbyNickname(String email) {
		String nickname = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT nickname " + " FROM member1 "
					 + " WHERE email = ? ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, email);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				nickname = rs.getString("nickname");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return nickname;
	}
	
	public String findbyEmail(String nickname) {
		String email = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT distinct email " + " FROM member1 "
					 + " WHERE nickname = ? ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, nickname);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				email = rs.getString("email");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return email;
	}


}
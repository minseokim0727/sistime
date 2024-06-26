package com.sist.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.MemberDTO;
import com.sist.domain.MypageDTO;

import com.sist.util.DBConn;
import com.sist.util.DBUtil;


//memerDTO를 가져다 씀
public class MypageDAO {
	private Connection conn = DBConn.getConnection();
	
	
	// 이메일 찾기 
	public MemberDTO findByEmail(String email) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.email, user_Name, user_Pwd,");
			sb.append("      enabled, register_date,nickname,univ_num, ");
			sb.append("      birth, ");
			sb.append("       tel,");
			sb.append("      zip, addr1, addr2 ");
			sb.append("  FROM member1 m1");
			sb.append("  JOIN member2 m2 ON m1.email=m2.email ");
			sb.append("  WHERE m1.email = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, email);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setEmail(rs.getString("email"));
				dto.setUserPwd(rs.getString("user_Pwd"));
				dto.setUserName(rs.getString("user_Name"));
				dto.setEnabled(rs.getInt("enabled"));
				dto.setRegister_date(rs.getString("register_date"));
				dto.setNickname(rs.getString("nickname"));
				dto.setUniv_num(rs.getInt("univ_num"));
				dto.setBirth(rs.getString("birth"));
				dto.setTel(rs.getString("tel"));
				dto.setZip(rs.getString("zip"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}	
	// 닉네임 찾기
	public MemberDTO findByNickname(String nickname) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.email, user_Name, user_Pwd,");
			sb.append("      enabled, register_date,nickname,univ_num, ");
			sb.append("      birth, ");
			sb.append("       tel,");
			sb.append("      zip, addr1, addr2 ");
			sb.append("  FROM member1 m1");
			sb.append("  JOIN member2 m2 ON m1.email=m2.email ");
			sb.append("  WHERE nickname = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, nickname);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setEmail(rs.getString("email"));
				dto.setUserPwd(rs.getString("user_Pwd"));
				dto.setUserName(rs.getString("user_Name"));
				dto.setEnabled(rs.getInt("enabled"));
				dto.setRegister_date(rs.getString("register_date"));
				dto.setNickname(rs.getString("nickname"));
				dto.setUniv_num(rs.getInt("univ_num"));
				dto.setBirth(rs.getString("birth"));
				dto.setTel(rs.getString("tel"));
				dto.setZip(rs.getString("zip"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}	
	// 비밀번호 수정
	public void updatePwd(MemberDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update member1 set user_pwd=? where email = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserPwd());
			pstmt.setString(2, dto.getEmail());
			
			pstmt.executeUpdate();			

			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 이름 수정
	public void updateName(MemberDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update member1 set user_name=? where email = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserName());
			pstmt.setString(2, dto.getEmail());
			
			pstmt.executeUpdate();			

			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	// 닉네임 수정
	public void updateNickname(MemberDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update member1 set nickname=? where email = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getNickname());
			pstmt.setString(2, dto.getEmail());
			
			pstmt.executeUpdate();			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	
	// 상세 정보 수정(member2에 대한 정보)
	public void updateMember(MemberDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE member2 SET birth=TO_DATE(?,'YYYY-MM-DD'), tel=?, zip=?, addr1=?, addr2=? WHERE email=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getBirth());
			pstmt.setString(2, dto.getTel());
			pstmt.setString(3, dto.getZip());
			pstmt.setString(4, dto.getAddr1());
			pstmt.setString(5, dto.getAddr2());
			pstmt.setString(6, dto.getEmail());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	// 회원 탈퇴
	public void deleteMember(String email) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE member1 SET enabled=0 WHERE email=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql = "DELETE FROM member2 WHERE email=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	

	}
	
	// 내가 쓴 글 리스트
	public List<MypageDTO> myListpageList(String email, int offset, int size){
		List<MypageDTO> list = new ArrayList<MypageDTO>();
		//System.out.println(11);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT board_name, title, content, reg_date ,board_num "
					+ " FROM board "
					+ " WHERE email = '"+email+"' "
					+ " UNION "
					+ " SELECT board_name, title, content, reg_date, RB_num "
					+ " FROM requestboard "
					+ " WHERE email = '"+email+"' "
					+ " UNION "
					+ " SELECT board_name, title, content, reg_date, qna_num "
					+ " FROM qna "
					+ " WHERE email = '"+email+"' "
					+ " UNION "
					+ " SELECT board_name, title, to_char(content), reg_date ,Eventpage_num "
					+ " FROM eventpage "
					+ " WHERE email = '"+email+"' "
					+ " UNION "
					+ " SELECT board_name, title, content, reg_date,Notice_num "
					+ " FROM Notice "
					+ " WHERE email = '"+email+"' "
					+ " ORDER BY reg_date DESC "
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt = conn.prepareStatement(sql);
			/*
			pstmt.setString(1, email);
			pstmt.setString(2, email);
			pstmt.setString(3, email);
			pstmt.setString(4, email);
			*/
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				
				MypageDTO dto = new MypageDTO();
				
				
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setBoard_name(rs.getString("board_name"));
				dto.setBoard_num(rs.getLong("board_num"));
				dto.setReg_date(rs.getString("reg_date"));
				list.add(dto);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	// 내가 쓴 글 카운트 용도
	public List<MypageDTO> myListpage(String email){
		List<MypageDTO> list = new ArrayList<MypageDTO>();
		//System.out.println(11);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT board_name, title, content, reg_date ,board_num "
					+ " FROM board "
					+ " WHERE email = '"+email+"' "
					+ " UNION "
					+ " SELECT board_name, title, content, reg_date, RB_num "
					+ " FROM requestboard "
					+ " WHERE email = '"+email+"' "
					+ " UNION "
					+ " SELECT board_name, title, content, reg_date, qna_num "
					+ " FROM qna "
					+ " WHERE email = '"+email+"' "
					+ " UNION "
					+ " SELECT board_name, title, to_char(content), reg_date ,Eventpage_num "
					+ " FROM eventpage "
					+ " WHERE email = '"+email+"' "
					+ " UNION "
					+ " SELECT board_name, title, content, reg_date,Notice_num "
					+ " FROM Notice "
					+ " WHERE email = '"+email+"' ";
			
			pstmt = conn.prepareStatement(sql);
			/*
			pstmt.setString(1, email);
			pstmt.setString(2, email);
			pstmt.setString(3, email);
			pstmt.setString(4, email);
			 */
			rs = pstmt.executeQuery();
			while(rs.next()) {
				
				MypageDTO dto = new MypageDTO();
				
				
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setBoard_name(rs.getString("board_name"));
				dto.setBoard_num(rs.getLong("board_num"));
				dto.setReg_date(rs.getString("reg_date"));
				list.add(dto);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	// 내가 쓴 댓글 카운트용도
	public List<MypageDTO> myListReply(String email){
		List<MypageDTO> list = new ArrayList<MypageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select board_name , replycontent , email , board_num "
					+ " from (select b.board_num , board_name , replycontent , b.email from board b join board_reply r on b.board_num = r.board_num ) "
					+ " where email = ? and replycontent is not null "
					+ " union "
					+ " select board_name, content, email , eventpage_num "
					+ " from(select b.eventpage_num , board_name , r.content , b.email from Eventpage b join Event_Reply r on b.Eventpage_num = r.Eventpage_num)"
					+ " where email = ? and content is not null "
					+ " union "
					+ " select board_name , answer_content , email , qna_num "
					+ " from qna "
					+ " where email = ? and answer_content is not null "
					+ " union "
					+ " select board_name , answer_content , email , rb_num "
					+ " from requestboard "
					+ " where email = ? and answer_content is not null";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			pstmt.setString(2, email);
			pstmt.setString(3, email);
			pstmt.setString(4, email);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MypageDTO dto = new MypageDTO();
				
				
				dto.setBoard_num(rs.getLong("board_num"));
				dto.setReply_content(rs.getString("replycontent"));
				dto.setBoard_name(rs.getString("board_name"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list;
	}
	
	// 내가 쓴 댓글 리스트
	public List<MypageDTO> myListReplyList(String email, int offset, int size){
		List<MypageDTO> list = new ArrayList<MypageDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select board_name , replycontent , email , board_num , B_ReplyDate as reply_reg_date "
					+ " from (select b.board_num ,B_ReplyDate, board_name , replycontent , b.email from board b join board_reply r on b.board_num = r.board_num ) "
					+ " where email = ? and replycontent is not null "
					+ " union "
					+ " select board_name, content, email , eventpage_num, reg_date as reply_reg_date "
					+ " from(select b.eventpage_num ,r.reg_date, board_name , r.content , b.email from Eventpage b join Event_Reply r on b.Eventpage_num = r.Eventpage_num) "
					+ " where email = ? and content is not null "
					+ " union "
					+ " select board_name , answer_content , email , qna_num ,answer_reg_date as reply_reg_date  "
					+ " from qna "
					+ " where email = ? and answer_content is not null "
					+ " union "
					+ " select board_name , answer_content , email , rb_num, answer_reg_date as reply_reg_date "
					+ " from requestboard "
					+ " where email = ? and answer_content is not null "
					+ " ORDER BY reply_reg_date DESC "
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			pstmt.setString(2, email);
			pstmt.setString(3, email);
			pstmt.setString(4, email);
			pstmt.setInt(5, offset);
			pstmt.setInt(6, size);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MypageDTO dto = new MypageDTO();
				
				
				dto.setBoard_num(rs.getLong("board_num"));
				dto.setReply_content(rs.getString("replycontent"));
				
				dto.setBoard_name(rs.getString("board_name"));
				dto.setReply_reg_date(rs.getString(5));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list;
	}
	
}

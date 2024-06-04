package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.ComplainDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class ComplainDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertComplain(ComplainDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into complain(comp_num , board_name , num , comp_reason , email) "
					+ " values (COMPLAINTBOARD_SEQ.nextval , ? , ? , ? , ? )";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getBoard_name());
			pstmt.setLong(2, dto.getNum());
			pstmt.setString(3, dto.getComp_reason());
			pstmt.setString(4, dto.getEmail());
			
			pstmt.executeUpdate();
		
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public List<ComplainDTO> listComplain() {
		List<ComplainDTO> list = new ArrayList<ComplainDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT comp_num, email, board_name, comp_reason , num");
			sb.append(" FROM complain ");
			sb.append(" ORDER BY comp_num DESC ");

			pstmt = conn.prepareStatement(sb.toString());
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				ComplainDTO dto = new ComplainDTO();

				dto.setComp_num(rs.getLong("comp_num"));
				dto.setEmail(rs.getString("email"));
				dto.setBoard_name(rs.getString("board_name"));
				dto.setComp_reason(rs.getString("comp_reason"));
				dto.setNum(rs.getLong("num"));

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
	
	public List<ComplainDTO> listComplain(int offset, int size) {
		List<ComplainDTO> list = new ArrayList<ComplainDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT comp_num, email, board_name, comp_reason , num");
			sb.append(" FROM complain ");
			sb.append(" ORDER BY comp_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				ComplainDTO dto = new ComplainDTO();

				dto.setComp_num(rs.getLong("comp_num"));
				dto.setEmail(rs.getString("email"));
				dto.setBoard_name(rs.getString("board_name"));
				dto.setComp_reason(rs.getString("comp_reason"));
				dto.setNum(rs.getLong("num"));

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
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM complain";
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
	
	
	
}

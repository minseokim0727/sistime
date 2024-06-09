package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.BoardDTO;
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
				    "    FOREIGN KEY (EMAIL) REFERENCES member1(EMAIL)," +
				    "    FOREIGN KEY (BOARD_ID) REFERENCES board_names(BOARD_ID)" +
				    ")";



			pstmt = conn.prepareStatement(sql);
			pstmt.execute();

			pstmt = null;

			sql = "CREATE SEQUENCE " + name + "_seq " + " START WITH 1" + " INCREMENT BY 1" + " NOCACHE" + " NOCYCLE";

			pstmt = conn.prepareStatement(sql);
			pstmt.execute();

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
			
			while(rs.next()) {
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

}

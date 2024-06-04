package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.ComplainDTO;
import com.sist.domain.NoticeDTO;
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
	
	
	
	
}

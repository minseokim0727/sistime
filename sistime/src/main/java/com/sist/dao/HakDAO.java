package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.HakDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class HakDAO {
	private Connection conn = DBConn.getConnection();
	
	public List<HakDTO> hakselect(String sub_year, String sub_sem, String email) {
		HakDTO dto = null;
		List<HakDTO> list = new ArrayList<HakDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT DISTINCT(sub.sub_name), sub.sub_grade "
					+ " FROM sub_timeboard "
					+ " JOIN ( "
					+ "    SELECT sem_num "
					+ "    FROM sub_semester "
					+ "    WHERE sub_year = ? AND sub_sem = ? AND email = ? "
					+ " ) mysemnum ON sub_timeboard.sem_num = mysemnum.sem_num "
					+ " JOIN sub_time ON sub_timeboard.sub_tnum = sub_time.sub_tnum "
					+ " JOIN sub ON sub.sub_num = sub_time.sub_num ";
			
			pstmt = conn.prepareStatement(sql);
			
			
			pstmt.setString(1, sub_year);
	        pstmt.setString(2, sub_sem);
	        pstmt.setString(3, email);
	        rs = pstmt.executeQuery();
	        
			while (rs.next()) {
				 dto = new HakDTO();

				dto.setSub_name(rs.getString("sub_name"));
				dto.setSub_grade(rs.getInt("sub_grade"));

				
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
}

package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.domain.TimeDTO;
import com.sist.util.DBConn;
import com.sist.util.DBUtil;

public class TimeDAO {

	private Connection conn = DBConn.getConnection();
	
	public void insertSublist(String year,String semester,String email) throws SQLException{
		PreparedStatement ps = null;
		String sql;
		
		try {
			sql ="insert into sub_semester(sem_num , sub_year , sub_sem , tot_grade , email ) "
					+ "values (sub_semester_seq.nextval , ? , ? , 18 , ?)";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, year);
			ps.setString(2, semester);
			ps.setString(3, email);
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(ps);
		}
	}
	
	public List<TimeDTO> semesterList(String email) throws SQLException{
		TimeDTO dto = null;
		List<TimeDTO> list = new ArrayList<TimeDTO>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select sem_num , sub_year , sub_sem from sub_semester where email = ?";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			
			rs=ps.executeQuery();
			
			while(rs.next()) {
				dto = new TimeDTO();
				dto.setSem_num(rs.getInt("sem_num"));
				dto.setSub_year(rs.getString("sub_year"));
				dto.setSemester(rs.getString("sub_sem"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(ps);
		}
		
		return list;
	}
	
	public List<TimeDTO> semesterSelect(String sub_year , String semester) throws SQLException{
		
		TimeDTO dto = null;
		List<TimeDTO> list = new ArrayList<TimeDTO>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select s.sub_num , sub_name , sub_pro , sub_year , semester , sub_grade , "
					+ " listagg ( sub_date || ' (' || t.sub_start || '-' || sub_end || ')' , ' , ') within group (order by sub_date ) as sub_schedule "
					+ " from sub s "
					+ " join sub_time t on s.sub_num = t.sub_num "
					+ " where sub_year = ? and semester = ? "
					+ " group by s.sub_num , sub_name , sub_pro , sub_year , semester , sub_grade";
			
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, sub_year);
			ps.setString(2, semester);
			
			rs=ps.executeQuery();
			
			while(rs.next()) {
				dto = new TimeDTO();
				
				dto.setSub_num(rs.getInt("sub_num"));
				dto.setSub_name(rs.getString("sub_name"));
				dto.setSub_pro(rs.getString("sub_pro"));
				dto.setSub_year(sub_year);
				dto.setSemester(semester);
				dto.setSub_grade(rs.getInt("sub_grade"));
				dto.setSub_time(rs.getString("sub_schedule"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(ps);
		}
		
		return list;
	}
	
	public void insertSub(int sub_num , int sem_num) throws SQLException{
		PreparedStatement ps = null;
		String sql;
		
		try {
			sql = "insert into sub_timeboard (tb_num, sem_num, sub_tnum)\r\n"
					+ "with subject_schedule as (\r\n"
					+ "    select \r\n"
					+ "        s.sub_num, \r\n"
					+ "        s.sub_name, \r\n"
					+ "        s.sub_pro, \r\n"
					+ "        s.sub_year, \r\n"
					+ "        s.semester, \r\n"
					+ "        s.sub_grade, \r\n"
					+ "        listagg(t.sub_date || ' (' || t.sub_start || '-' || t.sub_end || ')', ', ') \r\n"
					+ "            within group (order by t.sub_date) as sub_schedule\r\n"
					+ "    from\r\n"
					+ "        sub s\r\n"
					+ "    join\r\n"
					+ "        sub_time t \r\n"
					+ "    on\r\n"
					+ "        s.sub_num = t.sub_num\r\n"
					+ "    where s.sub_num = ?\r\n"
					+ "    group by\r\n"
					+ "        s.sub_num, s.sub_name, s.sub_pro, s.sub_year, s.semester, s.sub_grade\r\n"
					+ ")\r\n"
					+ "select sub_timeboard_seq.nextval, \r\n"
					+ "       (select sem_num from sub_semester where sem_num=?),\r\n"
					+ "       s.sub_num \r\n"
					+ "from subject_schedule s";
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, sub_num);
			ps.setInt(2, sem_num);
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(ps);
		}
	}
}

package com.sist.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.dao.TimeDAO;
import com.sist.domain.SessionInfo;
import com.sist.domain.TimeDTO;
import com.sist.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class TimeController {
	
	@RequestMapping("/timetable/list")
	public ModelAndView tolist(HttpServletRequest req, HttpServletResponse resp) throws SQLException , IOException{
		ModelAndView mav = new ModelAndView("timetable/list");
		TimeDAO dao = new TimeDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		try {
			List<TimeDTO> list = dao.semesterList(info.getEmail());
			
			mav.addObject("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
	
	@RequestMapping(value="/timetable/newsublist",method = RequestMethod.GET)
	public ModelAndView insertSublist(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
		ModelAndView mav = new ModelAndView("timetable/list");
		TimeDAO dao = new TimeDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
    	
    	try {	
    		
    		String sub_year = req.getParameter("year");
	        String semester = req.getParameter("semester");

	        dao.insertSublist(sub_year, semester, info.getEmail());
  
	        mav.addObject("year", sub_year);
	        mav.addObject("semester", semester);
	        
	        
	           
		} catch (Exception e) {
			e.printStackTrace();
		}	        
        
    	
    	return new ModelAndView("redirect:/timetable/list");
		
	}
	
	@RequestMapping(value="/timetable/sublist",method = RequestMethod.GET)
    public ModelAndView tosublist(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
    	ModelAndView mav = new ModelAndView("timetable/sublist");
    	TimeDAO dao = new TimeDAO();
    	
    	try {
    		int sem_num = Integer.parseInt(req.getParameter("sem_num"));
    		String sub_year = req.getParameter("year");
	        String semester = req.getParameter("semester");
	        
	        
	        List<TimeDTO> list = dao.semesterSelect(sub_year, semester);
	        		        
	        mav.addObject("list", list);
	        mav.addObject("year", sub_year);
	        mav.addObject("semester", semester);
	        mav.addObject("sem_num", sem_num);
	           
		} catch (Exception e) {
			e.printStackTrace();
		}	        
        
    	
        return mav;
    }
	@RequestMapping(value = "/timetable/insert", method = RequestMethod.POST)
	public ModelAndView insertSub(HttpServletRequest req, HttpServletResponse resp) throws SQLException , IOException{
		TimeDAO dao = new TimeDAO();
			String year = null;
			String semester =null;
			int sub_num = 0;
			int sem_num = 0;
		
		
		try {
			sem_num = Integer.parseInt(req.getParameter("sem_num"));
			
			year = req.getParameter("sub_year");
			semester = req.getParameter("semester");
			System.out.println(sem_num);
			System.out.println(year);
			System.out.println(semester);
			sub_num = Integer.parseInt(req.getParameter("sub_num"));
			System.out.println(sub_num);
			dao.insertSub(sub_num, sem_num);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/timetable/sublist?sem_num=" + sem_num + "&year=" + year + "&semester=" + semester);
	}
	
	
	
	
	
}
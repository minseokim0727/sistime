package com.sist.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.dao.CreateDAO;
import com.sist.dao.HakDAO;
import com.sist.domain.CreateDTO;
import com.sist.domain.HakDTO;
import com.sist.domain.SessionInfo;
import com.sist.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class HakController {
	    @RequestMapping(value="/test/grade",method = RequestMethod.GET)
	    public ModelAndView gradeForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
	    	ModelAndView mav = new ModelAndView("test/grade");
	    	HakDAO dao = new HakDAO();
	    	
	    	HttpSession session = req.getSession();

	    	SessionInfo info = (SessionInfo) session.getAttribute("member");
	    	
	    	try {
	    		String sub_year = req.getParameter("year");
		        String sub_sem = req.getParameter("semester");
		        String email = info.getEmail();
		        
		        List<HakDTO> list = dao.hakselect(sub_year, sub_sem, email);
		        CreateDAO createDAO = new CreateDAO();
				List<CreateDTO> listcreate = createDAO.selectBoardname();
				// 최근 베스트 게시판 5개
				
				mav.addObject("listcreate", listcreate);
		        mav.addObject("list", list);
		           
			} catch (Exception e) {
				e.printStackTrace();
			}	        
	        
	    	
	        return mav;
	    }
	    
}
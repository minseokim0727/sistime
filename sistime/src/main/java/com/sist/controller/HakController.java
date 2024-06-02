package com.sist.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.List;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.dao.HakDAO;
import com.sist.domain.HakDTO;
import com.sist.servlet.ModelAndView;
import com.sist.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HakController {
	    @RequestMapping(value="/test/template",method = RequestMethod.POST)
	    public ModelAndView gradeForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
	    	ModelAndView mav = new ModelAndView("/test/template");
	    	HakDAO dao = new HakDAO();
	    	List<HakDTO> list = null;
	    	
	    	try {
	    		String sub_year = req.getParameter("sub_year");
		        String sub_sem = req.getParameter("sub_sem");
		        String email = req.getParameter("email");
		        
		        list = dao.hakselect(sub_year, sub_sem, email);
		        
		        mav.addObject("list", list);
		        
			} catch (Exception e) {
				e.printStackTrace();
			}	        
	        
	        
	        return mav;
	    }
	    
}

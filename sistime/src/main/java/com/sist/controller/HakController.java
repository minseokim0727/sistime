package com.sist.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.dao.HakDAO;
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
	    	System.out.print(1);
	    	HakDAO dao = new HakDAO();
	    	
	    	HttpSession session = req.getSession();

	    	SessionInfo info = (SessionInfo) session.getAttribute("member");
	    	
	    	try {
	    		String sub_year = req.getParameter("sub_year");
		        String sub_sem = req.getParameter("sub_sem");
		        String email = info.getEmail();
		        
		        List<HakDTO> list = dao.hakselect(sub_year, sub_sem, email);
		        
		        mav.addObject("list", list);
		        
		        for(HakDTO hak1 : list) {
		            System.out.println(hak1.getSub_name());
		            System.out.println(hak1.getSub_grade());
		         }
		        // DAO의 쿼리는 제대로 되어 있지만, 컨트롤러에서 36번째 코드에서
		        // 제대로 값을 받아오지 못하고있음
		        System.out.print(2);
			} catch (Exception e) {
				e.printStackTrace();
			}	        
	        
	    	
	        return mav;
	    }
	    
}
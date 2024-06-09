package com.sist.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.annotation.ResponseBody;
import com.sist.dao.CreateDAO;
import com.sist.domain.CreateDTO;
import com.sist.servlet.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminController {
	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	public ModelAndView adminMain(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/home");
		
		return mav;
	}

	@RequestMapping(value = "/admin/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/write");
		
		return mav;
	}
	
	
	@RequestMapping(value = "/admin/write", method = RequestMethod.POST)
	public ModelAndView adminWrite(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		ModelAndView mav = new ModelAndView("admin/list");
		String name = req.getParameter("pageName");
		CreateDAO dao = new CreateDAO();
		dao.createBoard(name);
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value = "/admin/pageSend", method = RequestMethod.POST)
	public Map<String, Object> pageSend(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		System.out.println("실행");
		CreateDAO dao = new CreateDAO();
		String pageName = req.getParameter("pageName");
		String pageDescription = req.getParameter("pageDescription");

		
		dao.insertBoardname(pageName, pageDescription);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("passed", "true");

		return map;
	}
	
	@RequestMapping(value = "/admin/list", method = RequestMethod.GET)
	public ModelAndView listForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/list");
		int num = Integer.parseInt(req.getParameter("board_id"));
		
		return mav;
	}

}

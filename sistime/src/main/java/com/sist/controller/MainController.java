package com.sist.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.dao.BoardDAO;
import com.sist.dao.CreateDAO;
import com.sist.dao.EventDAO;
import com.sist.dao.NoticeDAO;
import com.sist.domain.BoardDTO;
import com.sist.domain.CreateDTO;
import com.sist.domain.EventDTO;
import com.sist.domain.NoticeDTO;
import com.sist.servlet.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MainController {
	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
		ModelAndView mav = new ModelAndView("main/main");
		
		// 최근 공지 5개
		NoticeDAO noticeDAO = new NoticeDAO();
		List<NoticeDTO> listNotice = noticeDAO.listNotice(0,5);
		
		// 최근 이벤트 페이지 5개
		EventDAO eventDAO = new EventDAO();
		List<EventDTO> listEvent = eventDAO.listEvent(0,5);
		
		// 최근 자유 게시판 5개
		BoardDAO boardDAO = new BoardDAO();
		List<BoardDTO> listboard = boardDAO.listBoard(0, 5);
		
		
		
		
		
		
		mav.addObject("listNotice", listNotice);
		mav.addObject("listEvent", listEvent);
		mav.addObject("listboard", listboard);
	
		return mav;
	}

}

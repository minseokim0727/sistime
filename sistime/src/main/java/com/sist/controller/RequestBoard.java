package com.sist.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.dao.RequestBoardDAO;
import com.sist.domain.RequestBoardDTO;
import com.sist.domain.SessionInfo;
import com.sist.servlet.ModelAndView;
import com.sist.util.MyUtil;
import com.sist.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class RequestBoard {
	@RequestMapping(value = "/requestboard/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ModelAndView mav = new ModelAndView("requestboard/list");
		
		RequestBoardDAO dao = new RequestBoardDAO();
		MyUtil util = new MyUtilBootstrap(); 
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}
			// 검색
			String kwd = req.getParameter("kwd");
			if (kwd == null) {
				kwd = "";
			}
			
			// GET 방식인 경우 디코딩
			if (req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}			
			// 전체 데이터 개수
			int dataCount;
			if (kwd.length() == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(kwd);
			}		
			
			// 전체 페이지 수
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}
			
			// 게시물 가져오기
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;		
			
			List<RequestBoardDTO> list = null;
			if(kwd.length() == 0) {
				list = dao.listRequestBoard(offset, size);
			} else {
				//list = dao.listQuestion(offset, size, kwd);
			}
			
			
			String query = "";
			if (kwd.length() != 0) {
				query = "kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			
			
			String cp = req.getContextPath();
			String listUrl = cp + "/requestboard/list";
			String articleUrl = cp + "/requestboard/article?page=" + current_page;
			if (query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}
			
			String paging = util.paging(current_page, total_page, listUrl);

			// 포워딩할 JSP에 전달할 속성
			mav.addObject("list", list);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);
			mav.addObject("kwd", kwd);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/requestboard/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		ModelAndView mav = new ModelAndView("requestboard/write");		
		mav.addObject("mode", "write");
		return mav;
	}
	@RequestMapping(value = "/requestboard/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RequestBoardDAO dao = new RequestBoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		
		try {
			RequestBoardDTO dto = new RequestBoardDTO();
			
			dto.setEmail(info.getEmail());
			
			// 파라미터
			dto.setSecret(Integer.parseInt(req.getParameter("secret")));
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			dto.setReason(req.getParameter("reason"));
			
			dao.insertRequestBoard(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return new ModelAndView("redirect:/requestboard/list");
	}
	
	// 글 보기
	@RequestMapping(value = "/requestboard/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RequestBoardDAO dao = new RequestBoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		
		try {
			long requestboardnum = Long.parseLong(req.getParameter("num"));
			
			String kwd = req.getParameter("kwd");
			if (kwd == null) {
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");
			
			if (kwd.length() != 0) {
				query += "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
			}
			
			RequestBoardDTO dto = dao.findById(requestboardnum);
			if (dto == null) {
				return new ModelAndView("redirect:/requestboard/list?" + query);
			}
			
			if(dto.getSecret() == 1) {
				if( ! dto.getEmail().equals(info.getEmail()) && ! info.getEmail().equals("admin") ) {
					return new ModelAndView("redirect:/requestboard/list?" + query);
				}
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			if(dto.getAnswer_content() != null) {
				dto.setAnswer_content(dto.getAnswer_content().replaceAll("\n", "<br>"));
			}
			// 이전글 다음글
			RequestBoardDTO prevDto = dao.findByPrev(dto.getRB_num(), kwd);
			RequestBoardDTO nextDto = dao.findByNext(dto.getRB_num(), kwd);

			ModelAndView mav = new ModelAndView("requestboard/article");			
			
			// JSP로 전달할 속성
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);			
			
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/requestboard/list?" + query);
	}
	
	@RequestMapping(value = "/requestboard/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RequestBoardDAO dao = new RequestBoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");		
		
		
		try {
			long num = Long.parseLong(req.getParameter("num"));
			RequestBoardDTO dto = dao.findById(num);
			
			if (dto == null) {
				return new ModelAndView("redirect:/requestboard/list?page=" + page);
			}
			
			// 게시물을 올린 사용자가 아니면
			if (! dto.getEmail().equals(info.getEmail())) {
				return new ModelAndView("redirect:/requestboard/list?page=" + page);
			}
			ModelAndView mav = new ModelAndView("requestboard/write");
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");
			
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/requestboard/list?page=" + page);
	}
	
	@RequestMapping(value = "/requestboard/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RequestBoardDAO dao = new RequestBoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		
		try {
			RequestBoardDTO dto = new RequestBoardDTO();
			
			dto.setRB_num(Long.parseLong(req.getParameter("num")));
			dto.setSecret(Integer.parseInt(req.getParameter("secret")));
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			
			
			dto.setEmail(info.getEmail());
			
			dao.updateRequestBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return new ModelAndView("redirect:/requestboard/list?page=" + page);
	}
	
	@RequestMapping(value = "/requestboard/answer", method = RequestMethod.POST)
	public ModelAndView answerSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestBoardDAO dao = new RequestBoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		if (! info.getEmail().equals("admin")) {
			return new ModelAndView("redirect:/requestboard/list");
		}
		
		String page = req.getParameter("page");
		
		try {
			RequestBoardDTO dto = new RequestBoardDTO();
			
			dto.setRB_num(Long.parseLong(req.getParameter("num")));
			dto.setAnswer_content(req.getParameter("answer"));
			
			dao.updateAnswer(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/requestboard/list?page=" + page);
	}
	@RequestMapping(value = "/requestboard/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		RequestBoardDAO dao = new RequestBoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		try {
			long num = Long.parseLong(req.getParameter("num"));
			String mode = req.getParameter("mode");
			
			String kwd = req.getParameter("kwd");
			if (kwd == null) {
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");

			if (kwd.length() != 0) {
				query += "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
			}
			
			if(mode.equals("answer") && info.getEmail().equals("admin")) {
				// 답변 삭제
				RequestBoardDTO dto = new RequestBoardDTO();
				dto.setRB_num(num);
				dto.setAnswer_content("");
				
				dao.updateAnswer(dto);
			} else if(mode.equals("question")) {
				// 질문 삭제
				dao.deleteRequestBoard(num, info.getEmail());
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/requestboard/list?" + query);
	}
}
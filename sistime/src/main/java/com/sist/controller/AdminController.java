package com.sist.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.annotation.ResponseBody;
import com.sist.dao.BoardDAO;
import com.sist.dao.CreateDAO;
import com.sist.domain.BoardDTO;
import com.sist.domain.SessionInfo;
import com.sist.servlet.ModelAndView;
import com.sist.util.MyUtil;
import com.sist.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
		String name = req.getParameter("board_name");
		
		CreateDAO dao = new CreateDAO();
		MyUtil util = new MyUtilBootstrap();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}
			
			// 검색
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}

			// GET 방식인 경우 디코딩
			if (req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}

			// 전체 데이터 개수
			int dataCount;
			if (kwd.length() == 0) {
				dataCount = dao.dataCount(name);
			} else {
				dataCount = dao.dataCount(schType, kwd,name);
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
			
			List<BoardDTO> list = null;
			if (kwd.length() == 0) {
				list = dao.listBoard(offset, size,name);
			} else {
				list = dao.listBoard(offset, size, schType, kwd,name);
			}

			String query = "";
			if (kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}

			// 페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/admin/list";
			String articleUrl = cp + "/admin/article?page=" + current_page;
			if (query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}

			String paging = util.paging(current_page, total_page, listUrl);

			// 포워딩할 JSP에 전달할 속성
			mav.addObject("board_name", name);
			mav.addObject("list", list);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/write2", method = RequestMethod.GET)
	public ModelAndView realWrite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		ModelAndView mav = new ModelAndView("admin/write2");
		String name = req.getParameter("board_name");
		mav.addObject("board_name", name);
		
		mav.addObject("mode", "write2?board_name="+name);
		return mav;
	}
	
	@RequestMapping(value = "/admin/write2", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		CreateDAO dao = new CreateDAO();
		String board_name = req.getParameter("board_name");
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		int board_id = 0;
		try {
			BoardDTO dto = new BoardDTO();
			board_id = dao.findbyBoardId(board_name);

			
			dto.setEmail(info.getEmail());

			
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));

			dao.insertBoard(dto,board_name,board_id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/list?board_name="+board_name);
	}

}

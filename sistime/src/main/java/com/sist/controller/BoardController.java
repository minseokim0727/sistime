package com.sist.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.dao.BoardDAO;
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
public class BoardController {
	@RequestMapping(value = "/board/list", method = RequestMethod.GET)
		public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 게시물 리스트
			ModelAndView mav = new ModelAndView("board/list");

			BoardDAO dao = new BoardDAO();
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
					dataCount = dao.dataCount();
				} else {
					dataCount = dao.dataCount(schType, kwd);
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
					list = dao.listBoard(offset, size);
				} else {
					list = dao.listBoard(offset, size, schType, kwd);
				}

				String query = "";
				if (kwd.length() != 0) {
					query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
				}

				// 페이징 처리
				String cp = req.getContextPath();
				String listUrl = cp + "/board/list";
				String articleUrl = cp + "/board/article?page=" + current_page;
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
				mav.addObject("schType", schType);
				mav.addObject("kwd", kwd);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			// JSP로 포워딩
			return mav;
		}
	
	@RequestMapping(value = "/board/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		ModelAndView mav = new ModelAndView("board/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/board/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		BoardDAO dao = new BoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		try {
			BoardDTO dto = new BoardDTO();

			// userId는 세션에 저장된 정보
			dto.setEmail(info.getEmail());

			// 파라미터
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));

			dao.insertBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/board/list");
	}
	
	@RequestMapping(value = "/board/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtilBootstrap();
		
		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			long num = Long.parseLong(req.getParameter("num"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
			}

			// 조회수 증가
			dao.updateHitCount(num);

			// 게시물 가져오기
			BoardDTO dto = dao.findById(num);
			if (dto == null) { // 게시물이 없으면 다시 리스트로
				return new ModelAndView("redirect:/board/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));

			// 이전글 다음글
			BoardDTO prevDto = dao.findByPrev(dto.getBoard_num(), schType, kwd);
			BoardDTO nextDto = dao.findByNext(dto.getBoard_num(), schType, kwd);
			
			// 로그인 유지의 게시글 공감 여부
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			// boolean isUserLike = dao.isUserBoardLike(num, info.getEmail());
			
			ModelAndView mav = new ModelAndView("board/article");
			
			
			// JSP로 전달할 속성
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			// mav.addObject("isUserLike", isUserLike);
			// 포워딩
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/board/list?" + query);
	}
	
	@RequestMapping(value = "/board/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		BoardDAO dao = new BoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");

		try {
			long num = Long.parseLong(req.getParameter("num"));
			BoardDTO dto = dao.findById(num);

			if (dto == null) {
				return new ModelAndView("redirect:/board/list?page=" + page);
			}

			// 게시물을 올린 사용자가 아니면
			if (! dto.getEmail().equals(info.getEmail())) {
				return new ModelAndView("redirect:/board/list?page=" + page);
			}

			ModelAndView mav = new ModelAndView("board/write");
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/board/list?page=" + page);
	}
	
	@RequestMapping(value = "/board/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		BoardDAO dao = new BoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		try {
			BoardDTO dto = new BoardDTO();
			
			dto.setBoard_num(Long.parseLong(req.getParameter("Board_num")));
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));

			dto.setEmail(info.getEmail());

			dao.updateBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/board/list?page=" + page);
	}
	
	@RequestMapping(value = "/board/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		BoardDAO dao = new BoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			long num = Long.parseLong(req.getParameter("num"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
			}

			dao.deleteBoard(num, info.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/board/list?" + query);
	}
	
	
}

package com.sist.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.annotation.ResponseBody;
import com.sist.dao.SecretDAO;
import com.sist.dao.BanDAO;
import com.sist.dao.ComplainDAO;
import com.sist.dao.CreateDAO;
import com.sist.domain.BoardDTO;
import com.sist.domain.Board_ReplyDTO;
import com.sist.domain.ComplainDTO;
import com.sist.domain.CreateDTO;
import com.sist.domain.SessionInfo;
import com.sist.servlet.ModelAndView;
import com.sist.util.MyUtil;
import com.sist.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class SecretController {
	@RequestMapping(value = "/anonymous/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		ModelAndView mav = new ModelAndView("anonymous/list");
		
		SecretDAO dao = new SecretDAO();
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
			String listUrl = cp + "/anonymous/list";
			String articleUrl = cp + "/anonymous/article?page=" + current_page;
			if (query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}

			String paging = util.paging(current_page, total_page, listUrl);
			
			CreateDAO createDAO = new CreateDAO();
			List<CreateDTO> listcreate = createDAO.selectBoardname();
			// 최근 베스트 게시판 5개
			
			mav.addObject("listcreate", listcreate);
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
		return mav;
	}
	
	@RequestMapping(value = "/anonymous/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		ModelAndView mav = new ModelAndView("anonymous/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/anonymous/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		SecretDAO dao = new SecretDAO();
		
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

		return new ModelAndView("redirect:/anonymous/list");
	}
	
	@RequestMapping(value = "/anonymous/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		SecretDAO dao = new SecretDAO();
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
				return new ModelAndView("redirect:/anonymous/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));

			// 이전글 다음글
			BoardDTO prevDto = dao.findByPrev(dto.getBoard_num(), schType, kwd);
			BoardDTO nextDto = dao.findByNext(dto.getBoard_num(), schType, kwd);
			
			// 로그인 유지의 게시글 공감 여부
			// HttpSession session = req.getSession();
			// SessionInfo info = (SessionInfo)session.getAttribute("member");
			// boolean isUserLike = dao.isUserBoardLike(num, info.getEmail());
			
			ModelAndView mav = new ModelAndView("anonymous/article");
			
			
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

		return new ModelAndView("redirect:/anonymous/list?" + query);
	}
	
		
		// 댓글/답글 저장 - AJAX/JSON
		@ResponseBody
		@RequestMapping(value = "/anonymous/insertReply", method = RequestMethod.POST)
		public Map<String, Object> insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			Map<String, Object> model = new HashMap<String, Object>();
			
			// 넘어온 파라미터 : 게시글번호, 댓글(답글), 부모번호
			
			SecretDAO dao = new SecretDAO();
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			String state = "false";
			try {
				Board_ReplyDTO dto = new Board_ReplyDTO();
				
				long num = Long.parseLong(req.getParameter("num"));
				dto.setBoard_num(num);
				dto.setEmail(info.getEmail());
				dto.setReplycontent(req.getParameter("content"));
				
				dao.insertReply(dto);
				
				state = "true";
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			model.put("state", state);
			
			return model;
		}
		
		
		// 댓글 리스트 - AJAX : Text
		@RequestMapping(value = "/anonymous/listReply", method = RequestMethod.GET)
		public ModelAndView listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 넘어온 파라미터 : 글번호 [,페이지번호]
			
			SecretDAO dao = new SecretDAO();
			MyUtil util = new MyUtilBootstrap();
			
			try {
				long num = Long.parseLong(req.getParameter("num"));
				String pageNo = req.getParameter("pageNo");
				int current_page = 1;
				if(pageNo != null) {
					current_page = Integer.parseInt(pageNo);
				}
				
				int size = 5;
				int total_page = 0;
				int replyCount = 0;
				
				replyCount = dao.dataCountReply(num);
				total_page = util.pageCount(replyCount, size);
				if(current_page > total_page) {
					current_page = total_page;
				}
				
				int offset = (current_page - 1) * size;
				if(offset < 0) offset = 0;
				
			 	List<Board_ReplyDTO> listReply = dao.listReply(num, offset, size);

				for(Board_ReplyDTO dto : listReply) {
					dto.setReplycontent(dto.getReplycontent().replaceAll("\n", "<br>"));
				}
				
				// 페이징 : 자바 스크립트 함수(listPage)를 호출
				String paging = util.pagingMethod(current_page, total_page, "listPage");
				
				ModelAndView mav = new ModelAndView("anonymous/listReply");
				
				mav.addObject("listReply", listReply);
				mav.addObject("pageNo", current_page);
				mav.addObject("replyCount", replyCount);
				mav.addObject("total_page", total_page);
				mav.addObject("paging", paging);
				
				return mav;
				
			} catch (Exception e) {
				e.printStackTrace();
				
				resp.sendError(400);
				
				throw e;
			}
		}
	
		
		@ResponseBody
		@RequestMapping(value = "/anonymous/deleteReply", method = RequestMethod.POST)
		public Map<String, Object> deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			Map<String, Object> model = new HashMap<String, Object>();
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			
			String state = "false";
			
			SecretDAO dao = new SecretDAO();
			try {
				long replyNum = Long.parseLong(req.getParameter("replyNum"));
				
				dao.deleteReply(replyNum, info.getEmail());
				
				state = "true";
			} catch (Exception e) {
			}
			
			model.put("state", state);
			
			return model;
		}
		
		// 게시글 공감 저장 - AJAX/JSON
		@ResponseBody
		@RequestMapping(value = "/anonymous/insertBoardLike", method = RequestMethod.POST)
		public Map<String, Object> insertBoardLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// 넘어온 파라미터 : 글번호, 공감/공감취소여부
			Map<String, Object> model = new HashMap<String, Object>();
			
			SecretDAO dao = new SecretDAO();
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			
			String state = "false";
			int boardLikeCount = 0;
			
			try {
				long num = Long.parseLong(req.getParameter("num"));
				String isNoLike = req.getParameter("isNoLike");
				
				if(isNoLike.equals("true")) {
					// 공감
					dao.insertBoardLike(num, info.getEmail());
				} else {
					// 공감 취소
					dao.deleteBoardLike(num, info.getEmail());
				}
				
				boardLikeCount = dao.countBoardLike(num);
				
				state = "true";
			} catch (Exception e) {
			}
			
			model.put("state", state);
			model.put("boardLikeCount", boardLikeCount);
			
			return model;
		}
		
		@RequestMapping(value = "/anonymous/complain", method = RequestMethod.GET)
		public ModelAndView complain(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			// 신고
			ComplainDAO dao = new ComplainDAO();

			String page = req.getParameter("page");
			String size = req.getParameter("size");
			long num = Long.parseLong(req.getParameter("num"));

			try {
				ComplainDTO dto = new ComplainDTO();

				String board_name = req.getParameter("board_name");
				String email = req.getParameter("email");
				String comp_reason = req.getParameter("comp_reason");

				dto.setBoard_name(board_name);
				dto.setComp_reason(comp_reason);
				dto.setEmail(email);
				dto.setNum(num);

				dao.insertComplain(dto);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return new ModelAndView("redirect:/anonymous/article?page=" + page + "&size=" + size + "&num=" + num);

		}
		
		@RequestMapping(value = "/anonymous/ban", method = RequestMethod.GET)
		public ModelAndView ban(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			// 신고
			BanDAO dao = new BanDAO();

			String page = req.getParameter("page");
			String size = req.getParameter("size");
			long num = Long.parseLong(req.getParameter("num"));
			try {
				int ban_date = Integer.parseInt(req.getParameter("ban_date"));
				String email = req.getParameter("email");
				String ban_reason = req.getParameter("ban_reason");
				
				dao.insertBan(ban_date, ban_reason, email);
				

			} catch (Exception e) {
				e.printStackTrace();
			}

			return new ModelAndView("redirect:/anonymous/article?page=" + page + "&size=" + size + "&num=" + num );

		}
}
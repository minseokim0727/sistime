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
import com.sist.dao.CreateDAO;
import com.sist.domain.BoardDTO;
import com.sist.domain.Board_ReplyDTO;
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
			throws ServletException, IOException, SQLException {
		ModelAndView mav = new ModelAndView("admin/list");
		String board_name = req.getParameter("board_name");
		CreateDAO createDAO = new CreateDAO();
		List<CreateDTO> listcreate = createDAO.selectBoardname();
		// 최근 베스트 게시판 5개
		
		
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
				dataCount = dao.dataCount(board_name);
			} else {
				dataCount = dao.dataCount(schType, kwd, board_name);
			}

			// 전체 페이지 수
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			// 게시물 가져오기
			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			List<BoardDTO> list = null;
			if (kwd.length() == 0) {
				list = dao.listBoard(offset, size, board_name);
			} else {
				list = dao.listBoard(offset, size, schType, kwd, board_name);
			}

			String query = "";
			if (kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			String description = dao.findDescription(board_name);
			// 페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/admin/list";
			String articleUrl = cp + "/admin/article?page=" + current_page + "&board_name=" + board_name;

			if (query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}

			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩할 JSP에 전달할 속성
			mav.addObject("description", description);
			mav.addObject("board_name", board_name);
			mav.addObject("list", list);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
			mav.addObject("listcreate", listcreate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/admin/write2", method = RequestMethod.GET)
	public ModelAndView realWrite(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 글쓰기 폼
		ModelAndView mav = new ModelAndView("admin/write2");
		String name = req.getParameter("board_name");
		mav.addObject("board_name", name);

		mav.addObject("mode", "write2?board_name=" + name);
		return mav;
	}

	@RequestMapping(value = "/admin/write2", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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

			dao.insertBoard(dto, board_name, board_id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/list?board_name=" + board_name);
	}

	@RequestMapping(value = "/admin/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		CreateDAO dao = new CreateDAO();
		MyUtil util = new MyUtilBootstrap();
		String board_name = req.getParameter("board_name");
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
			dao.updateHitCount(num, board_name);

			// 게시물 가져오기
			BoardDTO dto = dao.findById(num, board_name);
			if (dto == null) { // 게시물이 없으면 다시 리스트로
				return new ModelAndView("redirect:/admin/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));

			// 이전글 다음글
			BoardDTO prevDto = dao.findByPrev(dto.getBoard_num(), schType, kwd, board_name);
			BoardDTO nextDto = dao.findByNext(dto.getBoard_num(), schType, kwd, board_name);

			ModelAndView mav = new ModelAndView("admin/article");

			// JSP로 전달할 속성
			mav.addObject("board_name", board_name);
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

		return new ModelAndView("redirect:/admin/list?" + query);
	}

	@RequestMapping(value = "/admin/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 수정 폼
		CreateDAO dao = new CreateDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String board_name = req.getParameter("board_name");
		String page = req.getParameter("page");

		try {
			long num = Long.parseLong(req.getParameter("num"));
			BoardDTO dto = dao.findById(num, board_name);

			if (dto == null) {
				return new ModelAndView("redirect:/admin/list?board_name="+board_name+"&page="+page);
			}

			// 게시물을 올린 사용자가 아니면
			if (!dto.getEmail().equals(info.getEmail())) {
				return new ModelAndView("redirect:/admin/list?board_name=" + board_name + "&page=" + page);
			}
			long board_num = dto.getBoard_num();
			ModelAndView mav = new ModelAndView("admin/write2");
			mav.addObject("board_name", board_name);
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update?board_name="+board_name+"&page="+page+"&board_num="+board_num);

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/list?board_name=" + board_name + "&page=" + page);
	}

	@RequestMapping(value = "/admin/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 수정 완료
		CreateDAO dao = new CreateDAO();
		String board_name = req.getParameter("board_name");
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		

		String page = req.getParameter("page");
		try {
			BoardDTO dto = new BoardDTO();

			dto.setBoard_num(Long.parseLong(req.getParameter("board_num")));
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));

			dto.setEmail(info.getEmail());

			dao.updateBoard(dto, board_name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/list?board_name=" + board_name + "&page=" + page);
	}

	@RequestMapping(value = "/admin/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		CreateDAO dao = new CreateDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String board_name = req.getParameter("board_name");
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

			dao.deleteBoard(num, info.getEmail(), board_name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/list?board_name=" + board_name + "&page=" + page);
	}

	@ResponseBody
	@RequestMapping(value = "/admin/insertReply", method = RequestMethod.POST)
	public Map<String, Object> insertReply(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();

		// 넘어온 파라미터 : 게시글번호, 댓글(답글), 부모번호

		CreateDAO dao = new CreateDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String board_name = req.getParameter("board_name");
		String state = "false";
		try {
			Board_ReplyDTO dto = new Board_ReplyDTO();

			long num = Long.parseLong(req.getParameter("num"));
			dto.setBoard_num(num);
			dto.setEmail(info.getEmail());
			dto.setReplycontent(req.getParameter("content"));

			dao.insertReply(dto, board_name);

			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("state", state);

		return model;

	}
	
	@RequestMapping(value = "/admin/listReply", method = RequestMethod.GET)
	public ModelAndView listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 넘어온 파라미터 : 글번호 [,페이지번호]
		
		CreateDAO dao = new CreateDAO();
		MyUtil util = new MyUtilBootstrap();
		
		try {
			long num = Long.parseLong(req.getParameter("num"));
			String pageNo = req.getParameter("pageNo");
			String board_name = req.getParameter("board_name");
			
			int current_page = 1;
			if(pageNo != null) {
				current_page = Integer.parseInt(pageNo);
			}
			
			int size = 5;
			int total_page = 0;
			int replyCount = 0;
			
			replyCount = dao.dataCountReply(num,board_name);
			total_page = util.pageCount(replyCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
		 	List<Board_ReplyDTO> listReply = dao.listReply(num, offset, size,board_name);

			for(Board_ReplyDTO dto : listReply) {
				dto.setReplycontent(dto.getReplycontent().replaceAll("\n", "<br>"));
			}
			
			// 페이징 : 자바 스크립트 함수(listPage)를 호출
			String paging = util.pagingMethod(current_page, total_page, "listPage");
			
			ModelAndView mav = new ModelAndView("admin/listReply");
			
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
	@RequestMapping(value = "/admin/deleteReply", method = RequestMethod.POST)
	public Map<String, Object> deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String state = "false";
		
		CreateDAO dao = new CreateDAO();
		try {
			long replyNum = Long.parseLong(req.getParameter("replyNum"));
			String board_name = req.getParameter("board_name");
			dao.deleteReply(replyNum, info.getEmail(),board_name);
			
			state = "true";
		} catch (Exception e) {
		}
		
		model.put("state", state);
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping(value = "/admin/insertBoardLike", method = RequestMethod.POST)
	public Map<String, Object> insertBoardLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 넘어온 파라미터 : 글번호, 공감/공감취소여부
		Map<String, Object> model = new HashMap<String, Object>();
		
		CreateDAO dao = new CreateDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String state = "false";
		int boardLikeCount = 0;
		
		try {
			long num = Long.parseLong(req.getParameter("num"));
			String isNoLike = req.getParameter("isNoLike");
			String board_name = req.getParameter("board_name");
			if(isNoLike.equals("true")) {
				// 공감
				dao.insertBoardLike(num, info.getEmail(),board_name);
			} else {
				// 공감 취소
				dao.deleteBoardLike(num, info.getEmail(),board_name);
			}
			
			boardLikeCount = dao.countBoardLike(num,board_name);
			
			state = "true";
		} catch (Exception e) {
		}
		
		model.put("state", state);
		model.put("boardLikeCount", boardLikeCount);
		
		return model;
	}
}

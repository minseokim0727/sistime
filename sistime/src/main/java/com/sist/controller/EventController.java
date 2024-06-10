package com.sist.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
import com.sist.dao.BanDAO;
import com.sist.dao.ComplainDAO;
import com.sist.dao.CreateDAO;
import com.sist.dao.EventDAO;
import com.sist.domain.ComplainDTO;
import com.sist.domain.CreateDTO;
import com.sist.domain.EventDTO;
import com.sist.domain.EventReplyDTO;
import com.sist.domain.SessionInfo;
import com.sist.servlet.ModelAndView;
import com.sist.util.FileManager;
import com.sist.util.MyMultipartFile;
import com.sist.util.MyUtil;
import com.sist.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@Controller
public class EventController {
	@RequestMapping("/event/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글리스트
		// 넘어온 파라미터 : [페이지번호, size, 검색컬럼, 검색값]

		ModelAndView mav = new ModelAndView("event/list");

		EventDAO dao = new EventDAO();
		MyUtil util = new MyUtilBootstrap();
		
		

		try {
			String page = req.getParameter("page");
			int current_page = 1;

			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			if (req.getMethod().equals("GET")) {
				kwd = URLDecoder.decode(kwd, "UTF-8");
			}

			// 한화면에 출력할 개수
			String pageSize = req.getParameter("size");
			int size = pageSize == null ? 10 : Integer.parseInt(pageSize);

			int dataCount, total_page;

			if (kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
			total_page = util.pageCount(dataCount, size);

			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			List<EventDTO> list;
			if (kwd.length() == 0) {
				list = dao.listEvent(offset, size);
			} else {
				list = dao.listEvent(offset, size, schType, kwd);
			}

			// 공지글
			List<EventDTO> listEvent = null;
			if (current_page == 1) {
				listEvent = dao.listEvent();
			}

			// 게시글이 등록된 시간 구하기
			String cp = req.getContextPath();
			String listUrl;
			String articleUrl;
			String query = "size=" + size;

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
			}

			listUrl = cp + "/event/list?" + query;
			articleUrl = cp + "/event/article?page=" + current_page + "&" + query;
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩할 JSP에 전달할 데이터
			CreateDAO createDAO = new CreateDAO();
			List<CreateDTO> listcreate = createDAO.selectBoardname();
			// 최근 베스트 게시판 5개
			
			mav.addObject("listcreate", listcreate);
			mav.addObject("list", list);
			mav.addObject("listEvent", listEvent);
			mav.addObject("page", current_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("total_page", total_page);
			mav.addObject("size", size);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
			mav.addObject("paging", paging);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/event/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("event/write");
		mav.addObject("mode", "write");
		return mav;
	}

	@RequestMapping(value = "/event/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		EventDAO dao = new EventDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		FileManager fileManager = new FileManager();

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "event/write";
		
		try {
			EventDTO dto = new EventDTO();
			String startdate = req.getParameter("eventStartDate");

			dto.setStart_date(startdate);
			dto.setEmail(info.getEmail());
			dto.setEvent_title(req.getParameter("title"));
			dto.setEvent_content(req.getParameter("content"));
			
			if(info.getEmail().equals("admin")) {
				dto.setNotice(1);
			}else {
				dto.setNotice(0);
			}
			
			String gap = req.getParameter("event");
			if (gap != null) {
				dto.setGap(Long.parseLong(req.getParameter("event")));
			} else {
				dto.setGap(0);
			}

			Part p = req.getPart("selectFile");

			MyMultipartFile multiFile = fileManager.doFileUpload(p, pathname);
			if (multiFile != null) {
				String saveFilename = multiFile.getSaveFilename();
				String originalFilename = multiFile.getOriginalFilename();
				long size = multiFile.getSize();
				dto.setSaveFilename(saveFilename);
				dto.setOriginalFilename(originalFilename);
				dto.setFilesize(size);
			}
			dao.insertEvent(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/event/list");
	}
	
	@RequestMapping(value = "/event/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		EventDAO dao = new EventDAO();
		MyUtil util = new MyUtilBootstrap();

		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;

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
			EventDTO dto = dao.findById(num);
			if (dto == null) { // 게시물이 없으면 다시 리스트로
				return new ModelAndView("redirect:/event/list?" + query);
			}
			dto.setEvent_content(util.htmlSymbols(dto.getEvent_content()));

			dto.setEvent_content(dto.getEvent_content().replaceAll(">", "&gt;"));
			dto.setEvent_content(dto.getEvent_content().replaceAll("<", "&lt;"));
			dto.setEvent_content(dto.getEvent_content().replaceAll(" ", "&nbsp;"));
			dto.setEvent_content(dto.getEvent_content().replaceAll("\n", "<br>"));

			// 이전글 다음글
			EventDTO prevDto = dao.findByPrev(dto.getEventpage_num(), schType, kwd);
			EventDTO nextDto = dao.findByNext(dto.getEventpage_num(), schType, kwd);

			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			boolean isUserLike = dao.isUserEventLike(num, info.getEmail());

			ModelAndView mav = new ModelAndView("event/article");

			// JSP로 전달할 속성
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			mav.addObject("size", size);
			mav.addObject("isUserLike", isUserLike);

			// 포워딩
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/event/list?" + query);
	}
	
	@RequestMapping(value = "/event/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 수정 폼
		// 넘어온 파라미터 : 글번호, 페이지번호, size

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		EventDAO dao = new EventDAO();

		String page = req.getParameter("page");
		String size = req.getParameter("size");
		try {
			long num = Long.parseLong(req.getParameter("num"));

			EventDTO dto = dao.findById(num);
			if (dto == null) {
				return new ModelAndView("redirect:/event/list?page=" + page + "&size=" + size);
			}

			ModelAndView mav = new ModelAndView("event/write");
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("size", size);

			mav.addObject("mode", "update");

			return mav;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/event/list?page=" + page + "&size=" + size);
	}
	
	@RequestMapping(value = "/event/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 수정 완료
		// 넘어온 폼데이터 : 글번호, 제목, 내용, 공지여부 [, 파일], page, size
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");


		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "event";

		String page = req.getParameter("page");
		String size = req.getParameter("size");

		EventDAO dao = new EventDAO();
		FileManager fileManager = new FileManager();

		try {
			EventDTO dto = new EventDTO();

			dto.setEventpage_num(Integer.parseInt(req.getParameter("num")));
			if (req.getParameter("notice") != null) {
				dto.setNotice(Integer.parseInt(req.getParameter("notice")));
			}
			dto.setEvent_title(req.getParameter("title"));
			dto.setEvent_content(req.getParameter("content"));
			dto.setSaveFilename(req.getParameter("saveFilename"));
			dto.setOriginalFilename(req.getParameter("originalFilename"));
			dto.setFilesize(Long.parseLong(req.getParameter("fileSize")));
			
			Part p = req.getPart("selectFile");
			MyMultipartFile multiFile = fileManager.doFileUpload(p, pathname);
			if (multiFile != null) {
				if (req.getParameter("saveFilename").length() != 0) {
					// 기존파일 삭제
					fileManager.doFiledelete(pathname, req.getParameter("saveFilename"));
				}

				// 새로운 파일
				String saveFilename = multiFile.getSaveFilename();
				String originalFilename = multiFile.getOriginalFilename();
				long size1 = multiFile.getSize();
				dto.setSaveFilename(saveFilename);
				dto.setOriginalFilename(originalFilename);
				dto.setFilesize(size1);
			}
			dao.updateEvent(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/event/list?page=" + page + "&size=" + size);
	}
	
	@RequestMapping(value = "/event/deleteFile")
	public ModelAndView deleteFile(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 수정에서 파일 삭제
		// 넘어온 파라미터 : 글번호, 파일번호, page, size
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");


		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "event";

		String page = req.getParameter("page");
		String size = req.getParameter("size");

		EventDAO dao = new EventDAO();
		FileManager fileManager = new FileManager();

		try {
			long num = Long.parseLong(req.getParameter("num"));
			EventDTO dto = dao.findById(num);
			if (dto != null) {
				// 파일 지우기
				fileManager.doFiledelete(pathname, dto.getSaveFilename());

				dto.setOriginalFilename("");
				dto.setSaveFilename("");
				dto.setFilesize(0);
				dao.updateEvent(dto);
			}

			// 다시 수정화면으로
			return new ModelAndView("redirect:/event/update?num=" + num + "&page=" + page + "&size=" + size);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/event/list");
	}

	@RequestMapping(value = "/event/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		// 넘어온 파라미터 : 글번호, 페이지번호, size [, 검색컬럼, 검색값]
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");


		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "event";

		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;

		EventDAO dao = new EventDAO();
		FileManager fileManager = new FileManager();

		try {
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");
			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}

			long num = Long.parseLong(req.getParameter("num"));

			// 게시글 지우기
			dao.deleteEvent(num);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/event/list?" + query);
	}

	@RequestMapping(value = "/event/deleteList", method = RequestMethod.POST)
	public ModelAndView deleteList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 선택 파일 삭제
		// 넘어온 파라미터 : 글번호들, 페이지번호, size [, 검색컬럼, 검색값]
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (!info.getEmail().equals("admin")) {
			return new ModelAndView("redirect:/event/list");
		}

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "event";

		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;

		EventDAO dao = new EventDAO();
		FileManager fileManager = new FileManager();

		try {
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}

			String[] snums = req.getParameterValues("nums");
			long[] nums = new long[snums.length];
			for (int i = 0; i < snums.length; i++) {
				nums[i] = Long.parseLong(snums[i]);
			}

			// 게시글 지우기
			dao.deleteEvent(nums);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/event/list?" + query);
	}

	@RequestMapping(value = "/event/download", method = RequestMethod.GET)
	public void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파일 다운로드
		// 넘어온 파라미터 : 파일번호
		EventDAO dao = new EventDAO();

		HttpSession session = req.getSession();
		FileManager fileManager = new FileManager();

		// 파일 저장경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "event/write";

		boolean b = false;
		try {
			long num = Long.parseLong(req.getParameter("num"));
			
			EventDTO dto = dao.findById(num);
			
			if (dto != null) {
				b = fileManager.doFiledownload(dto.getSaveFilename(), dto.getOriginalFilename(), pathname, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!b) {
			resp.setContentType("text/html; charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.print("<script>alert('파일다운로드가실패했습니다.');history.back();</script>");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/event/insertEventLike", method = RequestMethod.POST)
	public Map<String, Object> insertLectureLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		System.out.println("22");
		EventDAO dao = new EventDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String state = "false";
		int likeCount = 0;

		try {
			long num = Long.parseLong(req.getParameter("num"));
			System.out.println("num:" + num);
			String isNoLike = req.getParameter("isNoLike");
			
			if(isNoLike.equals("true")) {
				dao.insertEventLike(num, info.getEmail()); // 공감
			} else {
				dao.deleteEventLike(num, info.getEmail()); // 공감 취소
			}
			
			likeCount = dao.countEventLike(num);
			state = "true";
		} catch (SQLException e) {
			state = "liked";
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("state", state);
		model.put("likeCount", likeCount);

		return model;
	}


	// 리플 리스트 - AJAX:TEXT
	@RequestMapping(value = "/event/listReply", method = RequestMethod.GET)	
	public ModelAndView listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		EventDAO dao = new EventDAO();
		MyUtil util = new MyUtilBootstrap();

		try {
			long num = Long.parseLong(req.getParameter("num"));

			String page = req.getParameter("page");

			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			int size = 5;
			int total_page = 0;
			int replyCount = 0;

			replyCount = dao.dataCountReply(num);
			total_page = util.pageCount(replyCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			// 리스트에 출력할 데이터
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<EventReplyDTO> listReply = dao.listReply(num, offset, size);

			// 엔터를 <br>
			for (EventReplyDTO dto : listReply) {
				
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}

			// 페이징 처리 : AJAX 용 - listPage : 자바스크립트 함수명
			String paging = util.pagingMethod(current_page, total_page, "listPage");

			ModelAndView mav = new ModelAndView("event/listReply");
			
			mav.addObject("listReply", listReply);
			mav.addObject("page", current_page);
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

	// 리플 또는 답글 저장 - AJAX:JSON
	@ResponseBody
	@RequestMapping(value = "/event/insertReply", method = RequestMethod.POST)
	public Map<String, Object> insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		EventDAO dao = new EventDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String state = "false";
		try {
			EventReplyDTO dto = new EventReplyDTO();

			long num = Long.parseLong(req.getParameter("num"));
			dto.setEVENTPAGE_NUM(num);
			dto.setEmail(info.getEmail());
			dto.setContent(req.getParameter("content"));
			String answer = req.getParameter("answer");
			if (answer != null) {
				dto.setAnswer(Long.parseLong(answer));
			}
			
			dao.insertReply(dto);
			
			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("state", state);

		return model;
	}

	// 리플 또는 답글 삭제 - AJAX:JSON
	@com.sist.annotation.ResponseBody
	@RequestMapping(value = "/event/deleteReply", method = RequestMethod.POST)
	public Map<String, Object> deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		EventDAO dao = new EventDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String state = "false";

		try {
			long replyNum = Long.parseLong(req.getParameter("replyNum"));

			dao.deleteReply(replyNum, info.getNickname());
			
			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.put("state", state);

		return model;
	}
	
	@RequestMapping(value = "/event/listReplyAnswer", method = RequestMethod.GET)
	public ModelAndView listReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		EventDAO dao = new EventDAO();

		try {
			long answer = Long.parseLong(req.getParameter("answer"));

			List<EventReplyDTO> listReplyAnswer = dao.listReplyAnswer(answer);

			// 엔터를 <br>(스타일 => style="white-space:pre;")
			for (EventReplyDTO dto : listReplyAnswer) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			}

			ModelAndView mav = new ModelAndView("event/listReplyAnswer");
			mav.addObject("listReplyAnswer", listReplyAnswer);

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(400);
			throw e;
		}
	}

	// 리플의 답글 개수 - AJAX:JSON
	@ResponseBody
	@RequestMapping(value = "/event/countReplyAnswer", method = RequestMethod.POST)
	public Map<String, Object> countReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		EventDAO dao = new EventDAO();
		int count = 0;

		try {
			long answer = Long.parseLong(req.getParameter("answer"));
			count = dao.dataCountReplyAnswer(answer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("count", count);

		return model;
	}
	
	@RequestMapping(value = "/event/complain", method = RequestMethod.GET)
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

		return new ModelAndView("redirect:/event/article?page=" + page + "&size=" + size + "&num=" + num);

	}
	
	@RequestMapping(value = "/event/ban", method = RequestMethod.GET)
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

		return new ModelAndView("redirect:/event/article?page=" + page + "&size=" + size + "&num=" + num );

	}
	

}

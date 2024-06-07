package com.sist.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.dao.EventDAO;
import com.sist.domain.EventDTO;
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

			dto.setEmail(info.getEmail());
			dto.setEvent_title(req.getParameter("title"));
			dto.setEvent_content(req.getParameter("content"));
			
			String gap = req.getParameter("notice");
			if(gap != null) {
				dto.setGap(Long.parseLong(req.getParameter("notice")));
			}else {
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
			System.out.println(num);
			kwd = URLDecoder.decode(kwd, "utf-8");

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
			}

			// 조회수 증가
			dao.updateHitCount(num);

			// 게시물 가져오기
			EventDTO dto = dao.findById(num);
			if (dto == null) { // 게시물이 없으면 다시 리스트로
				return new ModelAndView("redirect:/Event/list?" + query);
			}
			 dto.setEvent_content(util.htmlSymbols(dto.getEvent_content()));

			// 이전글 다음글
			EventDTO prevDto = dao.findByPrev(dto.getEventpage_num(), schType, kwd);
			EventDTO nextDto = dao.findByNext(dto.getEventpage_num(), schType, kwd);

			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			boolean isUserLike = dao.isUserEventLike(num, info.getEmail());
			
			ModelAndView mav = new ModelAndView("event/article");
			
			// JSP로 전달할 속성
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			
			mav.addObject("isUserLike", isUserLike);

			// 포워딩
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/event/list?" + query);
	}


}

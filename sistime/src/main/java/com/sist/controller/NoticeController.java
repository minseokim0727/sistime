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
import com.sist.dao.NoticeDAO;
import com.sist.domain.NoticeDTO;
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

@Controller
public class NoticeController {

	@RequestMapping("/notice/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글리스트
		// 넘어온 파라미터 : [페이지번호, size, 검색컬럼, 검색값]
		
		ModelAndView mav = new ModelAndView("notice/list");
		
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtilBootstrap();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			if(req.getMethod().equals("GET")) {
				kwd = URLDecoder.decode(kwd, "UTF-8");
			}
			
			// 한화면에 출력할 개수
			String pageSize = req.getParameter("size");
			int size = pageSize == null ? 10 : Integer.parseInt(pageSize);
			
			int dataCount, total_page;
			
			if(kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
			total_page = util.pageCount(dataCount, size);
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<NoticeDTO> list;
			if(kwd.length() == 0) {
			list = dao.listNotice(offset, size);
			} else {
			list = dao.listNotice(offset, size, schType, kwd);
			}
			
			// 공지글
			List<NoticeDTO> listNotice = null;
			if(current_page == 1) {
				listNotice = dao.listNotice();
			}
			
			//  게시글이 등록된 시간 구하기
			long gap;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime today = LocalDateTime.now();
			for(NoticeDTO dto : list) {
				LocalDateTime dateTime = LocalDateTime.parse(dto.getReg_date(), dtf);
				
				gap = dateTime.until(today, ChronoUnit.HOURS);
				dto.setGap(gap);
				
				dto.setReg_date(dto.getReg_date().substring(0, 10));
			}
			
			String cp = req.getContextPath();
			String listUrl;
			String articleUrl;
			String query = "size=" + size;
			
			if(kwd.length()!=0) {
				query += "&schType="+schType+
						"&kwd="+URLEncoder.encode(kwd,"UTF-8");
			}
			
			listUrl = cp + "/notice/list?" + query;
			articleUrl = cp + "/notice/article?page=" 
					+ current_page + "&" + query;
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩할 JSP에 전달할 데이터
			mav.addObject("list", list);
			mav.addObject("listNotice", listNotice);
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
	
	@RequestMapping(value = "/notice/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(! info.getEmail().equals("admin")) {
			return new ModelAndView("redirect:/notice/list");
		}
		
		ModelAndView mav = new ModelAndView("notice/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/notice/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글등록
		// 넘어온 폼데이터 : 제목, 내용, 공지여부 [, 파일] 
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(! info.getEmail().equals("admin")) {
			return new ModelAndView("redirect:/notice/list");
		}
		
		String path = session.getServletContext().getRealPath("/");
		String pathname = path + "uploads" + File.separator + "notice";
		
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		try {
			NoticeDTO dto = new NoticeDTO();
			
			dto.setEmail(info.getEmail());
			if(req.getParameter("notice") != null) {
				dto.setNotice(Integer.parseInt(req.getParameter("notice")));
			}
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			
			List<MyMultipartFile> listFile = 
					fileManager.doFileUpload(req.getParts(), pathname);
			dto.setListFile(listFile);
			
			dao.insertNotice(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/notice/list");
	}
	
	@RequestMapping(value = "/notice/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		// 넘어온 파라미터 : 글번호, 페이지번호, size [, 검색컬럼, 검색값]
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		NoticeDAO dao = new NoticeDAO();
		
		try {
			long num = Long.parseLong(req.getParameter("notice_num"));
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");
			
			if(kwd.length() != 0) {
				query += "&schType=" + schType
						+ "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			
			NoticeDTO dto = dao.findById(num);
			if(dto == null) {
				return new ModelAndView("redirect:/notice/list?"+query);
			}
			
			dto.setContent(dto.getContent().replaceAll(">", "&gt;"));
			dto.setContent(dto.getContent().replaceAll("<", "&lt;"));
			dto.setContent(dto.getContent().replaceAll(" ", "&nbsp;"));
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			// 이전글/다음글
			NoticeDTO prevDto = dao.findByPrev(num, schType, kwd);
			NoticeDTO nextDto = dao.findByNext(num, schType, kwd);
			
			// 파일
			List<NoticeDTO> listFile = dao.listNoticeFile(num);
			
			ModelAndView mav = new ModelAndView("notice/article");
			
			mav.addObject("dto", dto);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			mav.addObject("listFile", listFile);
			mav.addObject("page", page);
			mav.addObject("size", size);
			mav.addObject("query", query);
			
			return mav;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/notice/list?" + query);
	}
}

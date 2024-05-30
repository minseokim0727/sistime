package com.sist.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
		
		if(! info.getUserId().equals("admin")) {
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
		
		if(! info.getUserId().equals("admin")) {
			return new ModelAndView("redirect:/notice/list");
		}
		
		String path = session.getServletContext().getRealPath("/");
		String pathname = path + "uploads" + File.separator + "notice";
		
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		try {
			NoticeDTO dto = new NoticeDTO();
			
			dto.setUserId(info.getUserId());
			if(req.getParameter("notice") != null) {
				dto.setNotice(Integer.parseInt(req.getParameter("notice")));
			}
			dto.setSubject(req.getParameter("subject"));
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
			long num = Long.parseLong(req.getParameter("num"));
			
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
			
			// 조회수
			dao.updateHitCount(num);
			
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
	
	@RequestMapping(value = "/notice/download", method = RequestMethod.GET)
	public void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파일 다운로드
		// 넘어온 파라미터 : 파일번호
		NoticeDAO dao = new NoticeDAO();
		
		HttpSession session = req.getSession();
		FileManager fileManager = new FileManager();
		
		// 파일 저장경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		boolean b = false;
		try {
			long fileNum = Long.parseLong(req.getParameter("fileNum"));
			
			NoticeDTO dto = dao.findByFileId(fileNum);
			if(dto != null) {
				b = fileManager.doFiledownload(dto.getSaveFilename(),
						dto.getOriginalFilename(), pathname, resp);
			}
		} catch (Exception e) {
		}
		
		if(! b) {
			resp.setContentType("text/html; charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.print("<script>alert('파일다운로드가실패했습니다.');history.back();</script>");
		}
	}
	
	@RequestMapping(value = "/notice/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		// 넘어온 파라미터 : 글번호, 페이지번호, size
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(! info.getUserId().equals("admin")) {
			return new ModelAndView("redirect:/notice/list");
		}
		
		NoticeDAO dao = new NoticeDAO();
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		
		try {
			long num = Long.parseLong(req.getParameter("num"));
			
			NoticeDTO dto = dao.findById(num);
			if(dto == null) {
				return new ModelAndView("redirect:/notice/list?page="+page+"&size="+size);
			}
			
			List<NoticeDTO> listFile = dao.listNoticeFile(num);
			
			ModelAndView mav = new ModelAndView("notice/write");
			mav.addObject("dto", dto);
			mav.addObject("listFile", listFile);
			mav.addObject("page", page);
			mav.addObject("size", size);
			
			mav.addObject("mode", "update");
			
			return mav;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ModelAndView("redirect:/notice/list?page="+page+"&size="+size);
	}
	
	@RequestMapping(value = "/notice/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		// 넘어온 폼데이터 : 글번호, 제목, 내용, 공지여부 [, 파일], page, size
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(! info.getUserId().equals("admin")) {
			return new ModelAndView("redirect:/notice/list");
		}
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		try {
			NoticeDTO dto = new NoticeDTO();
			
			dto.setNum(Long.parseLong(req.getParameter("num")));
			if(req.getParameter("notice") != null) {
				dto.setNotice(Integer.parseInt(req.getParameter("notice")));
			}
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			List<MyMultipartFile> listFile = 
					fileManager.doFileUpload(req.getParts(), pathname);
			dto.setListFile(listFile);
			
			dao.updateNotice(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/notice/list?page="+page+"&size="+size);
	}
	
	@RequestMapping(value = "/notice/deleteFile", method = RequestMethod.GET)
	public ModelAndView deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정에서 파일 삭제
		// 넘어온 파라미터 : 글번호, 파일번호, page, size
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(! info.getUserId().equals("admin")) {
			return new ModelAndView("redirect:/notice/list");
		}
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		try {
			long num = Long.parseLong(req.getParameter("num"));
			long fileNum = Long.parseLong(req.getParameter("fileNum"));
			
			NoticeDTO dto = dao.findByFileId(fileNum);
			if(dto != null) {
				// 파일 지우기
				fileManager.doFiledelete(pathname, dto.getSaveFilename());
				
				// 테이블의 파일 정보 지우기
				dao.deleteNoticeFile("one", fileNum);
			}
			
			// 다시 수정화면으로
			return new ModelAndView("redirect:/notice/update?num="+num+"&page="+page+"&size="+size);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/notice/list");
	}
	
	@RequestMapping(value = "/notice/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		// 넘어온 파라미터 : 글번호, 페이지번호, size [, 검색컬럼, 검색값]
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(! info.getUserId().equals("admin")) {
			return new ModelAndView("redirect:/notice/list");
		}
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		try {
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");
			if(kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd="
						+ URLEncoder.encode(kwd, "utf-8");
			}
			
			long num = Long.parseLong(req.getParameter("num"));
			
			// 업로드된 파일 삭제
			List<NoticeDTO> listFile = dao.listNoticeFile(num);
			for(NoticeDTO vo : listFile) {
				fileManager.doFiledelete(pathname, vo.getSaveFilename());
			}
			
			// 테이블의 파일 정보 지우기
			dao.deleteNoticeFile("all", num);
			
			// 게시글 지우기
			dao.deleteNotice(num);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/notice/list?"+query);
	}
	
	@RequestMapping(value = "/notice/deleteList", method = RequestMethod.POST)
	public ModelAndView deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 선택 파일 삭제
		// 넘어온 파라미터 : 글번호들, 페이지번호, size [, 검색컬럼, 검색값]
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(! info.getUserId().equals("admin")) {
			return new ModelAndView("redirect:/notice/list");
		}
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		try {
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			if(kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd="
						+ URLEncoder.encode(kwd, "utf-8");
			}
			
			String []snums = req.getParameterValues("nums");
			long []nums = new long[snums.length];
			for(int i=0; i<snums.length; i++) {
				nums[i] = Long.parseLong(snums[i]);
			}
			
			// 업로드된 파일 및 파일 테이블의 정보 삭제
			for(int i=0; i<nums.length; i++) {
				List<NoticeDTO> listFile = dao.listNoticeFile(nums[i]);
				for(NoticeDTO vo : listFile) {
					fileManager.doFiledelete(pathname, vo.getSaveFilename());
				}
				
				dao.deleteNoticeFile("all", nums[i]);
			}
			
			// 게시글 지우기
			dao.deleteNotice(nums);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/notice/list?"+query);
	}
	
}

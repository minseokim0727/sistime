package com.sist.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.dao.BestboardDAO;
import com.sist.domain.BoardDTO;
import com.sist.servlet.ModelAndView;
import com.sist.util.MyUtil;
import com.sist.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BestboardController {
	@RequestMapping(value = "/bestboard/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
			// 게시물 리스트
			ModelAndView mav = new ModelAndView("bestboard/list");

			
			MyUtil util = new MyUtilBootstrap();
			BestboardDAO dao2 = new BestboardDAO();
			
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
					dataCount = dao2.dataCount();
				} else {
					dataCount = dao2.dataCount(schType, kwd);
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
					list = dao2.listbestBoard(offset, size);
				} else {
					list = dao2.listbestBoard(offset, size, schType, kwd);
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
	
	
}

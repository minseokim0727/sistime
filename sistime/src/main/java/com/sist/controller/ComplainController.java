package com.sist.controller;

import java.io.IOException;
import java.util.List;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.dao.ComplainDAO;
import com.sist.domain.ComplainDTO;
import com.sist.servlet.ModelAndView;
import com.sist.util.MyUtil;
import com.sist.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ComplainController {
	
	@RequestMapping("/complain/list")
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글리스트
		// 넘어온 파라미터 : [페이지번호, size, 검색컬럼, 검색값]

		ModelAndView mav = new ModelAndView("complain/list");

		ComplainDAO dao = new ComplainDAO();
		MyUtil util = new MyUtilBootstrap();

		try {
			String page = req.getParameter("page");
			int current_page = 1;

			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			// 한화면에 출력할 개수
			String pageSize = req.getParameter("size");
			int size = pageSize == null ? 10 : Integer.parseInt(pageSize);

			int dataCount, total_page;
			
			dataCount = dao.dataCount();
			
			total_page = util.pageCount(dataCount, size);

			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			List<ComplainDTO> list;
			list = dao.listComplain(offset, size);

			String cp = req.getContextPath();
			String listUrl;
			String articleUrl;
			String query = "size=" + size;

			listUrl = cp + "/complain/list?" + query;
			articleUrl = cp + "/board_name/article?page=" + current_page + "&" + query;

			String paging = util.paging(current_page, total_page, listUrl);

			// 포워딩할 JSP에 전달할 데이터
			mav.addObject("list", list);
			mav.addObject("page", current_page);
			mav.addObject("dataCount", dataCount);
			mav.addObject("total_page", total_page);
			mav.addObject("size", size);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
}

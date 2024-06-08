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
import com.sist.dao.MessageDAO;
import com.sist.domain.MemberDTO;
import com.sist.domain.MessageDTO;
import com.sist.domain.SessionInfo;
import com.sist.servlet.ModelAndView;
import com.sist.util.MyUtil;
import com.sist.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MessageController {
	@RequestMapping(value = "/message/list")
	public com.sist.servlet.ModelAndView MessageForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ModelAndView mav = new ModelAndView("message/list");

		MessageDAO dao = new MessageDAO();
		MyUtil util = new MyUtilBootstrap();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			String email = info.getEmail();
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
				dataCount = dao.dataCount(email);
			} else {
				dataCount = dao.dataCount(schType, kwd, email);
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

			List<MessageDTO> list = null;
			if (kwd.length() == 0) {
				list = dao.listMessage(offset, size, email);
			} else {
				list = dao.listMessage(offset, size, schType, kwd, email);
			}

			String query = "";
			if (kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}

			// 페이징 처리
			String cp = req.getContextPath();
			String listUrl = cp + "/message/list";
			String articleUrl = cp + "/message/article?page=" + current_page;
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

	@RequestMapping(value = "/message/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 글쓰기 폼
		ModelAndView mav = new ModelAndView("message/write");

		return mav;
	}

	@RequestMapping(value = "/message/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 글쓰기 폼

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		MessageDAO dao = new MessageDAO();

		try {
			MessageDTO dto = new MessageDTO();
			dto.setSend_email(info.getEmail());
			dto.setContent(req.getParameter("content"));
			dto.setRead_email(req.getParameter("read_email"));

			dao.insertMessage(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/message/list");
	}

	@RequestMapping(value = "/message/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기

		MessageDAO dao = new MessageDAO();

		String page = req.getParameter("page");
		String query = "page=" + page;

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			int msg_num = Integer.parseInt(req.getParameter("msg_num"));

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

			// 게시물 가져오기
			MessageDTO dto = dao.findbyNum(msg_num);
			if (dto == null) { // 게시물이 없으면 다시 리스트로
				return new ModelAndView("redirect:/message/list?" + query);
			}

			String send_email = dto.getSend_email();
			String read_email = dto.getRead_email();

			List<MessageDTO> list = dao.findMessage(send_email, read_email);

			ModelAndView mav = new ModelAndView("message/article");

			String send_nickname = dao.findbyNickname(send_email);
			String read_nickname = dao.findbyNickname(read_email);

			if (read_nickname.equals(info.getNickname())) {
				read_nickname = send_nickname;
			}
			// JSP로 전달할 속성
			mav.addObject("read_nickname", read_nickname);

			mav.addObject("dto", dto);
			mav.addObject("list", list);
			mav.addObject("page", page);
			mav.addObject("query", query);

			// 포워딩
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/message/list?" + query);
	}

	@ResponseBody
	@RequestMapping(value = "/message/messageSend", method = RequestMethod.POST)
	public Map<String, Object> messageSend(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		int msg_num = 0;
		MessageDAO dao = new MessageDAO();

		String send_name = req.getParameter("send_name");
		String read_name = req.getParameter("read_name");
		String message = req.getParameter("message");

		String send_email = dao.findbyEmail(send_name);
		String read_email = dao.findbyEmail(read_name);
		
		MessageDTO dto = new MessageDTO();
		dto.setSend_email(send_email);
		dto.setRead_email(read_email);
		dto.setContent(message);
		
		System.out.println("********************");
		System.out.println(send_email);
		System.out.println(read_email);
		System.out.println("********************");

		msg_num = dao.insertMessage(dto);

		Map<String, Object> map = new HashMap<String, Object>();
		String passed = "false";
		if (!read_email.equals(null)) {
			passed = "true";
		}
		map.put("msg_num", msg_num);
		map.put("passed", passed);

		return map;
	}

	@RequestMapping(value = "/message/messageGet", method = RequestMethod.GET)
	public ModelAndView messageGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		MyUtil util = new MyUtilBootstrap();
		MessageDAO dao = new MessageDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {

			String page = req.getParameter("page");
			int msg_num = Integer.parseInt(req.getParameter("msg_num"));

			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			int size = 10;
			int total_page = 0;
			int messageCount = 0;

			total_page = util.pageCount(messageCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;
			MessageDTO dto = dao.findbyNum(msg_num);

			String send_email = dto.getSend_email();
			String read_email = dto.getRead_email();
			String send_nickname2 = dao.findbyNickname(send_email);

			String read_nickname = dao.findbyNickname(read_email);

			if (read_nickname.equals(info.getNickname())) {
				read_nickname = send_nickname2;
			}

			List<MessageDTO> list = dao.findMessage(send_email, read_email);

			for (MessageDTO dto1 : list) {
				dto1.setContent(dto1.getContent().replaceAll("\n", "<br>"));
			}

			String paging = util.pagingMethod(current_page, total_page, "listPage");

			ModelAndView mav = new ModelAndView("message/subMessage");

			// JSP로 전달할 속성
			mav.addObject("send_nickname", info.getNickname());
			mav.addObject("read_nickname", read_nickname);

			mav.addObject("dto", dto);
			mav.addObject("page", current_page);
			mav.addObject("list", list);
			mav.addObject("paging", paging);
			mav.addObject("total_page", total_page);

			return mav;

		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(400);
			throw e;
		}
	}

}

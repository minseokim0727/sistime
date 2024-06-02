package com.sist.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.annotation.ResponseBody;
import com.sist.dao.MemberDAO;
import com.sist.domain.MemberDTO;
import com.sist.domain.SessionInfo;
import com.sist.servlet.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	@RequestMapping(value = "/member/login", method = RequestMethod.GET)
	public com.sist.servlet.ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 로그인 폼
		return new ModelAndView("member/login");
	}

	@RequestMapping(value = "/member/login", method = RequestMethod.POST)
	public ModelAndView loginSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 로그인 처리
		// 세션객체. 세션 정보는 서버에 저장(로그인 정보, 권한등을 저장)
		// 메인화면으로 리다이렉트
		HttpSession session = req.getSession();
		MemberDAO dao = new MemberDAO();

		String email = req.getParameter("email");
		String userPwd = req.getParameter("userPwd");

		MemberDTO dto = dao.loginMember(email, userPwd);
		if (dto != null) {
			// 로그인 성공하면 서버에 정보 저장하고 20분 세팅
			session.setMaxInactiveInterval(20 * 60);

			SessionInfo info = new SessionInfo();
			info.setEmail(dto.getEmail());
			info.setUserName(dto.getUserName());

			session.setAttribute("member", info);

			String preLoginURI = (String) session.getAttribute("preLoginURI");
			session.removeAttribute("preLoginURI");
			if (preLoginURI != null) {
				// 로그인 전페이지로 리다이렉트
				return new ModelAndView(preLoginURI);
			}

			// 메인화면으로 리다이렉트
			return new ModelAndView("redirect:/");

		}

		ModelAndView mav = new ModelAndView("member/login");

		String msg = "아이디 또는 패스워드가 일치하지 않습니다.";
		mav.addObject("message", msg);

		return mav;
	}

	@RequestMapping(value = "/member/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그아웃
		HttpSession session = req.getSession();

		// 세션에 저장된 정보를 지운다.
		session.removeAttribute("member");

		// 세션에 저장된 모든 정보를 지우고 세션을 초기화 한다.
		session.invalidate();

		// 루트로 리다이렉트
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/member/member", method = RequestMethod.GET)
	public ModelAndView memberForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 회원가입 폼
		ModelAndView mav = new ModelAndView("member/member");

		mav.addObject("title", "회원 가입");
		mav.addObject("mode", "member");

		return mav;
	}

	@RequestMapping(value = "/member/member", method = RequestMethod.POST)
	public ModelAndView memberSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 회원가입 처리
		MemberDAO dao = new MemberDAO();

		String message = "";
		try {
			MemberDTO dto = new MemberDTO();
			dto.setEmail(req.getParameter("email"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setUserName(req.getParameter("userName"));
			dto.setNickname(req.getParameter("nickname"));
			dto.setUniv_num(Integer.parseInt(req.getParameter("univ_num")));
			dto.setBirth(req.getParameter("birth"));

			String tel1 = req.getParameter("tel1");
			String tel2 = req.getParameter("tel2");
			String tel3 = req.getParameter("tel3");
			dto.setTel(tel1 + "-" + tel2 + "-" + tel3);

			dto.setZip(req.getParameter("zip"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));

			dao.insertMember(dto);

			return new ModelAndView("redirect:/");
		} catch (SQLException e) {
			if (e.getErrorCode() == 1)
				message = "아이디 중복으로 회원 가입이 실패 했습니다.";
			else if (e.getErrorCode() == 1400)
				message = "필수 사항을 입력하지 않았습니다.";
			else if (e.getErrorCode() == 1840 || e.getErrorCode() == 1861)
				message = "날짜 형식이 일치하지 않습니다.";
			else
				message = "회원 가입이 실패 했습니다.";
			// 기타 - 2291:참조키 위반, 12899:폭보다 문자열 입력 값이 큰경우
		} catch (Exception e) {
			message = "회원 가입이 실패 했습니다.";
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("member/member");

		mav.addObject("title", "회원 가입");
		mav.addObject("mode", "member");
		mav.addObject("message", message);

		return mav;
	}

	
	
	@ResponseBody
	@RequestMapping(value = "/member/emailCheck", method = RequestMethod.POST)
	public Map<String, Object> emailCheck(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 아이디 중복 검사 - AJAX : JSON 으로 응답
		Map<String, Object> map = new HashMap<String, Object>();

		MemberDAO dao = new MemberDAO();

		String email = req.getParameter("email");
		MemberDTO dto = dao.findByEmail(email);

		String passed = "false";
		if (dto == null) {
			passed = "true";
		}

		map.put("passed", passed);

		return map;
	}
	
	@ResponseBody
	@RequestMapping(value = "/member/nicknameCheck", method = RequestMethod.POST)
	public Map<String, Object> nicknameCheck(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 아이디 중복 검사 - AJAX : JSON 으로 응답
		Map<String, Object> map = new HashMap<String, Object>();

		MemberDAO dao = new MemberDAO();

		String nickname = req.getParameter("nickname");
		MemberDTO dto = dao.findByNickname(nickname);

		String passed = "false";
		if (dto == null) {
			passed = "true";
		}

		map.put("passed", passed);

		return map;
	}


}

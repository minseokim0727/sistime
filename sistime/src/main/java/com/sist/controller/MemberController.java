package com.sist.controller;

import java.io.IOException;

import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.dao.MemberDAO;
import com.sist.domain.MemberDTO;
import com.sist.domain.SessionInfo;
import com.sist.servlet.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@com.sist.annotation.Controller
public class MemberController {
	@com.sist.annotation.RequestMapping(value = "/member/login", method = RequestMethod.GET)
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
		if(dto != null) {
			//로그인 성공하면 서버에 정보 저장하고 20분 세팅
			session.setMaxInactiveInterval(20*60);
			
			SessionInfo info = new SessionInfo();
			info.setEmail(dto.getEmail());
			info.setUserName(dto.getUserName());
			
			session.setAttribute("member", info);
			
			String preLoginURI = (String)session.getAttribute("preLoginURI");
			session.removeAttribute("preLoginURI");
			if(preLoginURI != null) {
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
	
}

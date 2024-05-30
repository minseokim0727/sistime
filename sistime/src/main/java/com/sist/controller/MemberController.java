package com.sist.controller;

import java.io.IOException;

import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;


import com.sist.servlet.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


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
		return new ModelAndView("redirect:/");
	}
	
}

package com.sist.controller;

import java.io.IOException;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMethod;
import com.sist.servlet.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class TestController {
	@com.sist.annotation.RequestMapping(value = "/test/template")
	public com.sist.servlet.ModelAndView testForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 로그인 폼
		ModelAndView mav = new ModelAndView("test/template");
		return mav;
	}
}

package com.sist.controller;

import java.io.IOException;

import com.sist.annotation.Controller;
import com.sist.servlet.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MessageController {
	@com.sist.annotation.RequestMapping(value = "/message/list")
	public com.sist.servlet.ModelAndView testForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ModelAndView mav = new ModelAndView("message/list");
		return mav;
	}
}
package com.sist.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
	public void initialize() throws ServletException;
	public HandlerExecution getHandler(final HttpServletRequest req);
}

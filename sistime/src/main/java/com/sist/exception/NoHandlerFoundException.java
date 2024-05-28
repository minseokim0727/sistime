package com.sist.exception;

import jakarta.servlet.ServletException;

public class NoHandlerFoundException extends ServletException {
	private static final long serialVersionUID = 1L;
	
	private String httpMethod;
	private String requestURL;
	
	public NoHandlerFoundException(String httpMethod, String requestURL) {
		super("No endpoint : " + httpMethod + " " + requestURL);
		
		this.httpMethod = httpMethod;
		this.requestURL = requestURL;
	}
	
	public String getHttpMethod() {
		return httpMethod;
	}

	public String getRequestURL() {
		return requestURL;
	}
}

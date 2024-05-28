package com.sist.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.sist.exception.NoHandlerFoundException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@MultipartConfig(
	// location = "c:/temp",			// 파일을 임시로 저장할 경로(생략가능. 기본값 ""), 지정된 경로가 없으면 업로드가 안됨
	fileSizeThreshold = 1024 * 1024,	// 업로드된 파일이 임시로 서버에 저장되지 않고 메모리에서 스트림으로 바로 전달되는 크기
	maxFileSize = 1024 * 1024 * 5,		// 업로드된 하나의 파일 크기. 기본 용량 제한 없음
	maxRequestSize = 1024 * 1024 * 10	// 폼 전체 용량
)
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 애노테이션 이외의 핸들러 매핑도 존재할 수 있으므로 List로 작성
	private final List<HandlerMapping> handlerMappings;

	public DispatcherServlet() {
		this.handlerMappings = new ArrayList<>();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		HandlerMapping handlerMapping = new AnnotationHandlerMapping("com.sist.controller");
		handlerMappings.add(handlerMapping);
		handlerMapping.initialize();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		handleRequest(req, resp);
	}

	protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			// System.out.printf("Method : %s, Request URI : %s\n", req.getMethod(), req.getRequestURI());
			
			final Object controller = getController(req);

			if (controller instanceof HandlerExecution) {
				HandlerExecution handlerExecution = (HandlerExecution) controller;
				
				handlerExecution.handle(req, resp);
			}
		} catch (NoHandlerFoundException e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			e.printStackTrace();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	public void addHandlerMapping(final HandlerMapping handlerMapping) {
		handlerMappings.add(handlerMapping);
	}

	private Object getController(final HttpServletRequest req) throws NoHandlerFoundException {
		// orElseThrow를 통해 Optional에서 원하는 객체를 바로 얻거나 예외를 던질 수 있다.
		return handlerMappings.stream()
				.map(handlerMapping -> handlerMapping.getHandler(req))
				.filter(Objects::nonNull)
				.findFirst()
				.orElseThrow(() -> new NoHandlerFoundException(req.getMethod(), req.getRequestURI()));
	}

	@Override
	public void destroy() {
	}
}

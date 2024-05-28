package com.sist.servlet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import com.sist.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {
    private final Class<?> controller;
    private final Method method;

    public HandlerExecution(final Method method) {
    	// 해당 메소드가 정의된 클래스를 반환
        this.controller = method.getDeclaringClass();
        this.method = method;
    }

    public void handle(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        Constructor<?> constructor = controller.getConstructor();
        Object instance = constructor.newInstance();
        
		Class<?> cls = method.getReturnType();

		if(cls.getSimpleName().equals("void")) {
			method.invoke(instance, req, resp);
		} else if(cls.getSimpleName().equals("Map")) {
			// 리턴 타입이 Map 인 경우
			@SuppressWarnings("unchecked")
			Map<String, Object> model = (Map<String, Object>)method.invoke(instance, req, resp);
			
			if(method.isAnnotationPresent(ResponseBody.class)) {
				// JSON 반환
				JsonView jsonView = new JsonView();
				jsonView.render(model, req, resp);
			} else {
				// URI 가 jsp 이름
				String uri = req.getRequestURI();
				String cp = req.getContextPath();
				String viewName = uri.substring(cp.length() + 1);

				if(viewName.lastIndexOf(".") != -1) {
					viewName = viewName.substring(0, viewName.lastIndexOf("."));
				}
				ModelAndView modelAndView = new ModelAndView(viewName, model);
				modelAndView.renderView(req, resp);
			}
		} else {
			ModelAndView modelAndView = (ModelAndView)method.invoke(instance, req, resp);
			modelAndView.renderView(req, resp);
		}
    }
}

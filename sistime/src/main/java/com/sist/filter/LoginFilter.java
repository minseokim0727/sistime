package com.sist.filter;

import java.io.IOException;

import com.sist.domain.SessionInfo;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class LoginFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	
		// request 필터
		
		// 로그인 체크
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(info == null && isExcludeUri(req) == false) {
			if(isAjaxRequest(req)) {
				resp.sendError(403);
			} else {
				// 로그인 전 주소가 존재하는 경우 로그인 전 주소로 가기 위해 세션에 로그인 전주소 저장
				// 로그인 주소로 돌아가는 코드는 login 에서 처리
				String uri = req.getRequestURI();
				
				// uri에서 ContextPath 제거
				if(uri.indexOf(req.getContextPath()) == 0) {
					uri = uri.substring(req.getContextPath().length());
				}
				uri = "redirect:" + uri;
				
				String queryString = req.getQueryString();
				if(queryString != null) {
					uri += "?" + queryString;
				}
				session.setAttribute("preLoginURI", uri);
				
				resp.sendRedirect(cp + "/member/login");
			}

			return;
		}
		
		// 다음 필터 또는 필터의 마지막이면 end-pointer(서블릿, jsp 등)를 실행
		chain.doFilter(request, response);
		
		// response 필터
	}

	@Override
	public void destroy() {
	}
	
	// 요청이 AJAX 인지를 확인하는 메소드
	private boolean isAjaxRequest(HttpServletRequest req) {
		String h = req.getHeader("AJAX");
		
		return h != null && h.equals("true");
	}
	
	// 로그인 체크 필요하지 않는지의 여부 판단
	private boolean isExcludeUri(HttpServletRequest req) {
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		uri = uri.substring(cp.length());
		
		String uris[] = {
				"/index.jsp", "/main", 
				"/member/login", "/member/logout",
				"/member/member", "/member/userIdCheck",
				"/notice/list",
				"/uploads/photo/**",
				"/resources/**","/test/template"
		};
		
		if(uri.length() <= 1) {
			return true;
		}
		
		for(String s : uris) {
			if(s.lastIndexOf("**") != -1) {
                s = s.substring(0, s.lastIndexOf("**"));
                if(uri.indexOf(s) == 0) {
					return true;
				}
			} else if(uri.equals(s)) {
				return true;
			}
		}
		
		return false;
	}

}

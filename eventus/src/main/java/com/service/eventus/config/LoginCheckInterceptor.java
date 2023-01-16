package com.service.eventus.config;

import java.io.PrintWriter;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String requestURI = request.getRequestURI();
//		System.out.println("[interceptor] : " + requestURI);
		HttpSession session = request.getSession(false);
		
		if(session == null || session.getAttribute("user_id") == null) {
       		// 로그인 되지 않음
			System.out.println("[미인증 사용자 요청] : "+ requestURI);
			
			//로그인으로 redirect
			//response.sendRedirect("/");
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('로그인 후 이용가능합니다.'); location.href='/login';</script>");
			out.flush();
			return false;
		}
		
        // 로그인 되어있을 떄
		return true;
	}
}
package com.service.eventus.config;

import java.io.PrintWriter;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthorityCheckInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String requestURI = request.getRequestURI();
		System.out.println("[interceptor] : " + requestURI);
		HttpSession session = request.getSession(false);
		int authority = (int) session.getAttribute("authority");
		
		if(authority != 0) {
       		// 사용자 권한부족
			System.out.println("[권한부족 사용자 요청]");
			
			//main_ForStaff redirect
			//response.sendRedirect("/main_ForStaff");
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('잘못된 접근입니다.'); location.href='/main_ForStaff';</script>");
			out.flush();
			return false;
		}
		
        // 로그인 되어있을 떄
		return true;
	}
}
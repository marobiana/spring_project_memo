package com.marobiana.memo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class PermissionInterceptor implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
		
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 세션이 있는지 확인한다. => 있으면 로그인 된 상태
		HttpSession session = request.getSession();
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		// URI 확인(url path를 가져온다.)
		String uri = request.getRequestURI();
		
		if (userLoginId == null && uri.startsWith("/post")) {
			// 비로그인 && 접근을 시도한 uri path가 /post 이면 로그인 페이지로 리다이렉트
			response.sendRedirect("/user/sign_in_view");
			return false;
		} else if (userLoginId != null && uri.startsWith("/user")) {
			// 로그인 && 접근을 시도한 uri path가 /user 이면 게시판으로 리다이렉트
			response.sendRedirect("/post/post_list_view");
			return false;
		}
		
		return true;
	}
	
	/*
	 * 인터셉터 메소드 호출 순서 확인 
	 */
	
	@Override
	public void postHandle(HttpServletRequest request, 
			    HttpServletResponse response, 
			    Object handler, 
			    ModelAndView modelAndView) throws Exception {
		
		// URI 확인(url path를 가져온다.)
		String uri = request.getRequestURI();
		logger.info("###### postHandler:" + uri);
	}
	
	@Override
	public void afterCompletion(
	        HttpServletRequest request, 
	        HttpServletResponse response, 
	        Object handler,
	        Exception ex) throws Exception {
		
		// URI 확인(url path를 가져온다.)
		String uri = request.getRequestURI();
		logger.info("###### afterCompletion:" + uri);
	}
}

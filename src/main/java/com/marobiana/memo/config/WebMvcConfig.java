package com.marobiana.memo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.marobiana.memo.interceptor.PermissionInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired 
	private PermissionInterceptor interceptor;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler("/images/**") // http://localhost/images/marobiana_1620995857660/sun.png 와 같이 접근 가능하게 매핑해준다. 
		.addResourceLocations("file:///C:\\Users\\신보람\\Documents\\강의\\강의용프로젝트\\webProjectMEMO\\images/"); // 실제 파일 저장 위치
	}
	
	@Override 
	public void addInterceptors(InterceptorRegistry registry) { 
		registry.addInterceptor(interceptor) 
		.addPathPatterns("/**") // ** 아래 디렉토리까지 확인
		.excludePathPatterns("/user/sign_out", "/static/**", "/error"); // 예외처리
	}
}

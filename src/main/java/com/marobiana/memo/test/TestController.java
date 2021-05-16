package com.marobiana.memo.test;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TestDAO testDAO;
	
	@ResponseBody
	@RequestMapping("/test1")
	public String helloworld() {
		return "Hello world!";
	}
	
	@ResponseBody
	@RequestMapping("/test2")
	public Map<String, Object> getUser() {
		logger.warn("### test2 호출!!!!");
		return testDAO.selectUser();
	}
}

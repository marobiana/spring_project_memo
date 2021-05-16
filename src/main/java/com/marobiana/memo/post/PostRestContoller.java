package com.marobiana.memo.post;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.marobiana.memo.post.bo.PostBO;

@RequestMapping("/post")
@RestController
public class PostRestContoller {
	@Autowired
	private PostBO postBO;
	
	@RequestMapping("/create")
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam(value = "content") String content,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		String loginId = (String) session.getAttribute("userLoginId");
		int userId = (Integer) session.getAttribute("userId");
		
		Map<String, Object> result = new HashMap<>();
		int row = postBO.createPost(loginId, userId, subject, content, file);
		if (row > 0) {
			result.put("result", "success");
		} else {
			result.put("result", "error");
		}
		
		return result;
	}
	
	@RequestMapping("/update")
	public Map<String, Object> update(
			@RequestParam("postId") int postId,
			@RequestParam("subject") String subject,
			@RequestParam(value = "content") String content,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		String loginId = (String) session.getAttribute("userLoginId");
		int userId = (Integer) session.getAttribute("userId");
		
		Map<String, Object> result = new HashMap<>();
		int row = postBO.updatePost(postId, loginId, userId, subject, content, file);
		if (row > 0) {
			result.put("result", "success");
		} else {
			result.put("result", "error");
		}
		
		return result;
	}
	
	@RequestMapping("/delete")
	public Map<String, Object> delete(
			@RequestParam("postId") int postId) {
		
		Map<String, Object> result = new HashMap<>();
		int row = postBO.deletePost(postId);
		if (row > 0) {
			result.put("result", "success");
		} else {
			result.put("result", "error");
		}
		return result;
	}
}

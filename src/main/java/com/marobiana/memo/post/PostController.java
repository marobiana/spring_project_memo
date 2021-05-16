package com.marobiana.memo.post;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marobiana.memo.post.bo.PostBO;
import com.marobiana.memo.post.model.Post;

@RequestMapping("/post")
@Controller
public class PostController {
	@Autowired
	private PostBO postBO;
	
	/**
	 * 게시판 목록
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/post_list_view")
	public String postListView(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			// 세션에 로그인 아이디가 없으면 로그인 페이지로 리다이렉트
			return "redirect:/user/sign_in_view";
		}
		
		List<Post> postList = postBO.getPostListByUserId(userId);
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/post_list");
		return "template/layout";
	}
	
	/**
	 * 글쓰기 화면
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/post_create_view")
	public String postCreateView(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			// 세션에 로그인 아이디가 없으면 로그인 페이지로 리다이렉트
			return "redirect:/user/sign_in_view";
		}
		
		model.addAttribute("viewName", "post/post_create");
		
		return "template/layout";
	}
	
	@RequestMapping("/post_detail_view")
	public String postDetailView(
			@RequestParam("postId") int postId,
			HttpServletRequest request, 
			Model model) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			// 세션에 로그인 아이디가 없으면 로그인 페이지로 리다이렉트
			return "redirect:/user/sign_in_view";
		}
		
		Post post = postBO.getPost(postId);
		model.addAttribute("post", post);
		model.addAttribute("viewName", "post/post_detail");
		
		return "template/layout";
	}
}

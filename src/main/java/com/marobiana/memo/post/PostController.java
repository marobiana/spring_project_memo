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
	public String postListView(
			@RequestParam(value = "prevId", required=false) Integer prevId,
			@RequestParam(value = "nextId", required=false) Integer nextId,
			HttpServletRequest request, 
			Model model) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			// 세션에 로그인 아이디가 없으면 로그인 페이지로 리다이렉트
			return "redirect:/user/sign_in_view";
		}
		
		List<Post> postList = postBO.getPostListByUserId(userId, prevId, nextId);
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/post_list");
		
		// 게시글 번호: 10 9 8 | 7 6 5 | 4 3 2 | 1
		// 만약 7 6 5 페이지에 있다면 beforeId는 7을 넘기고, afterId는 5를 넘긴다.
		if (postList.isEmpty() == false) {
			model.addAttribute("prevId", postList.get(0).getId()); // 내려간 리스트 중 가장 앞쪽 id
			model.addAttribute("nextId", postList.get(postList.size() - 1).getId()); // 내려간 리스트 중 가장 뒤쪽 id
		}
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

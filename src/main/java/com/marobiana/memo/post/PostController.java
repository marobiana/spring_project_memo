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
			@RequestParam(value = "prevId", required=false) Integer prevIdParam,
			@RequestParam(value = "nextId", required=false) Integer nextIdParam,
			HttpServletRequest request, 
			Model model) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			// 세션에 로그인 아이디가 없으면 로그인 페이지로 리다이렉트
			return "redirect:/user/sign_in_view";
		}
		
		List<Post> postList = postBO.getPostListByUserId(userId, prevIdParam, nextIdParam);
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/post_list");
		
		// 이전이나 다음이 없는 경우 nextId, prevId를 0으로 세팅한다.(뷰화면에서 0인지 검사)
		int prevId = postList.get(0).getId();
		int nextId = postList.get(postList.size() - 1).getId();
		
		// 게시글 번호 1이 있는 마지막 페이지
		if (postBO.isLastPage(userId, nextId)) {
			nextId = 0;
		}
		
		// 게시글 번호 10 9 8이 있는 첫 페이지
		if (postBO.isFirstPage(userId, prevId)) {
			prevId = 0;
		}
		
		// 게시글 번호: 10 9 8 | 7 6 5 | 4 3 2 | 1
		// 만약 7 6 5 페이지에 있다면 beforeId는 7을 넘기고, afterId는 5를 넘긴다.
		//  1) 이전 눌렀을 때: 7보다 큰 3개 가져오고 코드에서 reverse
		//  2) 다음 눌렀을 때: 5보다 작은 3개
		if (postList.isEmpty() == false) {
			model.addAttribute("prevId", prevId); // 내려간 리스트 중 가장 앞쪽 id
			model.addAttribute("nextId", nextId); // 내려간 리스트 중 가장 뒤쪽 id
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

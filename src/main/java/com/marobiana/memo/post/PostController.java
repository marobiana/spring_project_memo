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
		
		int prevId = 0;
		int nextId = 0;
		if (postList.isEmpty() == false) { // post가 없는 경우 에러 발생 방지
			prevId = postList.get(0).getId();
			nextId = postList.get(postList.size() - 1).getId();
			
			// 이전이나 다음이 없는 경우 nextId, prevId를 0으로 세팅한다.(뷰화면에서 0인지 검사)
			
			// 마지막 페이지 => nextId를 0으로 세팅한다.
			if (postBO.isLastPage(userId, nextId)) {
				nextId = 0;
			}
			
			// 첫페이지 => prevId를 0으로 세팅한다.
			if (postBO.isFirstPage(userId, prevId)) {
				prevId = 0;
			}
		}
		
		// 게시글 번호: 10 9 8 | 7 6 5 | 4 3 2 | 1
		// 만약 7 6 5 페이지에 있다면 prevId는 7을 넘기고, nextId는 5를 넘긴다.
		//  1) 이전 눌렀을 때: 7보다 큰 3개 가져오고 코드에서 reverse
		//  2) 다음 눌렀을 때: 5보다 작은 3개
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/post_list");
		model.addAttribute("prevId", prevId); // 리스트 중 가장 앞쪽(제일 큰) id
		model.addAttribute("nextId", nextId); // 리스트 중 가장 뒷쪽(제일 작은) id
		
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

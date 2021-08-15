package com.marobiana.memo.post.bo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.marobiana.memo.common.FileManagerService;
import com.marobiana.memo.post.dao.PostDAO;
import com.marobiana.memo.post.model.Post;

@Service
public class PostBO {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	private static final int POST_MAX_SIZE = 2;
	
	public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId) {
		// 페이징 계산
		// 게시글 번호: 10 9 8 | 7 6 5 | 4 3 2 | 1
		//-- 만약 7 6 5 에서
			//  1) 이전 눌렀을 때: 7보다 큰 3개 가져오고 코드에서 reverse
			//  2) 다음 눌렀을 때: 5보다 작은 3개
		Integer standardId = null;
		String direction = null;
		if (prevId != null) {
			// '이전' 클릭
			direction = "prev";
			standardId = prevId;
			
			// 7보다 큰 3개   8 9 10이 나오므로 reverse 정렬 시킨다.
			List<Post> postList = postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
			Collections.reverse(postList);
			return postList;
		} else if (nextId != null) {
			// '다음' 클릭
			direction = "next";
			standardId = nextId;
		}
		
		// 처음 페이지
		return postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
	}
	
	public boolean isLastPage(int userId, int nextId) {
		return nextId == postDAO.selectPostIdByUserIdAndSort(userId, "ASC");
	}
	
	public boolean isFirstPage(int userId, int prevId) {
		return prevId == postDAO.selectPostIdByUserIdAndSort(userId, "DESC");
	}
	
	public int createPost(String loginId, int userId, String subject, String content, MultipartFile file) {
		String imagePath = null;
		if (file != null) {
			try {
				imagePath = fileManagerService.saveFile(loginId, file); // 컴퓨터에 파일 업로드 후 URL path를 얻어낸다.
			} catch (IOException e) {
				logger.error("[파일업로드 에러] " + e.getMessage());
			}
		}
		
		return postDAO.insertPost(userId, subject, content, imagePath);
	}
	
	public Post getPost(int postId) {
		return postDAO.selectPost(postId);
	}
	
	public int updatePost(int postId, String loginId, int userId, String subject, String content, MultipartFile file) {
		Post post = getPost(postId);
		if (post == null) {
			logger.error("[update post] 수정할 메모가 존재하지 않습니다.");
			return 0;
		}
		
		// file이 있으면 수정하고 없으면 수정하지 않는다.
		String imagePath = null;
		if (file != null) {
			try {
				imagePath = fileManagerService.saveFile(loginId, file); // 컴퓨터에 파일 업로드 후 URL path를 얻어낸다.
				
				if (imagePath != null && post.getImagePath() != null) {
					// 기존에 이미지가 있었으면 파일을 제거한다. -- 업로드가 실패할 수 있으므로 업로드가 성공 후 제거
					fileManagerService.deleteFile(post.getImagePath());
				}
			} catch (IOException e) {
				logger.error("[파일 수정중 에러] " + e.getMessage());
			}
		}
		
		return postDAO.updatePost(postId, userId, subject, content, imagePath);
	}
	
	public int deletePost(int id) {
		// 파일이 있으면 파일도 삭제한다.
		Post post = getPost(id);
		if (post == null) {
			logger.warn("[update post] 수정할 메모가 존재하지 않습니다.");
			return 0;
		}
		
		if (post.getImagePath() != null) {
			try {
				fileManagerService.deleteFile(post.getImagePath());
			} catch (IOException e) {
				logger.error("[파일 삭제중 에러] " + e.getMessage());
			}
		}
		
		return postDAO.deletePost(id);
	}
}

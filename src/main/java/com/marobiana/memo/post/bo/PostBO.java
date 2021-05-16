package com.marobiana.memo.post.bo;

import java.io.IOException;
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
	
	public List<Post> getPostListByUserId(int userId) {
		return postDAO.selectPostListByUserId(userId);
	}
	
	public int createPost(String loginId, int userId, String subject, String content, MultipartFile file) {
		String imagePath = null;
		try {
			imagePath = fileManagerService.saveFile(loginId, file); // 컴퓨터에 파일 업로드 후 URL path를 얻어낸다.
		} catch (IOException e) {
			logger.error("[파일업로드 에러] " + e.getMessage());
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

package com.marobiana.memo.post.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.marobiana.memo.post.model.Post;

@Repository
public interface PostDAO {
	public List<Post> selectPostListByUserId(
			@Param("userId") int userId,
			@Param("direction") String direction,
			@Param("standardId") Integer standardId,
			@Param("limit") int limit);
	
	public int selectPostIdByUserIdAndSort(
			@Param("userId") int userId, 
			@Param("sort") String sort);
	
	public int insertPost(
			@Param("userId") int userId, 
			@Param("subject") String subject, 
			@Param("content") String content, 
			@Param("imagePath") String imagePath);
	
	public Post selectPost(int postId);
	
	public int updatePost(
			@Param("id") int id,
			@Param("userId") int userId, 
			@Param("subject") String subject, 
			@Param("content") String content, 
			@Param("imagePath") String imagePath);
	
	public int deletePost(int id);
}

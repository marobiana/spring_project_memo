<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글 목록</h1>
		
		<table class="table table-hover">
			<thead>
				<tr>
					<th>No.</th>
					<th>제목</th>
					<th>작성날짜</th>
					<th>수정날짜</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${postList}" var="post">
				<tr>
					<td>${post.id}</td>
					<td>
						<a href="/post/post_detail_view?postId=${post.id}">${post.subject}</a>
					</td>
					<td>
						<%-- ${post.createdAt} (Date 객체)를 String Format으로 바꾸어 출력한다. --%>
						<%-- ${post.createdAt} --%>
						<fmt:formatDate value="${post.createdAt}" var="createdAt" pattern="yyyy-MM-dd HH:mm:ss" />
						${createdAt}
					</td>
					<td>
						<%-- ${post.updatedAt} (Date 객체)를 String Format으로 바꾸어 출력한다. --%>
						<%-- ${post.updatedAt} --%>
						<fmt:formatDate value="${post.updatedAt}" var="updatedAt" pattern="yyyy-MM-dd HH:mm:ss" />
						${updatedAt}
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<div class="paging-area d-flex justify-content-center">
			<c:if test="${prevId ne 0}">
				<a href="/post/post_list_view?prevId=${prevId}" class="mr-5">&lt;&lt; 이전</a>
			</c:if>
			<c:if test="${nextId ne 0}">
				<a href="/post/post_list_view?nextId=${nextId}">다음 &gt;&gt;</a>
			</c:if>
		</div>
		
		<div class="float-right">
			<a href="/post/post_create_view" class="btn btn-primary">글쓰기</a>
		</div>
	</div>
</div>
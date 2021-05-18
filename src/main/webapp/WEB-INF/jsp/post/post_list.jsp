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
			<a href="/post/post_list_view?prevId=${prevId}" class="mr-5">&lt;&lt; 이전</a>
			<%-- next를 계속 누르면 리스트가 비게되고 한번 더 누르면 prevId, nextId가 모두 null이라 첫화면으로 돌아가는 버그 방지 --%>
			<%-- 위의 상황에서 이전을 눌러도 첫화면으로 가지만 이것은 known issue --%>
			<c:if test="${not empty postList}">
			<a href="/post/post_list_view?nextId=${nextId}">다음 &gt;&gt;</a>
			</c:if>
		</div>
		
		<div class="float-right">
			<a href="/post/post_create_view" class="btn btn-primary">글쓰기</a>
		</div>
	</div>
</div>
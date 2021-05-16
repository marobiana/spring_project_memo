<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<div class="d-flex justify-content-center">
	<div class="w-50">
		<div class="m-5">
			<h1>글 상세/수정</h1>
			<input name="subject" type="text" class="form-control mb-2" placeholder="제목을 입력해주세요." value="${post.subject}">
			<textarea name="content" class="form-control mb-2" rows="15" cols="100" placeholder="내용을 입력해주세요.">${post.content}</textarea>
			<%-- float를 사용한 정렬을 사용할 때는 1차 상위부모에 clearfix를 해주어야 float를 사용한 아래 태그들에 영향을 주지 않는다. --%>
			<div class="file-upload-btn clearfix">
				<input id="file" type="file" class="float-right" accept=".jpg,.jpeg,.png,.gif">
			</div>
			
			<%-- 이미지가 있을 때만 이미지 영역 추가 --%>
			<c:if test="${not empty post.imagePath}">
			<div class="image-area">
				<img src="${post.imagePath}" alt="업로드 이미지" width="300">
			</div>
			</c:if>
			
			<div class="btn-area clearfix mt-5">
				<button id="postDeleteBtn" data-post-id="${post.id}" class="btn btn-secondary float-left">삭제</button>
					
				<div class="float-right">
					<button id="postListBtn" class="btn btn-dark">목록</button>
					<%-- postId를 자바스크립트에서 사용하기 위해 data 속성을 추가한다. data-속성명 --%>
					<button id="saveBtn" data-post-id="${post.id}" class="btn btn-primary ml-2">저장</button>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
$(document).ready(function() {
	// 목록 버튼 클릭
	$('#postListBtn').on('click', function() {
		location.href="/post/post_list_view";
	});
	
	// 삭제 버튼 클릭
	$('#postDeleteBtn').on('click', function() {
		console.log($(this).data('post-id'));
		var postId = $(this).data('post-id');
		
		// AJAX 통신으로 삭제 요청
		$.ajax({
			url: '/post/delete',
			method: 'POST',
			data: {'postId':postId},
			success: function(data) {
				if (data.result == 'success') {
					alert("삭제되었습니다.");
					location.href = "/post/post_list_view";
				}
			},
			error: function(e) {
				alert("메모를 삭제하는데 실패했습니다. 관리자에게 문의해주세요.");
			}
		});
	});
	
	// 글 내용 저장
	$('#saveBtn').on('click', function(e) {
		e.preventDefault(); // 기본 이벤트 중단 - 화면이 상단으로 올라가는 현상 제거
		
		var subject = $('input[name=subject]').val().trim(); // 제목만 필수
		console.log(subject);
		if (subject == '') {
			alert("제목을 입력해주세요.");
			return;
		}
		
		var content = $('textarea[name=content]').val();
		console.log(content);
		
		// 파일이 업로드 된 경우 확장자 체크
		var file = $('#file').val();
		if (file != "") {
			console.log(file.split('.')); // 파일명을 . 기준으로 나눈다.
			var ext = file.split('.').pop().toLowerCase(); // 파일명을 .기준으로 나누고, 확장자가 있는 마지막 문자를 가져온 후 모두 소문자로 변경
			if($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
				 alert('gif, png, jpg, jpeg 파일만 업로드 할 수 있습니다.');
				 $('#file').val(''); // 파일을 비운다.
				 return;
			}
		}
		
		// 폼 태그를 자바스크립트에서 만든다.
		var formData = new FormData();
		console.log($(this).data('post-id'));
		formData.append("postId", $(this).data('post-id'));
		formData.append("subject", subject);
		formData.append("content", content);
		formData.append("file", $('#file')[0].files[0]); // $('#file')[0]는 첫번째 input file 태그를 의미, files[0]는 업로드된 첫번째 파일을 의미

		// AJAX 통신으로 form에 있는 데이터를 전송한다.
		$.ajax({
			url: '/post/update',
			method: 'POST',
			data: formData,
			enctype: 'multipart/form-data', // 파일 업로드를 위한 필수 설정
			processData: false, 			// 파일 업로드를 위한 필수 설정
            contentType: false, 			// 파일 업로드를 위한 필수 설정
			success: function(data) {
				if (data.result == 'success') {
					alert("메모가 수정되었습니다.");
					location.reload(true); // 새로고침
				}
			},
			error: function(e) {
				alert("메모 저장에 실패했습니다. 관리자에게 문의해주세요.");
			}
		});
	});
});
</script>
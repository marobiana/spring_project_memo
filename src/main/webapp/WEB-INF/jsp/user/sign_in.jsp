<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%-- 참고: class container는 맨 바깥 레이아웃 잡을 때만 사용한다. --%>
<%-- d-flex로 하위 요소를 유동적으로 배치한다. --%>
<%-- justify-content-center로 d-flex 적용된 하위 요소를 가운데에 배치한다. --%>
<div class="d-flex justify-content-center">
	<div class="login-box">
		<h1 class="mb-4">로그인</h1>
		
		<%-- 키보드 Enter키로 로그인이 될 수 있도록 form 태그를 만들어준다.(submit 타입의 버튼이 동작됨) --%>
		<form id="loginForm" action="/user/sign_in" method="post">
			<div class="input-group mb-3">
				<%-- input-group-prepend: input box 앞에 ID 부분을 회색으로 붙인다. --%>
				<div class="input-group-prepend">
					<span class="input-group-text">ID</span>
				</div>
				<input type="text" class="form-control" id="loginId" name="loginId">
			</div>
	
			<div class="input-group mb-3">
				<div class="input-group-prepend">
					<span class="input-group-text">PW</span>
				</div>
				<input type="password" class="form-control" id="password" name="password">
			</div>
			
			<%-- btn-block: 로그인 박스 영역에 버튼을 가득 채운다. --%>
			<a class="btn btn-block btn-dark" href="/user/sign_up_view">회원가입</a>
			<input type="submit" class="btn btn-block btn-primary" value="로그인">
		</form>
	</div>
</div>

<script>
$(document).ready(function() {
	$('#loginForm').submit(function(e) {
		e.preventDefault(); // submit 자동 수행 중단
		
		// validation
		var loginId = $('#loginId').val().trim();
		if (loginId == '') {
			alert("아이디를 입력해주세요");
			return;
		}
		
		var password = $('#password').val();
		if (password == '') {
			alert("비밀번호를 입력해주세요.");
			return;
		}
		
		
		// AJAX로 submit
		var url = $(this).attr("action");
		var data = $(this).serialize();  // form의 name 속성으로 data를 구성한다.
		
		$.post(url, data)
		.done(function(data) {
			if (data.result == "success") {
				location.href="/post/post_list_view"; 
			} else {
				alert("로그인에 실패했습니다. 다시 시도해주세요.");
			}
		}); 
	}); 
});	
</script>
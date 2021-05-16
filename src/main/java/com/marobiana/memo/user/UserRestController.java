package com.marobiana.memo.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marobiana.memo.common.EncryptUtils;
import com.marobiana.memo.user.bo.UserBO;
import com.marobiana.memo.user.model.User;

@RequestMapping("/user")
@RestController // @ResponseBody + @Controller: 데이터로 내려준다.
public class UserRestController {
	
	@Autowired
	private UserBO userBO;
	
	/**
	 * ID 중복 확인
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/is_duplicated_id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId) {
		
		Map<String, Object> result = new HashMap<>();
		int existRowCount = userBO.existLoginId(loginId);
		if (existRowCount > 0) { // 이미 id가 존재하면 true
			result.put("result", true);
		} else {
			result.put("result", false);
		}
		
		return result;
	}
	
	/**
	 * 회원가입 ajax Submit
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@RequestMapping("/sign_up_for_ajax")
	public Map<String, Object> signUpForAjax(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email) {
		
		String encryptPassword = EncryptUtils.md5(password);
		int row = userBO.insertUser(loginId, encryptPassword, name, email);
		
		Map<String, Object> result = new HashMap<>();
		if (row == 1) {
			result.put("result", "success");
		} else {
			result.put("error", "입력 실패");
		}
		return result;
	}
	
	/**
	 * 로그인
	 * @param loginId
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping("/sign_in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request) {
		
		String encryptPassword = EncryptUtils.md5(password);
		User user = userBO.getUser(loginId, encryptPassword);
		
		Map<String, Object> result = new HashMap<>();
		if (user != null) {
			result.put("result", "success");
			// 로그인 처리 - 세션에 저장(로그인 상태를 유지한다)
			HttpSession session = request.getSession();
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userName", user.getName());	
			session.setAttribute("userId", user.getId());	
		} else {
			result.put("error", "존재하지 않는 사용자 입니다.");
		}
		return result;
	}
	
}

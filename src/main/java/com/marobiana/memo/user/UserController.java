package com.marobiana.memo.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marobiana.memo.common.EncryptUtils;
import com.marobiana.memo.user.bo.UserBO;

@RequestMapping("/user")
@Controller
public class UserController {
	
	@Autowired
	private UserBO userBO;
	
	/**
	 * 회원가입 화면
	 * @param model
	 * @return
	 */
	@RequestMapping("/sign_up_view")
	public String signUpView(Model model) {
		model.addAttribute("viewName", "user/sign_up");
		return "template/layout";
	}
	
	/**
	 * 로그인 화면
	 * @param model
	 * @return
	 */
	@RequestMapping("/sign_in_view")
	public String signInView(Model model) {
		model.addAttribute("viewName", "user/sign_in");
		return "template/layout";
	}
	
	/**
	 * 회원가입 non ajax Submit
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@RequestMapping("/sign_up_for_submit")
	public String signUpForSubmit(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email) {
		
		String encryptPassword = EncryptUtils.md5(password);
		userBO.insertUser(loginId, encryptPassword, name, email);
		
		return "redirect:/user/sign_in_view";  // @Response가 아닌 일반 컨트롤러여야 리다이렉트가 된다.
	}
	
	/**
	 * 로그아웃
	 * @param request
	 * @return
	 */
	@RequestMapping("/sign_out")
	public String signOut(HttpServletRequest request) {
		// 로그아웃
		HttpSession session = request.getSession();
		session.removeAttribute("userLoginId");
		session.removeAttribute("userName");
		session.removeAttribute("userId");
		return "redirect:/user/sign_in_view";
	}
}

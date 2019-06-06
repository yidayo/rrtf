package org.rrtf.user.controller;

import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.rrtf.user.LoginRegister.service.MailService;
import org.rrtf.user.LoginRegister.service.UserService;
import org.rrtf.user.dao.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("password")
public class PasswordController {

	String username;// 记录用户名

	@Resource
	UserService userService;

	@Resource
	MailService mailService;

	@Resource
	UserRepository userRepository;

	@RequestMapping("index")
	public String index() {
		return "forget-password/index";
	}

	@RequestMapping(value = "forgetPwd")
	public String forgetPwd() {
		return "forget-password/forgetPwd2";
	}
	
	@RequestMapping(value = "forgetPwd2")
	public String forgetPwd2() {
		return "redirect:forgetPwd";
	}

	// 判断用户名是否已存在
	@PostMapping("checkUsername")
	@ResponseBody
	public boolean checkUsername(HttpServletRequest request) {
		username = request.getParameter("username");
		return userService.checkUsername(username);
	}

	@RequestMapping("code")
	@ResponseBody
	public boolean getCode(HttpServletRequest request, String inCode) {
		String code = (String) request.getSession().getAttribute("code");// 获取注册页面的验证码
		if (inCode.equals(code)) {
			return true;
		}
		return false;
	}

	@RequestMapping("email")
	@ResponseBody
	public String getemail(HttpSession session, String email) {
		Random random = new Random();
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			code.append(random.nextInt(9));
		}
		String subject = "人人托福验证码";
		String content = "您本次操作验证码为：" + code + "，请在页面输入完成验证，如非本人操作请忽略。";
		mailService.sendSimpleMail(email, subject, content);
		return code.toString();
	}

	@RequestMapping("update")
	@ResponseBody
	public String modifyPassword(String password) {
		String salt = new SecureRandomNumberGenerator().nextBytes().toHex(); // 生成盐值
		String newPassword = new Md5Hash(password, salt, 2).toString(); // 生成的密文
		userRepository.updatePassword(newPassword, salt, username);
		return "";
	}
}

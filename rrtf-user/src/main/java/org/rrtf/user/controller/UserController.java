package org.rrtf.user.controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.rrtf.user.LoginRegister.service.UserService;
import org.rrtf.user.dao.TeacherRepository;
import org.rrtf.user.dao.UserRepository;
import org.rrtf.user.entity.Member;
import org.rrtf.user.entity.Teacher;
import org.rrtf.user.entity.User;
import org.rrtf.user.entity.UserInfo;
import org.rrtf.user.info.fileupload.FileUploadService;
import org.rrtf.user.info.service.MemberService;
import org.rrtf.user.info.service.TeacherService;
import org.rrtf.user.info.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

@Controller
public class UserController<V, K> {

	User user = new User();

	@Autowired
	UserService userService;

	@Resource
	UserInfoService userInfoService;

	@Resource
	UserRepository userRepository;

	@Resource
	MemberService memberService;

	@Resource
	TeacherService teacherService;

	@Resource
	FileUploadService fileUploadService;

	@Resource
	TeacherRepository teacherRepository;

	@Resource
	RedisTemplate redisTemplate;

	
	// 首页
	@RequestMapping({ "/", "index" })
	public String index(HttpSession session, HttpServletResponse response) {
		user = (User) SecurityUtils.getSubject().getPrincipal();
		if (user != null) {
			Cookie cookie = new Cookie("user", user.getUsername());
			cookie.setMaxAge(60 * 60000);
			response.addCookie(cookie);
		}
		if (user != null && "admin".equals(user.getName())) {
			return "System-administrator-loginPage";
		}
		return "homepage";
	}

	// 404页面
	@RequestMapping("404")
	public String unAuth() {
		return "404";
	}

	// 登录页
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String toLogin() {
		return "login-register/login";
	}

	// 登录
	@RequestMapping("/login")
	public String login(HttpSession session, HttpServletRequest request, Model model) {
		// 获取用户登录时的错误
		String exeption = (String) request.getAttribute("shiroLoginFailure");
		String msg = "";
		if (exeption != null) {
			if (UnknownAccountException.class.getName().equals(exeption)) {
				msg = "提示：账号不存在";
			} else if (IncorrectCredentialsException.class.getName().equals(exeption)) {
				msg = "提示：密码错误";
			} else if ("kaptchaValidateFailed".equals(exeption)) {
				model.addAttribute("codeErrorMsg", "提示：验证码输入错误");
			} else {
				msg = "提示：用户名或密码错误：";
			}
			model.addAttribute("loginErrorMsg", msg);
		}
		return "login-register/login";
	}

	// 注册页
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String toRegister() {
		return "login-register/register";
	}

	// 判断用户名是否已存在
	@PostMapping("checkUsername")
	@ResponseBody
	public boolean checkUsername(HttpServletRequest request) {
		String username = request.getParameter("username");
		boolean flag = userService.checkUsername(username);
		return flag;
	}

	// 注册
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String saveUser(HttpServletRequest request, String username, String password, String userCode, Model model) {
		String code = (String) request.getSession().getAttribute("code");// 获取注册页面的验证码
		/*
		 * 当验证码输入正确时，再进行注册, 否则提示用户验证码输入错误
		 */
		if (StringUtils.equalsIgnoreCase(code, userCode)) {
			userService.registerData(username, password);
			model.addAttribute("success", "恭喜您注册成功，请登录后到用户中心完善个人信息");
		} else {
			model.addAttribute("tipCode", "提示：验证码输入错误");
			/*
			 * 将用户输入的用户名和密码返回到页面显示，以免用户再次输入
			 */
			model.addAttribute("username", username);
			model.addAttribute("password", password);
		}
		return "login-register/register";
	}

	// 用户中心
	@RequestMapping("user-center")
	public String userCenter(HttpSession session) throws Exception {
		session.setAttribute("userinfo", userInfoService.getUserInfo(user));
		return "personal-profile/general-user-home-page";
	}

	// 用户个人资料修改
	@RequestMapping("modify")
	public String modify(HttpSession session) {
		UserInfo userInfo = userInfoService.getUserInfo(user);
		session.setAttribute("userinfo", userInfo);
		return "personal-profile/user-center-userinfo";
	}

	// 保存会员修改信息
	@PostMapping("saveMember")
	public String saveMember(Member member) {
		member.setUserId(user.getUserId());
		memberService.saveMember(member);
		return "redirect:modify";
	}

	// 保存讲师修改信息
	@PostMapping("saveTeacher")
	public String saveTeacher(Teacher teacher) {
		teacher.setUserId(user.getUserId());
		teacherService.saveTeacher(teacher);
		return "redirect:modify";
	}

	// 保存用户修改头像
	@RequestMapping("modifyPicture")
	public String modiyPicture(MultipartFile file, RedirectAttributes attributes) {
		try {
			fileUploadService.uploadFile(user.getUsername(), file, attributes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		user = userRepository.findByUsername(user.getUsername());
		// 更新用户信息
		SecurityUtils.getSubject().getSession().setAttribute("user", user);
		return "redirect:modify";
	}

	// 保存用户修改的密码
	@PostMapping("modifyPassword")
	public String modifyPassword(String agoPassword, String password, Model model) {
		/*
		 * 如果用户输入的旧密码通过该用户对应盐值加密后与数据库中存储密码相同， 进行修改密码，否则提示用户重新输入旧密码
		 */
		String conPassword = new Md5Hash(agoPassword, user.getSalt(), 2).toString();
		if (conPassword.equals(user.getPassword())) {
			String salt = new SecureRandomNumberGenerator().nextBytes().toHex(); // 生成盐值
			String rePassword = new Md5Hash(password, salt, 2).toString(); // 生成的密文
			userRepository.updatePassword(rePassword, salt, user.getUsername());
		} else {
			model.addAttribute("message", "您输入的当前密码不正确，请重新输入!");
		}
		return "redirect:modify";
	}

	// 查看我的公开课
	@RequestMapping("myOpenClass")
	public String myOpenClass() {
		return "personal-profile/user-center-myOpenClass";
	}

	// 查看我的群组
	@RequestMapping("myGroup")
	public String myGroup() {
		return "personal-profile/user-center-myGroup";
	}

	// 查看我的福利城堡
	@RequestMapping("myWelfareCastle")
	public String myWelfareCastle() {
		return "personal-profile/user-center-myWelfareCastle";
	}

	// 查看我的托福人
	@RequestMapping("myTOEFL")
	public String myTOEFL() {
		return "personal-profile/user-center-myTOEFL";
	}

}

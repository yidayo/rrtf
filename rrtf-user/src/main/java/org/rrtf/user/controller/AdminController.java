package org.rrtf.user.controller;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.rrtf.user.LoginRegister.service.UserService;
import org.rrtf.user.dao.MemberRepository;
import org.rrtf.user.dao.TeacherRepository;
import org.rrtf.user.dao.UserRepository;
import org.rrtf.user.entity.Member;
import org.rrtf.user.entity.Teacher;
import org.rrtf.user.entity.User;
import org.rrtf.user.entity.UserInfo;
import org.rrtf.user.info.service.MemberService;
import org.rrtf.user.info.service.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Resource
	UserRepository userRepository;

	@Resource
	MemberRepository memberRepository;

	@Resource
	TeacherRepository teacherRepository;

	@Resource
	MemberService memberService;

	@Resource
	TeacherService teacherService;

	@Resource
	UserService UserService;

	// 管理员首页
	@RequestMapping("index")
	public String user() {
		return "System-administrator-loginPage";
	}

	// 管理员权限
	@RequestMapping("management")
	@RequiresPermissions("management")
	public String userinfo() {
		return "userinfo";
	}

	// 讲师管理
	@PostMapping("/teacherInfo")
	@ResponseBody
	public List<Object> userInfo(String no, String username) {
		int page = Integer.parseInt(no);
		List<Object> listMember = new LinkedList<Object>();
		Pageable pageable = new PageRequest(page, 2);
		if (!"".equals(username)) {
			User user = userRepository.findByUsername(username);
			if ("teacher".equals(user.getName())) {
				Teacher teacher = teacherRepository.findByUserId(user.getUserId());
				UserInfo userInfo = teacherService.teacherInfo(user, teacher);
				listMember.add(1);
				listMember.add(userInfo);
				return listMember;
			}
		} else {
			Page<Teacher> teachers = teacherRepository.findAll(pageable);
			listMember.add(teachers.getTotalPages());
			for (Teacher teacher : teachers) {
				User user = userRepository.findByUserId(teacher.getUserId());
				UserInfo userInfo = teacherService.teacherInfo(user, teacher);
				listMember.add(userInfo);
			}
			return listMember;
		}
		return listMember;
	}

	// 会员管理
	@PostMapping("/memberInfo")
	@ResponseBody
	public List<Object> userInfo(String no, String username, String status, String grade) {
		int page = Integer.parseInt(no);
		List<Object> listMember = new LinkedList<Object>();
		Pageable pageable = new PageRequest(page, 2);
		if (!"".equals(username)) {
			User user = userRepository.findByUsername(username);
			if (user != null && "vip".equals(user.getName())) {
				listMember.add(1);
				Member member = memberRepository.findByUserId(user.getUserId());
				UserInfo userInfo = memberService.getUserInfo(user, member);
				listMember.add(userInfo);
				return listMember;
			}
		} else if (!"".equals(status)) {
			int newStatus = Integer.parseInt(status);
			Page<Member> members = memberRepository.findAll(pageable);
			if (!members.equals(null)) {
				listMember.add(members.getTotalPages());
				for (Member member : members) {
					User user = userRepository.findByUserId(member.getUserId());
					if (user.getStatus() == newStatus) {
						UserInfo userInfo = memberService.getUserInfo(user, member);
						listMember.add(userInfo);
					}
				}
				return listMember;
			}
		} else if (!"".equals(grade)) {
			Page<Member> members = memberRepository.findAll(pageable);
			if (!members.equals(null)) {
				listMember.add(members.getTotalPages());
				for (Member member : members) {
					if (member.getGrade().getGrade().equals(grade)) {
						User user = userRepository.findByUserId(member.getUserId());
						UserInfo userInfo = memberService.getUserInfo(user, member);
						listMember.add(userInfo);
					}
				}
				return listMember;
			}
		} else {
			Page<Member> member = memberRepository.findAll(pageable);
			int totalPages = member.getTotalPages();
			listMember.add(totalPages);
			for (Member m : member) {
				int userId = m.getUserId();
				User user = userRepository.findByUserId(userId);
				UserInfo userInfo = memberService.getUserInfo(user, m);
				listMember.add(userInfo);
			}
			return listMember;
		}
		return listMember;
	}

	// 冻结用户
	@PostMapping("/freeze")
	@ResponseBody
	public String freezeUser(@RequestParam("ids") List<String> ids) {
		userRepository.updateStatus(0, ids);
		return "success";
	}

	// 解冻用户
	@PostMapping("/thaw")
	@ResponseBody
	public String thawUser(@RequestParam("ids") List<String> ids) {
		userRepository.updateStatus(1, ids);
		return "success";
	}

	// 删除用户
	@PostMapping("/delete")
	@ResponseBody
	public String deleteUser(@RequestParam("ids") List<String> ids) {
		userRepository.deleteUser(ids);
		return "success";
	}

	// 添加讲师
	@PostMapping("addTeacher")
	@ResponseBody
	public String teacher(HttpServletRequest request) {
		String username = request.getParameter("username");
		String realname = request.getParameter("realname");
		String sex = request.getParameter("sex");
		String edu = request.getParameter("edu");
		String email = request.getParameter("email");
		String tel = request.getParameter("tel");
		String introduce = request.getParameter("signature");
		UserService.registerTeacher(username);
		User user = userRepository.findByUsername(username);
		Integer userId = user.getUserId();
		Teacher teacher = new Teacher();
		teacher.setUserId(userId);
		teacher.setRealname(realname);
		teacher.setSex(sex);
		teacher.setEdu(edu);
		teacher.setEmail(email);
		teacher.setTel(tel);
		teacher.setIntroduce(introduce);
		teacherRepository.save(teacher);
		String data = "success";
		return JSON.toJSONString(data);
	}
}
